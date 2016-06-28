package com.tomtop.entry.bo;

import java.io.Serializable;
import java.util.List;

public class ShippingCalculateLessParam implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2440143096667170307L;
	private String currency;
	//private double totalWeight;				  //总重量
	private double totalPrice;				  //总价格
	private int storageId;					  //仓库
	private String country;				  	  //国家
	private int language;		   		      //语言
	private List<ShippingCalculateLessParamBase> ShippingCalculateLessParamBase;
	
	public int getLanguage() {
		return language;
	}
	public void setLanguage(int language) {
		this.language = language;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public int getStorageId() {
		return storageId;
	}
	public void setStorageId(int storageId) {
		this.storageId = storageId;
	}
/*	public double getTotalWeight() {
		return totalWeight;
	}
	public void setTotalWeight(double totalWeight) {
		this.totalWeight = totalWeight;
	}*/
	public double getTotalPrice() {
		return totalPrice;
	}
	public void setTotalPrice(double totalPrice) {
		this.totalPrice = totalPrice;
	}
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	public List<ShippingCalculateLessParamBase> getShippingCalculateLessParamBase() {
		return ShippingCalculateLessParamBase;
	}
	public void setShippingCalculateLessParamBase(
			List<ShippingCalculateLessParamBase> shippingCalculateLessParamBase) {
		ShippingCalculateLessParamBase = shippingCalculateLessParamBase;
	}
	
	
}
