package com.tomtop.entity;

import java.io.Serializable;

public class HomeSearchKeyword implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6911019222883520124L;

	private Integer sort;

	private String keyword;

	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword == null ? null : keyword.trim();
	}
}