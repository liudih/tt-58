package com.tomtop.entry.po;
import java.io.Serializable;
import java.util.List;

public class ShippingPriceCalculate implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2739729525915963743L;
	private List<ShippingCalculateBase> shippingCalculateBaseList;
	private String currency;
	private double totalWeight;				  //总重量
	private double totalPrice;				  //总价格
	private int storageId;					  //仓库
	private String country;				  	  //国家
	private int language;		   		  //语言
	//private int templateId;		   		      //模板
	
	private String surfaceTitle;
	private String surfaceDescription;
	private String registTitle;
	private String registDescription;
	private String expressTitle;
	private String expressDescription;
	private String specialTitle;
	private String specialDescription;
	private List<String> surfaceCodeList;
	private List<String> registCodeList;
	private List<String> expressCodeList;
	private List<String> specialCodeList;
	
	public List<String> getSurfaceCodeList() {
		return surfaceCodeList;
	}
	public void setSurfaceCodeList(List<String> surfaceCodeList) {
		this.surfaceCodeList = surfaceCodeList;
	}
	public List<String> getRegistCodeList() {
		return registCodeList;
	}
	public void setRegistCodeList(List<String> registCodeList) {
		this.registCodeList = registCodeList;
	}
	public List<String> getExpressCodeList() {
		return expressCodeList;
	}
	public void setExpressCodeList(List<String> expressCodeList) {
		this.expressCodeList = expressCodeList;
	}
	public List<String> getSpecialCodeList() {
		return specialCodeList;
	}
	public void setSpecialCodeList(List<String> specialCodeList) {
		this.specialCodeList = specialCodeList;
	}
	/*public int getTemplateId() {
		return templateId;
	}
	public void setTemplateId(int templateId) {
		this.templateId = templateId;
	}*/
	public String getSurfaceTitle() {
		return surfaceTitle;
	}
	public void setSurfaceTitle(String surfaceTitle) {
		this.surfaceTitle = surfaceTitle;
	}
	public String getSurfaceDescription() {
		return surfaceDescription;
	}
	public void setSurfaceDescription(String surfaceDescription) {
		this.surfaceDescription = surfaceDescription;
	}
	public String getRegistTitle() {
		return registTitle;
	}
	public void setRegistTitle(String registTitle) {
		this.registTitle = registTitle;
	}
	public String getRegistDescription() {
		return registDescription;
	}
	public void setRegistDescription(String registDescription) {
		this.registDescription = registDescription;
	}
	public String getExpressTitle() {
		return expressTitle;
	}
	public void setExpressTitle(String expressTitle) {
		this.expressTitle = expressTitle;
	}
	public String getExpressDescription() {
		return expressDescription;
	}
	public void setExpressDescription(String expressDescription) {
		this.expressDescription = expressDescription;
	}
	public String getSpecialTitle() {
		return specialTitle;
	}
	public void setSpecialTitle(String specialTitle) {
		this.specialTitle = specialTitle;
	}
	public String getSpecialDescription() {
		return specialDescription;
	}
	public void setSpecialDescription(String specialDescription) {
		this.specialDescription = specialDescription;
	}
	public int getLanguage() {
		return language;
	}
	public void setLanguage(int language) {
		this.language = language;
	}
	public int getStorageId() {
		return storageId;
	}
	public void setStorageId(int storageId) {
		this.storageId = storageId;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public double getTotalWeight() {
		return totalWeight;
	}
	public void setTotalWeight(double totalWeight) {
		this.totalWeight = totalWeight;
	}
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
	public List<ShippingCalculateBase> getShippingCalculateBaseList() {
		return shippingCalculateBaseList;
	}
	public void setShippingCalculateBaseList(
			List<ShippingCalculateBase> shippingCalculateBase) {
		this.shippingCalculateBaseList = shippingCalculateBase;
	}
	
	
	
}
