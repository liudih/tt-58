package com.tomtop.loyalty.models;

import java.util.List;

public class Product {
	/**
	 * 
	 */
	private String listingId;
	private Double price;
	private Integer qty;
	private String sku;
	private List<Integer> categoryIds;
	private List<String> lables;

	public String getListingId() {
		return listingId;
	}

	public void setListingId(String listingId) {
		this.listingId = listingId;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public Integer getQty() {
		return qty;
	}

	public void setQty(Integer qty) {
		this.qty = qty;
	}

	public String getSku() {
		return sku;
	}

	public void setSku(String sku) {
		this.sku = sku;
	}

	public List<Integer> getCategoryIds() {
		return categoryIds;
	}

	public void setCategoryIds(List<Integer> categoryIds) {
		this.categoryIds = categoryIds;
	}

	public List<String> getLables() {
		return lables;
	}

	public void setLables(List<String> lables) {
		this.lables = lables;
	}

}
