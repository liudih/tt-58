package com.tomtop.valueobjects.order;

import java.io.Serializable;

public class OrderTag implements Serializable {

	private static final long serialVersionUID = 1L;

	int iorderid;

	String ctag;

	public int getIorderid() {
		return iorderid;
	}

	public void setIorderid(int iorderid) {
		this.iorderid = iorderid;
	}

	public String getCtag() {
		return ctag;
	}

	public void setCtag(String ctag) {
		this.ctag = ctag;
	}

}
