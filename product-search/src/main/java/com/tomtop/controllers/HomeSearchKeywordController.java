package com.tomtop.controllers;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tomtop.entity.HomeSearchKeyword;
import com.tomtop.framework.core.utils.Result;
import com.tomtop.services.IHomeSearchKeywordService;

/**
 * 首页搜索关键字action
 * 
 * @author liulj
 *
 */
@RestController
public class HomeSearchKeywordController {

	@Autowired
	private IHomeSearchKeywordService keywordService;

	/**
	 * 根具类目获取搜索关皱键字
	 * 
	 * @param categoryId
	 *            类目Id为0表示获取首页搜索关键字
	 * @param client
	 *            客户端
	 * @param language
	 *            语言
	 * @return
	 */
	@Cacheable(value = "home_keyword", keyGenerator = "customKeyGenerator")
	@RequestMapping(value = "/ic/v1/home/search/keyword", method = RequestMethod.GET)
	public Result keyword(@RequestParam("category") int categoryId,
			@RequestParam(value="client", required = false, defaultValue = "1") int client,
			@RequestParam(value="lang", required = false, defaultValue = "1") int lang) {
		List<HomeSearchKeyword> keyList = keywordService.getKeywordList(categoryId, client, lang);
		if(keyList != null && keyList.size() > 0){
			return new Result(Result.SUCCESS, keyList);
		}
		return new Result(Result.FAIL, null);
	}

	/**
	 * 获取首页搜索关键字自动补全
	 * 
	 * @param keyword
	 *            补全的关键字
	 * @param client
	 *            客户端
	 * @param language
	 *            语言
	 * @return
	 */
	@Cacheable(value = "home_keyword", keyGenerator = "customKeyGenerator")
	@RequestMapping(value = "/ic/v1/home/search/keyword_autocomplete", method = RequestMethod.GET)
	public Result autocomplete(@RequestParam("keyword") String keyword,
			@RequestParam(value="client", required = false, defaultValue = "1") int client,
			@RequestParam(value="lang", required = false, defaultValue = "1") int lang) {
		List<String> keyAutoList = keywordService.getKeywordAutoList(keyword, client, lang);
		if(keyAutoList != null && keyAutoList.size() > 0){
			return new Result(Result.SUCCESS, keyAutoList);
		}
		return new Result(Result.FAIL, null);
	}
}
