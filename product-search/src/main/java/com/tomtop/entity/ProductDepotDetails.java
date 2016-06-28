package com.tomtop.entity;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class ProductDepotDetails implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String listingId;
	/**
	 * sku
	 */
	private String sku;
	/**
	 * 商品标题
	 */
	private String title;
	/**
	 * 图片集合地址
	 */
	private List<ProductImage> imgList;
	/**
	 * 属性集合
	 */
	private Map<String,String> attributeMap;
	
	/**
	 * 产品路由地址
	 */
	private String url;
	/**
	 * 301跳转url地址
	 */
	private String jumpUrl;
	/**
	 * 仓库名对应状态价格
	 */
	private Map<String,Depot> whouse;
	
	public String getListingId() {
		return listingId;
	}
	public void setListingId(String listingId) {
		this.listingId = listingId;
	}
	public String getSku() {
		return sku;
	}
	public void setSku(String sku) {
		this.sku = sku;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public List<ProductImage> getImgList() {
		return imgList;
	}
	public void setImgList(List<ProductImage> imgList) {
		this.imgList = imgList;
	}
	public Map<String, String> getAttributeMap() {
		return attributeMap;
	}
	public void setAttributeMap(Map<String, String> attributeMap) {
		this.attributeMap = attributeMap;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getJumpUrl() {
		return jumpUrl;
	}
	public void setJumpUrl(String jumpUrl) {
		this.jumpUrl = jumpUrl;
	}
	public Map<String, Depot> getWhouse() {
		return whouse;
	}
	public void setWhouse(Map<String, Depot> whouse) {
		this.whouse = whouse;
	}
}
