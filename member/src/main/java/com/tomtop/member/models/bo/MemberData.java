package com.tomtop.member.models.bo;


import java.util.Date;

import com.tomtop.member.models.base.FilterBaseBean;
import com.tomtop.member.utils.FormatDateUtils;

public class MemberData extends FilterBaseBean {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2318731540328126748L;

	private String account;
	private String fname;
	private String lname;
	private Integer gender;
	private String countryName;
	private String about;
	private String email;
	private String birth;
	
	public String getAccount() {
		return account;
	}
	public void setAccount(String account) {
		this.account = account;
	}
	public String getFname() {
		return fname;
	}
	public void setFname(String fname) {
		this.fname = fname;
	}
	public String getLname() {
		return lname;
	}
	public void setLname(String lname) {
		this.lname = lname;
	}
	public Integer getGender() {
		return gender;
	}
	public void setGender(Integer gender) {
		this.gender = gender;
	}
	public String getCountryName() {
		return countryName;
	}
	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}
	public String getAbout() {
		return about;
	}
	public void setAbout(String about) {
		this.about = about;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getBirth() {
		return birth;
	}
	public void setBirth(String birth) {
		this.birth = birth;
	}
	public Date getBirthday(){
		if(birth.length() == 10){
			birth += " 00:00:00";
		}
		Date birthday = FormatDateUtils.parseDate(birth, FormatDateUtils.YYYYMMDDHHMMSS);
		return birthday;
	}
}
