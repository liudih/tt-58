package com.tomtop.base.models.base;


import java.util.Date;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * 所有表的基本类
 * @author renyy
 *
 */
@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
public class TableBaseBean {

	/**
	 * 是否为逻辑删除 1:是 0:否  
	 */
	private Integer isDeleted;
	/**
	 * 创建人
	 */
	private String createdBy;
	/**
	 * 创建时间
	 */
	private Date createdOn;
	/**
	 * 最后一次更新的人
	 */
	private String lastUpdatedBy;
	/**
	 * 最后一次更新时间
	 */
	private Date lastUpdatedOn;
	/**
	 * 是否为启用  1:是 0:否 
	 */
	private Integer isEnabled;
	
	public Integer getIsDeleted() {
		return isDeleted;
	}
	public void setIsDeleted(Integer isDeleted) {
		this.isDeleted = isDeleted;
	}
	public String getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	public Date getCreatedOn() {
		return createdOn;
	}
	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}
	public String getLastUpdatedBy() {
		return lastUpdatedBy;
	}
	public void setLastUpdatedBy(String lastUpdatedBy) {
		this.lastUpdatedBy = lastUpdatedBy;
	}
	public Date getLastUpdatedOn() {
		return lastUpdatedOn;
	}
	public void setLastUpdatedOn(Date lastUpdateOn) {
		this.lastUpdatedOn = lastUpdateOn;
	}
	public Integer getIsEnabled() {
		return isEnabled;
	}
	public void setIsEnabled(Integer isEnabled) {
		this.isEnabled = isEnabled;
	}
	public String checkNull(String str){
		if(str == null){
			str = "";
		}
		return str.trim();
	}
}
