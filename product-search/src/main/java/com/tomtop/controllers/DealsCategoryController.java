package com.tomtop.controllers;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tomtop.entity.DealsCategory;
import com.tomtop.entity.ProductBaseDepotSearchKeyword;
import com.tomtop.framework.core.utils.Result;
import com.tomtop.services.ICategoryService;
import com.tomtop.services.IDealsCategoryService;

/**
 * Deals频道专区
 * @author renyy
 *
 */
@RestController
@RequestMapping(value = "/ic")
public class DealsCategoryController {

	@Autowired
	IDealsCategoryService dealsCategoryService;
	@Autowired
	ICategoryService categoryService;
	
	
	/**
	 *  获取有折扣商品的类目列表
	 * @param client
	 * @return
	 */
	@Cacheable(value = "deals_category", key = "#root.caches[0].name + '_' + #client", cacheManager = "noCacheResultManager")
	@RequestMapping(value = "/v1/product/deals/category", method = RequestMethod.GET)
	public Result getDealsCategorys(
			@RequestParam(value = "client", required = false, defaultValue = "1") Integer client) {
		Result res = new Result();
		List<DealsCategory> tspList = dealsCategoryService.getDealsCategoryList(client);
		
		res.setRet(Result.SUCCESS);
		res.setData(tspList);
		return res;
	}
	
	/**
	 *  更新获取有折扣商品的类目列表
	 * @param client
	 * @return
	 */
	@CachePut(value = "deals_category", key = "#root.caches[0].name + '_' + #client" , cacheManager = "noCacheResultManager")
	@RequestMapping(value = "/v1/product/deals/category/update", method = RequestMethod.GET)
	public Result updateDealsCategorys(
			@RequestParam(value = "client", required = false, defaultValue = "1") Integer client) {
		Result res = new Result();
		List<DealsCategory> tspList = dealsCategoryService.getDealsCategoryList(client);
		
		res.setRet(Result.SUCCESS);
		res.setData(tspList);
		return res;
	}
	
	
	/**
	 * Deals商品搜索
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
	 *            排序方式  (暂不适用)
	 * @param depotName
	 *            仓库名称  (暂不适用)
	 * @param brand
	 *            品牌  (暂不适用)
	 * @param yjPrice
	 *            价格范围过滤条件 (暂不适用)
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/v1/product/deals")
	public Result getProductDeals(
			@RequestParam(value = "keyword", required = false, defaultValue = "") String keyword,
			@RequestParam(value = "lang", required = false, defaultValue = "1") Integer lang,
			@RequestParam(value = "client", required = false, defaultValue = "1") Integer client,
			@RequestParam(value = "website", required = false, defaultValue = "1") Integer website,
			@RequestParam(value = "currency", required = false, defaultValue = "USD") String currency,
			@RequestParam(value = "cpath", required = false, defaultValue = "") String cpath,
			@RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
			@RequestParam(value = "size", required = false, defaultValue = "10") Integer size,
			@RequestParam(value = "sort", required = false, defaultValue = "newest") String sort,
			@RequestParam(value = "tagName", required = false, defaultValue = "") String tagName,
			@RequestParam(value = "depotName", required = false, defaultValue = "CN") String depotName,
			@RequestParam(value = "brand", required = false, defaultValue = "") String brand,
			@RequestParam(value = "yjPrice", required = false, defaultValue = "") String yjPrice) {
		Result res = new Result();
		HashMap<String, Integer> cidMap = categoryService.getCategoryPath(website);
		Integer categoryId = 0;
		if(cpath != null && !"".equals(cpath)){
			categoryId = cidMap.get(cpath);
		}
		//实现deals接口
		ProductBaseDepotSearchKeyword vo = dealsCategoryService.getDealsProducts(keyword, website, lang, currency, categoryId, page, size, sort, depotName);
		if(vo != null && vo.getPblist() != null && vo.getPblist().size() > 0){
			res.setRet(Result.SUCCESS);
			res.setData(vo);
		}else{
			res.setRet(Result.FAIL);
			res.setErrCode("58001");
			res.setErrMsg("deals product not find");
		}
		return res;
	}
}
