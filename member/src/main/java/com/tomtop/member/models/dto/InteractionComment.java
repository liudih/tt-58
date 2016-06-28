package com.tomtop.member.models.dto;

import java.io.Serializable;
import java.util.Date;

import com.tomtop.member.utils.CommonUtils;

public class InteractionComment implements Serializable {
	private static final long serialVersionUID = 1L;

	private Integer iid;

	private String clistingid;

	private String csku;

	private String cmemberemail;

	private String ccomment;
	
	private Integer client;

	private Integer iprice;

	private Integer iquality;

	private Integer ishipping;

	private Integer iusefulness;

	private Double foverallrating;

	private Date dcreatedate;

	private Date dauditdate;

	private Integer istate;

	private String ccountry;

	private String cplatform;

	private Integer iwebsiteid;

	private Integer iorderid;

	private Integer count;

	private String ctitle;

	public Integer getClient() {
		return client;
	}

	public void setClient(Integer client) {
		this.client = client;
	}

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

	public String getCmemberemail() {
		return cmemberemail;
	}

	public void setCmemberemail(String cmemberemail) {
		this.cmemberemail = cmemberemail;
	}

	public String getCcomment() {
		return ccomment;
	}

	public void setCcomment(String ccomment) {
		if(ccomment == null){
			ccomment = "";
		}else if(ccomment.length() > 2000){
			ccomment = ccomment.substring(2000);
		}else{
			ccomment = ccomment.trim();
		}
		this.ccomment = CommonUtils.checkSpecialChar(ccomment);
	}

	public Integer getIprice() {
		if(iprice == null || iprice > 5 || iprice < 1 ){
			iprice = 5;
		}
		return iprice;
	}

	public void setIprice(Integer iprice) {
		this.iprice = iprice;
	}

	public Integer getIquality() {
		if(iquality == null || iquality > 5 || iquality < 1 ){
			iquality = 5;
		}
		return iquality;
	}

	public void setIquality(Integer iquality) {
		this.iquality = iquality;
	}

	public Integer getIshipping() {
		if(ishipping == null || ishipping > 5 || ishipping < 1 ){
			ishipping = 5;
		}
		return ishipping;
	}

	public Integer getIusefulness() {
		if(iusefulness == null || iusefulness > 5 || iusefulness < 1 ){
			iusefulness = 5;
		}
		return iusefulness;
	}

	public void setIusefulness(Integer iusefulness) {
		this.iusefulness = iusefulness;
	}

	public Double getFoverallrating() {
		double total = iprice + iquality + ishipping + iusefulness;
		foverallrating = total / 4;
		return foverallrating;
	}

	public void setIshipping(Integer ishipping) {
		this.ishipping = ishipping;
	}

	public void setFoverallrating(Double foverallrating) {
		this.foverallrating = foverallrating;
	}

	public Date getDcreatedate() {
		return dcreatedate;
	}


	public void setDcreatedate(Date dcreatedate) {
		this.dcreatedate = dcreatedate;
	}

	public Date getDauditdate() {
		return dauditdate;
	}

	public void setDauditdate(Date dauditdate) {
		this.dauditdate = dauditdate;
	}

	public Integer getIstate() {
		return istate;
	}

	public void setIstate(Integer istate) {
		this.istate = istate;
	}

	public Integer getIpriceStarWidth() {
		return iprice;
	}

	public Integer getIqualityStarWidth() {
		return iquality;
	}

	public Integer getIshippingStarWidth() {

		return ishipping;
	}

	public Integer getIusefulnessStarWidth() {
		return iusefulness;
	}

	public String getCcountry() {
		return ccountry;
	}

	public void setCcountry(String ccountry) {
		this.ccountry = ccountry;
	}

	public String getCplatform() {
		return cplatform;
	}

	public void setCplatform(String cplatform) {
		this.cplatform = cplatform;
	}

	public Integer getIwebsiteid() {
		return iwebsiteid;
	}

	public void setIwebsiteid(Integer iwebsiteid) {
		this.iwebsiteid = iwebsiteid;
	}

	public Integer getIorderid() {
		return iorderid;
	}

	public void setIorderid(Integer iorderid) {
		this.iorderid = iorderid;
	}

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	public String getCtitle() {
		return ctitle;
	}

	public void setCtitle(String ctitle) {
		this.ctitle = ctitle;
	}

}