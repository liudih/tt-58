package com.tomtop.controllers;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tomtop.entity.ProductBase;
import com.tomtop.framework.core.utils.Result;
import com.tomtop.services.IProductBaseService;

/**
 * 商品基础信息接口
 * 
 * @author liulj
 *
 */
@RestController
public class ProductBaseController {


	@Autowired
	IProductBaseService productBaseService;
	
	/**
	 * 根具商品id获，商品的信息 (参数有,语言ID，站点ID获取某个商品信息)
	 * 
	 * @param listingId
	 * @param lang
	 *            语言ID， 默认为1(英文)
	 * @param client 
	 *            客户端Id
	 * @param website 
	 *            站点ID 默认为1
	 * @return
	 */
	@RequestMapping(value = "/ic/v1/products/{listingId}", method = RequestMethod.GET)
	public Result get(@PathVariable(value = "listingId") String listingId,
			@RequestParam(value="lang", required = false, defaultValue = "1") Integer lang,
			@RequestParam(value="client", required = false, defaultValue = "1") Integer client,
			@RequestParam(value="website", required = false, defaultValue = "1") Integer website,
			@RequestParam(value="currency",required = false, defaultValue = "USD") String currency) {
		ProductBase pb = productBaseService.getProductBaseByListId(listingId, lang, website, currency);
		if(pb != null){
			return new Result(Result.SUCCESS, pb);
		}
		return new Result(Result.FAIL, null);
	}

	/**
	 * 根具商品id列表获取，商品的信息 (参数有,语言ID，站点ID获取某个商品信息)
	 * 
	 * @param listingIds
	 *            商品id列表，值以,隔开，如：1,2,3
	 * @param lang
	 *            语言ID， 默认为1(英文)
	 * @param client 
	 *            客户端Id
	 * @param website 
	 *            站点ID 默认为1
	 * @return
	 */
	@RequestMapping(value = "/ic/v1/products", method = RequestMethod.GET)
	public Result getListByListingIds(
			@RequestParam(value = "listingIds") List<String> listingIds,
			@RequestParam(value="lang", required = false, defaultValue = "1") Integer lang,
			@RequestParam(value="client", required = false, defaultValue = "1") Integer client,
			@RequestParam(value="website", required = false, defaultValue = "1") Integer website,
			@RequestParam(value="currency",required = false, defaultValue = "USD") String currency) {
		if (listingIds != null && listingIds.size() > 0) {
			List<ProductBase> pbList = productBaseService.getProductBaseByListIds(listingIds, lang, website, currency);
			if(pbList == null || pbList.size() == 0){
				return new Result(Result.FAIL, "product base not find");
			}
			return new Result(Result.SUCCESS, pbList);
		}
		return new Result(Result.FAIL, null);
	}
	
	/**
	 * 根具商品id获，商品的信息 (版本2)
	 * 
	 * @param listingId
	 * @param lang
	 *            语言ID， 默认为1(英文)
	 * @param client 
	 *            客户端Id
	 * @param website 
	 *            站点ID 默认为1
	 * @param depotName
	 * 
	 * @return
	 */
	@RequestMapping(value = "/ic/v2/products/{listingId}", method = RequestMethod.GET)
	public Result getV2(@PathVariable(value = "listingId") String listingId,
			@RequestParam(value="lang", required = false, defaultValue = "1") Integer lang,
			@RequestParam(value="client", required = false, defaultValue = "1") Integer client,
			@RequestParam(value="website", required = false, defaultValue = "1") Integer website,
			@RequestParam(value="currency",required = false, defaultValue = "USD") String currency,
			@RequestParam(value="depotName",required = false, defaultValue = "CN") String depotName) {
		ProductBase pb = productBaseService.getProductBaseByListId(listingId, lang, website, currency,depotName);
		if(pb != null){
			return new Result(Result.SUCCESS, pb);
		}
		return new Result(Result.FAIL, null);
	}

	/**
	 * 根具商品id列表获取，商品的信息 (版本2)
	 * 
	 * @param listingIds
	 *            商品id列表，值以,隔开，如：1,2,3
	 * @param lang
	 *            语言ID， 默认为1(英文)
	 * @param client 
	 *            客户端Id
	 * @param website 
	 *            站点ID 默认为1
	 * @param depotName
	 * 
	 * @return
	 */
	@RequestMapping(value = "/ic/v2/products", method = RequestMethod.GET)
	public Result getListByListingIdsV2(
			@RequestParam(value = "listingIds") List<String> listingIds,
			@RequestParam(value="lang", required = false, defaultValue = "1") Integer lang,
			@RequestParam(value="client", required = false, defaultValue = "1") Integer client,
			@RequestParam(value="website", required = false, defaultValue = "1") Integer website,
			@RequestParam(value="currency",required = false, defaultValue = "USD") String currency,
			@RequestParam(value="depotName",required = false, defaultValue = "CN") String depotName) {
		if (listingIds != null && listingIds.size() > 0) {
			List<ProductBase> pbList = productBaseService.getProductBaseByListIds(listingIds, lang, website, currency,depotName);
			if(pbList == null || pbList.size() == 0){
				return new Result(Result.FAIL, "product base not find");
			}
			return new Result(Result.SUCCESS, pbList);
		}
		return new Result(Result.FAIL, null);
	}

}
