package com.tomtop.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tomtop.entity.ProductReview;
import com.tomtop.entity.rc.FiveReview;
import com.tomtop.framework.core.utils.Page;
import com.tomtop.framework.core.utils.Result;
import com.tomtop.services.IInteractionCommentService;

/**
 * 产品评论控制类
 * 
 * @author renyy
 *
 */
@RestController
@RequestMapping(value = "/ic")
public class ProductReviewController {
	
	@Autowired
	IInteractionCommentService interactionCommentService;
	/**
	 * 获取商品评论列表
	 * 
	 * @param listingId
	 * @param client
	 * @param website
	 * @param page
	 * @param size
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/v1/product/review/list")
	public Result getProductReview(
			@RequestParam(value = "listingId") String listingId,
			@RequestParam(value = "client", required = false, defaultValue = "1") Integer client,
			@RequestParam(value = "website", required = false, defaultValue = "1") Integer website,
			@RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
			@RequestParam(value = "size", required = false, defaultValue = "10") Integer size){
		List<ProductReview> prboList = interactionCommentService.getProductReviewPage(listingId, website, page, size);
		Integer total = interactionCommentService.getProductReviewCount(listingId, website);
		Result res = new Result();
		if(prboList != null && prboList.size() > 0){
			res.setRet(Result.SUCCESS);
			res.setPage(Page.getPage(page, total, size));
			res.setData(prboList);
		}else{
			res.setRet(Result.FAIL);
			res.setErrCode("33011");
			res.setErrMsg("product review not find");
		}
		return res;
	}
	
	/**
	 * 获取商品评论详情
	 * 
	 * @param listingId
	 *            listingId
	 * @param lang
	 *            语言ID
	 * @param client
	 *            客户端ID
	 * 	@param website
	 *            站点ID
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/v1/product/review/{listingId}")
	public Result getProductReviewList(
			@PathVariable("listingId") String listingId,
			@RequestParam(value = "client", required = false, defaultValue = "1") Integer client,
			@RequestParam(value = "website", required = false, defaultValue = "1") Integer website) {
		List<ProductReview> prboList = interactionCommentService.getProductReviewBoList(listingId, website);
		Result res = new Result();
		if(prboList != null && prboList.size() > 0){
			res.setRet(Result.SUCCESS);
			res.setData(prboList);
		}else{
			res.setRet(Result.FAIL);
			res.setErrCode("33011");
			res.setErrMsg("product review not find");
		}
		return res;
	}
	
	/**
	 * 获取RC首页评论
	 * @param client
	 * @param website
	 * @param lang
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/v1/product/home/review")
	public Result getProductReviewRc(
			@RequestParam(value = "client", required = false, defaultValue = "1") Integer client,
			@RequestParam(value = "website", required = false, defaultValue = "1") Integer website,
			@RequestParam(value = "lang", required = false, defaultValue = "1") Integer lang) {
		List<FiveReview> frList = interactionCommentService.getRcHomeFiveReview(website, lang);
		Result res = new Result();
		if(frList != null && frList.size() > 0){
			res.setRet(Result.SUCCESS);
			res.setData(frList);
		}else{
			res.setRet(Result.FAIL);
			res.setErrCode("33012");
			res.setErrMsg("rc product review not find");
		}
		return res;
	}
}
