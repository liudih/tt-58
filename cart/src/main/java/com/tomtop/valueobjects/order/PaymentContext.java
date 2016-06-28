package com.tomtop.valueobjects.order;

import java.io.Serializable;
import java.util.Map;

import com.tomtop.dto.Currency;
import com.tomtop.dto.order.ShippingMethodDetail;
import com.tomtop.forms.PlaceOrderForm;

public class PaymentContext implements Serializable {

	private static final long serialVersionUID = 1L;

	final ConfirmedOrder order;
	final ShippingMethodDetail shippingMethod;
	final Currency currency;
	private String orderLable;
	private String backUrl;
	private Integer billID;

	// 支付是走新流程还是老流程
	private boolean modeNew = false;

	private PlaceOrderForm form;

	public PaymentContext(ConfirmedOrder order,
			ShippingMethodDetail shippingMethod, Currency currency) {
		this.order = order;
		this.shippingMethod = shippingMethod;
		this.currency = currency;
	}

	public boolean isModeNew() {
		return modeNew;
	}

	public void setModeNew(boolean modeNew) {
		this.modeNew = modeNew;
	}

	public ConfirmedOrder getOrder() {
		return order;
	}

	public ShippingMethodDetail getShippingMethod() {
		return shippingMethod;
	}

	public Currency getCurrency() {
		return currency;
	}

	public String getOrderLable() {
		return orderLable;
	}

	public void setOrderLable(String orderLable) {
		this.orderLable = orderLable;
	}

	public String getBackUrl() {
		return backUrl;
	}

	public void setBackUrl(String backUrl) {
		this.backUrl = backUrl;
	}

	public Integer getBillID() {
		return billID;
	}

	public void setBillID(Integer billID) {
		this.billID = billID;
	}

	public PlaceOrderForm getForm() {
		return form;
	}

	public void setForm(PlaceOrderForm form) {
		this.form = form;
	}

}
