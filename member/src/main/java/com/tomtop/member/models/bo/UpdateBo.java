package com.tomtop.member.models.bo;

import com.tomtop.member.models.base.FilterBaseBean;

public class UpdateBo extends FilterBaseBean {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3581246350720986032L;
	private String email;
	private String ids;
	
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getIds() {
		if(ids == null){
			return "";
		}
		return ids;
	}
	public void setIds(String ids) {
		this.ids = ids;
	}
}
