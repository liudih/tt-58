package com.tomtop.base.models.bo;

import com.tomtop.base.models.base.BaseBean;

public class LanguageBo extends BaseBean {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6145302644611968936L;
	private Integer id;
	private String name;//语言名称
	private String code;//标识
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
}
