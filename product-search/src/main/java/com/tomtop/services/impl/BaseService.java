package com.tomtop.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.tomtop.entity.Currency;
import com.tomtop.entity.Langage;
import com.tomtop.services.IBaseInfoService;

@Component
public class BaseService {

	@Autowired
	IBaseInfoService baseInfoService;
	
	/**
	 * 获取内存语言 - 通过语言Id 获取语言
	 */
	public String getLangCode(Integer langId){
		if(BaseInfoServiceImpl.langageMap == null || BaseInfoServiceImpl.langageMap.size() == 0){
			BaseInfoServiceImpl.langageMap = baseInfoService.getLangageBeanMap();
		}
		Langage langBean = BaseInfoServiceImpl.langageMap.get(langId);
		String lang = "";
		if(langBean == null || langBean.getCode() == null){
			lang = "en";
		}else{
			lang = langBean.getCode();
		}
		return lang;
	}
	
	public Currency getCurrencyBean(String currency){
		return baseInfoService.getCurrencyBean(currency);
	}
}
