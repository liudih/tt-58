package com.tomtop.controllers;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tomtop.entity.Langage;
import com.tomtop.framework.core.utils.Result;
import com.tomtop.services.IAttributesService;
import com.tomtop.services.IBaseInfoService;
/**
 * 类目属性操作
 * @author renyy
 *
 */
@RestController
public class CategoryAttributeController {

	
	@Autowired
	IAttributesService attributesService;
	
	@Autowired
	IBaseInfoService baseInfoService;
	
	/**
	 *  清楚类目属性聚合Map
	 * 
	 */
	@CacheEvict(value = "agg_att_map", allEntries = true)
	@RequestMapping(value = "/ic/v1/cache/agg_att_map/clean", method = RequestMethod.GET)
	public Result cleanAggAttMap() {
		return new Result(Result.SUCCESS, null);
	}
	
	/**
	 *  清楚类目属性聚合Key List
	 * 
	 */
	@CacheEvict(value = "agg_att_key", allEntries = true)
	@RequestMapping(value = "/ic/v1/cache/agg_att_key/clean", method = RequestMethod.GET)
	public Result cleanAggAttKey() {
		return new Result(Result.SUCCESS, null);
	}
	
	/**
	 * 更新聚合显示属性
	 * @param categoryId
	 * @return
	 */
	@RequestMapping(value = "/ic/v1/agg_att/update", method = RequestMethod.GET)
	public Result putAggAttKey(	
			@RequestParam(value = "categoryId", required = false, defaultValue = "1") Integer categoryId) {
		Map<Integer, Langage> langageMap = baseInfoService.getLangageBeanMap();
	
		Iterator<Entry<Integer, Langage>> iter = langageMap.entrySet().iterator();
		while (iter.hasNext()) {
			Entry<Integer, Langage> entry =  iter.next();
			Integer lang = entry.getKey();
			attributesService.putCategoryAttributeAllKey(categoryId, lang);
			attributesService.putCategoryAttributesMap(categoryId, lang);
		}
		
		return new Result(Result.SUCCESS, null);
	}
}
