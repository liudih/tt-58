package com.tomtop.controllers;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tomtop.entity.HomeFeaturedCategory;
import com.tomtop.entity.HomeFeaturedCategoryContent;
import com.tomtop.framework.core.utils.Result;
import com.tomtop.services.IHomeFeaturedCategoryService;

/**
 * 首页面特别类目action
 * 
 * @author liulj
 *
 */
@RestController
public class HomeFcategoryController {

	@Autowired
	IHomeFeaturedCategoryService homeFcategoryService;
	
	/**
	 * 根具语言id，和客户端id获取首页特别类目
	 * 
	 * @param client
	 *            客户端: 1 TOMTOP-PC,2 TOMTOP-Mobile,3 TOMTOP-APP-IOS,4
	 *            TOMTOP-APP-Android
	 * @param lang
	 *            语言 1 en
	 *  @param website
	 *            站点id
	 * @return
	 */
	@RequestMapping(value = "/ic/v1/home/fcategory", method = RequestMethod.GET)
	public Result getListByClientLanguage(
			@RequestParam(value="client", required = false, defaultValue = "1") int client,
			@RequestParam(value="website", required = false, defaultValue = "1") int website,
			@RequestParam(value="lang", required = false, defaultValue = "1") int lang) {
		
		List<HomeFeaturedCategory> hfcList = homeFcategoryService.getHomeFeaturedCategory(client, lang, website);
		if(hfcList == null){
			return new Result(Result.FAIL, "home fcategory not find");
		}
		return new Result(Result.SUCCESS, hfcList);
	}

	/**
	 * 根具布局标识，模块标识，语言，客户端获取模块内容列表
	 * 
	 * @param fcategoryid
	 *            特别类目id
	 * @param currency
	 *            贷币: USD,EUR,RUB,JPY,GBP,BRL,AUD,CNY
	 * @param client
	 *            客户端: 1 TOMTOP-PC,2 TOMTOP-Mobile,3 TOMTOP-APP-IOS,4
	 *            TOMTOP-APP-Android
	 * @param language
	 *            语言 1 en
	 * @param website
	 *            站点id
	 * @return 返回特别类目的内容
	 */
	@RequestMapping(value = "/ic/v1/home/fcategorycontents", method = RequestMethod.GET)
	public Result getFactegoryContext(
			@RequestParam("fcategoryid") int fcategoryid,
			@RequestParam(value="client", required = false, defaultValue = "1") int client,
			@RequestParam(value="website", required = false, defaultValue = "1") int website,
			@RequestParam(value="lang", required = false, defaultValue = "1") int lang,
			@RequestParam(value="currency",required = false, defaultValue = "USD") String currency) {
		
		HomeFeaturedCategoryContent hfcc = homeFcategoryService.getHomeFeaturedCategoryContent(fcategoryid, client, lang, website, currency);
		if(hfcc == null){
			return new Result(Result.FAIL, "home fcategory contents not find");
		}
		return new Result(Result.SUCCESS, hfcc);
	}
	
	/**
	 * 根具布局标识，模块标识，语言，客户端获取模块内容列表
	 * 
	 * @param fcategoryid
	 * @param currency
	 * @param client
	 * @param language
	 * @param website

	 * @return 返回特别类目的内容
	 */
	@RequestMapping(value = "/ic/v2/home/fcategorycontents", method = RequestMethod.GET)
	public Result getFactegoryContextV2(
			@RequestParam("fcategoryid") int fcategoryid,
			@RequestParam(value="client", required = false, defaultValue = "1") int client,
			@RequestParam(value="website", required = false, defaultValue = "1") int website,
			@RequestParam(value="lang", required = false, defaultValue = "1") int lang,
			@RequestParam(value="currency",required = false, defaultValue = "USD") String currency,
			@RequestParam(value="depotName",required = false, defaultValue = "CN") String depotName) {
		
		HomeFeaturedCategoryContent hfcc = homeFcategoryService.getHomeFeaturedCategoryContent(fcategoryid, client, lang, website, currency,depotName);
		if(hfcc == null){
			return new Result(Result.FAIL, "home fcategory contents not find");
		}
		return new Result(Result.SUCCESS, hfcc);
	}
}
