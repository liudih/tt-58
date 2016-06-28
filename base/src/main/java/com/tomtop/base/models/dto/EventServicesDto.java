package com.tomtop.base.models.dto;

import java.io.Serializable;

import com.tomtop.base.models.base.TableBaseBean;

/**
 * 事件服务对象
 */
public class EventServicesDto extends TableBaseBean implements Serializable {
	
	
	private static final long serialVersionUID = -9060153217456206219L;
	private int id;
	private String code;
	private String name;
	private String type;
	private String paramRemark;//事件服务参数说明
	private String remark;//事件服务说明
	private String field1;
	private String field2;
	private String field3;
	private String field4;
	private String field5;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getCode() {
		return checkNull(code);
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getName() {
		return checkNull(name);
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getType() {
		return checkNull(type);
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getParamRemark() {
		return checkNull(paramRemark);
	}
	public void setParamRemark(String paramRemark) {
		this.paramRemark = paramRemark;
	}
	public String getRemark() {
		return checkNull(remark);
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getField1() {
		return checkNull(field1);
	}
	public void setField1(String field1) {
		this.field1 = field1;
	}
	public String getField2() {
		return checkNull(field2);
	}
	public void setField2(String field2) {
		this.field2 = field2;
	}
	public String getField3() {
		return checkNull(field3);
	}
	public void setField3(String field3) {
		this.field3 = field3;
	}
	public String getField4() {
		return checkNull(field4);
	}
	public void setField4(String field4) {
		this.field4 = field4;
	}
	public String getField5() {
		return checkNull(field5);
	}
	public void setField5(String field5) {
		this.field5 = field5;
	}
}
