package com.tomtop.loyalty.models.filter;

public class PreferFilter {

	private String code;
	private String preferType;
	private String extra;
	private String orderNumber;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getPreferType() {
		return preferType;
	}

	public void setPreferType(String preferType) {
		this.preferType = preferType;
	}

	public String getExtra() {
		return extra;
	}

	public void setExtra(String extra) {
		this.extra = extra;
	}

	public String getOrderNumber() {
		return orderNumber;
	}

	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
	}

}
