package com.tomtop.member.mappers.user;

import org.apache.ibatis.annotations.Select;

import com.tomtop.member.models.dto.MemberEmailVerify;


public interface MemberEmailVerifyMapper {

	MemberEmailVerify selectByPrimaryKey(Integer iid);
	
	int deleteByPrimaryKey(Integer iid);
	
	int insert(MemberEmailVerify record);

	@Select("select iid, cmark,cemail, bisending,cactivationcode,idaynumber, "
			+ "idaynumber,dvaliddate,dsenddate, dcreatedate,iresendcount "
			+ " from t_member_email_verify where cactivationcode=#{0} limit 1")
	MemberEmailVerify getActivationCode(String activationcode);
}
