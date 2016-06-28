package com.tomtop.base.models.bo;

import java.io.Serializable;

/**
 * 订阅事件服务业务层对象
 */
public class SubEventServicesBo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4858228275438286005L;
	private String code;
	private String name;
	private String type;
	private String url;
	private String method;
	private String param;
	
	public SubEventServicesBo(){
		
	}
	public SubEventServicesBo(String code,String name,String type,String url,String method,String param){
		this.code = code;
		this.name = name;
		this.type = type;
		this.url = url;
		this.method = method;
		this.param = param;
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
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getParam() {
		return param;
	}
	public void setParam(String param) {
		this.param = param;
	}
	public String getMethod() {
		return method;
	}
	public void setMethod(String method) {
		this.method = method;
	}
	
}
