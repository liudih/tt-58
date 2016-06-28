package com.tomtop.member.models.dto;

import java.io.Serializable;
import java.util.Date;

public class MemberEmailVerify implements Serializable {
	private static final long serialVersionUID = 1L;
	

	private Integer iid;
	/**
	 * 邮件
	 */
	private String cemail;
	/**
	 * 今天是否发送邮箱认证标记
	 */
	private String cmark;
	/**
	 * 每天发送邮箱次数(最多为3次)
	 */
	private Integer idaynumber;
	/**
	 * 是否已发送
	 */
	private Boolean bisending;
	/**
	 * 邮箱激活码
	 */
	private String cactivationcode;
	/**
	 * 邮件激活有效时间(3天)
	 */
	private Date dvaliddate;
	/**
	 * 邮件发送日期
	 */
	private Date dsenddate;
	/**
	 * 创建时间
	 */
	private Date dcreatedate;
	
	public String getCmark() {
		return cmark;
	}

	public void setCmark(String cmark) {
		this.cmark = cmark;
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
		this.cemail = cemail;
	}

	public Integer getIdaynumber() {
		return idaynumber;
	}

	public void setIdaynumber(Integer idaynumber) {
		this.idaynumber = idaynumber;
	}

	public Boolean getBisending() {
		return bisending;
	}

	public void setBisending(Boolean bisending) {
		this.bisending = bisending;
	}

	public String getCactivationcode() {
		return cactivationcode;
	}

	public void setCactivationcode(String cactivationcode) {
		this.cactivationcode = cactivationcode;
	}

	public Date getDvaliddate() {
		return dvaliddate;
	}

	public void setDvaliddate(Date dvaliddate) {
		this.dvaliddate = dvaliddate;
	}

	public Date getDsenddate() {
		return dsenddate;
	}

	public void setDsenddate(Date dsenddate) {
		this.dsenddate = dsenddate;
	}

	public Date getDcreatedate() {
		return dcreatedate;
	}

	public void setDcreatedate(Date dcreatedate) {
		this.dcreatedate = dcreatedate;
	}

	
}
