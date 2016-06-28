package com.tomtop.entry.form;

import java.io.Serializable;

public class ShippingPriceResultItem  implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -5663318534657386133L;
	/**
	 * 
	 */
	private int id;
	private int order;
	private Boolean isStrack;
	private String type;
	private Double price;	
	private String code;	
	private String title;
	private String description;
	private Boolean isShow;
	private String errorCode;
	private String errorDescription;
	
	public String getErrorCode() {
		return errorCode;
	}
	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}
	public String getErrorDescription() {
		return errorDescription;
	}
	public void setErrorDescription(String errorDescription) {
		this.errorDescription = errorDescription;
	}
	public Boolean getIsShow() {
		return isShow;
	}
	public void setIsShow(Boolean isShow) {
		this.isShow = isShow;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public Boolean getIsStrack() {
		return isStrack;
	}
	public void setIsStrack(Boolean isStrack) {
		this.isStrack = isStrack;
	}
	public Double getPrice() {
		return price;
	}
	public void setPrice(Double price) {
		this.price = price;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public int getOrder() {
		return order;
	}
	public void setOrder(int order) {
		this.order = order;
	}
	@Override
	public String toString() {
		return "ShippingPriceResultItem [id=" + id + ", order=" + order
				+ ", isStrack=" + isStrack + ", type=" + type + ", price="
				+ price + ", code=" + code + ", title=" + title
				+ ", description=" + description + ", isShow=" + isShow + "]";
	}
	
	
}
