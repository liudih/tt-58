package com.tomtop.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 商品的基本信息 (多仓库)
 */
public class ProductBaseDepot implements Serializable {

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
	 * 多仓库
	 */
	private List<Depot> dlist = new ArrayList<Depot>(); 

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

	public List<Depot> getDlist() {
		return dlist;
	}

	public void setDlist(List<Depot> dlist) {
		this.dlist = dlist;
	}
	
}
