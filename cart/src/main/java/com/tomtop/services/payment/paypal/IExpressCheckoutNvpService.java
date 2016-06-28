package com.tomtop.services.payment.paypal;

import java.util.Map;

import com.tomtop.valueobjects.payment.paypal.PaypalNvpPaymentStatus;
import com.tomtop.valueobjects.payment.paypal.SetExpressCheckout;

/**
 * 
 * @author lijun
 *
 */
public interface IExpressCheckoutNvpService {

	/**
	 * paypal setExpressCheckout
	 * 
	 * @author lijun
	 * @param returnUrl
	 *            全路径地址(http://....)
	 * @param cancelUrl
	 *            全路径地址(http://....)
	 * @return PaypalNvpPaymentStatus
	 */
	public PaypalNvpPaymentStatus setExpressCheckout(SetExpressCheckout set);

	/**
	 * 获取订单详情
	 * 
	 * @param token
	 * @param PayerID
	 * @param orderNum
	 * @return
	 */
	public Map<String, String> GetExpressCheckoutDetails(String token,
			String PayerID, String orderNum);

	public PaypalNvpPaymentStatus DoExpressCheckoutPayment(String token,
			String PayerID, String orderNum);

	/**
	 * 保存邮寄地址
	 * 
	 * @param token
	 * @param PayerID
	 * @param orderNum
	 * @return
	 */
	public boolean saveShipAddress(String token, String PayerID, String orderNum) throws Exception;

}