package com.tomtop.member.models.dto;

import java.io.Serializable;

public class Purl implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1852399266530405934L;
	private String url;
	private String type;
	private String title;
	
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	
}
