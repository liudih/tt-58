package com.tomtop.member.models.base;

import java.io.Serializable;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * 过滤条件有需要时可继承的基本类
 * @author renyy
 *
 */
@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
public class FilterBaseBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2861400560442550557L;
	/**
	 * 站点Id
	 */
	private Integer website;	
	/**
	 * 客户端Id
	 */
	private Integer client;	
	/**
	 * 语言id
	 */
	private Integer lang;	
	/**
	 * 币种
	 */
	private String currency;	
	/**
	 * 国家id
	 */
	private Integer country;
	
	public Integer getWebsite() {
		if(website == null){
			website = 1;
		}
		return website;
	}
	public void setWebsite(Integer website) {
		this.website = website;
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
	public Integer getLang() {
		if(lang == null){
			lang = 1;
		}
		return lang;
	}
	public void setLang(Integer lang) {
		this.lang = lang;
	}

	public String getCurrency() {
		if(currency == null){
			currency = "";
		}
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	public Integer getCountry() {
		if(country == null){
			country = 1;
		}
		return country;
	}
	public void setCountry(Integer country) {
		this.country = country;
	}	
}
