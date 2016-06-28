package com.tomtop.entity;

import java.io.Serializable;

/**
 * 首页特色类目的搜索关键字
 */
public class HomeFeaturedCategoryKey implements Serializable {

	private static final long serialVersionUID = 568598696754369510L;

	private String keyword;

	private Integer sort;

	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer isort) {
		this.sort = isort;
	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String ckeyword) {
		this.keyword = ckeyword;
	}
}