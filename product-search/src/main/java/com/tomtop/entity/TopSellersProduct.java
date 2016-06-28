package com.tomtop.entity;

import java.io.Serializable;

public class TopSellersProduct implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6669589627650661499L;
	private Integer categoryId;
	private String cname;
	private String cpath;
	private String listingId;
	private String sku;
	
	public Integer getCategoryId() {
		return categoryId;
	}
	public void setCategoryId(Integer categoryId) {
		this.categoryId = categoryId;
	}
	public String getCname() {
		return cname;
	}
	public void setCname(String cname) {
		this.cname = cname;
	}
	public String getCpath() {
		return cpath;
	}
	public void setCpath(String cpath) {
		this.cpath = cpath;
	}
	public String getListingId() {
		return listingId;
	}
	public void setListingId(String listingId) {
		this.listingId = listingId;
	}
	public String getSku() {
		return sku;
	}
	public void setSku(String sku) {
		this.sku = sku;
	}
	
}
