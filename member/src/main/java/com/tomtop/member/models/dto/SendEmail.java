package com.tomtop.member.models.dto;

import java.io.Serializable;

public class SendEmail implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String from;//发件人邮箱
	private String toEmail;//收件人邮箱
	private String title;//邮件标题
	private String content;//邮件内容
	private String fromName;//发件人名称
	
	public SendEmail(){
		
	}
	
	public SendEmail(String from,String toEmail,String title,String content,String fromName){
		this.from = from;
		this.toEmail = toEmail;
		this.title = title;
		this.content = content;
		this.fromName = fromName;
	}

	public String getFrom() {
		return from;
	}
	public void setFrom(String from) {
		this.from = from;
	}
	public String getToEmail() {
		return toEmail;
	}
	public void setToEmail(String toEmail) {
		this.toEmail = toEmail;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getFromName() {
		return fromName;
	}
	public void setFromName(String fromName) {
		this.fromName = fromName;
	}
	
}
