package com.tomtop.base.models.dto;

import java.io.Serializable;

import com.tomtop.base.models.base.TableBaseBean;


/**
 * 站点基类
 * @author renyy
 *
 */
public class SiteBase extends TableBaseBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Integer id;
	private String name;//站点名称
	private String description;//描述
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
}
