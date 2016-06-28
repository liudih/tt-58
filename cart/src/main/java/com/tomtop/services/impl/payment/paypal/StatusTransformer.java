package com.tomtop.services.impl.payment.paypal;

import org.apache.commons.lang.StringUtils;

import com.tomtop.enums.PaymentStatus;
import com.tomtop.valueobjects.payment.paypal.IPaypalPaymentStatus;

/**
 * 订单付款状态转换器(把状态转换成t_order_status表中的状态)
 * 
 * @author lijun
 *
 */
public class StatusTransformer {

	/**
	 * 把paypal返回的付款状态转换成tomtop中定义的付款状态
	 * 
	 * @param status
	 * @return
	 */
	public static PaymentStatus transformPaypal(String status) {
		PaymentStatus result = PaymentStatus.UN_PAY;
		
		if(StringUtils.isBlank(status)){
			return result;
		}

		switch (status) {
		case IPaypalPaymentStatus.COMPLETED:
		case IPaypalPaymentStatus.PROCESSED:
		case IPaypalPaymentStatus.CANCELED_REVERSAL:
			result = PaymentStatus.CONFIRMED;
			break;
		case IPaypalPaymentStatus.PENDING:
			result = PaymentStatus.PENDING;
			break;
		case IPaypalPaymentStatus.FAILED:
		case IPaypalPaymentStatus.EXPIRED:
		case IPaypalPaymentStatus.VOIDED:
		case IPaypalPaymentStatus.DENIED:
			result = PaymentStatus.FAILURE;
			break;
		case IPaypalPaymentStatus.REFUNDED:
		case IPaypalPaymentStatus.REVERSED:
			result = PaymentStatus.REFUNDED;
			break;
		default:
			break;
		}
		return result;
	}
}
