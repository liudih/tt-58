package com.tomtop.member.models.bo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * 业务逻辑Bo基本类
 * @author renyy
 *
 */
@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
public class BaseBo{

	private Integer res;
	private String  msg;
	
	public Integer getRes() {
		return res;
	}
	public void setRes(Integer res) {
		this.res = res;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public String checkNull(String str){
		if(str == null){
			str = "";
		}
		return str.trim();
	}
}
