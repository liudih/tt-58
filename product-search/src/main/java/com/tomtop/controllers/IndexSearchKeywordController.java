package com.tomtop.controllers;



import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tomtop.entity.CategoryPath;
import com.tomtop.entity.ProductBaseDepotSearchKeyword;
import com.tomtop.entity.ProductBaseSearchKeyword;
import com.tomtop.entity.ProductDepotSearchKeywordAggSort;
import com.tomtop.entity.ResultCategory;
import com.tomtop.framework.core.utils.Result;
import com.tomtop.services.ICategoryService;
import com.tomtop.services.IEsProductSearchKeywordService;
import com.tomtop.utils.CommonsUtil;

/**
 * 主页关键字搜索商品控制类(搜索引擎)
 * 
 * @author renyy
 *
 */
@RestController
@RequestMapping(value = "/ic")
public class IndexSearchKeywordController {
	//private static final Logger logger = LoggerFactory
	//		.getLogger(IndexSearchKeywordController.class);
	@Autowired
	IEsProductSearchKeywordService esProductSearchKeywordService;
	@Autowired
	ICategoryService categoryService;
	
	/**
	 * 根据关键字搜索商品列表
	 * 
	 * @param keyword
	 *            商品keyword
	 * @param lang
	 *            语言ID
	 * @param client
	 *            站点ID
	 * @param currency
	 *            币种 (保留，暂不需要)
	 * @param categoryId
	 *            品类Id
	 * @param page
	 *            页数
	 * @param size
	 *            大小
	 * @param sort
	 *            排序方式
	 * @param bmain
	 *            设置是否为主商品 默认为true
	 * @param  tagName 
	 *           标签过滤
	 * @param  depotName 
	 *           仓库过滤
	 * @param  brand 
	 *           品牌过滤
	 * @param  yjPrice 
	 *           价格过滤         
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/v1/product/search/keyword/{keyword}")
	public Result getProductByKeyword(
			@PathVariable("keyword") String keyword,
			@RequestParam(value = "lang", required = false, defaultValue = "1") Integer lang,
			@RequestParam(value = "client", required = false, defaultValue = "1") Integer client,
			@RequestParam(value = "website", required = false, defaultValue = "1") Integer website,
			@RequestParam(value = "currency", required = false, defaultValue = "USD") String currency,
			@RequestParam(value = "categoryId", required = false, defaultValue = "") String category,
			@RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
			@RequestParam(value = "size", required = false, defaultValue = "10") Integer size,
			@RequestParam(value = "sort", required = false, defaultValue = "") String sort,
			@RequestParam(value = "bmain", required = false, defaultValue = "true") boolean bmain,
			@RequestParam(value = "tagName", required = false, defaultValue = "") String tagName,
			@RequestParam(value = "depotName", required = false, defaultValue = "") String depotName,
			@RequestParam(value = "brand", required = false, defaultValue = "") String brand,
			@RequestParam(value = "yjPrice", required = false, defaultValue = "") String yjPrice,
			@RequestParam(value = "type", required = false, defaultValue = "") String type) {
		keyword = CommonsUtil.checkKeyWord(keyword);
		if(CommonsUtil.checkLength(keyword, 1000) == false){
			return new Result(Result.FAIL,"47005","keyword length over");
		}
		HashMap<String, CategoryPath> cidMap = categoryService.getCategoryPathV2(website);
		Integer categoryId = 0;
		Integer level = 1;
		if(category != null && !"".equals(category)){
			CategoryPath cp = cidMap.get(category);
			if(cp != null){
				categoryId = cp.getId();
				level = cp.getLevel();
			}
		}
		Result res = null;
		if(categoryId == null){
			res = new Result();
			res.setRet(Result.FAIL);
			res.setErrCode("47003");
			res.setErrMsg("categoryId not find");
			return res;
		}
		ProductBaseSearchKeyword vo = esProductSearchKeywordService.getProductBaseSearch(
										keyword, website, lang,currency,categoryId,level,page,size,
										sort,bmain,tagName,depotName,brand,yjPrice,type);
		res = new Result();
		if(vo != null && vo.getPblist() != null && vo.getPblist().size() > 0){
			res.setRet(Result.SUCCESS);
			res.setData(vo);
		}else{
			ProductBaseSearchKeyword twoVo = null;
			if(keyword.indexOf("-") != -1){
				String titleKeyword = keyword.replace("-", " ");
				twoVo = esProductSearchKeywordService.getProductBaseSearch(
						titleKeyword, client, lang,currency,categoryId,level,page,size,
						sort,bmain,tagName,depotName,brand,yjPrice,type);
			}
			if(twoVo == null || twoVo.getPblist() == null || twoVo.getPblist().size() == 0){
				res.setRet(Result.FAIL);
				res.setErrCode("47001");
				res.setErrMsg("search keyword not find");
			}else{
				res.setRet(Result.SUCCESS);
				res.setData(twoVo);
			}
		}
		return res;
	}
	
	/**
	 * 根据品类Id搜索商品列表
	 * 
	 * @param categoryId
	 *            商品categoryId
	 * @param lang
	 *            语言ID
	 * @param client
	 *            站点ID
	 * @param currency
	 *            币种 (保留，暂不需要)
	 * @param page
	 *            页数
	 * @param size
	 *            大小
	 * @param sort
	 *            排序方式
	 * @param bmain
	 *            设置是否为主商品 默认为true
	 * @param  tagName 
	 *           标签过滤
	 * @param  depotName 
	 *           仓库过滤
	 * @param  brand 
	 *           品牌过滤
	 * @param  yjPrice 
	 *           价格过滤         
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/v1/product/search/category")
	public Result getProductByCategory(
			@RequestParam(value = "cpath", required = false, defaultValue = "")String category,
			@RequestParam(value = "lang", required = false, defaultValue = "1") Integer lang,
			@RequestParam(value = "client", required = false, defaultValue = "1") Integer client,
			@RequestParam(value = "website", required = false, defaultValue = "1") Integer website,
			@RequestParam(value = "currency", required = false, defaultValue = "USD") String currency,
			@RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
			@RequestParam(value = "size", required = false, defaultValue = "10") Integer size,
			@RequestParam(value = "sort", required = false, defaultValue = "") String sort,
			@RequestParam(value = "bmain", required = false, defaultValue = "true") boolean bmain,
			@RequestParam(value = "tagName", required = false, defaultValue = "") String tagName,
			@RequestParam(value = "depotName", required = false, defaultValue = "") String depotName,
			@RequestParam(value = "brand", required = false, defaultValue = "") String brand,
			@RequestParam(value = "yjPrice", required = false, defaultValue = "") String yjprice,
			@RequestParam(value = "type", required = false, defaultValue = "") String type) {
		HashMap<String, CategoryPath> cidMap = categoryService.getCategoryPathV2(website);
		Integer categoryId = 0;
		Integer level = 1;
		if(category != null && !"".equals(category)){
			CategoryPath cp = cidMap.get(category);
			if(cp != null){
				categoryId = cp.getId();
				level = cp.getLevel();
			}
		}
		ResultCategory res = new ResultCategory();
		if(categoryId == null){
			res.setRet(Result.FAIL);
			res.setErrCode("47003");
			res.setErrMsg("categoryId not find");
			return res;
		}
		ProductBaseSearchKeyword vo = esProductSearchKeywordService.getProductBaseSearch(
											null,website, lang,currency, categoryId,level, page, size,sort,bmain,
											tagName,depotName,brand,yjprice,type);
		if(vo != null && vo.getPblist() != null && vo.getPblist().size() > 0){
			res.setRet(Result.SUCCESS);
			res.setCategoryId(categoryId);
			res.setData(vo);
		}else{
			res.setRet(Result.FAIL);
			res.setErrCode("47002");
			res.setErrMsg("search category not find");
		}
		return res;
	}
	
	/**
	 * 根据关键字搜索商品列表 (第二版) 主要支持tomtop多仓库
	 * 
	 * @param keyword
	 *            商品keyword
	 * @param lang
	 *            语言ID
	 * @param client
	 *            站点ID
	 * @param currency
	 *            币种 (保留，暂不需要)
	 * @param categoryId
	 *            品类Id
	 * @param page
	 *            页数
	 * @param size
	 *            大小
	 * @param sort
	 *            排序方式
	 * @param  tagName 
	 *           标签过滤
	 * @param  depotName 
	 *           仓库过滤
	 * @param  brand 
	 *           品牌过滤
	 * @param  yjPrice 
	 *           价格过滤         
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/v2/product/search/keyword/{keyword}")
	public Result getProductByKeywordV2(
			@PathVariable("keyword") String keyword,
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
			@RequestParam(value = "startPrice", required = false, defaultValue = "0") Double startPrice,
			@RequestParam(value = "endPrice", required = false, defaultValue = "0") Double endPrice) {
		keyword = CommonsUtil.checkKeyWord(keyword);
		if(CommonsUtil.checkLength(keyword, 1000) == false){
			return new Result(Result.FAIL,"47005","keyword length over");
		}
		HashMap<String, CategoryPath> cidMap = categoryService.getCategoryPathV2(website);
		Integer categoryId = 0;
		Integer level = 1;
		if(cpath != null && !"".equals(cpath)){
			CategoryPath cp = cidMap.get(cpath);
			if(cp != null){
				categoryId = cp.getId();
				level = cp.getLevel();
			}
		}
		Result res = null;
		if(categoryId == null){
			res = new Result();
			res.setRet(Result.FAIL);
			res.setErrCode("47003");
			res.setErrMsg("categoryId not find");
			return res;
		}
		ProductBaseDepotSearchKeyword vo = esProductSearchKeywordService.
				getProductBaseDepotSearch(keyword, website, lang, currency, categoryId,level,
						page, size, sort, tagName, depotName, brand, yjPrice, type,startPrice,endPrice);
	
								
		res = new Result();
		if(vo != null && vo.getPblist() != null && vo.getPblist().size() > 0){
			res.setRet(Result.SUCCESS);
			res.setData(vo);
		}else{
			ProductBaseDepotSearchKeyword twoVo = null;
			if(keyword.indexOf("-") != -1){
				String titleKeyword = keyword.replace("-", " ");
				twoVo = esProductSearchKeywordService.
						getProductBaseDepotSearch(titleKeyword, website, lang, currency, categoryId,level,
							page, size, sort, tagName, depotName, brand, yjPrice, type,startPrice,endPrice);
			}
			if(twoVo == null || twoVo.getPblist() == null || twoVo.getPblist().size() == 0){
				res.setRet(Result.FAIL);
				res.setErrCode("47001");
				res.setErrMsg("search keyword not find");
			}else{
				res.setRet(Result.SUCCESS);
				res.setData(twoVo);
			}
		}
		return res;
	}
	
	/**
	 * 根据品类Id搜索商品列表 (第二版) 主要支持tomtop多仓库
	 * 
	 * @param categoryId
	 *            商品categoryId
	 * @param lang
	 *            语言ID
	 * @param client
	 *            站点ID
	 * @param currency
	 *            币种 (保留，暂不需要)
	 * @param page
	 *            页数
	 * @param size
	 *            大小
	 * @param sort
	 *            排序方式
	 * @param  tagName 
	 *           标签过滤
	 * @param  depotName 
	 *           仓库过滤
	 * @param  brand 
	 *           品牌过滤
	 * @param  yjPrice 
	 *           价格过滤         
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/v2/product/search/category")
	public Result getProductByCategoryV2(
			@RequestParam(value = "cpath", required = false, defaultValue = "")String cpath,
			@RequestParam(value = "lang", required = false, defaultValue = "1") Integer lang,
			@RequestParam(value = "client", required = false, defaultValue = "1") Integer client,
			@RequestParam(value = "website", required = false, defaultValue = "1") Integer website,
			@RequestParam(value = "currency", required = false, defaultValue = "USD") String currency,
			@RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
			@RequestParam(value = "size", required = false, defaultValue = "10") Integer size,
			@RequestParam(value = "sort", required = false, defaultValue = "") String sort,
			@RequestParam(value = "tagName", required = false, defaultValue = "") String tagName,
			@RequestParam(value = "depotName", required = false, defaultValue = "") String depotName,
			@RequestParam(value = "brand", required = false, defaultValue = "") String brand,
			@RequestParam(value = "yjPrice", required = false, defaultValue = "") String yjPrice,
			@RequestParam(value = "type", required = false, defaultValue = "") String type,
			@RequestParam(value = "startPrice", required = false, defaultValue = "0") Double startPrice,
			@RequestParam(value = "endPrice", required = false, defaultValue = "0") Double endPrice) {
		
		HashMap<String, CategoryPath> cidMap = categoryService.getCategoryPathV2(website);
		Integer categoryId = 0;
		Integer level = 1;
		if(cpath != null && !"".equals(cpath)){
			CategoryPath cp = cidMap.get(cpath);
			if(cp != null){
				categoryId = cp.getId();
				level = cp.getLevel();
			}
		}
		ResultCategory res = new ResultCategory();
		if(categoryId == null){
			res.setRet(Result.FAIL);
			res.setErrCode("47003");
			res.setErrMsg("categoryId not find");
			return res;
		}
		ProductBaseDepotSearchKeyword vo = esProductSearchKeywordService.
				getProductBaseDepotSearch(null, website, lang, currency, categoryId,level,
						page, size, sort, tagName, depotName, brand, yjPrice, type,startPrice,endPrice);
		if(vo != null && vo.getPblist() != null && vo.getPblist().size() > 0){
			res.setRet(Result.SUCCESS);
			res.setCategoryId(categoryId);
			res.setData(vo);
		}else{
			res.setRet(Result.FAIL);
			res.setErrCode("47002");
			res.setErrMsg("search category not find");
		}
		return res;
	}
	
	/**
	 * 根据品类Id搜索商品列表 (第三版) 升级深度分页
	 * 
	 * @param cpath
	 *            类目路径
	 * @param lang
	 *            语言ID
	 * @param client
	 *            客户端ID
	 * @param website
	 *            站点ID
	 * @param currency
	 *            币种 
	 * @param page 页数
	 *  
	 * @param size 大小
	 *   
	 * @param sort 排序
	 * 
	 * @param tagName 标签名
	 *  
	 * @param depotName 仓库名
	 *   
	 * @param brand 品牌
	 *    
	 * @param yjPrice 价格区间
	 * 
	 * @param type 属性条件
	 * 
	 * @param startPrice 价格条件 区间 开始价 (目前用于m端)
	 * 
	 * @param endPrice 价格条件 区间 结束价  (目前用于m端)
	 * 
	 * @author renyy add by 20160531    
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/v3/product/search/category")
	public Result getProductByCategoryV3(
			@RequestParam(value = "cpath", required = false, defaultValue = "")String cpath,
			@RequestParam(value = "lang", required = false, defaultValue = "1") Integer lang,
			@RequestParam(value = "client", required = false, defaultValue = "1") Integer client,
			@RequestParam(value = "website", required = false, defaultValue = "1") Integer website,
			@RequestParam(value = "currency", required = false, defaultValue = "USD") String currency,
			@RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
			@RequestParam(value = "size", required = false, defaultValue = "10") Integer size,
			@RequestParam(value = "sort", required = false, defaultValue = "") String sort,
			@RequestParam(value = "tagName", required = false, defaultValue = "") String tagName,
			@RequestParam(value = "depotName", required = false, defaultValue = "") String depotName,
			@RequestParam(value = "brand", required = false, defaultValue = "") String brand,
			@RequestParam(value = "yjPrice", required = false, defaultValue = "") String yjPrice,
			@RequestParam(value = "type", required = false, defaultValue = "") String type,
			@RequestParam(value = "startPrice", required = false, defaultValue = "0") Double startPrice,
			@RequestParam(value = "endPrice", required = false, defaultValue = "0") Double endPrice) {
		
		HashMap<String, CategoryPath> cidMap = categoryService.getCategoryPathV2(website);
		Integer categoryId = 0;
		Integer level = 1;
		if(cpath != null && !"".equals(cpath)){
			CategoryPath cp = cidMap.get(cpath);
			if(cp != null){
				categoryId = cp.getId();
				level = cp.getLevel();
			}
		}
		ResultCategory res = new ResultCategory();
		if(categoryId == null){
			res.setRet(Result.FAIL);
			res.setErrCode("47004");
			res.setErrMsg("categoryId v3 not find");
			return res;
		}
		ProductDepotSearchKeywordAggSort vo = esProductSearchKeywordService.
				getProductBaseDepotSearchScroll(null, website, lang, currency, categoryId,level,
						page, size, sort, tagName, depotName, brand, yjPrice, type,startPrice,endPrice);
		if(vo != null && vo.getPblist() != null && vo.getPblist().size() > 0){
			res.setRet(Result.SUCCESS);
			res.setCategoryId(categoryId);
			res.setData(vo);
		}else{
			res.setRet(Result.FAIL);
			res.setErrCode("47005");
			res.setErrMsg("search category v3 not find");
		}
		return res;
	}
	
	/**
	 * 根据关键字搜索商品列表 (第三版) 升级深度分页
	 * 
	 * @param keyword
	 *            商品关键字
	 * @param lang
	 *            语言ID
	 * @param client
	 *            客户端ID
	 * @param website
	 *            站点ID
	 * @param currency
	 *            币种
	 * @param currency
	 *            币种
	 * @param cpath
	 *            类目路径          
	 * @param page 
	 *  		       页数
	 * @param size 
	 *    		      大小
	 * @param sort 
	 *            排序
	 * @param tagName 
	 * 			     标签名
	 * @param depotName 
	 *  		     仓库名
	 * @param brand 
	 *   		     品牌
	 * @param yjPrice 
	 * 			     价格区间
	 * @param type 
	 * 			     属性条件
	 * @param startPrice 
	 * 			   价格条件 区间 开始价 (目前用于m端)
	 * @param endPrice 
	 * 			   价格条件 区间 结束价  (目前用于m端)
	 * @author renyy add by 20160531    
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/v3/product/search/keyword/{keyword}")
	public Result getProductByKeywordV3(
			@PathVariable("keyword") String keyword,
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
			@RequestParam(value = "startPrice", required = false, defaultValue = "0") Double startPrice,
			@RequestParam(value = "endPrice", required = false, defaultValue = "0") Double endPrice) {
		keyword = CommonsUtil.checkKeyWord(keyword);
		if(CommonsUtil.checkLength(keyword, 1000) == false){
			return new Result(Result.FAIL,"47005","keyword length over");
		}
		HashMap<String, CategoryPath> cidMap = categoryService.getCategoryPathV2(website);
		Integer categoryId = 0;
		Integer level = 1;
		if(cpath != null && !"".equals(cpath)){
			CategoryPath cp = cidMap.get(cpath);
			if(cp != null){
				categoryId = cp.getId();
				level = cp.getLevel();
			}
		}
		Result res = null;
		if(categoryId == null){
			res = new Result();
			res.setRet(Result.FAIL);
			res.setErrCode("47003");
			res.setErrMsg("categoryId not find");
			return res;
		}
		ProductDepotSearchKeywordAggSort vo = esProductSearchKeywordService.
				getProductBaseDepotSearchScroll(keyword, website, lang, currency, categoryId,level,
						page, size, sort, tagName, depotName, brand, yjPrice, type,startPrice,endPrice);
	
		res = new Result();
		if(vo != null && vo.getPblist() != null && vo.getPblist().size() > 0){
			res.setRet(Result.SUCCESS);
			res.setData(vo);
		}else{
			ProductDepotSearchKeywordAggSort twoVo = null;
			if(keyword.indexOf("-") != -1){
				String titleKeyword = keyword.replace("-", " ");
				twoVo = esProductSearchKeywordService.
						getProductBaseDepotSearchScroll(titleKeyword, website, lang, currency, categoryId,level,
							page, size, sort, tagName, depotName, brand, yjPrice, type,startPrice,endPrice);
			}
			if(twoVo == null || twoVo.getPblist() == null || twoVo.getPblist().size() == 0){
				res.setRet(Result.FAIL);
				res.setErrCode("47001");
				res.setErrMsg("search keyword not find");
			}else{
				res.setRet(Result.SUCCESS);
				res.setData(twoVo);
			}
		}
		return res;
	}
}
