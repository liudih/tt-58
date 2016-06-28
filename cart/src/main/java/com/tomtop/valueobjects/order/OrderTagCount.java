package com.tomtop.valueobjects.order;

import java.io.Serializable;

public class OrderTagCount implements Serializable {

	private static final long serialVersionUID = 1L;

	int iorderid;
	int icount;

	public int getIorderid() {
		return iorderid;
	}

	public void setIorderid(int iorderid) {
		this.iorderid = iorderid;
	}

	public int getIcount() {
		return icount;
	}

	public void setIcount(int icount) {
		this.icount = icount;
	}

}
