package com.tomtop.loyalty.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.tomtop.framework.core.utils.Page;
import com.tomtop.framework.core.utils.Result;
import com.tomtop.loyalty.models.Point;
import com.tomtop.loyalty.models.Prefer;
import com.tomtop.loyalty.models.third.Member;
import com.tomtop.loyalty.service.IPointService;
import com.tomtop.loyalty.utils.ClientUtil;
import com.tomtop.loyalty.utils.CommonUtils;
import com.tomtop.loyalty.utils.TerminalUtil;

/**
 * 积分
 * 
 * @author xiaoch
 *
 */
@RestController
@RequestMapping(value = "/loyalty")
public class PointController {
	@Autowired
	IPointService pointService;

	@Value("${thirdMemberUrl}")
	private String thirdMemberUrl;
	
	private Logger logger = LoggerFactory.getLogger(PointController.class);

	/**
	 * 应用积分
	 * 
	 * @return
	 */
	@ResponseBody
	@RequestMapping(method = RequestMethod.PUT, value = "/v1/point/apply/{point}")
	public Result apply(
			@PathVariable("point") Integer point,
			@RequestParam(value = "email", required = true) String email,
			@RequestParam(value = "client", required = false, defaultValue = "5") String terminal,
			@RequestParam(value = "website", required = false, defaultValue = "10") Integer website,
			@RequestParam(value = "currency", required = false, defaultValue = "USD") String currency) {
		logger.info("start apply point-->email="+email+"website="+website+"point="+point);
		terminal = TerminalUtil.getTerminalById(terminal);
		Result res = new Result();
		Prefer result = new Prefer();
		String realEmail = "";
		if (StringUtils.isEmpty(email)) {
			res.setErrMsg("No user");
			res.setRet(CommonUtils.ERROR_RES);
			return res;
		} else {
			realEmail = email;
		}
		result = pointService.isPointAvailable(realEmail, point, website,
				currency);
		if (result.isSuccess() == true) {
			res.setRet(CommonUtils.SUCCESS_RES);
			res.setData(result);
			return res;
		} else {
			res.setData(result);
			res.setRet(CommonUtils.ERROR_RES);
			return res;
		}
	}

	/**
	 * 发放积分
	 * 
	 * @return
	 */
	@ResponseBody
	@RequestMapping(method = RequestMethod.POST, value = "v1/point/giving/{code}")
	public Result givingPoint(HttpServletRequest request,
			HttpServletResponse response) {
		Result res = new Result();
		res.setRet(CommonUtils.SUCCESS_RES);
		return res;
	}

	/**
	 * 获取用户的总积分
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(method = RequestMethod.GET, value = "v1/point")
	public Result getUserTotalPoint(
			@RequestParam(value = "email", required = true) String email,
			@RequestParam(value = "client", required = false, defaultValue = "5") String terminal,
			@RequestParam(value = "website", required = false, defaultValue = "10") Integer website,
			@RequestParam(value = "currency", required = false, defaultValue = "USD") String currency) {
		Result res = new Result();
		Integer point = pointService.getUserTotalPointByEmail(email, website);
		res.setData(point);
		res.setRet(CommonUtils.SUCCESS_RES);
		return res;
	}
	
	/**
	 * 
	  * @Description: 获取用户积分信息，总积分，剩余可用，锁定积分
	  * @param  email
	  * @param  terminal
	  * @param  website
	  * @param  currency
	  * @return Result  
	  * @author liuyufeng 
	  * @date 2016年5月17日 下午3:00:24
	 */
	@ResponseBody
	@RequestMapping(method = RequestMethod.GET, value = "v1/userPointInfo")
	public Result getUserPointInfo(
			@RequestParam(value = "email", required = true) String email,
			@RequestParam(value = "client", required = false, defaultValue = "5") String terminal,
			@RequestParam(value = "website", required = false, defaultValue = "10") Integer website,
			@RequestParam(value = "currency", required = false, defaultValue = "USD") String currency) {
		Result res = new Result();
		try{
			Map<String, Integer> userPointInfo = pointService.getUserPointInfo(email, website);
			res.setRet(CommonUtils.SUCCESS_RES);
			res.setData(userPointInfo);
		}catch(Exception e){
			res.setRet(CommonUtils.ERROR_RES);
			logger.error("getUserPointInfo,e:",e);
		}
		return res;
	}

	/**
	 * 未应用积分记录
	 * 
	 * @return
	 */
	@ResponseBody
	@RequestMapping(method = RequestMethod.GET, value = "/v1/point/unused/{uuid}")
	public Result getUnusedPointHistory(
			@PathVariable("uuid") String uuid,
			@RequestParam(value = "website", required = false, defaultValue = "10") Integer website,
			@RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
			@RequestParam(value = "size", required = false, defaultValue = "10") Integer size) {
		Result res = new Result();
		String realEmail = "";
		// 验证当前用户
		String thirdEmail = ClientUtil.getRequest(thirdMemberUrl
				+ "/member/v1/email/" + uuid + "?client=" + website);
		Member memberResult = JSON.parseObject(thirdEmail, Member.class);
		if (null == memberResult
				|| memberResult.getRet().equals(CommonUtils.ERROR_RES)) {
			res.setRet(CommonUtils.ERROR_RES);
			return res;
		} else {
			realEmail = memberResult.getData().getEmail();
		}
		List<Point> points = pointService.getUnusedPointHistory(realEmail,
				website, page, size);
		res.setRet(CommonUtils.SUCCESS_RES);
		// 查询总数
		int total = pointService.getUnusedTotalCountByEmail(realEmail, website);
		Page resultPage = Page.getPage(page, total, size);
		res.setPage(resultPage);
		res.setData(points);
		return res;

	}

	/**
	 * 已使用积分记录
	 * 
	 * @return
	 */
	@ResponseBody
	@RequestMapping(method = RequestMethod.GET, value = "/v1/point/used/{uuid}")
	public Result getUsedPointHistory(
			@PathVariable("uuid") String uuid,
			@RequestParam(value = "website", required = false, defaultValue = "10") Integer website,
			@RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
			@RequestParam(value = "size", required = false, defaultValue = "10") Integer size) {
		Result res = new Result();
		String realEmail = "";
		// 验证当前用户
		String thirdEmail = ClientUtil.getRequest(thirdMemberUrl
				+ "/member/v1/email/" + uuid + "?client=" + website);
		Member memberResult = JSON.parseObject(thirdEmail, Member.class);
		if (null == memberResult
				|| memberResult.getRet().equals(CommonUtils.ERROR_RES)) {
			res.setRet(CommonUtils.ERROR_RES);
			return res;
		} else {
			realEmail = memberResult.getData().getEmail();
		}
		List<Point> points = pointService.getUsedPointHistory(realEmail,
				website, page, size);
		res.setRet(CommonUtils.SUCCESS_RES);
		// 查询总数
		int total = pointService.getUsedTotalCountByEmail(realEmail, website);
		Page resultPage = Page.getPage(page, total, size);
		res.setPage(resultPage);
		res.setData(points);
		return res;
	}

}
