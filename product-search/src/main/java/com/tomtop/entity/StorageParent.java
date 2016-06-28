package com.tomtop.entity;

import java.io.Serializable;

/**
 * 仓库
 */
public class StorageParent implements Serializable  {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7695530152305431535L;
	private Integer id;
	private String name;
	private String shortName;
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
	public String getShortName() {
		return shortName;
	}
	public void setShortName(String shortName) {
		this.shortName = shortName;
	}
	
}
