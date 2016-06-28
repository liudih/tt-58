package com.tomtop.exceptions;

/**
 * 没有找过该支付方式
 * 
 * @author lijun
 *
 */
public class NotFindPaymentException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public NotFindPaymentException(String paymentId) {
		super("not find " + paymentId + " payment");
	}

}
