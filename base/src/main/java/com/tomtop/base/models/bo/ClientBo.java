package com.tomtop.base.models.bo;

import com.tomtop.base.models.base.BaseBean;

public class ClientBo extends BaseBean {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7894289776905205152L;
	private Integer id;
	private String name;//客户端名称
	private Integer platform;//平台
	private Integer siteId;//站点ID
	
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
	public Integer getPlatform() {
		return platform;
	}
	public void setPlatform(Integer platform) {
		this.platform = platform;
	}
	public Integer getSiteId() {
		return siteId;
	}
	public void setSiteId(Integer siteId) {
		this.siteId = siteId;
	}
}
