package com.tomtop.member.controllers;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.tomtop.framework.core.utils.Page;
import com.tomtop.framework.core.utils.Result;
import com.tomtop.member.models.bo.BaseBo;
import com.tomtop.member.models.bo.MemberReviewsBo;
import com.tomtop.member.models.bo.ReviewsBo;
import com.tomtop.member.models.dto.InteractionComment;
import com.tomtop.member.service.IMemberReviewsService;
import com.tomtop.member.service.IMemberService;
import com.tomtop.member.service.IReviewService;
import com.tomtop.member.utils.CommonUtils;

/**
 * 用户评论类
 * 
 * @author renyy
 * by 20160412 
 */
@RestController
@RequestMapping(value = "/member")
public class MemberReviewController {

	@Autowired
	IMemberService memberService;
	
	@Autowired
	IMemberReviewsService memberReviewService;
	
	@Autowired
	IReviewService reviewService;
	
	/**
	 * 获取用户评论列表
	 * 
	 * @add 20160412
	 */
	@RequestMapping(value = "/v1/review/list", method = RequestMethod.GET)
	public Result getMemberReviews(
			@RequestParam(value = "client", required = false, defaultValue = "1") Integer client,
			@RequestParam(value = "website", required = false, defaultValue = "1") Integer website,
			@RequestParam(value = "email") String email,
			@RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
			@RequestParam(value = "size", required = false, defaultValue = "10") Integer size,
			@RequestParam(value = "status", required = false, defaultValue = "-1") Integer status,
			@RequestParam(value = "dateType", required = false, defaultValue = "0") Integer dateType) {
		Result res = new Result();
		try {
				//获取记录总数
				Integer totalReviewsCount = memberReviewService
						.getTotalReviewsCountByMemberEmailAndSiteId(email,
								status, dateType, website);
			
				List<MemberReviewsBo> mrbolist = reviewService.getMemberReviewsBo(email, page, size, status, dateType, website);
				Page pageObj = Page.getPage(page, totalReviewsCount, size);

				res.setPage(pageObj);
				res.setData(mrbolist);
				res.setRet(CommonUtils.SUCCESS_RES);

		} catch (Exception e) {
			e.printStackTrace();
			res.setRet(CommonUtils.ERROR_RES);
			res.setErrCode("500");
			res.setErrMsg("getMemberReviews error" + e.getMessage());
		}
		return res;
	}
	
	/**
	 * 通过评论id删除
	 * 
	 * @add 20160412
	 */
	@RequestMapping(value = "/v1/review/delete/{rid}", method = RequestMethod.POST)
	public Result deleteCommentById(@PathVariable("rid") Integer rid,
			@RequestParam(value = "email") String email) {
		Result bb = new Result();
		try {
			InteractionComment comment = memberReviewService.getReviewById(rid,email);
			if(comment == null){
				bb.setRet(CommonUtils.COMMENT_LENT_ERR);
				bb.setErrMsg("comment is null");
				return bb;
			}
			if (comment.getIstate() == 0) { // 状态为未审核时才能删除
				boolean flag = memberReviewService.deleteInteractionComment(rid,email);
				if (flag) {
					bb.setRet(CommonUtils.SUCCESS_RES);
				} else {
					bb.setRet(0);
				}
			} else {
				bb.setRet(-1);
				bb.setData("comment status is not delete");
			}
		} catch (Exception e) {
			bb.setRet(CommonUtils.EXCEPTION);
			bb.setErrCode("500");
			bb.setData("delete review faile" + e.getMessage());
			e.printStackTrace();
		}
		return bb;
	}
	
	/**
	 * 用户添加评论
	 * 
	 * @add 20160413
	 */
	@RequestMapping(value = "/v1/review/add", method = RequestMethod.POST)
	public Result addReviews(@Valid ReviewsBo rb, MultipartHttpServletRequest request) {
		Result res = new Result();
		try {
			if(rb.getComment() == null || rb.getComment().length() > 2000 ){
				res.setRet(CommonUtils.COMMENT_LENT_ERR);
				res.setErrMsg("comment length over 2000 ");
				return res;
			}
			BaseBo bb = reviewService.addReview(rb, request);
			if(bb.getRes() == CommonUtils.SUCCESS_RES){
				res.setRet(CommonUtils.SUCCESS_RES);
			}else{
				res.setRet(bb.getRes());
				res.setErrMsg(bb.getMsg());
			}
		
		} catch (Exception e) {
			res.setRet(-1);
			res.setErrCode("500");
			res.setData("add review faile" + e.getMessage());
			e.printStackTrace();
		}

		return res;
	}
	
	/**
	 * 用户更新评论
	 * 
	 * @add 20160413
	 */
	@RequestMapping(value = "/v1/review/update", method = RequestMethod.POST)
	public Result updateReviews(@Valid ReviewsBo rb, 
			@RequestParam(value = "imageUrls", defaultValue = "", required = false) String[] imageUrls,
			MultipartHttpServletRequest request) {
		Result res = new Result();
		try {
			if(rb.getComment() == null || rb.getComment().length() > 500 ){
				res.setRet(CommonUtils.COMMENT_LENT_ERR);
				res.setErrMsg("comment length over ");
				return res;
			}
			BaseBo bb = reviewService.updateReview(rb, imageUrls, request);
			if(bb.getRes() == CommonUtils.SUCCESS_RES){
				res.setRet(CommonUtils.SUCCESS_RES);
			}else{
				res.setRet(bb.getRes());
				res.setErrMsg(bb.getMsg());
			}
		
		} catch (Exception e) {
			res.setRet(-1);
			res.setErrCode("500");
			res.setData("update review faile " + e.getMessage());
			e.printStackTrace();
		}

		return res;
	}
}
