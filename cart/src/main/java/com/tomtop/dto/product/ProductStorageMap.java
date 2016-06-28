package com.tomtop.dto.product;

import java.io.Serializable;
import java.util.Date;

public class ProductStorageMap implements Serializable {
	private static final long serialVersionUID = 1L;
	
    private Integer iid;

    private String clistingid;

    private String csku;

    private Integer istorageid;

    private String ccreateuser;

    private Date dcreatedate;

    private Boolean bavailable;

    private Integer ilogisticstemplateid;

    private Integer istatus;

    private Integer iqty;

    private Double fprice;

    public Integer getIid() {
        return iid;
    }

    public void setIid(Integer iid) {
        this.iid = iid;
    }

    public String getClistingid() {
        return clistingid;
    }

    public void setClistingid(String clistingid) {
        this.clistingid = clistingid == null ? null : clistingid.trim();
    }

    public String getCsku() {
        return csku;
    }

    public void setCsku(String csku) {
        this.csku = csku == null ? null : csku.trim();
    }

    public Integer getIstorageid() {
        return istorageid;
    }

    public void setIstorageid(Integer istorageid) {
        this.istorageid = istorageid;
    }

    public String getCcreateuser() {
        return ccreateuser;
    }

    public void setCcreateuser(String ccreateuser) {
        this.ccreateuser = ccreateuser == null ? null : ccreateuser.trim();
    }

    public Date getDcreatedate() {
        return dcreatedate;
    }

    public void setDcreatedate(Date dcreatedate) {
        this.dcreatedate = dcreatedate;
    }

    public Boolean getBavailable() {
        return bavailable;
    }

    public void setBavailable(Boolean bavailable) {
        this.bavailable = bavailable;
    }

    public Integer getIlogisticstemplateid() {
        return ilogisticstemplateid;
    }

    public void setIlogisticstemplateid(Integer ilogisticstemplateid) {
        this.ilogisticstemplateid = ilogisticstemplateid;
    }

    public Integer getIstatus() {
        return istatus;
    }

    public void setIstatus(Integer istatus) {
        this.istatus = istatus;
    }

    public Integer getIqty() {
        return iqty;
    }

    public void setIqty(Integer iqty) {
        this.iqty = iqty;
    }

    public Double getFprice() {
        return fprice;
    }

    public void setFprice(Double fprice) {
        this.fprice = fprice;
    }
}