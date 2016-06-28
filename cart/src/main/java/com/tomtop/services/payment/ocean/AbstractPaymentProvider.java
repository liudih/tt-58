package com.tomtop.services.payment.ocean;

import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import com.tomtop.configuration.PaymentSettings;
import com.tomtop.dto.order.Order;
import com.tomtop.exceptions.OrderNocompleteException;
import com.tomtop.forms.PlaceOrderForm;
import com.tomtop.mappers.order.PaymentAccountMapper;
import com.tomtop.services.ICurrencyService;
import com.tomtop.services.base.ISystemParameterService;
import com.tomtop.services.payment.IPaymentProvider;
import com.tomtop.utils.DoubleCalculateUtils;
import com.tomtop.utils.FoundationService;
import com.tomtop.valueobjects.Constants;
import com.tomtop.valueobjects.order.PaymentContext;

/**
 * 
 * @author lijun
 *
 */
public abstract class AbstractPaymentProvider implements IPaymentProvider {
	private static final Logger Logger = LoggerFactory
			.getLogger(AbstractPaymentProvider.class);

	@Autowired
	private PaymentAccountMapper accountMapper;

	@Autowired
	private ICurrencyService currencyService;

	@Autowired
	private PaymentSettings setting;

	@Autowired
	private OceanPaymentService paymentService;

	@Autowired
	private ISystemParameterService systemParameterService;

	@Autowired
	FoundationService foundationService;

	@Value("${payment.oceanIpn}")
	String ipnUrl;

	@Value("${payment.oceanBackUrl}")
	String oceanBackUrl;

	// @Autowired
	// private IOrderUpdateService updateService;

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, String> getDoPaymentParas(PaymentContext context) {

		Order order = context.getOrder().getOrder();
		String ccy = order.getCcurrency();
		if (StringUtils.isEmpty(ccy)) {
			throw new OrderNocompleteException(order.getCordernumber(),
					"currency is null");
		}
		if (order.getFgrandtotal() == null || order.getFgrandtotal() < 0) {
			throw new OrderNocompleteException(order.getCordernumber(),
					"grandtotal invalid");
		}
		String paymentId = order.getCpaymentid();
		// 美元总价
		double usdGrandprice = currencyService.exchange(order.getFgrandtotal(),
				ccy, "USD");

		int site = this.foundationService.getSiteID();

		// 如果是信用卡支付那么要检测一下是否要走3D通道
		if (OceanPaymentCreditPaymentProvider.OCEAN_PAYMENT_CREDIT.equals(this
				.id())) {
			double grandprice = order.getFgrandtotal();

			String paramValue = systemParameterService.getSystemParameter(site,
					null, "credit_" + ccy);

			if (paramValue == null) {
				grandprice = usdGrandprice;
				paramValue = systemParameterService.getSystemParameter(1, null,
						"credit_USD", "300");
			}
			double pricelimit = Double.parseDouble(paramValue);

			// 如果大于某金额就进入信用卡3D验证支付
			if (grandprice > pricelimit) {
				Logger.debug("goto credit_3D_payment");
				paymentId = OceanPaymentCreditPaymentProvider.credit_3D_payment;
			}
		}

		String accountString = accountMapper.getAccount(site, usdGrandprice,
				paymentId);
		if (StringUtils.isEmpty(accountString)) {
			return null;
		}
		LinkedHashMap<String, String> account = JSONObject.parseObject(
				accountString, LinkedHashMap.class);

		Map<String, String> form = Maps.newHashMap();
		form.put("account", account.get("account"));
		form.put("terminal", account.get("terminal"));
		form.put("order_number", order.getCordernumber());
		form.put("order_currency", order.getCcurrency());
		form.put("order_amount", String.valueOf(new DoubleCalculateUtils(order
				.getFgrandtotal()).doubleValue()));
		// set logo
		form.put("logoUrl", foundationService.getLogo());

		// recode 不一定共用
		form.put("billing_firstName", order.getCfirstname());
		form.put("billing_lastName", order.getClastname());
		form.put("billing_email", order.getCemail());
		form.put("billing_phone", order.getCtelephone());
		form.put("billing_country", order.getCcountrysn());
		form.put("billing_state", order.getCprovince());
		form.put("billing_city", order.getCcity());
		form.put("billing_address", order.getCstreetaddress());
		form.put("billing_zip", order.getCpostalcode());

		form.put("ship_firstName", order.getCfirstname());
		form.put("ship_lastName", order.getClastname());
		form.put("ship_phone", order.getCtelephone());
		form.put("ship_country", order.getCcountrysn());
		form.put("ship_state", order.getCprovince());
		form.put("ship_city", order.getCcity());
		form.put("ship_addr", order.getCstreetaddress());
		form.put("ship_zip", order.getCpostalcode());

		String host = foundationService.getHost();
		//当站点为1的时候（tomtop.com）,使用http协议，其它站点回调地址用https协议
	
		if(site==1){
			if (!host.startsWith("http://")) {
				host = "http://" + host;
			}
		}else{
			String subdomains = foundationService.getSubdomains();
			if (!host.startsWith("https://")) {
				if(!Constants.SUBDOMAINS_M.equals(subdomains)){
					host = "https://" + host;
				}else{
					host = "http://" + host;
				}
			}
		}
		
		form.put("backUrl", host + oceanBackUrl);
		form.put("noticeUrl", ipnUrl);
		Map<String, String> productMap = paymentService
				.getOceanProductMap(context.getOrder().getDetails());

		form.put("productSku", productMap.get("productSku"));
		form.put("productName", productMap.get("productName"));
		form.put("productNum", productMap.get("productNum"));

		String method = getPaymentMethod();
		if (StringUtils.isEmpty(method)) {
			throw new NullPointerException("payment method is null");
		}
		form.put("methods", method);

		form.put("cart_api", "V1.6.0");

		// 每个特定支付方式需要的额外参数
		Map<String, String> extraParas = this.getExtraParas(order,
				context.getForm());
		if (extraParas != null) {
			extraParas.keySet().forEach(key -> {
				// 如果已经有了该key能不能覆盖需再考虑
					form.put(key, extraParas.get(key));
				});
		}
		form.put(
				"signValue",
				paymentService.getOceanPostSignValue(form,
						account.get("secureCode")));

		// Map<String, String> form = parseToForm(order, account,
		// context.getMap(), context.getOrder().getDetails());
		return form;
	}

	/**
	 * subclass need to override this method 获取支付额外参数
	 * 
	 * @param string
	 * @param map
	 * @param order
	 * @return
	 */
	public Map<String, String> getExtraParas(Order order, PlaceOrderForm form) {
		return null;
	};

	/**
	 * 获取支付方法name
	 * 
	 * @return
	 */
	public abstract String getPaymentMethod();

	@Override
	public int getDisplayOrder() {
		return 0;
	}

	@Override
	public String iconUrl() {
		return null;
	}

	@Override
	public String getDescription(PaymentContext context) {
		return null;
	}

	@Override
	public boolean isManualProcess() {
		return false;
	}

	@Override
	public String getInstruction(PaymentContext context) {
		return null;
	}

	@Override
	public boolean isNeedExtraInfo() {
		return true;
	}

	@Override
	public boolean validForm(PlaceOrderForm from) {
		return true;
	}

	@Override
	public String area() {
		return null;
	}
}
