package com.tomtop.dto.order;

import java.io.Serializable;

public class ShippingParameter implements Serializable {

	private static final long serialVersionUID = 1L;

	private String ckey;
	private String cjsonvalue;

	public String getCkey() {
		return ckey;
	}

	public void setCkey(String ckey) {
		this.ckey = ckey;
	}

	public String getCjsonvalue() {
		return cjsonvalue;
	}

	public void setCjsonvalue(String cjsonvalue) {
		this.cjsonvalue = cjsonvalue;
	}

}
