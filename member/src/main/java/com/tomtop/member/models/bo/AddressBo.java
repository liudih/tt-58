package com.tomtop.member.models.bo;

import com.tomtop.member.models.base.FilterBaseBean;

public class AddressBo  extends FilterBaseBean {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5384741293904439169L;
	
	private Integer id;
	private String email;
	private Integer atype;//地址类型 1:收货地址 2:账单地址
	private Boolean isDef;//是否为默认地址
	private String fname;
	private String lname;
	private String company;//公司
	private String street;//街道
	private String city;//城市
	private String province;//洲
	private String postalcode;//邮政编码
	private String tel;//联系电话
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public Integer getAtype() {
		return atype;
	}
	public void setAtype(Integer atype) {
		this.atype = atype;
	}
	public Boolean getIsDef() {
		if(isDef == null){
			return false;
		}
		return isDef;
	}
	public void setIsDef(Boolean isDef) {
		this.isDef = isDef;
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
	public String getCompany() {
		return company;
	}
	public void setCompany(String company) {
		this.company = company;
	}
	public String getStreet() {
		return street;
	}
	public void setStreet(String street) {
		this.street = street;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getProvince() {
		return province;
	}
	public void setProvince(String province) {
		this.province = province;
	}
	public String getPostalcode() {
		return postalcode;
	}
	public void setPostalcode(String postalcode) {
		this.postalcode = postalcode;
	}
	public String getTel() {
		return tel;
	}
	public void setTel(String tel) {
		this.tel = tel;
	}
	@Override
	public String getCurrency(){
		return null;
	}
	@Override
	public Integer getLang(){
		return null;
	}
	@Override
	public Integer getClient(){
		return null;
	}
}
