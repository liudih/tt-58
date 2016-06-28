package com.tomtop.entity;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class ProductDetails implements Serializable {

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
	 * 商品状态
	 */
	private Integer status;
	/**
	 * 数量
	 */
	private long qty;
	/**
	 * 是否为免邮商品
	 */
	private Boolean isFreeShipping;
	/**
	 * 是否为促销商品
	 */
	private Boolean isOnSale;
	/**
	 * 促销结束时间
	 */
	private String saleEndDate;
	/**
	 * 产品路由地址
	 */
	private String url;
	/**
	 * 是否为清仓产品
	 */
	private Boolean isCleanStocks;
	/**
	 * 现价格
	 */
	private String nowprice;

	/**
	 * 原价
	 */
	private String origprice;
	/**
	 * 货币使用的符号
	 */
	private String symbol = "$";
	
	public Boolean getIsCleanStocks() {
		return isCleanStocks;
	}
	public void setIsCleanStocks(Boolean isCleanStocks) {
		this.isCleanStocks = isCleanStocks;
	}
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
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public long getQty() {
		return qty;
	}
	public void setQty(long qty) {
		this.qty = qty;
	}
	public Boolean getIsFreeShipping() {
		return isFreeShipping;
	}
	public void setIsFreeShipping(Boolean isFreeShipping) {
		this.isFreeShipping = isFreeShipping;
	}
	public Boolean getIsOnSale() {
		return isOnSale;
	}
	public void setIsOnSale(Boolean isOnSale) {
		this.isOnSale = isOnSale;
	}
	public String getSaleEndDate() {
		return saleEndDate;
	}
	public void setSaleEndDate(String saleEndDate) {
		this.saleEndDate = saleEndDate;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getNowprice() {
		return nowprice;
	}
	public void setNowprice(String nowprice) {
		this.nowprice = nowprice;
	}
	public String getOrigprice() {
		return origprice;
	}
	public void setOrigprice(String origprice) {
		this.origprice = origprice;
	}
	public String getSymbol() {
		return symbol;
	}
	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}
}
