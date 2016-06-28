package com.tomtop.entity;


import java.io.Serializable;

public class ProductImage implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String imgUrl;
	/**
	 * 是否为小图
	 */
	private Boolean isSmall;
	/**
	 * 是否为主图
	 */
	private Boolean isMain;
	/**
	 * 是否为橱窗图
	 */
	private Boolean isThumb;
	/**
	 * 是否在详情页显示
	 */
	private Boolean isDetails;
	
	public String getImgUrl() {
		return imgUrl;
	}
	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}
	public Boolean getIsSmall() {
		return isSmall;
	}
	public void setIsSmall(Boolean isSmall) {
		this.isSmall = isSmall;
	}
	public Boolean getIsMain() {
		return isMain;
	}
	public void setIsMain(Boolean isMain) {
		this.isMain = isMain;
	}
	public Boolean getIsThumb() {
		return isThumb;
	}
	public void setIsThumb(Boolean isThumb) {
		this.isThumb = isThumb;
	}
	public Boolean getIsDetails() {
		return isDetails;
	}
	public void setIsDetails(Boolean isDetails) {
		this.isDetails = isDetails;
	}
	
}
