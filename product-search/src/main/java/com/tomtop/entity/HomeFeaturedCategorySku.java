package com.tomtop.entity;


/**
 * 首页特色类目的产品详细
 */
public class HomeFeaturedCategorySku extends ProductBase  {
	
	private static final long serialVersionUID = 143439999633173476L;
	/**
	 * 排序号
	 */
	private Integer sort = 1;

	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}
	
}