package com.tomtop.base.models.bo;

import com.tomtop.base.models.base.BaseBean;

public class SiteBo  extends BaseBean {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6680674990940094385L;
	private Integer id;
	private String name;//站点名称
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getName() {
		return checkNull(name);
	}
	public void setName(String name) {
		this.name = name;
	}
}
