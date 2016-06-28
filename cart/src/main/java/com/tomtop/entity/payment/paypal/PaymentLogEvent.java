package com.tomtop.entity.payment.paypal;

public class PaymentLogEvent {

	private final Integer websiteId;
	private final String orderNum;
	private final String content;
	private final String ctransactionId;

	public PaymentLogEvent(Integer websiteId, String orderNum, String content,
			String ctransactionId) {
		this.websiteId = websiteId;
		this.orderNum = orderNum;
		this.content = content;
		this.ctransactionId = ctransactionId;
	}

	public Integer getWebsiteId() {
		return websiteId;
	}

	public String getOrderNum() {
		return orderNum;
	}

	public String getContent() {
		return content;
	}

	public String getCtransactionId() {
		return ctransactionId;
	}

}
