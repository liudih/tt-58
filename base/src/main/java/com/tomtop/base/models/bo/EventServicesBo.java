package com.tomtop.base.models.bo;

import java.io.Serializable;

/**
 * 事件服务业务层对象
 */
public class EventServicesBo implements Serializable {
	
	
	private static final long serialVersionUID = -9060153217456206219L;
	private String code;
	private String name;
	private String type;
	private String paramRemark;//事件服务参数说明
	private String remark;//事件服务说明
	
	public EventServicesBo(){
		
	}
	
	public EventServicesBo(String code,String name,String type,String paramRemark,String remark){
		this.code = code;
		this.name = name;
		this.type = type;
		this.paramRemark = paramRemark;
		this.remark = remark;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getParamRemark() {
		return paramRemark;
	}
	public void setParamRemark(String paramRemark) {
		this.paramRemark = paramRemark;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
}
