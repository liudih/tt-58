package com.tomtop.member.models.dto;

import java.io.Serializable;
import java.util.Date;

public class ProductCollect implements Serializable  {
	private static final long serialVersionUID = 1L;
    private Integer iid;

    private String cemail;

    private String clistingid;

    private Date dcreatedate;
    
    private Integer iwebsiteid;
    
    public Integer getIwebsiteid() {
		return iwebsiteid;
	}

	public void setIwebsiteid(Integer iwebsiteid) {
		this.iwebsiteid = iwebsiteid;
	}

	public Integer getIid() {
        return iid;
    }

    public void setIid(Integer iid) {
        this.iid = iid;
    }

    public String getCemail() {
        return cemail;
    }

    public void setCemail(String cemail) {
        this.cemail = (cemail == null ? "" : cemail.trim());
    }

    public String getClistingid() {
        return clistingid;
    }

    public void setClistingid(String clistingid) {
        this.clistingid = (clistingid == null ? "" : clistingid.trim());
    }

    public Date getDcreatedate() {
        return dcreatedate;
    }

    public void setDcreatedate(Date dcreatedate) {
        this.dcreatedate = (dcreatedate == null ? new Date() : dcreatedate);;
    }
}