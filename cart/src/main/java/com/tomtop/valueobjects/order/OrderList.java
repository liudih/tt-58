package com.tomtop.valueobjects.order;

import java.io.Serializable;
import java.util.List;

import com.tomtop.dto.Currency;
import com.tomtop.dto.order.BillDetail;
import com.tomtop.dto.order.Order;
public class OrderList implements Serializable {
	private static final long serialVersionUID = 1L;
	private Order order;
	private List<OrderItem> orderItems;
	//private List<BillDetail> orderBillDetails;
	private Currency currency;
	private String orderStatus;
	/**
	 * 追踪号
	 */
	private String trackingNumber;

	public Currency getCurrency() {
		return this.currency;
	}

	public void setCurrency(Currency currency) {
		this.currency = currency;
	}

	public Order getOrder() {
		return this.order;
	}

	public void setOrder(Order order) {
		this.order = order;
	}

	public List<OrderItem> getOrderItems() {
		return this.orderItems;
	}

	public void setOrderItems(List<OrderItem> orderItems) {
		this.orderItems = orderItems;
	}

//	public List<BillDetail> getOrderBillDetails() {
//		return this.orderBillDetails;
//	}
//
//	public void setOrderBillDetails(List<BillDetail> orderBillDetails) {
//		this.orderBillDetails = orderBillDetails;
//	}

	public String getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}

	public String getTrackingNumber() {
		return trackingNumber;
	}

	public void setTrackingNumber(String trackingNumber) {
		this.trackingNumber = trackingNumber;
	}

}
