package com.tomtop.member.models.dto;

import java.io.Serializable;

public class Statistics implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8975472634114881543L;
	private String name;
	private Integer qty;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getQty() {
		if(qty == null){
			qty = 0;
		}
		return qty;
	}
	public void setQty(Integer qty) {
		this.qty = qty;
	}
}
