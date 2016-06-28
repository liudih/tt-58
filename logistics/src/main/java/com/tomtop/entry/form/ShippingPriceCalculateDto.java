package com.tomtop.entry.form;

import java.io.Serializable;

public class ShippingPriceCalculateDto implements Serializable,Comparable<ShippingPriceCalculateDto> {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2204732399879934196L;
	private String country;
	private int storageNameId;
	private int shippingTypeId;
	private String shippingCode;
	private double price;
	
	
	
	
	public int getStorageNameId() {
		return storageNameId;
	}
	public void setStorageNameId(int storageNameId) {
		this.storageNameId = storageNameId;
	}
	public int getShippingTypeId() {
		return shippingTypeId;
	}
	public void setShippingTypeId(int shippingTypeId) {
		this.shippingTypeId = shippingTypeId;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public String getShippingCode() {
		return shippingCode;
	}
	public void setShippingCode(String shippingCode) {
		this.shippingCode = shippingCode;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	@Override
	public int compareTo(ShippingPriceCalculateDto o) {
		if(o==null){
			return 0;
		}
		return o.getStorageNameId();
	}

}