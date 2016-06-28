package com.tomtop.services.impl;


import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import com.tomtop.entity.Currency;
import com.tomtop.entity.Langage;
import com.tomtop.framework.core.utils.Result;
import com.tomtop.services.IBaseInfoService;
import com.tomtop.utils.HttpClientUtil;

/**
 * 语言、货币基础信息
 * 
 * @author liulj
 *
 */
@Service
public class BaseInfoServiceImpl implements IBaseInfoService {

	@Value("${baseUrl}")
	private String baseUrl;

	@Value("${currency_url}")
	private String currency_url;

	@Value("${cleanBaseTime}")
	private Long cleanBaseTime;

	public static Map<Integer, Langage> langageMap;
	
	public static Map<String, Langage> langageStrMap;

	public static Map<String, Currency> currencyMap;

	@PostConstruct
	public void init() {
		currencyMap = getAllRoteCurrency();
		langageMap = getAllRoteLangage();
		new Timer().schedule(new TimerTask() {
			@Override
			public void run() {
				langageMap = getAllRoteLangage();
				currencyMap = getAllRoteCurrency();
				LoggerFactory.getLogger(this.getClass()).info(
						"clean base info ,lanage,currency");
			}
		}, cleanBaseTime * 1000, cleanBaseTime * 1000);
	}

	public Map<String, Currency> getAllRoteCurrency() {
		JSONObject result = JSON
				.parseObject(HttpClientUtil.doGet(currency_url));
		if (result != null && Result.SUCCESS == result.get("ret")) {
			String data = result.getString("data");
			if (StringUtils.isNotBlank(data)) {
				List<Currency> currencys = JSON.parseArray(data,
						Currency.class);
				if (currencys != null && currencys.size() > 0) {
					return Maps.uniqueIndex(currencys, p -> p.getCode());
				}
			}
		}
		return Maps.newHashMap();
	}

	public Map<Integer, Langage> getAllRoteLangage() {
		String langStr = HttpClientUtil.doGet(baseUrl);
		Result res = JSON.parseObject(langStr, Result.class);
		List<Langage> langageBase = JSON.parseArray(res.getData()
				.toString(), Langage.class);
		if (langageBase != null && langageBase.size() > 0) {
			return Maps.uniqueIndex(langageBase, p -> p.getId());
		} else {
			return Maps.newHashMap();
		}
	}
	
	public Map<String, Langage> getAllStrRoteLangage() {
		String langStr = HttpClientUtil.doGet(baseUrl);
		Result res = JSON.parseObject(langStr, Result.class);
		List<Langage> langageBase = JSON.parseArray(res.getData()
				.toString(), Langage.class);
		if (langageBase != null && langageBase.size() > 0) {
			return Maps.uniqueIndex(langageBase, p -> p.getCode());
		} else {
			return Maps.newHashMap();
		}
	}

	@Override
	public Langage getLangageBean(Integer langid) {
		if (langageMap == null || langageMap.isEmpty()) {
			langageMap = getAllRoteLangage();
			if (langageMap != null && langageMap.size() > 0) {
				return langageMap.get(langid);
			}
		} else {
			Langage base = langageMap.get(langid);
			if (base == null) {
				langageMap = getAllRoteLangage();
				return langageMap.get(langid);
			} else {
				return base;
			}
		}
		return null;
	}

	@Override
	public Currency getCurrencyBean(String currency) {
		Currency bo = null;
		if (currencyMap == null || currencyMap.isEmpty()) {
			currencyMap = getAllRoteCurrency();
			if (currencyMap != null && currencyMap.size() > 0) {
				return currencyMap.get(currency.toUpperCase());
			}
		} else {
			bo = currencyMap.get(currency.toUpperCase());
			if (bo == null) {
				currencyMap = getAllRoteCurrency();
				bo = currencyMap.get(currency.toUpperCase());
				if(bo != null){
					return bo;
				}else{
					bo = new Currency();
					bo.setCode("USD");
					bo.setCurrentRate(1.00);
					bo.setSymbolCode("US$");
					return bo;
				}
			} 
			return bo;
		}
		bo = new Currency();
		bo.setCode("USD");
		bo.setCurrentRate(1.00);
		bo.setSymbolCode("US$");
		return bo;
	}
	
	@Override
	public Map<Integer, Langage> getLangageBeanMap() {
		if (langageMap == null || langageMap.isEmpty()) {
			langageMap = getAllRoteLangage();
			if (langageMap != null && langageMap.size() > 0) {
				return langageMap;
			}
		} else {
			return langageMap;
		}
		return getAllRoteLangage();
	}
	
	@Override
	public Map<String, Langage> getLangageBeanStrMap() {
		if (langageStrMap == null || langageStrMap.isEmpty()) {
			langageStrMap = getAllStrRoteLangage();
			if (langageStrMap != null && langageStrMap.size() > 0) {
				return langageStrMap;
			}
		} else {
			return langageStrMap;
		}
		return getAllStrRoteLangage();
	}
}
