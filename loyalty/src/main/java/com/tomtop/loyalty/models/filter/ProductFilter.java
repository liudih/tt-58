package com.tomtop.loyalty.models.filter;

import java.io.Serializable;

public class ProductFilter implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String listingId;
	private Double price;
	private Integer qty;

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public String getListingId() {
		return listingId;
	}

	public void setListingId(String listingId) {
		this.listingId = listingId;
	}

	public Integer getQty() {
		return qty;
	}

	public void setQty(Integer qty) {
		this.qty = qty;
	}

}
