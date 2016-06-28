package com.tomtop.member.service;

import com.tomtop.member.models.bo.BaseBo;

public interface IForgotPasswordService {

	public BaseBo forgotPassword(String email,Integer siteId,Integer lang);

	public BaseBo alertPassword(String cid,String pwd,Integer siteId);
	
	public boolean updatePassword(String email, String pwd,
			Integer siteId);
}
