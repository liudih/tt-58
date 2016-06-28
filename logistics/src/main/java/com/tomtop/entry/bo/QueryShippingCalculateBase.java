package com.tomtop.entry.bo;

import java.io.Serializable;

public class QueryShippingCalculateBase implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7245785285758542297L;
	/**
	 * 
	 */
	private String templateId;			  //模板ID
	private double price;				  //单价	
	private double weight;				  //重量	
	private String country;				  //国家	
	private int language;
	private String countryType;			   // 国家操作类型 0 包含，1 排除， 2 全部
	
	public int getLanguage() {
		return language;
	}
	public void setLanguage(int language) {
		this.language = language;
	}
	public String getCountryType() {
		return countryType;
	}
	public void setCountryType(String countryType) {
		this.countryType = countryType;
	}
	public String getTemplateId() {
		return templateId;
	}
	public void setTemplateId(String templateId) {
		this.templateId = templateId;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	public double getWeight() {
		return weight;
	}
	public void setWeight(double weight) {
		this.weight = weight;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	
	
	
}
