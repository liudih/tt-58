package com.tomtop.entry.form;

import java.io.Serializable;

public class ShippingPriceParamsDto implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 902076069220315297L;
	private String country;
	private double weight;
	private int storageId;
	private int shippingTypeId;
	//private String currency;
	//private double extra;
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public double getWeight() {
		return weight;
	}
	public void setWeight(double weight) {
		this.weight = weight;
	}
	public int getStorageId() {
		return storageId;
	}
	public void setStorageId(int storageId) {
		this.storageId = storageId;
	}
	public int getShippingTypeId() {
		return shippingTypeId;
	}
	public void setShippingTypeId(int shippingTypeId) {
		this.shippingTypeId = shippingTypeId;
	}
/*	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}*/
	/*public double getExtra() {
		return extra;
	}
	public void setExtra(double extra) {
		this.extra = extra;
	}*/
	
}