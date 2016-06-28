package com.tomtop.entity.index;

/**
 * 嵌套标签排序
 * @author renyy
 * @Date 2016-06-08
 */
public class TagOrder {
	//仓库名称
	private String tagName;
	//排序
	private int sort = 999;
	
	public TagOrder() {

	}

	public TagOrder(String tagName, Integer sort) {
		this.tagName = tagName;
		this.sort = sort;
	}
	
	public String getTagName() {
		return tagName;
	}

	public void setTagName(String tagName) {
		this.tagName = tagName;
	}

	public int getSort() {
		return sort;
	}

	public void setSort(int sort) {
		this.sort = sort;
	}
	
}
