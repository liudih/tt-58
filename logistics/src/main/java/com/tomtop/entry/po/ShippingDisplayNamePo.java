package com.tomtop.entry.po;

import java.io.Serializable;

public class ShippingDisplayNamePo implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8458912005405174556L;
	private Integer id;
	private String title;
	private String description;
	private Integer shippingTypeId;
	private Integer languageId;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
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

	public Integer getShippingTypeId() {
		return shippingTypeId;
	}

	public void setShippingTypeId(Integer shippingTypeId) {
		this.shippingTypeId = shippingTypeId;
	}

	public Integer getLanguageId() {
		return languageId;
	}

	public void setLanguageId(Integer languageId) {
		this.languageId = languageId;
	}

	@Override
	public String toString() {
		return "ShippingDisplayNamePo [id=" + id + ", title=" + title
				+ ", description=" + description + ", shippingTypeId="
				+ shippingTypeId + ", languageId=" + languageId + "]";
	}

}