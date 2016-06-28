package com.tomtop.entity;

import java.io.Serializable;

/**
 * 商品的基本信息
 */
public class ProductBase implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2169391163527715996L;
	/**
	 * 标题
	 */
	private String title;
	/**
	 * url
	 */
	private String url;
	/**
	 * 图片地址
	 */
	private String imageUrl;
	/**
	 * 货币使用的符号
	 */
	private String symbol = "$";
	/**
	 * 现价格
	 */
	private String nowprice;

	/**
	 * 原价
	 */
	private String origprice;

	private String listingId;

	private String sku;

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

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public String getSymbol() {
		return symbol;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
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
}
