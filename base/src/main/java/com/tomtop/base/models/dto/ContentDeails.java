package com.tomtop.base.models.dto;

import com.tomtop.base.models.base.BaseBean;

public class ContentDeails extends BaseBean {

	/**
	 * 
	 */
	private static final long serialVersionUID = -384258830351276901L;
	private String title;
	private String url;
	private String content;
	private String metaTitle;
	private String metaKeyword;
	private String metaDescription;
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getMetaTitle() {
		return metaTitle;
	}
	public void setMetaTitle(String metaTitle) {
		this.metaTitle = metaTitle;
	}
	public String getMetaKeyword() {
		return metaKeyword;
	}
	public void setMetaKeyword(String metaKeyword) {
		this.metaKeyword = metaKeyword;
	}
	public String getMetaDescription() {
		return metaDescription;
	}
	public void setMetaDescription(String metaDescription) {
		this.metaDescription = metaDescription;
	}
}
