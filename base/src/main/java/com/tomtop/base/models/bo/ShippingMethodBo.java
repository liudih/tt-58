package com.tomtop.base.models.bo;

import java.io.Serializable;

public class ShippingMethodBo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6706401591477527538L;
	private Integer storageid;
	private Integer languageid;
	private String title;
	private String content;
	private String code;
	private String imageurl;
	private Integer groupid;

	public Integer getStorageid() {
		return storageid;
	}

	public void setStorageid(Integer storageid) {
		this.storageid = storageid;
	}

	public Integer getLanguageid() {
		return languageid;
	}

	public void setLanguageid(Integer languageid) {
		this.languageid = languageid;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getImageurl() {
		return imageurl;
	}

	public void setImageurl(String imageurl) {
		this.imageurl = imageurl;
	}

	public Integer getGroupid() {
		return groupid;
	}

	public void setGroupid(Integer groupid) {
		this.groupid = groupid;
	}
}
