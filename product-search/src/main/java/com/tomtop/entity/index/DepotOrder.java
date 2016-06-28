package com.tomtop.entity.index;


/**
 * 嵌套仓库排序
 * @author renyy
 * @Date 2016-06-08
 */
public class DepotOrder {
	//仓库名称
	private String depotName;
	//排序
	private int sort = 999;
	
	public DepotOrder() {

	}

	public DepotOrder(String depotName, Integer sort) {
		this.depotName = depotName;
		this.sort = sort;
	}

	public String getDepotName() {
		return depotName;
	}

	public void setDepotName(String depotName) {
		this.depotName = depotName;
	}

	public int getSort() {
		return sort;
	}

	public void setSort(int sort) {
		this.sort = sort;
	}
	
}
