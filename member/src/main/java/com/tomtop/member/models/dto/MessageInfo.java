package com.tomtop.member.models.dto;

import java.io.Serializable;
import java.util.Date;

public class MessageInfo implements Serializable {

	private static final long serialVersionUID = 1L;
	private int id;
	private String from;//消息来源
	private String subject;//标题
	private Date createDate;
	private String tab;//来自哪张表 i:站内消息 b:广播
	private Integer status;//消息状态（1已读， 0未读） 3:推送
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getFrom() {
		return from;
	}
	public void setFrom(String from) {
		this.from = from;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public String getTab() {
		return tab;
	}
	public void setTab(String tab) {
		this.tab = tab;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
}

