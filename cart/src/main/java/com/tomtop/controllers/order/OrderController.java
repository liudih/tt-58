package com.tomtop.controllers.order;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Collections2;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.Lists;
import com.tomtop.configuration.PaymentSettings;
import com.tomtop.dto.Country;
import com.tomtop.dto.Currency;
import com.tomtop.dto.base.Storage;
import com.tomtop.dto.member.MemberAddress;
import com.tomtop.dto.order.Order;
import com.tomtop.dto.order.OrderPayment;
import com.tomtop.entity.order.CreateOrderRequest;
import com.tomtop.exceptions.InvalidStorageException;
import com.tomtop.exceptions.MemberAddressException;
import com.tomtop.exceptions.NotFindPaymentException;
import com.tomtop.exceptions.OrderNocompleteException;
import com.tomtop.exceptions.UserNoLoginException;
import com.tomtop.forms.PlaceOrderForm;
import com.tomtop.forms.RetryOrderForm;
import com.tomtop.services.ICurrencyService;
import com.tomtop.services.base.ICountryService;
import com.tomtop.services.base.IStorageService;
import com.tomtop.services.cart.ICookieCartService;
import com.tomtop.services.impl.order.OrderPaymentService;
import com.tomtop.services.impl.order.OrderUpdateService;
import com.tomtop.services.member.IAddressService;
import com.tomtop.services.order.ICheckoutService;
import com.tomtop.services.order.IOrderService;
import com.tomtop.services.order.IOrderStatusService;
import com.tomtop.services.payment.IPaymentProvider;
import com.tomtop.services.payment.IPaymentService;
import com.tomtop.services.payment.gleepay.GleePayPaymentProvider;
import com.tomtop.utils.CookieUtils;
import com.tomtop.utils.FoundationService;
import com.tomtop.valueobjects.BundleCartItem;
import com.tomtop.valueobjects.CartItem;
import com.tomtop.valueobjects.Constants;
import com.tomtop.valueobjects.SingleCartItem;
import com.tomtop.valueobjects.base.LoginContext;
import com.tomtop.valueobjects.order.OrderItem;
import com.tomtop.valueobjects.order.PaymentContext;

/**
 * 订单流程
 * 
 * @author lijun
 *
 */
@Controller
@RequestMapping(value = "/checkout")
public class OrderController {

	private static final Logger Logger = LoggerFactory
			.getLogger(OrderController.class);

	@Autowired
	IAddressService addressService;

	@Autowired
	ICountryService countryService;

	@Autowired
	IOrderService orderService;

	@Autowired
	ICookieCartService cartService;

	@Autowired
	ICurrencyService currencyService;

	@Autowired
	ICookieCartService cookieCartService;

	@Autowired
	FoundationService foundationService;

	@Autowired
	IOrderStatusService statusService;

	@Autowired
	OrderUpdateService updateService;

	@Autowired
	ICheckoutService checkoutService;

	@Autowired
	IStorageService iStorageService;

	@Autowired
	IPaymentService paymentService;

	@Autowired
	OrderPaymentService orderPaymentService;

	@Value("${cart.url}")
	String cartUrl;

	@Value("${cart.imgurl}")
	String imgUrl;

	@Autowired
	PaymentSettings paymentSettings;
	
	/**
	 * 会员通道 如果是新用户第一次下单,如果用户没有录入过地址,那么强制用户先填地址
	 * 
	 * @throws IOException
	 */
	@RequestMapping(value = "/for-member-step1")
	public String checkoutForMemberStep1(
			@RequestParam("storageid") int storageid,
			HttpServletRequest request, HttpServletResponse response,
			Map<String, Object> model) throws Exception {
		LoginContext loginCtx = foundationService.getLoginContext();
		if (!loginCtx.isLogin()) {
			throw new UserNoLoginException();
		}
		//获取二级域名
		String subdomains = foundationService.getSubdomains();
		String email = loginCtx.getEmail();
		// 保存选择的仓库id
		CookieUtils.setCookie("storageid", storageid + "");
		Integer siteid = foundationService.getSiteID();

		// 查看用户是否已经填写过ship地址
		Integer shipCount = addressService.getShippingAddressCountByEmail(
				email, siteid);

//		model.put("mainurl", this.foundationService.getMainDomain());
		if (!Constants.SUBDOMAINS_M.equals(subdomains) && (shipCount == null || shipCount == 0)) {
			List<Country> countries = countryService.getAllCountries();
			String nextStepUrl = "/checkout/for-member-step2";
			model.put("countries", countries);
			model.put("nextStepUrl", nextStepUrl);
			return "order/checkout_step1";
		}

		return this.checkoutForMemberStep2(storageid, model, request, response);
	}

	@RequestMapping(value = "/for-member-step2")
	public String checkoutForMemberStep2(@RequestParam("storageid")int storageid,
			Map<String, Object> model, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String addressId = request.getParameter("addressId");
		//获取二级域名
		String subdomains = foundationService.getSubdomains();
		// 仓库id
		// MetaUtils.currentMetaBuilder().setTitle("Order Confirm");
		LoginContext loginCtx = foundationService.getLoginContext();
		boolean isLogin = loginCtx.isLogin();
		if (!isLogin) {
			throw new UserNoLoginException();
		}

		int site = this.foundationService.getSiteID();
		int lang = this.foundationService.getLanguage();
		String currencyCode = this.foundationService.getCurrency();

		List<CartItem> items = cartService.getAllItemsCurrentStorageid(site,
				lang, currencyCode);

		if (items == null || items.size() == 0) {
			response.sendRedirect(cartUrl);
			return null;
		}
		// 过滤仓库id
		items = Lists.newArrayList(Collections2.filter(items,
				c -> c.getStorageID() == storageid));
		if (items.size() == 0) {
			response.sendRedirect(cartUrl);
			return null;
		}
		model.put("cartItemlist", items);
		model.put("currencyBo", foundationService.getCurrencyBo());
		model.put("cartsize", items.size());
		String email = loginCtx.getEmail();
		// 测试账号
		email = email == null || "".equals(email) ? "test@test.com" : email;

		Integer siteId = foundationService.getSiteID();
		// 邮寄地址
		List<MemberAddress> shipAddress = addressService.getMemberShippingAddressByEmail(email, siteId);
		FluentIterable.from(shipAddress).forEach(
			a -> {
				//如果没有传入地址ID，则使用默认地址，否则使用选中的地址
				if(StringUtils.isBlank(addressId) && a.getBdefault()!=null && a.getBdefault()){
					model.put("defaultShipAddress", a);
				}else if(a.getIid() != null && a.getIid().toString().equals(addressId)){
					model.put("defaultShipAddress", a);
				}
				if (a.getIcountry() != null) {
					Country country = countryService.getCountryByCountryId(a.getIcountry());
					if(country!=null){
						a.setCountryFullName(country.getCname());
						a.setCountryCode(country.getCshortname());
					}
				}
			});
		//如果没有设置默认地址，则使用第一个地址
		if(model.get("defaultShipAddress") == null && CollectionUtils.isNotEmpty(shipAddress)){
			model.put("defaultShipAddress", shipAddress.get(0));
		}
		// 国家下拉框
		List<Country> countries = countryService.getAllCountries();
		model.put("shipAddress", shipAddress);
		model.put("countries", countries);

		//显示默认账单地址
		MemberAddress billAddress = addressService.getDefaultOrderAddress(email, site);
		if (billAddress != null && billAddress.getIcountry() != null) {
			Country billcountry = countryService.getCountryByCountryId(billAddress.getIcountry());
			if (billcountry != null) {
				billAddress.setCountryFullName(billcountry.getCname());
				billAddress.setCountryCode(billcountry.getCshortname());
				model.put("billAddress", billAddress);
			}
		}
		

		Order order = new Order();
		List<CartItem> allItems = Lists.newLinkedList();
		for (CartItem item : items) {
			if (item instanceof SingleCartItem) {
				allItems.add(item);
			} else if (item instanceof BundleCartItem) {
				List<SingleCartItem> childs = ((BundleCartItem) item)
						.getChildList();
				allItems.addAll(childs);
			}
		}
		double subTotal = checkoutService.subToatl(items);
		order.setFordersubtotal(subTotal);
		order.setFshippingprice(0.0);
		order.setCcurrency(currencyCode);
		// 优惠券使用
		double discount = 0.0;
		order.setFextra(discount);
		order.setFgrandtotal(subTotal + discount);
		model.put("order", order);
//		model.put("mainurl", this.foundationService.getMainDomain());
		model.put("storageid", storageid);

		//获取配置的支付方式
		String hostName = this.foundationService.getHostName();
		String[] methods = paymentSettings.getMethods(hostName);
		model.put("methods", methods);
		model.put("addressId",addressId);
		if(Constants.SUBDOMAINS_M.equals(subdomains)){
			return "mobile/order/checkout_step2";
		}
		return "order/checkout_step2";
	}

	@RequestMapping("/for-member-step3")
	public String placeOrder(@ModelAttribute PlaceOrderForm form,
			HttpServletRequest request, HttpServletResponse response,
			Map<String, Object> model) throws Exception {

		

		LoginContext loginCtx = foundationService.getLoginContext();
		boolean isLogin = loginCtx.isLogin();
		if (!isLogin) {
			throw new UserNoLoginException();
		}
		String email = loginCtx.getEmail();

		int site = this.foundationService.getSiteID();
		int lang = this.foundationService.getLanguage();
		String currencyCode = this.foundationService.getCurrency();
		List<CartItem> items = cartService.getAllItems(site, lang, currencyCode);

		// 获取仓库id
		Integer storageid = form.getStorageid();

		// 过滤仓库id
		items = Lists.newArrayList(Collections2.filter(items,
				c -> c.getStorageID() == storageid));
		if (items.size() == 0) {
			Logger.debug("filter items after is empty");
			response.sendRedirect(cartUrl);
			return null;
		}

		// 判断所有商品是否是同一个仓库
		Integer firstStorage = items.get(0).getStorageID();
		if (firstStorage == null) {
			throw new InvalidStorageException();
		}

		// ~ 获取真实仓库
		int tstorid = firstStorage;
		List<Storage> storagelist = iStorageService.getAllStorages();
		List<Storage> newstoragelist = Lists.newArrayList(Collections2.filter(
				storagelist, c -> c.getIparentstorage() == tstorid));
		if (newstoragelist != null && newstoragelist.size() > 0) {
			firstStorage = newstoragelist.get(0).getIid();
		}

		List<String> listingId = Lists.newLinkedList();
		FluentIterable.from(items).forEach(item -> {
			if (item instanceof BundleCartItem) {
				listingId.addAll(((BundleCartItem) item).getAllListingId());
			} else {
				listingId.add(item.getClistingid());
			}
		});



		String shipCode = form.getShipMethodCode();

		Integer addressId = form.getAddressId();

		String origin = this.foundationService.getOrigin();

		String message = form.getMessage();

		String ip = this.foundationService.getClientIP();
		String currency = this.foundationService.getCurrency();
		String vhost = this.foundationService.getVhost();

		CreateOrderRequest createOrderRequest = new CreateOrderRequest(items,
				site, addressId, shipCode, origin, message, ip, lang, currency,
				vhost, firstStorage);
		createOrderRequest.setCpaymenttype(form.getPaymentId());

		Order order = this.orderService.createOrderInstance(createOrderRequest);
		// 如果用户使用优惠后导致付款金额为0或负值时,不能去付款
		Double grandtotal = order.getFgrandtotal();
		if (grandtotal == null || grandtotal <= 0) {
			// 跳转到支付失败页面
			StringBuilder errorurl = new StringBuilder("/payment-result/error");
			errorurl.append("?orderNum=").append(order.getCordernumber());
			errorurl.append("&error=").append("order total less than zero");
			errorurl.append("&returnWhere=").append("no-ec");
			errorurl.append("&storageid=").append(form.getStorageid());
			response.sendRedirect(errorurl.toString());
			return null;
		}
		String paymentId = form.getPaymentId();
		String orderNum = order.getCordernumber();
		if ("paypal".equals(paymentId)) {
			response.sendRedirect("/paypal/set?n=" + orderNum);
			return null;
		} else {

			IPaymentProvider provider = paymentService.getPaymentById(form
					.getPaymentId());

			if (provider == null) {
				throw new NotFindPaymentException(form.getPaymentId());
			}
			if (!provider.validForm(form)) {
				throw new NullPointerException("country in bill address can not be empty,please set your default bill address.");
			}

			orderPaymentService.createOrderPayment(orderNum,
					form.getPaymentId(), form);

			Integer status = statusService
					.getIdByName(IOrderStatusService.PAYMENT_PENDING);
			boolean isSuccess = updateService.replaceOrder(order.getIid(),
					status, paymentId, email);

			if (isSuccess) {
				if(provider instanceof GleePayPaymentProvider){
					response.sendRedirect("/gleepay/doPayment?orderNum=" + orderNum);
					return null;
				}
				response.sendRedirect("/ocean/doPayment?orderNum=" + orderNum);
				return null;
			}
		}
		return null;

	}

	@RequestMapping("/retry/{orderNum}")
	public String retry(@PathVariable String orderNum,
			HttpServletRequest request, HttpServletResponse response,
			Map<String, Object> model) throws Exception {
		Order order = orderService.getOrderByOrderNumber(orderNum);
		
		//非本站点订单
		if(order == null || order.getIwebsiteid() != foundationService.getSite()){
			// 跳转到支付失败页面
			StringBuilder errorurl = new StringBuilder("/payment-result/error");
			errorurl.append("?orderNum=").append(orderNum);
			errorurl.append("&error=").append("can not find this order");
			response.sendRedirect(errorurl.toString());
			return null;
		}
				
		// 检查订单是否已经支付完成
		boolean isAlreadyPaid = orderService.isAlreadyPaid(order);
		if (isAlreadyPaid) {
			// 跳转到支付失败页面
			StringBuilder errorurl = new StringBuilder("/payment-result/error");
			errorurl.append("?orderNum=").append(orderNum);
			errorurl.append("&error=").append("Your order has been paid completed");
			response.sendRedirect(errorurl.toString());
			return null;
		}
		
		if (order.getFshippingprice() == null) {
			// modify by lijun if shipping price is null then go to EC
			// ok("This is a bad order,you need feedback to support staff");
			response.sendRedirect("/paypal/ec-set?n=" + orderNum);

			return null;
		}

		return retryView(orderNum, model,request);
	}

	private String retryView(@PathVariable("orderNumber") String orderNumber,
			Map<String, Object> model,HttpServletRequest request) {
		//是否是pc端访问
		boolean isPC = true;
		//访问终端的类型
		String terminalType = (String)request.getAttribute(Constants.TERMINAL_TYPE);
		// pc端访问
		if(Constants.TERMINAL_TYPE_MOBILE.equals(terminalType)){
			isPC = false;
		}
		
		LoginContext loginCtx = foundationService.getLoginContext();
		if (!loginCtx.isLogin()) {
			throw new UserNoLoginException();
		}
		String email = loginCtx.getEmail();
		Integer lang = foundationService.getLanguage();
		PaymentContext context = orderService.getPaymentContext(orderNumber,
				lang);
		if (context == null) {
			throw new OrderNocompleteException(orderNumber, "order invalid");
		}
		Order order = context.getOrder().getOrder();
		model.put("currencyBo", foundationService.getCurrencyBo());
		if (order == null) {
			throw new OrderNocompleteException(orderNumber, "invalid");
		}
		List<OrderItem> orderList = orderService.getOrderDetailByOrder(order,
				lang);
		Currency currency = currencyService.getCurrencyByCode(order
				.getCcurrency());
		if (currency != null) {
			model.put("symbol", currency.getCsymbol());
		}
		// 账单地址
		MemberAddress billAddress = addressService
				.getDefaultOrderAddress(email, order.getIwebsiteid());
		if (billAddress != null && billAddress.getIcountry() != null) {
			Country billcountry = countryService
					.getCountryByCountryId(billAddress.getIcountry());
			if (billcountry == null) {
				throw new MemberAddressException("bill country error");
			}
			billAddress.setCountryFullName(billcountry.getCname());
			billAddress.setCountryCode(billcountry.getCshortname());
		}
		//站点id
		Integer siteId = foundationService.getSiteID();
		//账单地址集合
		List<MemberAddress> billAddressList = addressService.getAllBillingAddress(email, siteId);
		for (MemberAddress billAddr : billAddressList) {
			if (billAddr.getIcountry() != null) {
				Country billcountry = countryService
						.getCountryByCountryId(billAddr.getIcountry());
				if (billcountry == null) {
					throw new MemberAddressException("bill country error");
				}
				billAddr.setCountryFullName(billcountry.getCname());
				billAddr.setCountryCode(billcountry.getCshortname());
			}
		}
		
		//所有国家
		List<Country> countries = countryService.getAllCountries();
		model.put("countries", countries);
		model.put("billAddress", billAddress);
		model.put("billAddressList", billAddressList);
		model.put("order", order);
		model.put("orderlist", orderList);
//		model.put("mainurl", this.foundationService.getMainDomain());
		//获取配置的支付方式
		String hostName = "";
		// pc端访问
		if(isPC){
			hostName = this.foundationService.getHostName();
		}else{
			hostName = this.foundationService.getMobileHostName();
		}
		
		String[] methods = paymentSettings.getMethods(hostName);
		model.put("methods", methods);
		
		// pc端访问
		if(isPC){
			return "/order/pay_retry";
		}
		// 移动端访问
		else{
			return "/mobile/order/pay_retry";
		}
	}
	
	/**
	 * 移动端再次支付
	 * @param form 再次支付表单
	 * @param request 请求
	 * @param response 响应
	 * @param model 模型
	 * @param billAddrIsShipAddr 是否把收货地址拷贝为账单地址
	 * @return
	 * @throws IOException
	 * @author shuliangxing
	 * @date 2016年5月18日 上午10:08:02
	 */
	@RequestMapping(value = "/mobileRetry/do", method = RequestMethod.POST)
	public String mobileRetryDo(@ModelAttribute RetryOrderForm form,
			HttpServletRequest request, HttpServletResponse response,
			Map<String, Object> model) throws Exception{
		LoginContext loginCtx = foundationService.getLoginContext();
		if (!loginCtx.isLogin()) {
			throw new UserNoLoginException();
		}
		// 信用卡或jcb支付
		if("oceanpayment_credit".equals(form.getPaymentId()) || "oceanpayment_jcb".equals(form.getPaymentId())){
			//账单地址是否是收货地址
			boolean billAddrIsShipAddr = false;
			//选择的账单地址id
			Integer chooseBillAddrId = null;
			if("oceanpayment_credit".equals(form.getPaymentId())){
				billAddrIsShipAddr = form.getBillAddrIsShipAddrCredit();
				chooseBillAddrId = form.getChooseBillAddrIdCredit();
			}else if( "oceanpayment_jcb".equals(form.getPaymentId())){
				billAddrIsShipAddr = form.getBillAddrIsShipAddrJcb();
				chooseBillAddrId = form.getChooseBillAddrIdJcb();
			}
			// 如果账单地址是订单收货地址,把收货地址拷贝为账单地址
			if(billAddrIsShipAddr){
				Order order = orderService.getOrderByOrderNumber(form.getOrderNum());
				if (order == null) {
					throw new OrderNocompleteException(form.getOrderNum(), "invalid");
				}
				// 根据国家简写查询国家
				Country country = countryService.getCountryByShortCountryName(order.getCcountrysn());
				if(country == null){
					throw new MemberAddressException("order country error");
				}
				
				// 查询是否存在重复记录
				MemberAddress billAddr = addressService.getBillAddrByOrderAddrParam(order,country.getIid());
				// 不存在则新增
				if(billAddr == null){
					MemberAddress copyBillAddr = new MemberAddress();
					BeanUtils.copyProperties(order, copyBillAddr,"iid");
					copyBillAddr.setIaddressid(2);
					copyBillAddr.setBdefault(true);
					copyBillAddr.setIcountry(country.getIid());
					addressService.addNewAddress(copyBillAddr);
				}
				// 存在则修改为默认地址
				else{
					addressService.setDefaultShippingaddress(billAddr.getIid(), loginCtx.getEmail(), 2);
				}
			}
			else{
				// 如果选择的账单id不为空, 则把该地址设为默认
				if(chooseBillAddrId != null && chooseBillAddrId > 0){
					addressService.setDefaultShippingaddress(chooseBillAddrId, loginCtx.getEmail(), 2);
				}
			}
		}
		
		return retryDo(form, request, response, model);
	}

	@RequestMapping(value = "/retry/do", method = RequestMethod.POST)
	public String retryDo(@ModelAttribute RetryOrderForm form,
			HttpServletRequest request, HttpServletResponse response,
			Map<String, Object> model) throws Exception {
		LoginContext loginCtx = foundationService.getLoginContext();
		if (!loginCtx.isLogin()) {
			throw new UserNoLoginException();
		}
		String orderNum = form.getOrderNum();

		Order order = orderService.getOrderByOrderNumber(orderNum);
		if(order==null){
			// 跳转到支付失败页面
			StringBuilder errorurl = new StringBuilder("/payment-result/error");
			errorurl.append("?orderNum=").append(orderNum);
			errorurl.append("&error=").append("Order is not found!");
			response.sendRedirect(errorurl.toString());
			return null;
		}
		//更新订单paymentid
		String paymentId = form.getPaymentId();
		boolean isChangePaymentId = orderService.updateOrderPaymentId(order.getIid(),paymentId);
		// 检查订单是否已经支付完成
		boolean isAlreadyPaid = orderService.isAlreadyPaid(order);
		if (isAlreadyPaid) {
			// 跳转到支付失败页面
			StringBuilder errorurl = new StringBuilder("/payment-result/error");
			errorurl.append("?orderNum=").append(orderNum);
			errorurl.append("&error=").append("Your order has been paid completed");
			response.sendRedirect(errorurl.toString());
			return null;
		}
		
		if ("paypal".equals(paymentId)) {
			response.sendRedirect("/paypal/set?n=" + orderNum);
			return null;
		}
		
		PlaceOrderForm oceanForm = orderPaymentService.getForm(orderNum,
				paymentId);
		if(oceanForm == null){
			oceanForm = new PlaceOrderForm();
		}
		MemberAddress memberAddress = addressService.getDefaultOrderAddress(loginCtx.getEmail(), order.getIwebsiteid());
		oceanForm.setPaymentId(paymentId);
		if(memberAddress!=null){
			oceanForm.setBillAddressId(memberAddress.getIid());
		}
		oceanForm.setQiwiAccount("");
		oceanForm.setQiwiCountry("");
		oceanForm.setPay_typeCode("");
		

		if ("oceanpayment_qiwi".equals(paymentId)) {
			String qiwiAccount = form.getQiwiAccount();
			String country = form.getQiwiCountry();
			Assert.hasText(qiwiAccount, "qiwi account is null");
			Assert.hasText(country, "qiwi country is null");

			String originalAccount = oceanForm.getQiwiAccount();
			String originalCountry = oceanForm.getQiwiCountry();

			if (originalAccount == null || originalCountry == null
					|| !originalAccount.equals(qiwiAccount)
					|| !originalCountry.equals(country)) {
				oceanForm.setQiwiAccount(qiwiAccount);
				oceanForm.setQiwiCountry(country);
			}
		} else if ("oceanpayment_boleto".equals(paymentId)) {
			String pay_typeCode = form.getPay_typeCode();
			Assert.hasText(pay_typeCode, "pay_typeCode is null");
			String original = oceanForm.getPay_typeCode();
			if (original == null || !original.equals(pay_typeCode)) {
				oceanForm.setPay_typeCode(pay_typeCode);
			}
		}
		
		OrderPayment op = new OrderPayment();
		op.setCjson(JSONObject.toJSONString(oceanForm));
		op.setCorderid(orderNum);
		op.setCpaymentid(paymentId);
		orderPaymentService.saveOrUpdate(op);
		
		IPaymentProvider provider = paymentService.getPaymentById(form
				.getPaymentId());

		if (provider == null) {
			throw new NotFindPaymentException(form.getPaymentId());
		}
		
		if(provider instanceof GleePayPaymentProvider){
			response.sendRedirect("/gleepay/doPayment?orderNum=" + orderNum);
			return null;
		}
		
		response.sendRedirect("/ocean/doPayment?orderNum=" + orderNum);
		return null;
	}
}
