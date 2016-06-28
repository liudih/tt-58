package com.tomtop.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import com.tomtop.framework.core.utils.Result;
import com.tomtop.services.IProductUrlService;
import com.tomtop.utils.CommonDefn;
import com.tomtop.utils.HttpClientUtil;

@RestController
public class TestController {

	private String testUrl = "http://product.api.tomtop.com/ic";
	
	@Autowired
	IProductUrlService productUrlService;
	
	private static final int site = 10;
	
	@RequestMapping(value = "/test", method = RequestMethod.GET)
	public Result test() {
		Result res = new Result();
		List<String> strList = new ArrayList<String>();
		long l = System.currentTimeMillis();
		//检测mysql接口
		String brand = HttpClientUtil.doGet(testUrl + "/v1/home/brand");
		long l2 = System.currentTimeMillis();
		if(brand != null){
			strList.add("mysql brand link time = " + (l2-l));
		}
		//检测pgsql接口
		l = System.currentTimeMillis();
		String categories = HttpClientUtil.doGet(testUrl + "/v1/categories");
		l2 = System.currentTimeMillis();
		if(categories != null){
			strList.add("pgsql categories link time = " + (l2-l));
		}
		//检测搜索引擎接口
		l = System.currentTimeMillis();
		String hot = HttpClientUtil.doGet(testUrl + "/v1/product/hot");
		l2 = System.currentTimeMillis();
		if(hot != null){
			strList.add("es hot product link time = " + (l2-l));
		}
		res.setRet(CommonDefn.ONE);
		res.setData(strList);
		return res;
	}
	
	@RequestMapping(value = "/update/chicuu/product/url", method = RequestMethod.GET)
	public Result updateChicuu() {
		Map<String, String> map = productUrlService.updateProductUrl(site);
		return new Result(Result.SUCCESS, map);
	}
	
	@RequestMapping(value = "/update/chicuu/product/url/listingId", method = RequestMethod.GET)
	public Result updateChicuuByListingId(@Param("listingId") String listingId) {
		Map<String, String> map = productUrlService.updateProductUrlByListingId(listingId, site);
		return new Result(Result.SUCCESS, map);
	}
}
