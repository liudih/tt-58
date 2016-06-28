package com.tomtop.member.mappers.user;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;

import com.tomtop.member.models.dto.ForgetPasswdBase;

public interface ForgetPasswdBaseMapper {

	@Select("select count(*) from t_forget_passwd_base where cmemberemail=#{0} "
			+ "and to_char(dcreatedate,'yyyymmdd')=to_char(CURRENT_DATE,'yyyymmdd') "
			+ "and iwebsiteid = #{1}")
	int getCountByCmembermailAndDcreatedate(String email, Integer iwebsiteId);

	@Insert("insert into t_forget_passwd_base(cid,cmemberemail,iwebsiteid,crandomcode)  "
			+ "values (#{cid},#{cmemberemail},#{iwebsiteid},#{crandomcode})")
	int insert(ForgetPasswdBase record);
	
	ForgetPasswdBase selectByCid(String cid);
	
	int update(ForgetPasswdBase fpb);
}
