package com.tomtop.controllers;


import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tomtop.entity.BaseLayoutmoduleContenthProduct;
import com.tomtop.entity.rc.LayoutmoduleContentRc;
import com.tomtop.framework.core.utils.Result;
import com.tomtop.services.ILayoutService;

/**
 * 布局模块内容action
 * 
 * @author liulj
 *
 */
@RestController
public class LayoutModuleContentController {

	@Autowired
	ILayoutService layoutService;

	
	/**
	 * 首页 TOP-SELLERS、 NEW-ARRIVALS、MOREITEM-TO-CONSIDER 三个接口合一
	 * 
	 * @param layoutcode
	 * @param lang
	 * @param client
	 * @param website
	 * @param currency
	 * @author renyy
	 *
	 */
	@RequestMapping(value = "/ic/v2/layout/module/contents", method = RequestMethod.GET)
	public Result getListByLayoutModule2(
			@RequestParam(value="layoutcode",required = false, defaultValue = "HOME") String layoutcode,
			@RequestParam(value="lang", required = false, defaultValue = "1") Integer lang,
			@RequestParam(value="client", required = false, defaultValue = "1") Integer client,
			@RequestParam(value="website", required = false, defaultValue = "1") Integer website,
			@RequestParam(value="currency",required = false, defaultValue = "USD") String currency
			) {
		HashMap<String,List<BaseLayoutmoduleContenthProduct>> layoutMap = layoutService.getBaseLayoutmoduleContenth(lang, client,website,layoutcode, currency);
		
		if(layoutMap == null || layoutMap.size() == 0){
			return new Result(Result.FAIL, "layout not find");
		}
		
		return new Result(Result.SUCCESS, layoutMap);
	}
	
	/**
	 * 首页 TOP-SELLERS、 NEW-ARRIVALS、MOREITEM-TO-CONSIDER 三个接口合一
	 * 
	 * @param layoutcode
	 * @param lang
	 * @param client
	 * @param website
	 * @param currency
	 * @param depotName
	 * 				仓库名称
	 * @author renyy
	 *
	 */
	@RequestMapping(value = "/ic/v3/layout/module/contents", method = RequestMethod.GET)
	public Result getListByLayoutModule3(
			@RequestParam(value="layoutcode",required = false, defaultValue = "HOME") String layoutcode,
			@RequestParam(value="lang", required = false, defaultValue = "1") Integer lang,
			@RequestParam(value="client", required = false, defaultValue = "1") Integer client,
			@RequestParam(value="website", required = false, defaultValue = "1") Integer website,
			@RequestParam(value="currency",required = false, defaultValue = "USD") String currency,
			@RequestParam(value="depotName",required = false, defaultValue = "CN") String depotName
			) {
		HashMap<String,List<BaseLayoutmoduleContenthProduct>> layoutMap = layoutService.getBaseLayoutmoduleContenth(lang, client,website,layoutcode, currency,depotName);
		
		if(layoutMap == null || layoutMap.size() == 0){
			return new Result(Result.FAIL, "layout not find");
		}
		
		return new Result(Result.SUCCESS, layoutMap);
	}
	
	/**
	 * 首页 支持rc站首页推荐位 (比原来的多一个类目名称和类目路径)
	 * 
	 * @param layoutcode
	 * @param lang
	 * @param client
	 * @param website
	 * @param currency
	 * @param depotName
	 * 				仓库名称
	 * @author renyy
	 *
	 */
	@RequestMapping(value = "/ic/v4/layout/module/contents", method = RequestMethod.GET)
	public Result getListByLayoutModule4(
			@RequestParam(value="layoutcode",required = false, defaultValue = "HOME") String layoutcode,
			@RequestParam(value="lang", required = false, defaultValue = "1") Integer lang,
			@RequestParam(value="client", required = false, defaultValue = "1") Integer client,
			@RequestParam(value="website", required = false, defaultValue = "1") Integer website,
			@RequestParam(value="currency",required = false, defaultValue = "USD") String currency,
			@RequestParam(value="depotName",required = false, defaultValue = "CN") String depotName
			) {
		HashMap<String,List<LayoutmoduleContentRc>> layoutMap = layoutService.getBaseLayoutmoduleContenthRc(lang, client,website,layoutcode, currency,depotName);
		
		if(layoutMap == null || layoutMap.size() == 0){
			return new Result(Result.FAIL, "layout not find");
		}
		
		return new Result(Result.SUCCESS, layoutMap);
	}
}
