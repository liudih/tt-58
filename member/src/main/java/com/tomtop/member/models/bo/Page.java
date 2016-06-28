package com.tomtop.member.models.bo;

import java.io.Serializable;
import java.util.List;

public class Page<T> implements Serializable {
	private static final long serialVersionUID = -6287141621150731167L;
	private List<T> list;
	private int total;
	private int page;
	private int recordPerPage;

	public Page(List<T> list, int total, int page, int recordPerPage) {
		super();
		this.list = list;
		this.total = total;
		this.page = page;
		this.recordPerPage = recordPerPage;
	}

	public List<T> getList() {
		return list;
	}

	public void setList(List<T> list) {
		this.list = list;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getRecordPerPage() {
		return recordPerPage;
	}

	public void setRecordPerPage(int recordPerPage) {
		this.recordPerPage = recordPerPage;
	}

}
