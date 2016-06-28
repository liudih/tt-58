package com.tomtop.entity;

import java.io.Serializable;

public class ProductCollectVo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String listingId;
	private String sku;
	private String email;
	private Integer lang;
	private Integer client;
	private Integer website;
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
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public Integer getLang() {
		if(lang == null){
			lang = 1;
		}
		return lang;
	}
	public void setLang(Integer lang) {
		this.lang = lang;
	}
	public Integer getClient() {
		if(client == null){
			client = 1;
		}
		return client;
	}
	public void setClient(Integer client) {
		this.client = client;
	}
	public Integer getWebsite() {
		if(website == null){
			website = 1;
		}
		return website;
	}
	public void setWebsite(Integer website) {
		this.website = website;
	}
	
}
