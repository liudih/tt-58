package com.tomtop.controllers.payment;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Collections2;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.tomtop.bo.CurrencyBo;
import com.tomtop.dao.order.IOrderDetailDao;
import com.tomtop.dto.Country;
import com.tomtop.dto.Currency;
import com.tomtop.dto.base.Storage;
import com.tomtop.dto.member.MemberAddress;
import com.tomtop.dto.order.Order;
import com.tomtop.dto.order.OrderDetail;
import com.tomtop.entity.order.CreateOrderRequest;
import com.tomtop.events.EventBroker;
import com.tomtop.events.order.OrderOpreationEvent;
import com.tomtop.events.order.OrderOpreationEvent.OpreationResult;
import com.tomtop.events.order.OrderOpreationEvent.OpreationType;
import com.tomtop.exceptions.BadRequestException;
import com.tomtop.exceptions.CreateOrderException;
import com.tomtop.exceptions.InvalidStorageException;
import com.tomtop.exceptions.InventoryShortageException;
import com.tomtop.forms.OrderConfirmForm;
import com.tomtop.mappers.order.DetailMapper;
import com.tomtop.mappers.order.OrderMapper;
import com.tomtop.services.ICurrencyService;
import com.tomtop.services.base.ICountryService;
import com.tomtop.services.base.IStorageService;
import com.tomtop.services.cart.ICartService;
import com.tomtop.services.cart.ICookieCartService;
import com.tomtop.services.order.IFreightService;
import com.tomtop.services.order.IOrderService;
import com.tomtop.services.payment.paypal.IExpressCheckoutNvpService;
import com.tomtop.utils.CookieUtils;
import com.tomtop.utils.FoundationService;
import com.tomtop.utils.Request;
import com.tomtop.utils.Utils;
import com.tomtop.valueobjects.BundleCartItem;
import com.tomtop.valueobjects.CartItem;
import com.tomtop.valueobjects.Constants;
import com.tomtop.valueobjects.SingleCartItem;
import com.tomtop.valueobjects.base.LoginContext;
import com.tomtop.valueobjects.order.ExistingOrderContext;
import com.tomtop.valueobjects.order.PaymentContext;
import com.tomtop.valueobjects.order.ShippingMethod;
import com.tomtop.valueobjects.payment.paypal.PaypalNvpPaymentStatus;
import com.tomtop.valueobjects.payment.paypal.SetExpressCheckout;

/**
 * nvp方式和paypal通信
 * 
 * @author lijun
 *
 */
@Controller
@RequestMapping("/paypal")
public class PaypalController {

	private static final Logger Logger = LoggerFactory
			.getLogger(PaypalController.class);

	private static final String REDIRECT = "/payment-result/succeed/paypal/";

	@Autowired
	IExpressCheckoutNvpService service;

	@Autowired
	FoundationService foundation;

	@Autowired
	IOrderService orderService;

	@Autowired
	ICurrencyService currencyService;

	@Autowired
	ICountryService countryService;

	@Autowired
	OrderMapper orderMapper;

	@Autowired
	ICookieCartService cartService;

	@Autowired
	IOrderDetailDao orderDetailDao;

	@Autowired
	IFreightService freightService;

	@Autowired
	IStorageService iStorageService;

	@Autowired
	DetailMapper detailMapper;

	@Autowired
	ICartService cartEnquiryService;

	@Value("${cart.url}")
	String cartUrl;

	@Value("${cart.imgurl}")
	String imgUrl;
	
	@Autowired
	EventBroker eventBroker;

	/**
	 * 用户未登陆的情况下快捷支付
	 * 
	 * @author lijun
	 * @param ordernum
	 * @return
	 */
	@RequestMapping("/set-guest")
	public String setExpressCheckoutForGuest(int storageid,
			Map<String, Object> model, HttpServletRequest request,
			HttpServletResponse response) throws IOException{
		
		// 币种
		String currency = this.foundation.getCurrency();
		int site = this.foundation.getSiteID();
		int lang = this.foundation.getLanguage();
		String vhost = this.foundation.getVhost();
		String ip = this.foundation.getClientIP();
		LoginContext loginCtx = this.foundation.getLoginContext();
		String email = loginCtx != null ? loginCtx.getEmail() : null;
		
		try {
			List<CartItem> items = cartService
					.getAllItems(site, lang, currency);

			// 保存选择的仓库id
			CookieUtils.setCookie("storageid", storageid + "");

			if (items == null || items.size() == 0) {
				response.sendRedirect(this.cartUrl);
				return null;
			}
			// 过滤仓库id
			items = Lists.newArrayList(Collections2.filter(items,
					c -> c.getStorageID() == storageid));
			if (items.size() == 0) {
				response.sendRedirect(this.cartUrl);
				return null;
			}

			// 判断所有商品是否是同一个仓库
			Integer firstStorage = items.get(0).getStorageID();
			if (firstStorage == null) {
				Logger.debug("storage id is null in cart");
				throw new InvalidStorageException();
			}
			// ~ 获取真实仓库
			int tstorid = firstStorage;
			List<Storage> storagelist = iStorageService.getAllStorages();
			List<Storage> newstoragelist = Lists
					.newArrayList(Collections2.filter(storagelist,
							c -> c.getIparentstorage() == tstorid));
			if (newstoragelist != null && newstoragelist.size() > 0) {
				firstStorage = newstoragelist.get(0).getIid();
				Logger.debug("get real storage -- > {} -- {} ", tstorid,
						firstStorage);
			}
			List<String> listingId = Lists.newLinkedList();
			FluentIterable.from(items).forEach(
					item -> {
						if (item instanceof BundleCartItem) {
							listingId.addAll(((BundleCartItem) item)
									.getAllListingId());
						} else {
							listingId.add(item.getClistingid());
						}
					});

			String origin = this.foundation.getOrigin();

			// 优惠券使用

			CreateOrderRequest reateOrderRequest = new CreateOrderRequest(
					items, site, null, null, origin, null, ip, lang, currency,
					vhost, firstStorage);
			reateOrderRequest.setCpaymenttype(Constants.PAYPAL_EC);

			Order order = this.orderService
					.createOrderInstance(reateOrderRequest);

			String host = this.foundation.getHost();
			
			if(site==1){
				if (!host.startsWith("http://")) {
					host = "http://" + host;
				}
			}else{
				String subdomains = foundation.getSubdomains();
				if (!host.startsWith("https://")) {
					if(!Constants.SUBDOMAINS_M.equals(subdomains)){
						host = "https://" + host;
					}else{
						host = "http://" + host;
					}
				}
			}

			String orderNum = order.getCordernumber();
			String returnUrl = host + "/paypal/ec-confirm?n=" + orderNum;

			Logger.debug("paypal returnUrl:{}", returnUrl);

			String cancalUrl = host + this.cartUrl;
			Logger.debug("paypal cancalUrl:{}", cancalUrl);

			SetExpressCheckout setEc = new SetExpressCheckout(orderNum,
					returnUrl, cancalUrl, true);
			PaypalNvpPaymentStatus status = service.setExpressCheckout(setEc);
			
			// 记录EC支付请求日志
			OrderOpreationEvent event = new OrderOpreationEvent(site, email,
					orderNum, OpreationType.EC_PAY_REQUEST,
					JSON.toJSONString(status));
			
			if (status != null && status.isNextStep()) {
				eventBroker.post(event);
				response.sendRedirect(status.getRedirectURL());
				return null;
			}
			event.setCresult(OpreationResult.FAILURE);
			eventBroker.post(event);
			// 跳转到支付失败页面
			StringBuilder errorurl = new StringBuilder("/payment-result/error");
			errorurl.append("?orderNum=").append(status.getOrderNum());
			errorurl.append("&errorCode=").append(status.getErrorCode());
			errorurl.append("&error=").append(status.getFailedInfo());
			response.sendRedirect(errorurl.toString());
			return null;

		}catch(InventoryShortageException e){
			// 记录开始结账日志
			OrderOpreationEvent event = new OrderOpreationEvent(site, email,
					null, OpreationType.EC_PAY_REQUEST, e.getMessage());
			event.setCresult(OpreationResult.FAILURE);
			eventBroker.post(event);
			Logger.error("express checkout for cart  failed", e);
			throw new InventoryShortageException(e.getMessage());
		}catch (Exception e) {
			// 记录开始结账日志
			OrderOpreationEvent event = new OrderOpreationEvent(site, email,
					null, OpreationType.EC_PAY_REQUEST, e.getMessage());
			event.setCresult(OpreationResult.FAILURE);
			eventBroker.post(event);
			Logger.error("express checkout for cart  failed", e);
			throw new BadRequestException("express checkout for cart  failed");
		}
	}

	/**
	 * EC支付后的确认页面
	 * 
	 * @param token
	 * @param PayerID
	 * @param n
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/ec-confirm")
	public String confirmOrderView(String token, String PayerID, String n,
			Map<String, Object> model, HttpServletRequest request,
			HttpServletResponse response)  throws Exception{
		//获取二级域名
		String subdomains = foundation.getSubdomains();
		if (StringUtils.isNotEmpty(PayerID)) {
			CookieUtils.setCookie("payerid", PayerID);
		} else {
			String cookie = CookieUtils.getCookie("payerid");
			if (StringUtils.isNotEmpty(cookie)) {
				PayerID = cookie;
			}
		}
		int site = this.foundation.getSiteID();
		int lang = this.foundation.getLanguage();
		LoginContext loginCtx = this.foundation.getLoginContext();
		String email = loginCtx != null ? loginCtx.getEmail() : null;
		//PayPal的邮件账号，用于记录log系统，以便观察支付数据 ，如果没有获取到用户PayPal的邮箱，默认用该地址
		String cmemberEmail = "defaultEmail@email.com";
		try{
			// 为什么要立刻保存一次信息而不是等用户确定后再来保存信息
			// 这样做事因为如果用户到了确认页面没有去支付,那么等用户再次去支付的时候不用再次填写信息
			service.saveShipAddress(token, PayerID, n);
			int language = foundation.getLanguage();
			PaymentContext paymentCtx = orderService.getPaymentContext(n, language);

			Order order = paymentCtx.getOrder().getOrder();
			//用于跟踪支付记录
			String paypalEmail = order.getCmemberemail();
			if(StringUtils.isNotBlank(paypalEmail)){
				cmemberEmail = paypalEmail;
			}
			
			String countryCode = order.getCcountrysn();
			Country country = countryService
					.getCountryByShortCountryName(countryCode);

			MemberAddress address = new MemberAddress();
			address.setCfirstname(order.getCfirstname());
			address.setCmiddlename(order.getCmiddlename());
			address.setClastname(order.getClastname());
			address.setCstreetaddress(order.getCstreetaddress());
			address.setCcity(order.getCcity());
			if (country != null) {
				address.setIcountry(country.getIid());
			}
			address.setCprovince(order.getCprovince());
			address.setCpostalcode(order.getCpostalcode());
			address.setCtelephone(order.getCtelephone());

			List<Country> countries = countryService.getAllCountries();
			Map<Integer, Country> countryMap = Maps.newLinkedHashMap();
			FluentIterable.from(countries).forEach(c -> {
				countryMap.put(c.getIid(), c);
			});

			String shipToCountry = order.getCcountry();
			// 判断用户邮寄的国家是否支持
			String shipToCountryCode = null;
			boolean isShipable = countryService.isShipable(country);
			if (isShipable) {
				shipToCountryCode = countryCode;
			}
			address.setCountryCode(countryCode);
			String currencyCode = order.getCcurrency();
			Currency currency = currencyService.getCurrencyByCode(currencyCode);

			ExistingOrderContext orderCtx = new ExistingOrderContext(order,
					paymentCtx.getOrder().getDetails(), false);
			orderCtx.setCountry(country);
			orderCtx.setStorageId(order.getIstorageid());

			List<String> renderNames = Lists.newLinkedList();
			renderNames.add("cart-product");
			// renderNames.add("shipping-method");
			
			List<CartItem> cartItemlist = Lists.newArrayList();
			if (order != null) {
				List<OrderDetail> details = detailMapper
						.getOrderDetailByOrderId(order.getIid());
				for (OrderDetail od : details) {
					SingleCartItem ci = new SingleCartItem();
					ci.setClistingid(od.getClistingid());
					ci.setIqty(od.getIqty());
					ci.setStorageID(order.getIstorageid());
					ci.setIitemtype(1);
					cartItemlist.add(ci);
				}
				cartItemlist = cartEnquiryService.getCartItems(cartItemlist,
						order.getIwebsiteid(), lang, currencyCode);
			}

			model.put("cartItemlist", cartItemlist);

			model.put("token", token);
			model.put("PayerID", PayerID);
			model.put("n", n);
			model.put("address", address);
			model.put("shipToCountry", shipToCountry);
			model.put("shipToCountryCode", shipToCountryCode);
			model.put("order", order);
			model.put("countries", countries);
			model.put("currency", currency);
			model.put("currencyBo",
					new CurrencyBo(currency.getCcode(), currency.getCsymbol()));
			
			// 记录EC支付确认日志
			OrderOpreationEvent event = new OrderOpreationEvent(site, email,
					n, OpreationType.EC_ORDER_CONFIRM,
					JSON.toJSONString(paymentCtx),cmemberEmail);
			eventBroker.post(event);
			
			if(Constants.SUBDOMAINS_M.equals(subdomains)){
				return "mobile/order/ec_confirm_order";
			}
			return "order/ec_confirm_order";
		}catch(Exception e){
			// 记录EC支付确认日志
			OrderOpreationEvent event = new OrderOpreationEvent(site, email,
					n, OpreationType.EC_ORDER_CONFIRM, e.getMessage(),cmemberEmail);
			event.setCresult(OpreationResult.FAILURE);
			eventBroker.post(event);
			Logger.error("ec-confirm for cart  failed", e);
			throw e;
		}
		
	}

	/**
	 * 
	 * @return token
	 * @throws IOException
	 */
	@RequestMapping("/set")
	public String setExpressCheckout(@RequestParam("n") String ordernum,
			Map<String, Object> model, HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		if (ordernum == null || ordernum.length() == 0) {
			throw new NullPointerException("ordernum is null");
		}
		
		int site = this.foundation.getSiteID();
		LoginContext loginCtx = this.foundation.getLoginContext();
		String email = loginCtx != null ? loginCtx.getEmail() : null;
		try{
			String host = this.foundation.getHost();
			if(site==1){
				if (!host.startsWith("http://")) {
					host = "http://" + host;
				}
			}else{
				String subdomains = foundation.getSubdomains();
				if (!host.startsWith("https://")) {
					if(!Constants.SUBDOMAINS_M.equals(subdomains)){
						host = "https://" + host;
					}else{
						host = "http://" + host;
					}
				}
			}
			String returnUrl = host + "/paypal/do?n=" + ordernum;
			Logger.debug("paypal returnUrl:{}", returnUrl);

			String cancalUrl = host;
			Logger.debug("paypal cancalUrl:{}", cancalUrl);

			SetExpressCheckout setEc = new SetExpressCheckout(ordernum, returnUrl,
					cancalUrl, false);

			PaypalNvpPaymentStatus status = service.setExpressCheckout(setEc);
			// 记录Paypal支付请求日志
			OrderOpreationEvent event = new OrderOpreationEvent(site, email,
					ordernum, OpreationType.PAYPAL_REQUEST,
					JSON.toJSONString(status));
			if (status != null && status.isNextStep()) {
				eventBroker.post(event);
				Logger.debug("redirect:{}", status.getRedirectURL());
				response.sendRedirect(status.getRedirectURL());
				return null;
			}
			event.setCresult(OpreationResult.FAILURE);
			eventBroker.post(event);
			// 跳转到支付失败页面
			StringBuilder errorurl = new StringBuilder("/payment-result/error");
			errorurl.append("?orderNum=").append(status.getOrderNum());
			errorurl.append("&errorCode=").append(status.getErrorCode());
			errorurl.append("&error=").append(status.getFailedInfo());
			response.sendRedirect(errorurl.toString());
			return null;
		}catch(Exception e){
			// 记录Paypal支付请求日志
			OrderOpreationEvent event = new OrderOpreationEvent(site, email,
					ordernum, OpreationType.PAYPAL_REQUEST, e.getMessage());
			event.setCresult(OpreationResult.FAILURE);
			eventBroker.post(event);
			Logger.error("paypal set for cart  failed", e);
			throw e;
		}
	}

	@RequestMapping("/do")
	public String DoExpressCheckoutPayment(String token, String PayerID,
			String n, Map<String, Object> model, HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		boolean dropShip = false;
		int site = this.foundation.getSiteID();
		LoginContext loginCtx = this.foundation.getLoginContext();
		String email = loginCtx != null ? loginCtx.getEmail() : null;
		if (n != null && n.endsWith("-DS")) {
			dropShip = true;
		}
//		model.put("mainurl", this.foundation.getMainDomain());
		if (n != null && !dropShip) {
			Order order = this.orderMapper.getOrderByOrderNumber(n);
			// 如果用户使用优惠后导致付款金额为0或负值时,不能去付款
			Double grandtotal = order.getFgrandtotal();
			if (grandtotal == null || grandtotal <= 0) {
				// 记录Paypal支付确认日志
				OrderOpreationEvent event = new OrderOpreationEvent(
						order.getIwebsiteid(), order.getCemail(),
						order.getCordernumber(), OpreationType.PAYPAL_DOPAYMENT,
						"Order total is invalid");
				event.setCresult(OpreationResult.FAILURE);
				eventBroker.post(event);
				
				// 跳转到支付失败页面
				StringBuilder errorurl = new StringBuilder("/payment-result/error");
				errorurl.append("?orderNum=").append(n);
				errorurl.append("&error=").append("Order total is invalid");
				response.sendRedirect(errorurl.toString());
				return null;
			}
		}

		// 重试url
		String retryUrl = "/paypal/ec-confirm?token=" + token + "&PayerID="
				+ PayerID + "&n=" + n;
		try{
			PaypalNvpPaymentStatus status = service.DoExpressCheckoutPayment(token,
					PayerID, n);
			String orderNum = status.getOrderNum();
			
			// 记录Paypal支付确认日志
			OrderOpreationEvent event = new OrderOpreationEvent(site, email,
					orderNum, OpreationType.PAYPAL_DOPAYMENT,
					JSON.toJSONString(status));
			
			if (status.isCompleted()) {
				eventBroker.post(event);
				Logger.debug("DoExpressCheckoutPayment successed");
				model.put("ordernumber", orderNum);
				return this.Completed(model, orderNum);
			}
			event.setCresult(OpreationResult.FAILURE);
			eventBroker.post(event);
			// 跳转到支付失败页面
			StringBuilder errorurl = new StringBuilder("/payment-result/error");
			errorurl.append("?orderNum=").append(orderNum);
			errorurl.append("&errorCode=").append(status.getErrorCode());
			errorurl.append("&error=").append(status.getFailedInfo());
			if (!loginCtx.isLogin()) {
				errorurl.append("&retryUrl=").append(retryUrl);
			}
			response.sendRedirect(errorurl.toString());
			return null;
		}catch(Exception e){
			// 记录Paypal支付确认日志
			OrderOpreationEvent event = new OrderOpreationEvent(site, email,
					n, OpreationType.PAYPAL_DOPAYMENT, e.getMessage());
			event.setCresult(OpreationResult.FAILURE);
			eventBroker.post(event);
			throw e;
		}
		
	}

	/**
	 * 处理用户确认过的订单
	 * 
	 * @return
	 * @throws IOException 
	 */
	@RequestMapping(value = "/ec-do", method = RequestMethod.POST)
	public String confirmOrder(@ModelAttribute OrderConfirmForm form,
			HttpServletRequest request, HttpServletResponse response,
			Map<String, Object> model) throws IOException {
		
		int site = this.foundation.getSiteID();
		LoginContext loginCtx = this.foundation.getLoginContext();
		String email = loginCtx != null ? loginCtx.getEmail() : null;
		String orderNum = form.getOrderNum();

		// 检查订单是否已经支付完成
		boolean isAlreadyPaid = orderService.isAlreadyPaid(orderNum);
		if (isAlreadyPaid) {
			
			// 记录EC支付确认日志
			OrderOpreationEvent event = new OrderOpreationEvent(site, email,
					orderNum, OpreationType.EC_PAY_CONFIRM,
					"Your order has been paid completed");
			event.setCresult(OpreationResult.FAILURE);
			eventBroker.post(event);
			
			// 跳转到支付失败页面
			StringBuilder errorurl = new StringBuilder("/payment-result/error");
			errorurl.append("?orderNum=").append(orderNum);
			errorurl.append("&error=").append("Your order has been paid completed");
			try {
				response.sendRedirect(errorurl.toString());
			} catch (IOException e) {
				e.printStackTrace();
			}
			return null;
		}
		// 判断国家是否是可发货的
		String countryCode = form.getCountryCode();
		Logger.debug("countryCode:{}", countryCode);
		Country country = countryService
				.getCountryByShortCountryName(countryCode);

		boolean isShipable = countryService.isShipable(country);
		Logger.debug("isShipable:{}", isShipable);
		Logger.debug("isShipable:{}", country.getBshow());
		if (!isShipable) {
			StringBuilder errorInfo = new StringBuilder();
			errorInfo.append(country.getCname());
			errorInfo.append(" shipping unavailable");
			
			// 跳转到支付失败页面
			StringBuilder errorurl = new StringBuilder("/payment-result/error");
			errorurl.append("?orderNum=").append(orderNum);
			errorurl.append("&error=").append(errorInfo.toString());
			
			// 记录EC支付确认日志
			OrderOpreationEvent event = new OrderOpreationEvent(site, email,
					orderNum, OpreationType.EC_PAY_CONFIRM,
					errorInfo.toString());
			event.setCresult(OpreationResult.FAILURE);
			eventBroker.post(event);
			try {
				response.sendRedirect(errorurl.toString());
			} catch (IOException e) {
				e.printStackTrace();
			}
			return null;
		}

		int lang = this.foundation.getLanguage();
		String shipCode = form.getShipMethodCode();
		Order originalOrder = orderMapper.getOrderByOrderNumber(form
				.getOrderNum());

		String currency = originalOrder.getCcurrency();

		List<CartItem> items = orderDetailDao
				.selectCartItemsByOrderNum(orderNum);

		List<String> listingId = Lists.newLinkedList();
		FluentIterable.from(items).forEach(item -> {
			if (item instanceof BundleCartItem) {
				listingId.addAll(((BundleCartItem) item).getAllListingId());
			} else {
				listingId.add(item.getClistingid());
			}
		});

		// 开始核对邮费方式是否正确
		ShippingMethod hit = orderService.checkShippingMethodCorrect(
				originalOrder.getIstorageid(), countryCode, shipCode,
				originalOrder.getFordersubtotal(), items, currency, lang);
		if (hit == null) {
			// 跳转到支付失败页面
			StringBuilder errorurl = new StringBuilder("/payment-result/error");
			errorurl.append("?orderNum=").append(orderNum);
			errorurl.append("&errorCode=").append("500");
			errorurl.append("&error=").append("shipping method is not correct");
			
			// 记录EC支付确认日志
			OrderOpreationEvent event = new OrderOpreationEvent(site, email,
					orderNum, OpreationType.EC_PAY_CONFIRM,
					"shipping method is not correct");
			event.setCresult(OpreationResult.FAILURE);
			eventBroker.post(event);
			try {
				response.sendRedirect(errorurl.toString());
			} catch (IOException e) {
				e.printStackTrace();
			}
			return null;
		}
		String token = form.getToken();
		String PayerID = form.getPayerID();

		Order order = new Order();
		order.setCordernumber(form.getOrderNum());
		order.setCfirstname(form.getCfirstname());
		order.setClastname(form.getClastname());
		String street = form.getAddress1();
		if (form.getAddress2() != null) {
			street = street + " " + form.getAddress2();
		}
		order.setCstreetaddress(street);
		order.setCprovince(form.getCprovince());
		order.setCcity(form.getCcity());
		order.setCpostalcode(form.getCpostalcode());
		order.setCtelephone(form.getCtelephone());
		order.setCmessage(form.getLeaveMessage());
		order.setCcountry(form.getCountryName());
		order.setCcountrysn(form.getCountryCode());
		// 开始计算邮费
		order.setCshippingcode(shipCode);
		order.setIshippingmethodid(hit.getId());
		Logger.debug("shipMethod:{}", shipCode);
		// recode
		double shipPrice = hit.getPrice();
		order.setFshippingprice(shipPrice);

		// 如果已经计算一次邮费了那么需要把以前的邮费减掉
		double total = originalOrder.getFgrandtotal();
		Double originalShipPrice = originalOrder.getFshippingprice();
		if (originalShipPrice != null && originalShipPrice > 0) {
			total = total - originalShipPrice;
		}

		total = total + shipPrice;

		// delete comma
		String money = Utils.money(total, currency);
		money = money.replaceAll(",", "");
		total = Double.parseDouble(money);
		order.setFgrandtotal(total);

		try{
			orderService.updateShipAddressAndShipPrice(order);
		}catch(Exception ex){
			OrderOpreationEvent event = new OrderOpreationEvent(site, email,
					orderNum, OpreationType.EC_PAY_CONFIRM,
					"updateShipAddressAndShipPrice method error ");
			event.setCresult(OpreationResult.FAILURE);
			
//			 跳转到支付失败页面
//			StringBuilder errorurl = new StringBuilder("/payment-result/error");
//			errorurl.append("?orderNum=").append(orderNum);
//			errorurl.append("&errorCode=").append("500");
//			errorurl.append("&error=").append("your shipping address error");
//			try {
//				response.sendRedirect(errorurl.toString());
//			} catch (IOException e) {
//				e.printStackTrace();
//			}		
			throw new CreateOrderException("your shipping address error");
		}
		
		// 记录EC支付确认日志
		OrderOpreationEvent event = new OrderOpreationEvent(site, email,
				orderNum, OpreationType.EC_PAY_CONFIRM,
				JSON.toJSONString(order));
		eventBroker.post(event);
		
		return this.DoExpressCheckoutPayment(token, PayerID, orderNum, model,
				request, response);
	}

	@RequestMapping("/ec-set")
	public String setExpressCheckoutForOrder(
			@RequestParam("n") String orderNum, Map<String, Object> model,
			HttpServletRequest request, HttpServletResponse response) {
		
		int site = this.foundation.getSiteID();
		LoginContext loginCtx = this.foundation.getLoginContext();
		String email = loginCtx != null ? loginCtx.getEmail() : null;
		
		if (orderNum == null || orderNum.length() == 0) {
			// 跳转到支付失败页面
			StringBuilder errorurl = new StringBuilder("/payment-result/error");
			errorurl.append("?orderNum=").append(orderNum);
			errorurl.append("&error=").append("your order num is null");
			try {
				response.sendRedirect(errorurl.toString());
			} catch (IOException e) {
				e.printStackTrace();
			}
			return null;
		}
		try {

			String host = this.foundation.getHost();
			if (!host.startsWith("http://")) {
				host = "http://" + host;
			}

			String returnUrl = host + "/paypal/ec-confirm?n=" + orderNum;

			Logger.debug("paypal returnUrl:{}", returnUrl);

			String cancalUrl = host + this.cartUrl;
			Logger.debug("paypal cancalUrl:{}", cancalUrl);

			SetExpressCheckout setEc = new SetExpressCheckout(orderNum,
					returnUrl, cancalUrl, true);
			PaypalNvpPaymentStatus status = service.setExpressCheckout(setEc);
			
			// 记录EC支付请求日志
			OrderOpreationEvent event = new OrderOpreationEvent(site, email,
					orderNum, OpreationType.EC_PAY_REQUEST,
					JSON.toJSONString(status));
			if (status != null && status.isNextStep()) {
				eventBroker.post(event);
				response.sendRedirect(status.getRedirectURL());
				return null;
			}
			event.setCresult(OpreationResult.FAILURE);
			eventBroker.post(event);
			// 跳转到支付失败页面
			StringBuilder errorurl = new StringBuilder("/payment-result/error");
			errorurl.append("?orderNum=").append(status.getOrderNum());
			errorurl.append("&error=").append(status.getFailedInfo());
			errorurl.append("&errorCode=").append(status.getErrorCode());
			try {
				response.sendRedirect(errorurl.toString());
			} catch (IOException e) {
				e.printStackTrace();
			}
			return null;

		}catch(InventoryShortageException e){
			// 记录开始结账日志
			OrderOpreationEvent event = new OrderOpreationEvent(site, email,
					null, OpreationType.EC_PAY_REQUEST, e.getMessage());
			event.setCresult(OpreationResult.FAILURE);
			eventBroker.post(event);
			Logger.error("express checkout for cart  failed", e);
			throw new InventoryShortageException(e.getMessage());
		} catch (Exception e) {
			Logger.error("express checkout for cart  failed", e);
			// 跳转到支付失败页面
			StringBuilder errorurl = new StringBuilder("/payment-result/error");
			errorurl.append("?orderNum=").append(orderNum);
			errorurl.append("&error=").append("exception occurred when express checkout for order");
			// 记录开始结账日志
			OrderOpreationEvent event = new OrderOpreationEvent(site, email,
					null, OpreationType.EC_PAY_REQUEST, e.getMessage());
			event.setCresult(OpreationResult.FAILURE);
			eventBroker.post(event);
			try {
				response.sendRedirect(errorurl.toString());
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			return null;
		}
	}

	@RequestMapping("/set-dropshipping")
	public String setExpressCheckoutForDropShipping(String dropShippingID,
			Map<String, Object> model, HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		LoginContext loginCtx = this.foundation.getLoginContext();
		if (!loginCtx.isLogin()) {
			// 跳转到支付失败页面
			StringBuilder errorurl = new StringBuilder("/payment-result/error");
			errorurl.append("&error=").append("you have to log in first");
			try {
				response.sendRedirect(errorurl.toString());
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			return null;
		}

		return this
				.setExpressCheckout(dropShippingID, model, request, response);
	}

	/**
	 * @author liudi 订单完成页面
	 * @throws IOException
	 */
	@RequestMapping("/completed")
	public String Completed(Map<String, Object> model, String ordernumber) {
		try {
			Request.currentResponse().sendRedirect(REDIRECT + ordernumber);
			return null;
		} catch (Exception e) {
			Logger.error("paypal redirect error", e);
			return null;
		}

		// LoginContext lc = foundation.getLoginContext();
		// Order order = this.orderMapper.getOrderByOrderNumber(ordernumber);
		// if (order != null) {
		// List<OrderDetail> dlist = detailMapper.getOrderDetailByOrderId(order
		// .getIid());
		// if ("USD".equals(order.getCcurrency())) {
		// model.put("order", order);
		// model.put("orderDetails", dlist);
		// } else {
		// // 美元总价
		// double usdGrandprice = currencyService.exchange(
		// order.getFgrandtotal(), order.getCcurrency(), "USD");
		// double shipPrice = currencyService.exchange(
		// order.getFshippingprice(), order.getCcurrency(), "USD");
		// double subtotal = currencyService.exchange(
		// order.getFordersubtotal(), order.getCcurrency(), "USD");
		// double extra = currencyService.exchange(
		// order.getFextra(), order.getCcurrency(), "USD");
		// order.setFgrandtotal(usdGrandprice);
		// order.setFshippingprice(shipPrice);
		// order.setFordersubtotal(subtotal);
		// order.setFextra(extra);
		// model.put("order", order);
		// for(OrderDetail od : dlist){
		// double odprice = currencyService.exchange(
		// od.getFprice(), order.getCcurrency(), "USD");
		// double odtotalprice = currencyService.exchange(
		// od.getFtotalprices(), order.getCcurrency(), "USD");
		// od.setFtotalprices(odtotalprice);
		// od.setFprice(odprice);
		// }
		// model.put("orderDetails", dlist);
		// }
		// }
		// return "/order/pay_success2";
	}
}
