package com.tomtop.controllers;



import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tomtop.entity.DealsCategory;
import com.tomtop.entity.TopSellersProduct;
import com.tomtop.framework.core.utils.Result;
import com.tomtop.services.IJobProductService;

/**
 * 供外部调用 
 * search-job
 * 
 * @author renyy
 *
 */
@RestController
public class JobProductController {

	@Autowired
	IJobProductService jobProductService;
	
	/**
	 * 获取 品类topsellers商品
	 * @param lang 语言Id
	 * @param client 客户端Id
	 * @author renyy
	 *
	 */
	@RequestMapping(value = "/ic/v1/job/topsellers", method = RequestMethod.GET)
	public Result getJobProductTopSellers(
			@RequestParam(value="lang", required = false, defaultValue = "1") Integer lang,
			@RequestParam(value="client", required = false, defaultValue = "1") Integer client,
			@RequestParam(value="website", required = false, defaultValue = "1") Integer website
			) {
		Result res = new Result();
		List<TopSellersProduct> tspList = jobProductService.getTopSellersProduct(website, lang);
		res.setRet(Result.SUCCESS);
		res.setData(tspList);
		
		return res;
	}
	
	/**
	 * 获取 Deals专区类目是否有促销商品
	 * @param lang 语言Id
	 * @param client 客户端Id
	 * @author renyy
	 *
	 */
	@RequestMapping(value = "/ic/v1/job/deals", method = RequestMethod.GET)
	public Result getJobProductDeals(
			@RequestParam(value="lang", required = false, defaultValue = "1") Integer lang,
			@RequestParam(value="client", required = false, defaultValue = "1") Integer client,
			@RequestParam(value="website", required = false, defaultValue = "1") Integer website
			) {
		Result res = new Result();
		List<DealsCategory> dcList = jobProductService.getDealsCategory(website, lang);
		res.setRet(Result.SUCCESS);
		res.setData(dcList);
		
		return res;
	}
}
