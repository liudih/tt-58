package com.tomtop.entity.order;

/**
 * 支付失败实体类
 * 
 * @author lijun
 *
 */
public class PaymentError {

	private final String errorCode;
	private final String orderNum;
	private final String error;
	private String retryUrl;

	public PaymentError(String errorCode, String orderNum, String error) {
		this.errorCode = errorCode;
		this.orderNum = orderNum;
		this.error = error;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public String getOrderNum() {
		return orderNum;
	}

	public String getError() {
		return error;
	}

	public String getRetryUrl() {
		return retryUrl;
	}

	public void setRetryUrl(String retryUrl) {
		this.retryUrl = retryUrl;
	}

}
