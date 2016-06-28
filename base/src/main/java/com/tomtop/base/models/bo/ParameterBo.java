package com.tomtop.base.models.bo;

import com.tomtop.base.models.base.BaseBean;

public class ParameterBo extends BaseBean{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7058206746002522504L;
	private Integer id;
	private Integer languageId;//语言Id
	private Integer clientId;//客户端Id
	private String type;//参数类型，为大写字母组成
	private String value;//参数值
	private String name;//名称
	
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
	public String getType() {
		return checkNull(type);
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getName() {
		return checkNull(name);
	}
	public void setName(String name) {
		this.name = name;
	}
}
