package com.tomtop.services.payment.gleepay;

import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import com.tomtop.dto.order.Order;
import com.tomtop.dto.order.OrderDetail;
import com.tomtop.exceptions.OrderNocompleteException;
import com.tomtop.forms.PlaceOrderForm;
import com.tomtop.mappers.order.PaymentAccountMapper;
import com.tomtop.services.ICurrencyService;
import com.tomtop.services.order.IOrderService;
import com.tomtop.services.payment.IPaymentProvider;
import com.tomtop.utils.DoubleCalculateUtils;
import com.tomtop.utils.Encryption;
import com.tomtop.utils.FoundationService;
import com.tomtop.valueobjects.order.PaymentContext;

/**
 * 鼎付支付
 * @author liuchengqiang
 * @time 2016年6月18日 下午4:40:47
 */
public abstract class GleePayPaymentProvider implements IPaymentProvider {
	
	protected static final Logger Logger = LoggerFactory
			.getLogger(GleePayPaymentProvider.class);

	@Autowired
	private PaymentAccountMapper accountMapper;

	@Autowired
	private ICurrencyService currencyService;

	@Autowired
	private FoundationService foundationService;
	
	@Autowired
	private Encryption encryption;
	
	@Autowired
	IOrderService orderService;

	@Value("${payment.gleepayIpn}")
	String ipnUrl;

	@Value("${payment.gleepayBackUrl}")
	String gleePayBackUrl;
	
	@Value("${payment.gleepayUrl}")
	String actionUrl;
	
	private static final String ATTRIBUTE_SEPARATOR = "#,#";
	private static final String GOODS_SEPARATOR = "#;#";

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

		String accountString = accountMapper.getAccount(site, usdGrandprice,
				paymentId);
		if (StringUtils.isBlank(accountString)) {
			Logger.error("对应的商家收款账号为空,usdGrandprice = {0}, paymentId = {1}",
					usdGrandprice, paymentId);
			return null;
		}
		Map<String, String> account = JSONObject.parseObject(
				accountString, Map.class);

		Map<String, String> form = Maps.newHashMap();
		form.put("merNo", account.get("merNo"));
		form.put("gatewayNo", account.get("gatewayNo"));
		form.put("orderNo", order.getCordernumber());
		form.put("orderCurrency", order.getCcurrency());
		form.put("orderAmount", String.valueOf(new DoubleCalculateUtils(order
				.getFgrandtotal()).doubleValue()));
		form.put("shipFee", String.valueOf(new DoubleCalculateUtils(order
				.getFshippingprice()).doubleValue()));
		form.put("discount", String.valueOf(new DoubleCalculateUtils(order
				.getFextra()).doubleValue()));

		form.put("firstName", order.getCfirstname());
		form.put("lastName", order.getClastname());
		form.put("phone", order.getCtelephone());
		form.put("email", order.getCmemberemail());
		form.put("country", order.getCcountrysn());
		form.put("customerID", order.getCmemberemail());
		form.put("state", order.getCprovince());
		form.put("city", order.getCcity());
		form.put("address", order.getCstreetaddress());
		form.put("zip", order.getCpostalcode());

		String host = foundationService.getHost();
		//当站点为1的时候（tomtop.com）,使用http协议，其它站点回调地址用https协议
	
		if(site==1){
			if (!host.startsWith("http://")) {
				host = "http://" + host;
			}
		}else{
			if (!host.startsWith("https://")) {
				host = "https://" + host;
			}
		}
		
		form.put("returnUrl", host + gleePayBackUrl);
		form.put("notifyUrl", ipnUrl);

		form.put("goodsInfo", goodsInfo(context.getOrder().getDetails()));

		String method = getPaymentMethod();
		if (StringUtils.isEmpty(method)) {
			throw new NullPointerException("payment method is null");
		}
		form.put("paymentMethod", method);
		form.put("actionUrl", actionUrl);
		// 每个特定支付方式需要的额外参数
		Map<String, String> extraParas = this.getExtraParas(order,
				context.getForm());
		if (extraParas != null) {
			extraParas.keySet().forEach(key -> {
					form.put(key, extraParas.get(key));
				});
		}
		form.put("signInfo",getSignValue(form,account.get("secureCode")));
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
	
	/**
	 * 获取密文
	 * @param form
	 * @param secureCode
	 * @return
	 */
	private String getSignValue(Map<String, String> form, String secureCode) {
		String text = new StringBuffer().append(form.get("merNo"))
				.append(form.get("gatewayNo")).append(form.get("orderNo"))
				.append(form.get("orderCurrency"))
				.append(form.get("orderAmount")).append(form.get("returnUrl"))
				.append(secureCode).toString();
		return encryption.encodeSHA256(text);
	}
	
	/**
	 * 拼接产品信息，用于支付
	 * @param orderDetails
	 * @return
	 */
	private String goodsInfo(List<OrderDetail> orderDetails){
		if(CollectionUtils.isEmpty(orderDetails)){
			return null;
		}
		StringBuffer goodsInfo = new StringBuffer();
		for (OrderDetail detail : orderDetails) {
			goodsInfo.append(detail.getCtitle()).append(ATTRIBUTE_SEPARATOR)
					.append(detail.getClistingid()).append(ATTRIBUTE_SEPARATOR)
					.append(detail.getFprice()).append(ATTRIBUTE_SEPARATOR)
					.append(detail.getIqty()).append(GOODS_SEPARATOR);
		}
		String goodsInfoStr = goodsInfo.toString();
		if(goodsInfoStr.endsWith(GOODS_SEPARATOR)){
			goodsInfoStr = StringUtils.substringBeforeLast(goodsInfoStr, GOODS_SEPARATOR);
		}
		return goodsInfoStr;
	}
}
