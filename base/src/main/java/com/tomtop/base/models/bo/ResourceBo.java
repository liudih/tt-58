package com.tomtop.base.models.bo;

import com.tomtop.base.models.base.BaseBean;

public class ResourceBo extends BaseBean {

	/**
	 * 
	 */
	private static final long serialVersionUID = -952738932752902318L;
	private Integer id;
	private Integer languageId;//语言Id
	private Integer clientId;//客户端Id
	private String key;
	private String value;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getLanguageId() {
		return languageId;
	}
	public void setLanguageId(Integer languageId) {
		this.languageId = languageId;
	}
	public Integer getClientId() {
		return clientId;
	}
	public void setClientId(Integer clientId) {
		this.clientId = clientId;
	}
	public String getKey() {
		return checkNull(key);
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getValue() {
		return checkNull(value);
	}
	public void setValue(String value) {
		this.value = value;
	}
}
