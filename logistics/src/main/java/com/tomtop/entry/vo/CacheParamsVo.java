package com.tomtop.entry.vo;

import java.io.Serializable;

public class CacheParamsVo implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2511926316943757826L;
	private String ckey;
	private String cvalue;
	public String getCkey() {
		return ckey;
	}
	public void setCkey(String ckey) {
		this.ckey = ckey;
	}
	public String getCvalue() {
		return cvalue;
	}
	public void setCvalue(String cvalue) {
		this.cvalue = cvalue;
	}
	
	
}
