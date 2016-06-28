package com.tomtop.base.models.dto;

public class VisitLogDto {
	public String aid;
	public Integer websiteid;
	public String ip;
	public String path;
	public String source;
	public String eid;
	public Integer tasktype;

	public VisitLogDto() {

	}

	public VisitLogDto(String aid, Integer websiteid, String ip, String path,String source) {
		this.aid = aid;
		this.websiteid = websiteid;
		this.ip = ip;
		this.path = path;
		this.source = source;
	}

	public String saler;

	public String getAid() {
		return aid;
	}

	public void setAid(String caid) {
		this.aid = caid;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String cip) {
		this.ip = cip;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String csource) {
		this.source = csource;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String cpath) {
		this.path = cpath;
	}

	public Integer getWebsiteid() {
		return websiteid;
	}

	public void setWebsiteid(Integer iwebsiteid) {
		this.websiteid = iwebsiteid;
	}

	public String getEid() {
		return eid;
	}

	public void setEid(String ceid) {
		this.eid = ceid;
	}

	public String getSaler() {
		return saler;
	}

	public void setSaler(String saler) {
		this.saler = saler;
	}

	public Integer getTasktype() {
		return tasktype;
	}

	public void setTasktype(Integer itasktype) {
		this.tasktype = itasktype;
	}

}
