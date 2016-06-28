package com.tomtop.member.service;

import com.tomtop.member.models.bo.BaseBo;

public interface IMemberOtherService {

	public BaseBo loginOrRegistration(String email,String source,String sourceId,
			Integer siteId,String country) throws Exception;
	
}
