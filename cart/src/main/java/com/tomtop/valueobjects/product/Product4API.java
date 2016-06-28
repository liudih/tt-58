package com.tomtop.valueobjects.product;

import java.io.Serializable;
import java.util.Map;

/**
 * 产品信息实体类,用于外部接口数据接收
 * 
 * @author shuliangxing
 *
 * @date 2016年6月3日 下午4:56:01
 */
public class Product4API implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7030383640531985617L;
	/** 产品唯一标识 */
	private String listingId;
	/** 标题 */
	private String title;
	/** 路由地址 */
	private String url;
	/** 图片地址 */
	private String imageUrl;
	/** sku */
	private String sku;
	/** 产品状态 */
	private Integer status;

	/** 产品属性 */
	private Map<String, String> attributeMap;

	public Product4API() {

	}

	public Product4API(String listingId, String title, String url,
			String imageUrl, String sku, Integer status,
			Map<String, String> attributeMap) {
	//	super();
		this.listingId = listingId;
		this.title = title;
		this.url = url;
		this.imageUrl = imageUrl;
		this.sku = sku;
		this.status = status;
		this.attributeMap = attributeMap;
	}

	public String getListingId() {
		return listingId;
	}

	public void setListingId(String listingId) {
		this.listingId = listingId;
	}

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

	public String getSku() {
		return sku;
	}

	public void setSku(String sku) {
		this.sku = sku;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Map<String, String> getAttributeMap() {
		return attributeMap;
	}

	public void setAttributeMap(Map<String, String> attributeMap) {
		this.attributeMap = attributeMap;
	}

	@Override
	public String toString() {
		return "ProductForServices [listingId=" + listingId + ", title="
				+ title + ", url=" + url + ", imageUrl=" + imageUrl + ", sku="
				+ sku + ", status=" + status + ", attributeMap=" + attributeMap
				+ "]";
	}

}
