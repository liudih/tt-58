package com.tomtop.member.models.dto;

import java.io.Serializable;

public class OrderDetailComment implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6297258210907761945L;
	private Integer cid;
	private String listingId;
	private String sku;
	
	public Integer getCid() {
		return cid;
	}
	public void setCid(Integer cid) {
		this.cid = cid;
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
