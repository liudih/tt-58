package com.tomtop.services.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.tomtop.mappers.mysql.WebsiteMapper;
import com.tomtop.services.IBaseService;

@Service
public class BaseServiceImpl implements IBaseService {

	@Autowired
	WebsiteMapper websiteMapper;
	
	@Cacheable(value = "website", keyGenerator = "customKeyGenerator")
	@Override
	public List<Integer> getAllWebsiteId() {
		return websiteMapper.getWebsite();
	}

}
