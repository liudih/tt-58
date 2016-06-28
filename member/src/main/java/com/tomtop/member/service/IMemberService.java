package com.tomtop.member.service;

import com.alibaba.fastjson.JSONObject;
import com.tomtop.member.models.bo.BaseBo;
import com.tomtop.member.models.bo.LoginMemberBo;
import com.tomtop.member.models.bo.MemberBaseBo;
import com.tomtop.member.models.bo.MemberData;
import com.tomtop.member.models.bo.MemberUidBo;
import com.tomtop.member.models.bo.RegisterMemberBo;
import com.tomtop.member.models.bo.TokenUUidBo;
import com.tomtop.member.models.dto.MemberBase;

public interface IMemberService {

	public LoginMemberBo memberLogin(String email,String pwd,Integer siteId);
	
	public RegisterMemberBo memberRegister(String email,String pwd,String country,
			Integer siteId,String vhost) throws Exception;
	
	public TokenUUidBo getToken(String email, String password, Integer siteId);
	
	public TokenUUidBo getOtherToken(String email, Integer siteId);
	
	public MemberUidBo getMemberEmailByUUid(String uuid,Integer siteId);
	
	public MemberBaseBo getMemberBaseByEmailAndWebsiteId(String email, int siteid);
	
	public boolean updateMemberBaseByEmailAndWebsiteId(MemberBase mb);
	
	public boolean validate(Integer websiteId, String email, String uuid);
	
	public MemberBase getMemberBaseByUuid(String uuid);
	
	public String getEmailByUUid(String uuid, Integer siteId);
	
	public TokenUUidBo getTokenByEmailAndPwdAndSiteId(String email, String password, Integer siteId);
	
	public JSONObject getMemberStatisticsByEmailAndWebsiteId(String email, Integer websiteId, String uuid);
	
	public String activation(String activationcode, Integer site) throws Exception;
	
	public BaseBo activationSuccess(String activationcode, Integer site, Integer lang)throws Exception ;
	
	public BaseBo activationSendEmail(String email, Integer siteId,Integer lang,
			BaseBo rmb) throws Exception;
	
	public Integer getDefaultLangId(String ccountry);
	
	public BaseBo sendEmailForSubscribe(String email, Integer websiteid,
			Integer language, String categoryLinks) throws Exception ;
	
	public int updateMemberData(MemberData md);
}
