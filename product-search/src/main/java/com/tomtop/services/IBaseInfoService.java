package com.tomtop.services;


import java.util.Map;

import com.tomtop.entity.Currency;
import com.tomtop.entity.Langage;


/**
 * 基础信息
 * 
 *
 */
public interface IBaseInfoService {
	public Langage getLangageBean(Integer langid);

	public Currency getCurrencyBean(String currency);
	
	public Map<String, Currency> getAllRoteCurrency();
	
	public Map<Integer, Langage> getAllRoteLangage();
	
	public Map<String, Langage> getAllStrRoteLangage();
	
	public Map<Integer, Langage> getLangageBeanMap();
	
	public Map<String, Langage> getLangageBeanStrMap();
}
