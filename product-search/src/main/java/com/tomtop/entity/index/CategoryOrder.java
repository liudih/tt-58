package com.tomtop.entity.index;

/**
 * 嵌套类目排序
 * @author renyy
 * @Date 2016-06-08
 */
public class CategoryOrder {

	//产品类目ID
	private int productTypeId;
	//排序
	private int sort = 999;
	
	public CategoryOrder(){
		
	}
	
	public CategoryOrder(Integer productTypeId, Integer sort) {
		this.productTypeId = productTypeId;
		this.sort = sort;
	}

	public int getProductTypeId() {
		return productTypeId;
	}

	public void setProductTypeId(int productTypeId) {
		this.productTypeId = productTypeId;
	}

	public int getSort() {
		return sort;
	}

	public void setSort(int sort) {
		this.sort = sort;
	}
	
}
