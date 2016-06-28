package com.tomtop.member.service;

import java.util.Map;

public interface IEmailTemplateService {

	public Map<String, String> getEmailContent(Object object, Integer siteId) throws Exception;
}
