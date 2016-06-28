package com.tomtop.member.service;

import com.tomtop.member.models.bo.OAuthUserBo;

public interface IOtherSignInOAuth {
	
	public String createLink(Integer website);
	
	public String getAccessToken(String code, Integer website);
	
	public OAuthUserBo getUserInfo(String token);
	
	public String type();
}
