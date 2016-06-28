package com.tomtop.services.impl.payment.paypal;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpContent;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.UrlEncodedContent;
import com.google.api.client.util.Maps;
import com.tomtop.configuration.PaymentSettings;
import com.tomtop.dto.Country;
import com.tomtop.dto.Province;
import com.tomtop.dto.order.Order;
import com.tomtop.dto.order.OrderDetail;
import com.tomtop.entity.payment.paypal.PaymentBase;
import com.tomtop.entity.payment.paypal.PaymentLogEvent;
import com.tomtop.enums.PaymentStatus;
import com.tomtop.events.EventBroker;
import com.tomtop.events.order.OrderOpreationEvent;
import com.tomtop.events.order.OrderOpreationEvent.OpreationResult;
import com.tomtop.events.order.OrderOpreationEvent.OpreationType;
import com.tomtop.events.order.ReduceQtyEvent;
import com.tomtop.events.payment.PaymentCompletedEvent;
import com.tomtop.mappers.base.ProvinceMapper;
import com.tomtop.services.base.ICountryService;
import com.tomtop.services.base.IWebsiteService;
import com.tomtop.services.impl.order.DropShippingMapEnquiryService;
import com.tomtop.services.impl.order.DropShippoingToOrderService;
import com.tomtop.services.impl.order.OrderStatusService;
import com.tomtop.services.impl.order.PaymentConfirmationService;
import com.tomtop.services.impl.payment.PaymentServices;
import com.tomtop.services.order.IOrderService;
import com.tomtop.services.order.IOrderUpdateService;
import com.tomtop.services.payment.paypal.IExpressCheckoutNvpService;
import com.tomtop.utils.DoubleCalculateUtils;
import com.tomtop.utils.FoundationService;
import com.tomtop.utils.Utils;
import com.tomtop.valueobjects.base.LoginContext;
import com.tomtop.valueobjects.order.ConfirmedOrder;
import com.tomtop.valueobjects.order.PaymentContext;
import com.tomtop.valueobjects.payment.paypal.PaypalNvpPaymentStatus;
import com.tomtop.valueobjects.payment.paypal.SetExpressCheckout;

/**
 * 
 * @author lijun
 *
 */
@Service
public class ExpressCheckoutNvpService implements IExpressCheckoutNvpService {

	private static final Logger Logger = LoggerFactory
			.getLogger(ExpressCheckoutNvpService.class);

	// 超时时间
	public static final int TIMEOUT = 30 * 1000;
	public static final String TT_ACCOUNT = "ttAccount";
	private static final String TT_ORDER_ID = "ttOrderId";
	private static final String DROP_SHIP = "dropShip";

	// private String user;
	// private String pwd;
	// private String signature;
	private String version;
	// 是否沙箱
	private boolean sandbox = false;
	private String endPoint;
	private String redirectURL;
	private String img;
	private String ipnUrl;

	@Autowired
	FoundationService foundation;

	@Autowired
	PaymentServices paymentservices;

	@Autowired
	EventBroker eventBroker;

	@Autowired
	IOrderUpdateService updateService;

	@Autowired
	IOrderService orderService;

	@Autowired
	IWebsiteService websiteService;

	@Autowired
	DropShippoingToOrderService dropShippoingToOrder;

	@Autowired
	DropShippingMapEnquiryService dropShippingMapEnquiry;

	@Autowired
	PaymentConfirmationService paymentEnquiry;

	@Autowired
	ICountryService countryService;

	@Autowired
	ProvinceMapper provinceMapper;

	@Autowired
	HttpRequestFactory requestFactory;

	PaymentSettings config;

	@Autowired
	OrderStatusService orderStatusService;
	 
	@Autowired
	public ExpressCheckoutNvpService(PaymentSettings setting) {
		this.config = setting;

		this.version = setting.getPaypalVersion();
		if (version == null) {
			version = "98.0";
		}
		this.img = setting.getPaypalImg();

		Boolean isSandbox = setting.isSandbox();
		if (isSandbox != null) {
			this.sandbox = isSandbox;
		}
		Logger.debug("paypal expressCheckout sandbox:{}", this.sandbox);

		this.endPoint = setting.getPaypalEndPoint();
		Logger.debug("paypal endPoint:{}", this.endPoint);

		Logger.debug("paypal endPoint:{}", this.endPoint);

		this.redirectURL = setting.getPaypalRedirectURL();

		Logger.debug("paypal redirectURL:{}", this.redirectURL);

		ipnUrl = setting.getPaypalIpn();
		Logger.debug("paypal ipnUrl:{}", this.ipnUrl);

	}

	/**
	 * 确认配置参数能取到
	 */
	private void assertConfig() {
		if (this.endPoint == null || this.endPoint.length() == 0) {
			throw new NullPointerException(
					"can not find config:paypal.endPoint");
		}
		if (this.redirectURL == null || this.redirectURL.length() == 0) {
			throw new NullPointerException(
					"can not find config:paypal.redirectURL");
		}
		if (this.ipnUrl == null || this.ipnUrl.length() == 0) {
			throw new NullPointerException("can not find config:paypal.ipn");
		}
	}

	/**
	 * 设置账户密码
	 * 
	 * @param paras
	 */
	private void setSecurity(Map<String, String> params, Order order) {
		String user = null;
		String pwd = null;
		String signature = null;
		// 不同金额用不同的账号
		PaymentBase account = null;
		String accountStr = null;
		if (!this.sandbox) {
			account = paymentservices.GetPayment(order);
			if (account == null) {
				throw new NullPointerException(
						"can not get paypal account from t_payment_paypal_account");
			}
			accountStr = account.getCbusiness();
			// 这个参数是自定义的
			params.put(TT_ACCOUNT, accountStr);

			user = account.getCuser();
			pwd = account.getCpwd();
			signature = account.getCsignature();

		} else {
			user = this.config.getPaypalUser();
			pwd = this.config.getPaypalPwd();
			signature = this.config.getPaypalSignature();
		}
		if (user == null || user.length() == 0) {
			throw new NullPointerException("can not find config : user");
		}
		if (pwd == null || pwd.length() == 0) {
			throw new NullPointerException("can not find config : pwd");
		}
		if (signature == null || signature.length() == 0) {
			throw new NullPointerException("can not find config : signature");
		}

		params.put("USER", user);
		params.put("PWD", pwd);
		params.put("SIGNATURE", signature);
		params.put("VERSION", this.version);

		// set BRANDNAME
		String brandName = this.config.getBrandName();
		if (StringUtils.isNotEmpty(brandName)) {
			params.put("BRANDNAME", brandName);
		}
		// 设置主账号 子账号
		// 子账号
		params.put("RECEIVERBUSINESS", accountStr);
		// 主账号
		// params.put("RECEIVEREMAIL", accountStr);
		params.put("PAYMENTREQUEST_0_SELLERPAYPALACCOUNTID", accountStr);
	}

	/**
	 * 创建paypal express checkout 需传的参数
	 * 
	 * @param orderNum
	 * @return
	 */
	private Map<String, String> createTransactionDetails(String orderNum) {
		this.assertConfig();
		int langId = foundation.getLanguage();

		Map<String, String> params = Maps.newHashMap();

		PaymentContext context = null;
		if (orderNum != null && orderNum.endsWith("-DS")) {
			LoginContext loginCtx = this.foundation.getLoginContext();
			String email = loginCtx.getEmail();
			context = dropShippoingToOrder.getPaymentContext(orderNum, email);
			params.put(DROP_SHIP, "1");

		} else {
			context = orderService.getPaymentContext(orderNum, langId);
		}

		if (context == null || context.getOrder() == null) {
			throw new NullPointerException("order number:" + orderNum
					+ " is invalid");
		}

		ConfirmedOrder order = context.getOrder();
		
		//校验库存
		orderService.validateInventory(order.getOrder().getIstorageid(),order.getDetails());

		String paymentid = order.getOrder().getCpaymentid();
		Logger.debug("paymentid===={}", paymentid);

		params.put(TT_ORDER_ID, order.getOrder().getIid() != null ? order
				.getOrder().getIid().toString() : order.getOrder()
				.getCordernumber());

		// 不同金额用不同的账号
		this.setSecurity(params, order.getOrder());

		// 公司image
		if (this.img != null) {
			params.put("HDRIMG", foundation.getLogo());
			// params.put("LOGOIMG", foundation.getLogo());
		}

		// 这个参数不能改动
		params.put("PAYMENTREQUEST_0_PAYMENTACTION", "Sale");
		String currency = order.getOrder().getCcurrency();
		if (currency == null || currency.length() == 0) {
			Logger.debug("orderNum:{} currency is null", orderNum);
			throw new NullPointerException("currency is null");
		}
		// 币种
		params.put("PAYMENTREQUEST_0_CURRENCYCODE", currency);
		// 该订单最终用户需要支付总金额
		String total = Utils.money(order.getOrder().getFgrandtotal(), currency)
				.replaceAll(",", "");
		params.put("PAYMENTREQUEST_0_AMT", total);

		double itemsTotal = order.getOrder().getFordersubtotal();
		List<OrderDetail> items = order.getDetails();
		for (int i = 0; i < items.size(); i++) {
			OrderDetail od = items.get(i);
			String item_name = od.getCtitle();
			if (item_name == null || item_name.length() == 0) {
				item_name = od.getCsku();
			}
			params.put("L_PAYMENTREQUEST_0_AMT" + i,
					Utils.money(od.getFprice(), currency).replaceAll(",", ""));
			params.put("L_PAYMENTREQUEST_0_NUMBER" + i, od.getCsku());
			params.put("L_PAYMENTREQUEST_0_NAME" + i, item_name);
			params.put("L_PAYMENTREQUEST_0_QTY" + i, od.getIqty().toString());

		}

		// 邮费
		// If you specify a value for PAYMENTREQUEST_n_SHIPPINGAMT
		// you must also specify a value for PAYMENTREQUEST_n_ITEMAMT
		if (order.getOrder() != null
				&& order.getOrder().getFshippingprice() != null
				&& order.getOrder().getFshippingprice() != 0) {
			params.put("PAYMENTREQUEST_0_SHIPPINGAMT",
					Utils.money(order.getOrder().getFshippingprice(), currency)
							.replaceAll(",", ""));
		}
		// 折扣
		if (order.getOrder().getFextra() != null
				&& order.getOrder().getFextra() < 0) {
			double extra = new DoubleCalculateUtils(order.getOrder()
					.getFextra()).doubleValue();
			params.put("L_PAYMENTREQUEST_0_AMT" + items.size(),
					Utils.money(extra).replaceAll(",", ""));

			itemsTotal = itemsTotal + extra;
		}

		String itemsTotalStr = Utils.money(itemsTotal, currency).replaceAll(
				",", "");
		params.put("PAYMENTREQUEST_0_ITEMAMT", itemsTotalStr);

		StringBuilder userName = new StringBuilder();

		String firstName = order.getOrder().getCfirstname();
		if (firstName != null && firstName.length() > 0) {
			userName.append(firstName);
		}

		String middleName = order.getOrder().getCmiddlename();
		if (middleName != null && middleName.length() > 0) {
			userName.append(" ");
			userName.append(middleName);
		}

		String lastName = order.getOrder().getClastname();
		if (lastName != null && lastName.length() > 0) {
			userName.append(" ");
			userName.append(lastName);
		}
		// 如果订单中ccountrysn为空则代表是用户未登陆情况下快捷支付
		String country = order.getOrder().getCcountrysn();
		if (country != null && country.length() > 0) {
			// 设置shipping address
			params.put("PAYMENTREQUEST_0_SHIPTONAME", userName.toString());
			// 街道
			params.put("PAYMENTREQUEST_0_SHIPTOSTREET", order.getOrder()
					.getCstreetaddress());
			// city
			params.put("PAYMENTREQUEST_0_SHIPTOCITY", order.getOrder()
					.getCcity());

			// 如果是paypal就传简拼
			if (paymentid != null && "paypal".equals(paymentid)) {
				Country coun = countryService
						.getCountryByShortCountryName("US");
				Province pro = provinceMapper.getSnByName(order.getOrder()
						.getCprovince(), coun.getIid());
				String provincename = pro == null ? order.getOrder()
						.getCprovince() : pro.getCshortname();
				// 省份
				params.put("PAYMENTREQUEST_0_SHIPTOSTATE", provincename);
			} else {
				// 省份
				params.put("PAYMENTREQUEST_0_SHIPTOSTATE", order.getOrder()
						.getCprovince());
			}

			Logger.debug("params=====province==={}",
					params.get("PAYMENTREQUEST_0_SHIPTOSTATE").toString());

			// 邮编
			params.put("PAYMENTREQUEST_0_SHIPTOZIP", order.getOrder()
					.getCpostalcode());
			// 国家代码
			params.put("PAYMENTREQUEST_0_SHIPTOCOUNTRYCODE", order.getOrder()
					.getCcountrysn());
			// 电话号码
			params.put("PAYMENTREQUEST_0_SHIPTOPHONENUM", order.getOrder()
					.getCtelephone());

			// 立即支付
			// params.put("PAYMENTREQUEST_0_ALLOWEDPAYMENTMETHOD",
			// "InstantPaymentOnly");
		}
		return params;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * services.paypal.IExpressCheckoutNvpService#setExpressCheckout(java.lang
	 * .String, java.lang.String, java.lang.String)
	 */
	public PaypalNvpPaymentStatus setExpressCheckout(SetExpressCheckout set) {

		String orderNum = set.getOrderNum();
		String returnUrl = set.getReturnUrl();
		String cancelUrl = set.getCancalUrl();

		this.assertConfig();

		if (orderNum == null || orderNum.length() == 0) {
			throw new NullPointerException("orderNum can not null");
		}
		if (returnUrl == null || returnUrl.length() == 0) {
			throw new NullPointerException("returnUrl can not null");
		}
		if (cancelUrl == null || cancelUrl.length() == 0) {
			throw new NullPointerException("cancelUrl can not null");
		}
		// 更新订单的cpaymentid字段,该字段标示用户默认的支付方式是paypal
		this.updateService.updatePaymentIdByOrderNum("paypal", orderNum);

		int siteId = foundation.getSiteID();
		// 该过程的状态信息
		PaypalNvpPaymentStatus status = new PaypalNvpPaymentStatus();
		status.setStage(PaypalNvpPaymentStatus.STAGE_SET);
		status.setOrderNum(orderNum);
		status.setWebSiteId(siteId);

		// 先检查订单是否已经支付过
		boolean isAlreadyPaid = this.orderService.isAlreadyPaid(orderNum);
		if (isAlreadyPaid) {
			StringBuilder sb = new StringBuilder();
			sb.append("youer order:");
			sb.append(orderNum);
			sb.append(" already paid");
			status.setFailedInfo(sb.toString());
			return status;
		}
		// this.assertConfig();
		Map<String, String> params = this.createTransactionDetails(orderNum);
		// 是否让用户留言
		if (set.isAllowNote()) {
			params.put("ALLOWNOTE", "1");
		} else {
			// 不让用户留言
			params.put("ALLOWNOTE", "0");
		}
		// 是否可用用paypal中的地址
		if (set.isUsePaypalAddress()) {
			params.put("NOSHIPPING", "2");
		} else {
			params.put("NOSHIPPING", "1");
		}
		// 展示shipping address 但是不能修改
		if (set.isAddroverride()) {
			params.put("ADDROVERRIDE", "1");
		} else {
			params.put("ADDROVERRIDE", "0");
		}

		params.put("METHOD", "SetExpressCheckout");
		params.put("RETURNURL", returnUrl);
		params.put("CANCELURL", cancelUrl);
		params.put("PAYMENTREQUEST_0_INVNUM", orderNum);

		String notifyUrl = this.config.getPaypalIpn();

		params.put("NOTIFYURL", notifyUrl);

		Logger.debug("set paypal notifyurl:{}", notifyUrl);

		String orderId = params.get(TT_ORDER_ID);
		params.remove(TT_ORDER_ID);

		Logger.debug("params-*-**-*--*-*-{}", params);

		try {
			GenericUrl url = new GenericUrl(this.endPoint);
			HttpContent content = new UrlEncodedContent(params);

			HttpRequest request = requestFactory.buildPostRequest(url, content);

			String payload = request.execute().parseAsString();

			HashMap<String, String> response = this.deformatNVP(payload);

			status.setPaypalReply(response);

			Logger.debug("paypal setExpressCheckout response:{}", response);
			// 设置这个参数是为了后面记录日志时能区分出日志时哪个阶段产生的
			response.put("customStage", PaypalNvpPaymentStatus.STAGE_SET);

			if (response.containsKey("ACK")) {
				String ack = response.get("ACK");
				if ("Success".equals(ack)) {
					String token = response.get("TOKEN");
					if (set.isEc()) {
						status.setRedirectURL(this.redirectURL + token
								+ "&useraction=continue");
					} else {
						status.setRedirectURL(this.redirectURL + token
								+ "&useraction=commit");
					}
					status.setNextStep(true);
				}
			}
			if (response.containsKey("L_ERRORCODE0")) {
				status.setErrorCode(response.get("L_ERRORCODE0"));
			}
			if (response.containsKey("L_LONGMESSAGE0")) {
				status.setFailedInfo(response.get("L_LONGMESSAGE0"));
			}
			// 发送事件去记录日志
			Map<Object, Object> paras = Maps.newLinkedHashMap();
			paras.putAll(response);
			// 参数记录到日志中
			paras.put("paras", params);

			ObjectMapper objectMapper = new ObjectMapper();
			JSONObject json = objectMapper
					.convertValue(paras, JSONObject.class);
			PaymentLogEvent log = new PaymentLogEvent(siteId, orderNum,
					json.toString(), null);
			eventBroker.post(log);
			return status;

		} catch (Exception e) {
			Logger.error("post data to paypal error", e);
		}

		return status;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * services.paypal.IExpressCheckoutNvpService#GetExpressCheckoutDetails(
	 * java.lang.String, java.lang.String)
	 */
	public Map<String, String> GetExpressCheckoutDetails(String token,
			String PayerID, String orderNum) {
		Map<String, String> params = Maps.newHashMap();

		Order order = orderService.getOrderByOrderNumber(orderNum);

		this.setSecurity(params, order);

		params.put("METHOD", "GetExpressCheckoutDetails");
		params.put("TOKEN", token);
		params.put("PAYERID", PayerID);

		try {

			GenericUrl url = new GenericUrl(this.endPoint);
			HttpContent content = new UrlEncodedContent(params);

			HttpRequest request = requestFactory.buildPostRequest(url, content);

			String payload = request.execute().parseAsString();

			HashMap<String, String> response = this.deformatNVP(payload);

			Logger.debug("paypal getExpressCheckoutDetails response:{}",
					response);
			// 记录获取Paypal信息日志
			OrderOpreationEvent event = new OrderOpreationEvent(
					OpreationType.GET_PAYPAL_DETAIL, OpreationResult.SUCCESS,
					JSON.toJSONString(response));
			event.setCordernumber(orderNum);
			event.setCmemberemail(response.get("EMAIL"));
			eventBroker.post(event);
			return response;
		} catch (Exception e) {
			// 记录获取Paypal信息日志
			OrderOpreationEvent event = new OrderOpreationEvent(
					OpreationType.GET_PAYPAL_DETAIL, OpreationResult.FAILURE,
					e.getMessage());
			event.setCordernumber(orderNum);
			eventBroker.post(event);
			Logger.error("paypal getExpressCheckoutDetails error", e);
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * services.paypal.IExpressCheckoutNvpService#DoExpressCheckoutPayment(java
	 * .lang.String, java.lang.String, java.lang.String)
	 */
	public PaypalNvpPaymentStatus DoExpressCheckoutPayment(String token,
			String PayerID, String orderNum) {
		this.assertConfig();

		// 该过程的状态信息
		PaypalNvpPaymentStatus status = new PaypalNvpPaymentStatus();
		status.setStage(PaypalNvpPaymentStatus.STAGE_DO);

		Logger.debug("orderNum:{}", orderNum);
		Map<String, String> params = this.createTransactionDetails(orderNum);
		params.put("METHOD", "DoExpressCheckoutPayment");
		params.put("TOKEN", token);
		params.put("PAYERID", PayerID);

		String notifyUrl = this.config.getPaypalIpn();

		params.put("PAYMENTREQUEST_0_NOTIFYURL", notifyUrl);
		Logger.debug("do paypal notifyurl:{}", notifyUrl);

		String orderId = params.get(TT_ORDER_ID);
		params.remove(TT_ORDER_ID);

		boolean dropShip = params.get(DROP_SHIP) != null ? true : false;
		params.remove(DROP_SHIP);

		int siteId = this.foundation.getSiteID();
		int lang = this.foundation.getLanguage();

		status.setOrderNum(orderNum);
		status.setWebSiteId(siteId);

		try {

			GenericUrl url = new GenericUrl(this.endPoint);
			HttpContent content = new UrlEncodedContent(params);

			HttpRequest request = requestFactory.buildPostRequest(url, content);

			String payload = request.execute().parseAsString();

			HashMap<String, String> response = this.deformatNVP(payload);

			Logger.debug("paypal doExpressCheckoutPayment response:{}",
					response);

			status.setPaypalReply(response);

			String transactionId = response.get("PAYMENTINFO_0_TRANSACTIONID");
			String receiverEmail = response.get("receiver_email");
			String paymentStatus = response.get("PAYMENTINFO_0_PAYMENTSTATUS");
			String ack = response.get("ACK");
			
			// 发送事件去记录日志
			PaymentLogEvent log = new PaymentLogEvent(siteId, orderNum,
					JSON.toJSONString(response), transactionId);
			eventBroker.post(log);
			
			//如果响应失败或者未支付成功
			if (!responseSuccess(ack)) {
				status.setErrorCode(response.get("L_ERRORCODE0"));
				status.setFailedInfo(response.get("L_LONGMESSAGE0"));
				return status;
			}
			
			status.setPaymentStatus(paymentStatus);
			//把paypal返回的付款状态转换成tomtop中定义的付款状态
			PaymentStatus state = StatusTransformer.transformPaypal(paymentStatus);
			
			//如果是Pending，则需要取pending原因
			if(PaymentStatus.PENDING.equals(state)){
				status.setErrorCode(response.get("PAYMENTINFO_0_ERRORCODE"));
				status.setFailedInfo(response.get("PAYMENTINFO_0_PENDINGREASON"));
				return status;
			}else if(!PaymentStatus.CONFIRMED.equals(state)){
				status.setErrorCode(response.get("L_ERRORCODE0"));
				status.setFailedInfo(response.get("L_LONGMESSAGE0"));
				return status;
			}
			
			//设置支付成功，执行下一步
			status.setCompleted(true);
			
			if (dropShip) {
				List<String> orderIDs = dropShippingMapEnquiry
						.getOrderNumbersByID(orderNum);

				for (String id : orderIDs) {
					paymentEnquiry.confirmPayment(id,
							transactionId, lang);
				}

			} else {
				Order order = orderService.getOrderByOrderNumber(orderNum);
				//如果已经支付，则直接返回
				if(!order.notPaid()){
					return status;
				}
				//修改订单支付状态，IPN可能失败，同步修改确保订单状态正确
				boolean flag = orderStatusService.payOrder(orderNum,transactionId, receiverEmail);
				Logger.debug("Paypal同步修改订单状态,flag={}",flag);
				if(flag){
					// 发送事件修改库存
					ReduceQtyEvent event = new ReduceQtyEvent(orderNum,siteId);
					eventBroker.post(event);
					
					// 发送支付完成事件
					PaymentCompletedEvent et = new PaymentCompletedEvent(orderNum);
					eventBroker.post(et);
				}
			}
		} catch (Exception e) {
			Logger.error("paypal getExpressCheckoutDetails error", e);
		}
		return status;
	}
	
	/**
	 * 判断是否响应成功
	 * @param ack
	 * @return
	 */
	private boolean responseSuccess(String ack) {
		return StringUtils.isNotBlank(ack)
				&& ("success".equals(ack.toLowerCase()) || "successwithwarning"
						.equals(ack.toLowerCase()));
	}
	
	/**
	 * break the NVP string into a HashMap
	 * 
	 * @param payload
	 * @return a HashMap object containing all the name value pairs of the
	 *         string.
	 */
	private HashMap<String, String> deformatNVP(String payload) {
		HashMap<String, String> nvp = Maps.newHashMap();
		StringTokenizer stTok = new StringTokenizer(payload, "&");
		while (stTok.hasMoreTokens()) {
			StringTokenizer stInternalTokenizer = new StringTokenizer(
					stTok.nextToken(), "=");
			if (stInternalTokenizer.countTokens() == 2) {
				try {
					String key = URLDecoder.decode(
							stInternalTokenizer.nextToken(), "UTF-8");
					String value = URLDecoder.decode(
							stInternalTokenizer.nextToken(), "UTF-8");
					nvp.put(key.toUpperCase(), value);
				} catch (UnsupportedEncodingException e) {
					Logger.debug("deformatNVP error", e);
				}

			}
		}
		return nvp;
	}

	@Override
	public boolean saveShipAddress(String token, String PayerID, String orderNum) throws Exception {
		Map<String, String> result = this.GetExpressCheckoutDetails(token,
				PayerID, orderNum);

		Order originalOrder = orderService.getOrderByOrderNumber(orderNum);
		// 填充订单shipping地址
		Order order = new Order();
		order.setCordernumber(orderNum);
		// 国家
		String countryCode = result.get("SHIPTOCOUNTRYCODE");
		String country = result.get("SHIPTOCOUNTRYNAME");
		order.setCcountry(country);
		order.setCcountrysn(countryCode);
		// email
		String email = result.get("EMAIL");
		if (StringUtils.isNoneEmpty(email)) {
			email = email.toLowerCase();
		}
		order.setCemail(email);
		if (StringUtils.isEmpty(originalOrder.getCmemberemail())) {
			order.setCmemberemail(email);
		}
		// 省份
		String shipToState = result.get("SHIPTOSTATE");
		order.setCprovince(shipToState);
		// city
		String shipToCity = result.get("SHIPTOCITY");
		order.setCcity(shipToCity);
		// street
		String shipTosTreet = result.get("SHIPTOSTREET");
		String shipTosTreet2 = result.get("SHIPTOSTREET2");
		order.setCstreetaddress(shipTosTreet
				+ (shipTosTreet2 == null ? "" : " " + shipTosTreet2));
		// 邮编
		String shipToZip = result.get("SHIPTOZIP");
		order.setCpostalcode(shipToZip);
		// shiptoname
		String shipToName = result.get("SHIPTONAME");
		// 把名称拆成first last
		if (shipToName != null) {
			shipToName = shipToName.trim();
			int index = shipToName.lastIndexOf(" ");
			if (index != -1) {
				order.setCfirstname(shipToName.substring(0, index));
				order.setClastname(shipToName.substring(index + 1));
			} else {
				order.setCfirstname(shipToName);
			}
		}
		order.setCtelephone(result.get("PHONENUM"));

		return orderService.updateShipAddressAndShipPrice(order);
	}
}
