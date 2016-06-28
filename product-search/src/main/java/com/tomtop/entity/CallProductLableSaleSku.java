package com.tomtop.entity;

import java.util.List;

/**
 * callproduct
 * 
 * @author renyy
 *
 */
public class CallProductLableSaleSku extends BaseBean {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String sku;
	private List<String> lables;//商品的所有标签
	private List<Integer> categoryIds;//品类Id集合
	
	public String getSku() {
		return sku;
	}
	public void setSku(String sku) {
		this.sku = sku;
	}
	public List<String> getLables() {
		return lables;
	}
	public void setLables(List<String> lables) {
		this.lables = lables;
	}
	public List<Integer> getCategoryIds() {
		return categoryIds;
	}
	public void setCategoryIds(List<Integer> categoryIds) {
		this.categoryIds = categoryIds;
	}
	
}
