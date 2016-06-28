package com.tomtop.base.models.dto;

import java.io.Serializable;

public class CategoryShapeDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4031048632268415307L;
	private Integer client;
	private Integer categoryId;
	private String path;
	private String name;
	private Integer type;
	public Integer getClient() {
		return client;
	}
	public void setClient(Integer client) {
		this.client = client;
	}
	public Integer getCategoryId() {
		return categoryId;
	}
	public void setCategoryId(Integer categoryId) {
		this.categoryId = categoryId;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
}
