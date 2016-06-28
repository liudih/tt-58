package com.tomtop.entry.vo;

import java.io.Serializable;

public class ShippingTitleDescribeParamsVo implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7781423518199421869L;
	/**
	 * 
	 */
	private String id;
	private String title;
	private String description;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
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
	
	
	
}
