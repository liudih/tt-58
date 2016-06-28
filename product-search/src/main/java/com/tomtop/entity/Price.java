package com.tomtop.entity;

import java.io.Serializable;

public class Price implements Serializable {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4844016379247757279L;
	private Double price;//现价
	private Double costPrice;//原价
	
	public Double getPrice() {
		return price;
	}
	public void setPrice(Double price) {
		this.price = price;
	}
	public Double getCostPrice() {
		return costPrice;
	}
	public void setCostPrice(Double costPrice) {
		this.costPrice = costPrice;
	}
}
