package com.tomtop.dto.order;

import java.io.Serializable;
import java.util.Date;
public class OrderPack implements Serializable {
	private Integer iid;
	private Integer iorderid;
	private String csku;
	private Integer iqty;
	private String cshippingtype;
	private Double fshippingprice;
	private String ctrackingnumber;
	private Date dshippingdate;
	private Date dcreatedate;
	private Integer iisregister;
	private String clocaltracknumber;

	public Integer getIid() {
		return this.iid;
	}

	public void setIid(Integer iid) {
		this.iid = iid;
	}

	public Integer getIorderid() {
		return this.iorderid;
	}

	public void setIorderid(Integer iorderid) {
		this.iorderid = iorderid;
	}

	public String getCsku() {
		return this.csku;
	}

	public void setCsku(String csku) {
		this.csku = ((csku == null) ? null : csku.trim());
	}

	public Integer getIqty() {
		return this.iqty;
	}

	public void setIqty(Integer iqty) {
		this.iqty = iqty;
	}

	public String getCshippingtype() {
		return this.cshippingtype;
	}

	public void setCshippingtype(String cshippingtype) {
		this.cshippingtype = ((cshippingtype == null) ? null : cshippingtype
				.trim());
	}

	public Double getFshippingprice() {
		return this.fshippingprice;
	}

	public void setFshippingprice(Double fshippingprice) {
		this.fshippingprice = fshippingprice;
	}

	public String getCtrackingnumber() {
		return this.ctrackingnumber;
	}

	public void setCtrackingnumber(String ctrackingnumber) {
		this.ctrackingnumber = ((ctrackingnumber == null) ? null
				: ctrackingnumber.trim());
	}

	public Date getDshippingdate() {
		return this.dshippingdate;
	}

	public void setDshippingdate(Date dshippingdate) {
		this.dshippingdate = dshippingdate;
	}

	public Date getDcreatedate() {
		return this.dcreatedate;
	}

	public void setDcreatedate(Date dcreatedate) {
		this.dcreatedate = dcreatedate;
	}

	public Integer getIisregister() {
		return this.iisregister;
	}

	public void setIisregister(Integer iisregister) {
		this.iisregister = iisregister;
	}

	public String getClocaltracknumber() {
		return this.clocaltracknumber;
	}

	public void setClocaltracknumber(String clocaltracknumber) {
		this.clocaltracknumber = clocaltracknumber;
	}
}