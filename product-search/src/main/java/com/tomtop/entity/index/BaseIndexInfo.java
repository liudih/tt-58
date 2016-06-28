package com.tomtop.entity.index;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.tomtop.configuration.EsConfigSettings;
import com.tomtop.utils.ConstantUtil;

/**
 * 索引基础信息
 * 
 * @author ztiny
 */
@Component
public class BaseIndexInfo {

	Logger logger = Logger.getLogger(BaseIndexInfo.class);
	
	@Autowired
	EsConfigSettings setting;
	
	// 局部更新索引时会用到
	public static JSONObject route;
	public static String[] indexAll;

	@PostConstruct
	public void init() {

		String routes = setting.getRoutes();
		route = JSONObject.parseObject(routes);

		String indexAllStr = setting.getProductAll();
		indexAll = indexAllStr.split(",");
	}

	/**
	 * 获取索引名称
	 * 
	 * @param product
	 * @return
	 */
	public List<String> getIndexNames() {
		List<String> languages = new ArrayList<String>();
		for (String index : indexAll) {
			String indexName = ConstantUtil.ES_INDEX_PREFIX + index;
			languages.add(indexName);
		}
		return languages;
	}


	/**
	 * 获取索引名称
	 * 
	 * @param languageName
	 *            国家域名缩写
	 * @return
	 */
	public String getIndexName(String languageName) {
		return ConstantUtil.ES_INDEX_PREFIX + languageName;
	}

	/**
	 * 获取索引名称
	 * 
	 * @param languageNames
	 *            国家域名缩写集合
	 * @return
	 */
	public List<String> getIndexName(List<String> languageNames) {
		if (languageNames == null || languageNames.size() < 1) {
			return null;
		}
		List<String> languages = new ArrayList<String>();
		for (String languageName : languageNames) {
			String indexName = getIndexName(languageName);
			languages.add(indexName);
		}
		return languages;
	}

	/**
	 * 字符串格式化之后，组装索引名称
	 * 
	 * @param languageName
	 *            语言名称,多个的话以逗号(,)号隔开
	 * @return
	 */
	public List<String> getDefaultIndexName(String languageName) {
		String[] languageNames = languageName.split(",");
		List<String> languages = new ArrayList<String>();
		for (String language : languageNames) {
			String indexName = getIndexName(language);
			languages.add(indexName);
		}
		return languages;
	}

}
