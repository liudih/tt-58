package com.tomtop.member.service.impl;

import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.PropertyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Maps;
import com.tomtop.member.mappers.base.EmailTemplateMapper;
import com.tomtop.member.mappers.base.EmailVariableMapper;
import com.tomtop.member.mappers.user.MemberBaseMapper;
import com.tomtop.member.mappers.user.MemberEmailVerifyMapper;
import com.tomtop.member.models.dto.EmailTemplate;
import com.tomtop.member.models.dto.EmailVariable;
import com.tomtop.member.service.IEmailTemplateService;

@Service
public class EmailTemplateServiceImpl implements IEmailTemplateService {

	@Autowired
	EmailVariableMapper emailVariableMapper;
	
	@Autowired
	EmailTemplateMapper emailTemplateMapper;
	
	@Autowired
	MemberBaseMapper memberBaseMapper;
	
	@Autowired
	MemberEmailVerifyMapper emailVerifiMapper;
	
	
	/**
	 * 从数据库查询出来的邮件发送模板在这个方法中进行相应链接替换
	 * @param object
	 * @param siteId
	 * @return
	 * @throws Exception
	 */
	public Map<String, String> getEmailContent(Object object, Integer siteId)
			throws Exception {
		Map<String, String> emailTitleAndContentMap = Maps.newHashMap();
		Map<String, Object> objectmap = PropertyUtils.describe(object);
		String emailContent = "";
		String title = "";
		if (null != objectmap && objectmap.size() > 0) {
			Integer language = Integer.valueOf((objectmap.get("language")
					.toString()));
			String emailType = objectmap.get("emailType").toString();
			EmailTemplate emailTemplate = emailTemplateMapper
					.getEmailTemplateBylangAndSiteAndType(language, siteId,
							emailType);
			if(null == emailTemplate){
				return null;
			}
			if (null != emailTemplate) {
				emailContent = emailTemplate.getCcontent();
				title = emailTemplate.getCtitle();
			}
			List<EmailVariable> emailVariables = emailVariableMapper
					.getEmailVariablesByType(emailType);
			if (null != emailVariables && emailVariables.size() > 0) {
				for (EmailVariable emailVariable : emailVariables) {
					String variable = emailVariable.getCname();
					String key = emailVariable.getCname().replace("#{", "")
							.replace("}", "");
					Object value = objectmap.get(key);
					if (title.contains(variable)) {
						if (null != value) {
							title = title.replace(variable, value.toString());
						}
					}
					if (emailContent.contains(variable)) {
						if (null != value) {
							emailContent = emailContent.replace(variable,
									value.toString());
						}
					}
				}

			}
		}
		emailTitleAndContentMap.put("content", emailContent);
		emailTitleAndContentMap.put("title", title);
		return emailTitleAndContentMap;
	}

}
