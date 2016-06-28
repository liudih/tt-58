package com.tomtop.controllers;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tomtop.entity.ProductBaseDepotSearchKeyword;
import com.tomtop.entity.ProductBaseRecommend;
import com.tomtop.entity.SearchDepotProduct;
import com.tomtop.entity.TopSellersProduct;
import com.tomtop.framework.core.utils.Result;
import com.tomtop.services.ICategoryService;
import com.tomtop.services.ITopSellersRecService;

@RestController
@RequestMapping(value = "/ic")
public class TopSellersRecController {

	@Autowired
	ITopSellersRecService topSellersRecService;
	@Autowired
	ICategoryService categoryService;
	
	
	/**
	 *  用于更新获取top-sellers首页的商品数据缓存
	 * @param client
	 * @return
	 */
	@RequestMapping(value = "/v1/product/put/topsellers", method = RequestMethod.GET)
	public Result putTopSellers(
			@RequestParam(value = "client", required = false, defaultValue = "1") Integer client) {
		Result res = new Result();
		List<TopSellersProduct> tspList = topSellersRecService.putTopSellersProductList(client);
		
		res.setRet(Result.SUCCESS);
		res.setData(tspList);
		return res;
	}
	
	
	/**
	 * 获取top-sellers首页的商品数据
	 * @param lang
	 * @param client
	 * @param website
	 * @param currency
	 * @return
	 */
	@RequestMapping(value = "/v1/product/topsellers/home", method = RequestMethod.GET)
	public Result getTopSellers(
			@RequestParam(value = "lang", required = false, defaultValue = "1") Integer lang,
			@RequestParam(value = "client", required = false, defaultValue = "1") Integer client,
			@RequestParam(value = "website", required = false, defaultValue = "1") Integer website,
			@RequestParam(value = "currency", required = false, defaultValue = "USD") String currency) {
		Result res = new Result();
		LinkedHashMap<String, List<SearchDepotProduct>> sdpMap = topSellersRecService.getProductMap(website, client, lang, currency);
		if(sdpMap != null && sdpMap.size() > 0){
			res.setRet(Result.SUCCESS);
			res.setData(sdpMap);
		}else{
			res.setRet(Result.FAIL);
			res.setErrMsg("topsellers not find");
		}
		return res;
	}
	
	/**
	 * Top-Sellers商品搜索
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
	@RequestMapping(method = RequestMethod.GET, value = "/v1/product/topsellers")
	public Result getProductTopSellers(
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
			@RequestParam(value = "depotName", required = false, defaultValue = "CN") String depotName,
			@RequestParam(value = "brand", required = false, defaultValue = "") String brand,
			@RequestParam(value = "yjPrice", required = false, defaultValue = "") String yjPrice) {
		Result res = new Result();
		HashMap<String, Integer> cidMap = categoryService.getCategoryPath(website);
		Integer categoryId = 0;
		if(cpath != null && !"".equals(cpath)){
			categoryId = cidMap.get(cpath);
		}
		
		//实现top-sellers接口
		ProductBaseDepotSearchKeyword vo = topSellersRecService.getProductTopSellers(keyword, website, lang, currency, categoryId, page, size, sort);
		if(vo != null && vo.getPblist() != null && vo.getPblist().size() > 0){
			res.setRet(Result.SUCCESS);
			res.setData(vo);
		}else{
			res.setRet(Result.FAIL);
			res.setErrCode("58001");
			res.setErrMsg("top sellers not find");
		}
		return res;
	}
	
	/**
	 * 获取hotsellers商品数据
	 * @param lang
	 * @param client
	 * @param website
	 * @param currency
	 * @param page
	 * @param size
	 * @param depotName
	 * @return
	 */
	@RequestMapping(value = "/v1/product/hotsellers", method = RequestMethod.GET)
	public Result getHotSellers(
			@RequestParam(value = "lang", required = false, defaultValue = "1") Integer lang,
			@RequestParam(value = "client", required = false, defaultValue = "1") Integer client,
			@RequestParam(value = "website", required = false, defaultValue = "1") Integer website,
			@RequestParam(value = "currency", required = false, defaultValue = "USD") String currency,
			@RequestParam(value = "cpath", required = false, defaultValue = "") String cpath,
			@RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
			@RequestParam(value = "size", required = false, defaultValue = "20") Integer size,
			@RequestParam(value = "depotName", required = false, defaultValue = "CN") String depotName) {
		Result res = new Result();
		HashMap<String, Integer> cidMap = categoryService.getCategoryPath(website);
		Integer categoryId = 0;
		if(cpath != null && !"".equals(cpath)){
			categoryId = cidMap.get(cpath);
		}
		List<ProductBaseRecommend> pbrlist = topSellersRecService.getHotSellersProduct(website, lang, currency,categoryId, page, size, depotName);
		
		if(pbrlist != null && pbrlist.size() > 0){
			res.setRet(Result.SUCCESS);
			res.setData(pbrlist);
		}else{
			res.setRet(Result.FAIL);
			res.setErrMsg("hotsellers not find");
		}
		return res;
	}
}
