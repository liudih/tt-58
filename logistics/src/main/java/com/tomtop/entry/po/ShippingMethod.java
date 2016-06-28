package com.tomtop.entry.po;

import java.io.Serializable;
import java.util.Date;

public class ShippingMethod implements Serializable {
	private static final long serialVersionUID = -1800413304302287174L;
	private Integer iid;			//ID
	private String ccode;			//发货代码
	private Integer istorageid;		//仓库Id
	private Boolean benabled;		//是否启用 t：启用  否则不启用 
	private Boolean bexistfree;		//是否存在免费
	private Double ffreebeginprice;	//免费开始价格
	private Double ffreeendprice;	//免费开始价格
	private Double fbeginprice;		//起始价格
	private Double fendprice;		//结束价格
	private String ccountrys;		//国家
	private String crule;			//公式
	private String csuperrule;		//全局公式
	private Date dcreatedate;		//创建日期
	private Boolean bistracking;	//有无跟踪号
	private Boolean bisspecial;		//是否可以运送特殊品
	private Integer istartweight;	//开始重量
	private Integer iendweight;		//结束重量

	public Integer getIid() {
		return iid;
	}

	public void setIid(Integer iid) {
		this.iid = iid;
	}

	public Integer getIstorageid() {
		return istorageid;
	}

	public void setIstorageid(Integer istorageid) {
		this.istorageid = istorageid;
	}

	public Boolean getBenabled() {
		return benabled;
	}

	public void setBenabled(Boolean benabled) {
		this.benabled = benabled;
	}

	public Boolean getBexistfree() {
		return bexistfree;
	}

	public void setBexistfree(Boolean bexistfree) {
		this.bexistfree = bexistfree;
	}

	public Double getFfreebeginprice() {
		return ffreebeginprice;
	}

	public void setFfreebeginprice(Double ffreebeginprice) {
		this.ffreebeginprice = ffreebeginprice;
	}

	public Double getFfreeendprice() {
		return ffreeendprice;
	}

	public void setFfreeendprice(Double ffreeendprice) {
		this.ffreeendprice = ffreeendprice;
	}

	public String getCcountrys() {
		return ccountrys;
	}

	public void setCcountrys(String ccountrys) {
		this.ccountrys = ccountrys;
	}

	public String getCrule() {
		return crule;
	}

	public void setCrule(String crule) {
		this.crule = crule;
	}

	public String getCsuperrule() {
		return csuperrule;
	}

	public void setCsuperrule(String csuperrule) {
		this.csuperrule = csuperrule;
	}

	public Date getDcreatedate() {
		return dcreatedate;
	}

	public void setDcreatedate(Date dcreatedate) {
		this.dcreatedate = dcreatedate;
	}

	public Double getFbeginprice() {
		return fbeginprice;
	}

	public void setFbeginprice(Double fbeginprice) {
		this.fbeginprice = fbeginprice;
	}

	public Double getFendprice() {
		return fendprice;
	}

	public void setFendprice(Double fendprice) {
		this.fendprice = fendprice;
	}

	public Boolean getBistracking() {
		return bistracking;
	}

	public void setBistracking(Boolean bistracking) {
		this.bistracking = bistracking;
	}

	public Boolean getBisspecial() {
		return bisspecial;
	}

	public void setBisspecial(Boolean bisspecial) {
		this.bisspecial = bisspecial;
	}

	public String getCcode() {
		return ccode;
	}

	public void setCcode(String ccode) {
		this.ccode = ccode;
	}

	public Integer getIstartweight() {
		return istartweight;
	}

	public void setIstartweight(Integer istartweight) {
		this.istartweight = istartweight;
	}

	public Integer getIendweight() {
		return iendweight;
	}

	public void setIendweight(Integer iendweight) {
		this.iendweight = iendweight;
	}

}
