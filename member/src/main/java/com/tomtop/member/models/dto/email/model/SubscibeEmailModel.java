package com.tomtop.member.models.dto.email.model;

public class SubscibeEmailModel {

	private String toemail;//发送邮件到哪个邮箱
	private String url;//
	private String emailType;//邮箱模板类型
	private Integer language;//语言Id
	
	public SubscibeEmailModel(){
		
	}
	
	public SubscibeEmailModel(String toemail,String url,String emailType,Integer language){
		this.toemail = toemail;
		this.url = url;
		this.emailType = emailType;
		this.language = language;
	}
	public String getToemail() {
		return toemail;
	}
	public void setToemail(String toemail) {
		this.toemail = toemail;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getEmailType() {
		return emailType;
	}
	public void setEmailType(String emailType) {
		this.emailType = emailType;
	}
	public Integer getLanguage() {
		return language;
	}
	public void setLanguage(Integer language) {
		this.language = language;
	}
	
	@Override
	public String toString() {
		return "FindPassEmailModel [toemail=" + toemail + ", url=" + url
				+ "]";
	}
	
}
