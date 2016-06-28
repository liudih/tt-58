package com.tomtop.controllers;


import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tomtop.entity.CallProductLableSaleSku;
import com.tomtop.entity.OrderProduct;
import com.tomtop.entity.ReportProductData;
import com.tomtop.framework.core.utils.Result;
import com.tomtop.services.ICallProductService;

/**
 * 供外部调用 
 * 获取商品信息类
 * 
 * @author renyy
 *
 */
@RestController
public class CallProductController {

	@Autowired
	ICallProductService callProductService;
	
	/**
	 * 获取 sku、商品标签、是否为促销商品
	 * @param listingId 
	 * @param lang 语言Id
	 * @param client 客户端Id
	 * @author renyy
	 *
	 */
	@RequestMapping(value = "/ic/v1/call/product/lable/{listingIds}", method = RequestMethod.GET)
	public Result getCallProduct(
			@PathVariable("listingIds") List<String> listingIds,
			@RequestParam(value="lang", required = false, defaultValue = "1") Integer lang,
			@RequestParam(value="client", required = false, defaultValue = "1") Integer client,
			@RequestParam(value="website", required = false, defaultValue = "1") Integer website
			) {
		Result res = new Result();
		Map<String,CallProductLableSaleSku> callMap = callProductService.getCallProductLableSaleSkuBo(listingIds, lang, website);
		res.setRet(Result.SUCCESS);
		res.setData(callMap);
		
		return res;
	}
	
	/**
	 * 获取 sku、商品标签、是否为促销商品
	 * @param listingId 
	 * @param lang 语言Id
	 * @param client 客户端Id
	 * @param depotId 仓库Id
	 * @author renyy
	 *
	 */
	@RequestMapping(value = "/ic/v2/call/product/lable/{listingIds}", method = RequestMethod.GET)
	public Result getCallProductV2(
			@PathVariable("listingIds") List<String> listingIds,
			@RequestParam(value="lang", required = false, defaultValue = "1") Integer lang,
			@RequestParam(value="client", required = false, defaultValue = "1") Integer client,
			@RequestParam(value="website", required = false, defaultValue = "1") Integer website,
			@RequestParam(value="depotId", required = false, defaultValue = "1") Integer depotId
			) {
		Result res = new Result();
		Map<String,CallProductLableSaleSku> callMap = callProductService.getCallProductLableSaleSkuBoV2(listingIds, lang, website,depotId);
		res.setRet(Result.SUCCESS);
		res.setData(callMap);
		
		return res;
	}
	
	/**
	 * 根据品类Id搜索商品列表
	 * 
	 * @param categoryId
	 *            商品categoryId
	 * @param lang
	 *            语言ID
	 * @param website
	 *            站点ID
	 * @param currency
	 *            币种 (保留，暂不需要)
	 * @param bmain           
	 *           是否为主产品(默认为true)
	 * @param page
	 *            页数
	 * @param size
	 *            大小
	 * @param status
	 *            状态
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/ic/v1/category/report")
	public Result getProductByCategoryId(
			@RequestParam(value = "categoryId", required = false, defaultValue = "1")Integer category,
			@RequestParam(value = "lang", required = false, defaultValue = "1") Integer languageid,
			@RequestParam(value = "website", required = false, defaultValue = "1") Integer website,
			@RequestParam(value = "currency", required = false, defaultValue = "USD") String currency,
			@RequestParam(value = "bmain", required = false, defaultValue = "true") Boolean bmain,
			@RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
			@RequestParam(value = "size", required = false, defaultValue = "40") Integer size,
			@RequestParam(value = "status", required = false, defaultValue = "1") String status,
			@RequestParam(value = "storage", required = false, defaultValue = "0") Integer storage 
			) {
		Result res = new Result();
		ReportProductData rpdvo = callProductService.getReportProductDataVo(category, languageid, website, currency,bmain, page, size, status,storage);
		if(rpdvo != null && rpdvo.getRpdbos() != null && rpdvo.getRpdbos().size() > 0){
			res.setRet(Result.SUCCESS);
			res.setData(rpdvo);
		}else{
			res.setRet(Result.FAIL);
			res.setErrCode("47002");
			res.setErrMsg("search category not find");
		}
		return res;
	}
	
	/**
	 * 根据品类Id搜索商品列表
	 * 
	 * @param listingIds
	 *            商品categoryId
	 * @param lang
	 *            语言ID
	 * @param website
	 *            站点ID
	 * @param currency
	 *            币种 (保留，暂不需要)
	 * @param storage           
	 *          仓库Id
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/ic/v1/order/product")
	public Result getOrderProduct(
			@RequestParam(value = "listingIds", required = false, defaultValue = "1")List<String> listingIds,
			@RequestParam(value = "lang", required = false, defaultValue = "1") Integer lang,
			@RequestParam(value = "website", required = false, defaultValue = "1") Integer website,
			@RequestParam(value = "currency", required = false, defaultValue = "USD") String currency,
			@RequestParam(value = "depotId", required = false, defaultValue = "1") Integer depotId 
			) {
		Result res = new Result();
		
		List<OrderProduct>  opList = callProductService.getOrderProducts(listingIds, lang, website, depotId);
		if(opList != null && opList.size() > 0){
			res.setRet(Result.SUCCESS);
			res.setData(opList);
		}else{
			res.setRet(Result.FAIL);
			res.setErrCode("47003");
			res.setErrMsg("order product not find");
		}
		return res;
	}
}
