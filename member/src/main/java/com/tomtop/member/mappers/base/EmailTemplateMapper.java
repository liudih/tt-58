package com.tomtop.member.mappers.base;

import org.apache.ibatis.annotations.Select;

import com.tomtop.member.models.dto.EmailTemplate;

public interface EmailTemplateMapper {

	@Select("select iid,iwebsiteid,ilanguage,ctype,ctitle,ccontent,ccreateuser,dcreatedate"
			+ " from t_email_template where ilanguage = #{0} and iwebsiteid=#{1} and ctype= #{2} limit 1")
	EmailTemplate getEmailTemplateBylangAndSiteAndType(Integer language,
			Integer siteId, String emailType);
}
