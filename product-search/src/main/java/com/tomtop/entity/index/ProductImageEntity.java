package com.tomtop.entity.index;

import java.io.Serializable;

/**
 * 产品数据格式
 * 
 * @author ztiny
 * @Date 2015-12-25
 */
public class ProductImageEntity implements Serializable {

	private static final long serialVersionUID = 2502281211062701703L;

	/**
	 * url
	 */
	private String url;
	/**
	 * 基础图
	 */
	private Boolean isBase;
	/**
	 * 排序
	 */
	private Integer order;
	/**
	 * 小图
	 */
	private Boolean isSmall;
	/**
	 * 是否为橱窗图
	 */
	private Boolean isThumb;
	/**
	 * 是否在详情页显示
	 */
	private Boolean isDetails;

	public ProductImageEntity(String url, Integer order, Boolean isBase, Boolean isSmall,Boolean isThumb,Boolean isDetails) {
		this.url = url;
		this.isBase = isBase;
		this.order = order;
		this.isSmall = isSmall;
		this.isThumb = isThumb;
		this.isDetails = isDetails;
	}

	public ProductImageEntity() {

	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Boolean getIsBase() {
		return isBase;
	}

	public void setIsBase(Boolean isBase) {
		this.isBase = isBase;
	}

	public Integer getOrder() {
		return order;
	}

	public void setOrder(Integer order) {
		this.order = order;
	}

	public Boolean getIsSmall() {
		return isSmall;
	}

	public void setIsSmall(Boolean isSmall) {
		this.isSmall = isSmall;
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
