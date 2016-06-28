package com.tomtop.controllers;


import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tomtop.entity.CategoryPath;
import com.tomtop.entity.NewArrivalsAgg;
import com.tomtop.entity.ProductBaseDepotSearchKeyword;
import com.tomtop.entity.ProductBaseSearchKeyword;
import com.tomtop.framework.core.utils.Result;
import com.tomtop.services.ICategoryService;
import com.tomtop.services.INewArrivalsService;

/**
 * 主页新品
 * 
 * @author renyy
 *
 */
@RestController
@RequestMapping(value = "/ic")
public class NewArrivalsController {
	
	private static Logger logger = Logger.getLogger(NewArrivalsController.class);
	
	@Autowired
	INewArrivalsService newArrivalsService;
	@Autowired
	ICategoryService categoryService;
	
	
	/**
	 * 新品搜索
	 * 
	 * @param lang
	 *            语言ID
	 * @param client
	 *            客户端ID
	 * @param website
	 *            站点ID
	 * @param currency
	 *            币种
	 * @param cpath
	 *            品类路径
	 * @param page
	 *            页数
	 * @param size
	 *            大小
	 * @param sort
	 *            排序方式
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/v1/product/new")
	public Result getProductNewArrivals(
			@RequestParam(value = "keyword", required = false, defaultValue = "") String keyword,
			@RequestParam(value = "lang", required = false, defaultValue = "1") Integer lang,
			@RequestParam(value = "client", required = false, defaultValue = "1") Integer client,
			@RequestParam(value = "website", required = false, defaultValue = "1") Integer website,
			@RequestParam(value = "currency", required = false, defaultValue = "USD") String currency,
			@RequestParam(value = "cpath", required = false, defaultValue = "") String cpath,
			@RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
			@RequestParam(value = "size", required = false, defaultValue = "10") Integer size,
			@RequestParam(value = "sort", required = false, defaultValue = "") String sort,
			@RequestParam(value = "tagName", required = false, defaultValue = "") String tagName,
			@RequestParam(value = "depotName", required = false, defaultValue = "") String depotName,
			@RequestParam(value = "brand", required = false, defaultValue = "") String brand,
			@RequestParam(value = "yjPrice", required = false, defaultValue = "") String yjPrice,
			@RequestParam(value = "type", required = false, defaultValue = "") String type,
			@RequestParam(value = "releaseTime", required = false, defaultValue = "") String releaseTime) {
		Result res = new Result();
		HashMap<String, CategoryPath> cidMap = categoryService.getCategoryPathV2(website);
		Integer categoryId = 0;
		if(cpath != null && !"".equals(cpath)){
			CategoryPath cp = cidMap.get(cpath);
			if(cp != null){
				categoryId = cp.getId();
			}
		}
		ProductBaseSearchKeyword vo = newArrivalsService.getProductNewArrivals(keyword, website, lang, currency, categoryId, page, size, sort, tagName, depotName, brand, yjPrice, type,releaseTime);
		if(vo != null && vo.getPblist() != null && vo.getPblist().size() > 0){
			res.setRet(Result.SUCCESS);
			res.setData(vo);
		}else{
			res.setRet(Result.FAIL);
			res.setErrCode("47002");
			res.setErrMsg("search category not find");
		}
		return res;
	}
	
	/**
	 * 新品搜索
	 * 
	 * @param lang
	 *            语言ID
	 * @param client
	 *            客户端ID
	 * @param website
	 *            站点ID
	 * @param currency
	 *            币种
	 * @param cpath
	 *            品类路径
	 * @param page
	 *            页数
	 * @param size
	 *            大小
	 * @param sort
	 *            排序方式
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/v2/product/new")
	public Result getProductNewArrivalsV2(
			@RequestParam(value = "keyword", required = false, defaultValue = "") String keyword,
			@RequestParam(value = "lang", required = false, defaultValue = "1") Integer lang,
			@RequestParam(value = "client", required = false, defaultValue = "1") Integer client,
			@RequestParam(value = "website", required = false, defaultValue = "1") Integer website,
			@RequestParam(value = "currency", required = false, defaultValue = "USD") String currency,
			@RequestParam(value = "cpath", required = false, defaultValue = "") String cpath,
			@RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
			@RequestParam(value = "size", required = false, defaultValue = "10") Integer size,
			@RequestParam(value = "sort", required = false, defaultValue = "") String sort,
			@RequestParam(value = "tagName", required = false, defaultValue = "") String tagName,
			@RequestParam(value = "depotName", required = false, defaultValue = "") String depotName,
			@RequestParam(value = "brand", required = false, defaultValue = "") String brand,
			@RequestParam(value = "yjPrice", required = false, defaultValue = "") String yjPrice,
			@RequestParam(value = "type", required = false, defaultValue = "") String type,
			@RequestParam(value = "releaseTime", required = false, defaultValue = "") String releaseTime) {
		Result res = new Result();
		HashMap<String, CategoryPath> cidMap = categoryService.getCategoryPathV2(website);
		Integer categoryId = 0;
		if(cpath != null && !"".equals(cpath)){
			CategoryPath cp = cidMap.get(cpath);
			categoryId = cp.getId();
		}
		ProductBaseDepotSearchKeyword vo = newArrivalsService.getProductNewArrivalsDepot(keyword, website, lang, currency, categoryId, page, size, sort, tagName, depotName, brand, yjPrice, type, releaseTime);
		
		if(vo != null && vo.getPblist() != null && vo.getPblist().size() > 0){
			res.setRet(Result.SUCCESS);
			res.setData(vo);
		}else{
			res.setRet(Result.FAIL);
			res.setErrCode("47002");
			res.setErrMsg("search category not find");
		}
		return res;
	}
	/**
	 * 获取新品聚合时间的缓存
	 * 
	 * @param website
	 * 
	 * @return
	 */
	@Cacheable(value = "new_agg", key = "#root.caches[0].name + '_' + #website", cacheManager = "noCacheResultManager")
	@RequestMapping(method = RequestMethod.GET, value = "/v1/product/new/agg")
	public Result getProductNewArrivalsAgg(
			@RequestParam(value = "website", required = false, defaultValue = "1") Integer website){
		Result res = new Result();
		logger.info("create new agg");
		List<NewArrivalsAgg> nagg = newArrivalsService.getNewAgg(website);
		if(nagg != null && nagg.size() > 0){
			res.setRet(Result.SUCCESS);
			res.setData(nagg);
		}else{
			res.setRet(Result.FAIL);
			res.setErrCode("47003");
			res.setErrMsg("agg not find");
		}
		return res;
	}
	
	/**
	 * 更新新品聚合的缓存
	 * 
	 * @param website
	 * 
	 * @return
	 */
	@CachePut(value = "new_agg", key = "#root.caches[0].name + '_' + #website" , cacheManager = "noCacheResultManager")
	@RequestMapping(value = "/v1/product/new/agg/update", method = RequestMethod.GET)
	public Result putNewAgg(
			@RequestParam(value = "website", required = false, defaultValue = "1") Integer website) {
		logger.info("update new agg");
		Result res = new Result();
		List<NewArrivalsAgg> nagg = newArrivalsService.getNewAgg(website);
		if(nagg != null && nagg.size() > 0){
			res.setRet(Result.SUCCESS);
			res.setData(nagg);
		}else{
			res.setRet(Result.FAIL);
			res.setErrCode("47003");
			res.setErrMsg("agg not find");
		}
		return res;
	}

}
