package com.tomtop.events.order;

import java.io.Serializable;

public class OrderConfirmationEvent implements Serializable {

	private static final long serialVersionUID = 1L;

	final String email;
	final String orderNum;
	final String currency;

	public OrderConfirmationEvent(String email, String orderNum, String currency) {
		this.email = email;
		this.orderNum = orderNum;
		this.currency = currency;
	}

	public String getEmail() {
		return email;
	}

	public String getOrderNum() {
		return orderNum;
	}

	public String getCurrency() {
		return currency;
	}

}
