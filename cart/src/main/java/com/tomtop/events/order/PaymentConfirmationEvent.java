package com.tomtop.events.order;

import java.io.Serializable;

import com.tomtop.valueobjects.order.OrderValue;

public class PaymentConfirmationEvent implements Serializable {

	private static final long serialVersionUID = 1L;

	private final OrderValue orderValue;
	/**
	 * 交易号
	 */
	private final String transactionId;
	private final Integer languageId;

	public PaymentConfirmationEvent(OrderValue orderValue,
			String transactionId, Integer languageId) {
		this.orderValue = orderValue;
		this.transactionId = transactionId;
		this.languageId = languageId;
	}

	public String getTransactionId() {
		return transactionId;
	}

	public OrderValue getOrderValue() {
		return orderValue;
	}

	public Integer getLanguageId() {
		return languageId;
	}

}
