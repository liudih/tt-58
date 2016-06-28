package com.tomtop.entity;

import java.io.Serializable;

/**
 * 首页品牌实体类
 */
public class HomeBrand implements Serializable {

	
	private static final long serialVersionUID = 9100090412374078005L;

	private String name;

	private String code;

	private String url;

	private String logoUrl;

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

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getLogoUrl() {
		return logoUrl;
	}

	public void setLogoUrl(String logoUrl) {
		this.logoUrl = logoUrl;
	}
}