package com.tomtop.services.payment.gleepay;

import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.tomtop.dto.order.Order;
import com.tomtop.mappers.order.PaymentAccountMapper;
import com.tomtop.services.ICurrencyService;
import com.tomtop.services.order.IOrderService;
import com.tomtop.utils.Encryption;
import com.tomtop.utils.FoundationService;
import com.tomtop.valueobjects.payment.gleepay.GleePayResponse;

/**
 * 鼎付支付SERVICE
 * @author liuchengqiang
 * @time 2016年6月21日 下午2:27:58
 */
@Service
public class GleePalService {

	private static Logger Logger = LoggerFactory
			.getLogger(GleePalService.class);

	@Autowired
	IOrderService orderService;

	@Autowired
	ICurrencyService currencyService;

	@Autowired
	Encryption encryption;
	
	@Autowired
	PaymentAccountMapper accountMapper;
	
	@Autowired
	FoundationService foundationService;

	/**
	 * 验证签名是否正确
	 * @param response
	 * @return
	 */
	public boolean validateSign(GleePayResponse response){
		return StringUtils.equalsIgnoreCase(response.getSignInfo(), getSignValue(response));
	}
	
	/**
	 * 获取密文
	 * @param response
	 * @param orderNum
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public String getSignValue(GleePayResponse response) {
		Order order = orderService.getOrderByOrderNumber(response.getOrderNo());
		if (order == null) {
			return null;
		}
		int site = foundationService.getSiteID();

		String ccy = order.getCcurrency();
		double usdgrandprice = currencyService.exchange(order.getFgrandtotal(),
				ccy, "USD");
		String paymentid = order.getCpaymentid();

		String accountString = accountMapper.getAccount(site, usdgrandprice,
				paymentid);
		if (StringUtils.isEmpty(accountString)) {
			Logger.error("对应的商家收款账号为空,usdGrandprice = {0}, paymentId = {1}",
					usdgrandprice, paymentid);
			return null;
		}
		Map<String, String> account = JSONObject.parseObject(accountString, Map.class);
		String text = new StringBuffer().append(response.getMerNo())
				.append(response.getGatewayNo()).append(response.getTradeNo())
				.append(response.getOrderNo())
				.append(response.getOrderCurrency())
				.append(response.getOrderAmount())
				.append(response.getOrderStatus())
				.append(response.getOrderInfo())
				.append(account.get("secureCode")).toString();
		return encryption.encodeSHA256(text);
	}
}
