package com.tomtop.controllers;


import java.util.HashMap;


//import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tomtop.entity.CategoryPath;
import com.tomtop.entity.ProductBaseSearchKeyword;
import com.tomtop.framework.core.utils.Result;
import com.tomtop.services.ICategoryService;
import com.tomtop.services.IClearanceService;

/**
 * 清仓商品
 * 
 * @author renyy
 *
 */
@RestController
@RequestMapping(value = "/ic")
public class ClearanceController {
	
	//private static Logger logger = Logger.getLogger(FreeShippingController.class);
	
	@Autowired
	ICategoryService categoryService;
	@Autowired
	IClearanceService clearanceService;
	
	/**
	 * 清仓商品搜索
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
	 * @param depotName
	 *            仓库名称
	 * @param brand
	 *            品牌
	 * @param yjPrice
	 *            价格范围过滤条件
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/v1/product/clearance")
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
			@RequestParam(value = "depotName", required = false, defaultValue = "CN") String depotName,
			@RequestParam(value = "brand", required = false, defaultValue = "") String brand,
			@RequestParam(value = "yjPrice", required = false, defaultValue = "") String yjPrice) {
		Result res = new Result();
		HashMap<String, CategoryPath> cidMap = categoryService.getCategoryPathV2(website);
		Integer categoryId = 0;
		if(cpath != null && !"".equals(cpath)){
			CategoryPath cp = cidMap.get(cpath);
			if(cp != null){
				categoryId = cp.getId();
			}
		}
		//实现clearance接口
		ProductBaseSearchKeyword vo = clearanceService.getProductClearance(keyword, website, lang, currency, categoryId, page, size, sort, depotName, brand, yjPrice);
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

}
