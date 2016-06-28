package com.tomtop.events.payment;


/**
 * 订单交易完成事件
 * @author lichengqiang
 *
 */
public class PaymentCompletedEvent {
	// 订单号
	final String orderNum;
	
	public PaymentCompletedEvent(String orderNum) {
		this.orderNum = orderNum;
	}
	
	
	public String getOrderNum() {
		return orderNum;
	}
}
