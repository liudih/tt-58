package com.tomtop.valueobjects.payment.paypal;

import java.io.Serializable;

/**
 * 
 * @author lijun
 *
 */
public class SetExpressCheckout implements Serializable {

	private static final long serialVersionUID = 1L;

	// 是否允许用户留言
	private boolean allowNote = false;
	// 是否应用paypal上客户的shipping地址
	private boolean usePaypalAddress = false;
	// 当传地址给paypal时是否允许用户修改地址
	private boolean addroverride = false;
	// 订单号
	private final String orderNum;
	private final String returnUrl;
	private final String cancalUrl;
	// 是否是ec支付
	private final boolean isEc;

	public SetExpressCheckout(String orderNum, String returnUrl,
			String cancalUrl, boolean isEc) {
		this.orderNum = orderNum;
		this.returnUrl = returnUrl;
		this.cancalUrl = cancalUrl;
		this.isEc = isEc;
		if (isEc) {
			usePaypalAddress = true;
		}
	}

	public boolean isEc() {
		return isEc;
	}

	public boolean isAllowNote() {
		return allowNote;
	}

	public void setAllowNote(boolean allowNote) {
		this.allowNote = allowNote;
	}

	public boolean isUsePaypalAddress() {
		return usePaypalAddress;
	}

	public boolean isAddroverride() {
		return addroverride;
	}

	public void setAddroverride(boolean addroverride) {
		this.addroverride = addroverride;
	}

	public String getOrderNum() {
		return orderNum;
	}

	public String getReturnUrl() {
		return returnUrl;
	}

	public String getCancalUrl() {
		return cancalUrl;
	}

}
