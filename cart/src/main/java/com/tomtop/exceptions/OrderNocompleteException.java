package com.tomtop.exceptions;

/**
 * 订单必须字段信息不完整异常
 * 
 * @author lijun
 *
 */
public class OrderNocompleteException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	private final String orderNum;

	public OrderNocompleteException(String orderNum, String message) {
		super(message);
		this.orderNum = orderNum;
	}

	public String getOrderNum() {
		return orderNum;
	}

}
