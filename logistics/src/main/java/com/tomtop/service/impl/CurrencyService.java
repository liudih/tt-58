package com.tomtop.service.impl;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tomtop.common.utils.CacheUtils;
import com.tomtop.service.ICacheManagerService;
import com.tomtop.service.ICurrencyService;

@Service
public class CurrencyService implements ICurrencyService {

	
	@Autowired
	CacheUtils cacheUtils;
	
	@Autowired
	ICacheManagerService cacheManagerService;
	@Override
	public Double exchange(String originalCCY, String targetCCY) {
		return cacheManagerService.exchange(originalCCY, targetCCY);
	}
	@Override
	public Double exchange(Double money, String originalCCY, String targetCCY) {
		if (originalCCY.equals(targetCCY)) {
			return money;
		}
		return money * exchange(originalCCY, targetCCY);
	}
}
