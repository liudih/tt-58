package com.tomtop.member.mappers.base;

import java.util.List;

import org.apache.ibatis.annotations.Select;

import com.tomtop.member.models.dto.EmailVariable;

public interface EmailVariableMapper {

	@Select("select iid,ctype,cname,cremark,ccreateuser,dcreatedate from t_email_variable where ctype=#{0}")
	List<EmailVariable> getEmailVariablesByType(String ctype);
}
