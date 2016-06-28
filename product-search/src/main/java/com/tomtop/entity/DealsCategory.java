package com.tomtop.entity;

import java.io.Serializable;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@SuppressWarnings({ "deprecation" })
@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
public class DealsCategory implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8492526357471195256L;
	private Integer categoryId;
	private String cname;
	private String cpath;
	private Boolean isDiscount;
	
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
	public Boolean getIsDiscount() {
		return isDiscount;
	}
	public void setIsDiscount(Boolean isDiscount) {
		this.isDiscount = isDiscount;
	}
	
	
}
