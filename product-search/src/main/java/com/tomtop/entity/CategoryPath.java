package com.tomtop.entity;

import java.io.Serializable;

/**
 * 类目Id 和路径
 * 
 *
 */
public class CategoryPath implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8231806805741341849L;
	private Integer id;
	private String path;
	private Integer level;
	
	public int getId() {
		if(id == null){
			id = 0;
		}
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public int getLevel() {
		if(level == null){
			level = 1;
		}
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}
}
