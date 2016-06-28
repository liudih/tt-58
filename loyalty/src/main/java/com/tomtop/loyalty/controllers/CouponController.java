package com.tomtop.loyalty.controllers;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.FluentIterable;
import com.tomtop.framework.core.utils.Page;
import com.tomtop.framework.core.utils.Result;
import com.tomtop.loyalty.models.Coupon;
import com.tomtop.loyalty.models.MemberEvent;
import com.tomtop.loyalty.models.Prefer;
import com.tomtop.loyalty.models.Product;
import com.tomtop.loyalty.models.SendGift;
import com.tomtop.loyalty.models.filter.ProductFilter;
import com.tomtop.loyalty.models.third.Member;
import com.tomtop.loyalty.models.vo.CouponVo;
import com.tomtop.loyalty.models.vo.CouponVoMore;
import com.tomtop.loyalty.service.ICouponService;
import com.tomtop.loyalty.service.IPointService;
import com.tomtop.loyalty.service.impl.ThirdService;
import com.tomtop.loyalty.utils.ClientUtil;
import com.tomtop.loyalty.utils.CommonUtils;
import com.tomtop.loyalty.utils.CookieUtils;
import com.tomtop.loyalty.utils.TerminalUtil;

/**
 * 优惠券
 * 
 * @author xiaoch
 *
 */
@RestController
@RequestMapping(value = "/loyalty")
public class CouponController {

	@Autowired
	ICouponService couponService;

	@Autowired 
	ThirdService thirdService;

	@Autowired 
	IPointService pointService;
	
	@Value("${thirdMemberUrl}")
	private String thirdMemberUrl;

	public static final String LOYALTY_PREFER = "loyalty";
	public static final String LOYALTY_TYPE_PROMO = "promo";
	public static final String LOYALTY_TYPE_COUPON = "coupon";
	public static final String LOYALTY_TYPE_POINT = "point";
	private static Logger log = LoggerFactory
			.getLogger(CouponController.class);
	/**
	 * 应用优惠券
	 * 
	 * @return
	 */
	@ResponseBody
	@RequestMapping(method = RequestMethod.PUT, value = "/v1/coupon/apply/{code}")
	public Result apply(
			@PathVariable("code") String code,
			@RequestBody List<ProductFilter> productFilters,
			@RequestParam(value = "email", required = true) String email,
			@RequestParam(value = "depotId", required = true) Integer depotId,
			@RequestParam(value = "client", required = false, defaultValue = "5") String terminal,
			@RequestParam(value = "website", required = false, defaultValue = "10") Integer website,
			@RequestParam(value = "currency", required = false, defaultValue = "USD") String currency) {
		log.info("---apply,code="+code+",productFilters="+JSONObject.toJSONString(productFilters)+
				",email="+email+",depotId="+depotId+",terminal="+terminal+",website="+website+",currency="+currency);
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
		
		if(website==1 && (depotId==null || depotId==0)){//tomtop 站点 depotId 必传
			res.setErrMsg("depotId is null");
			res.setRet(CommonUtils.ERROR_RES);
			return res;
		}
		// 商品对象转换
		List<Product> products = thirdService.convert(productFilters,website,depotId);
		result = couponService.isCouponAvailable(code, realEmail, products,
				website, terminal, currency);
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
	 * 用户获取当前购物车可用优惠券
	 * 
	 * @return
	 */
	@ResponseBody
	@RequestMapping(method = RequestMethod.POST, value = "/v1/coupon/get")
	public Result get(
			HttpServletRequest request,
			HttpServletResponse response,
			@RequestBody List<ProductFilter> productFilters,
			@RequestParam(value = "email", required = true) String email,
			@RequestParam(value = "depotId", required = true) Integer depotId,
			@RequestParam(value = "client", required = false, defaultValue = "5") String terminal,
			@RequestParam(value = "website", required = false, defaultValue = "10") Integer website,
			@RequestParam(value = "currency", required = false, defaultValue = "USD") String currency) {
		log.info("---get,productFilters="+JSONObject.toJSONString(productFilters)+
				",email="+email+",depotId="+depotId+",terminal="+terminal+",website="+website+",currency="+currency);
		final String terminalConvert = TerminalUtil.getTerminalById(terminal);
		Result res = new Result();
		// 验证当前用户
		if (StringUtils.isEmpty(email)) {
			res.setErrMsg("No user");
			res.setRet(CommonUtils.ERROR_RES);
			return res;
		}
		
		if(website==1 && (depotId==null || depotId==0)){//tomtop 站点 depotId 必传
			res.setErrMsg("depotId is null");
			res.setRet(CommonUtils.ERROR_RES);
			return res;
		}
		
		List<Coupon> unUsedCoupons = couponService.getUnusedCouponByEmail(
				email, website);
		final String realEmail = email;
		// 商品对象转换
		List<Product> products = thirdService.convert(productFilters,website,depotId);
		List<Coupon> validCoupons = FluentIterable
				.from(unUsedCoupons)
				.filter(c -> {
					Prefer prefer = couponService.isCouponAvailable(
							c.getCode(), realEmail, products, website,
							terminalConvert, currency);
					if (prefer.isSuccess()) {
						return true;
					} else {
						return false;
					}
				}).toList();
		List<CouponVo> result = couponService.convertVo(validCoupons, currency);
		res.setRet(CommonUtils.SUCCESS_RES);
		res.setData(result);
		return res;
	}
	
	/**
	 * 
	  * @Description: 获取当前购物车有效优惠券，详细数据
	  * @param  request
	  * @param  response
	  * @param  productFilters
	  * @param  email
	  * @param  terminal
	  * @param  website
	  * @param  currency
	  * @return Result  
	  * @author liuyufeng 
	  * @date 2016年5月19日 下午4:13:08
	 */
	@ResponseBody
	@RequestMapping(method = RequestMethod.POST, value = "/v2/coupon/get")
	public String getInfo(
			HttpServletRequest request,
			HttpServletResponse response,
			@RequestBody List<ProductFilter> productFilters,
			@RequestParam(value = "email", required = true) String email,
			@RequestParam(value = "depotId", required = true) Integer depotId,
			@RequestParam(value = "client", required = false, defaultValue = "5") String terminal,
			@RequestParam(value = "website", required = false, defaultValue = "10") Integer website,
			@RequestParam(value = "currency", required = false, defaultValue = "USD") String currency) {
		log.info("---v2 get,productFilters="+JSONObject.toJSONString(productFilters)+
				",email="+email+",depotId="+depotId+",terminal="+terminal+",website="+website+",currency="+currency);
		final String terminalConvert = TerminalUtil.getTerminalById(terminal);
		Result res = new Result();
		// 验证当前用户
		if (StringUtils.isEmpty(email)) {
			res.setErrMsg("No user");
			res.setRet(CommonUtils.ERROR_RES);
			return JSONObject.toJSONString(res);
		}
		
		if(website==1 && (depotId==null || depotId==0)){//tomtop 站点 depotId 必传
			res.setErrMsg("depotId is null");
			res.setRet(CommonUtils.ERROR_RES);
			return JSONObject.toJSONString(res);
		}
		
		List<Coupon> unUsedCoupons = couponService.getUnusedCouponByEmail(
				email, website);
		final String realEmail = email;
		// 商品对象转换
		List<Product> products = thirdService.convert(productFilters,website,depotId);
		List<Coupon> validCoupons = FluentIterable
				.from(unUsedCoupons)
				.filter(c -> {
					Prefer prefer = couponService.isCouponAvailable(
							c.getCode(), realEmail, products, website,
							terminalConvert, currency);
					if (prefer.isSuccess()) {
						return true;
					} else {
						return false;
					}
				}).toList();
		List<CouponVoMore> result = couponService.convertVoMore(validCoupons, currency);
		res.setRet(CommonUtils.SUCCESS_RES);
		res.setData(result);
		return JSONObject.toJSONString(res);
	}

	/**
	 * 活动触发--发放优惠券
	 * 
	 * @return
	 */
	@ResponseBody
	@RequestMapping(method = RequestMethod.POST, value = "/v1/coupon/giving/{ruleid}")
	public Result giveCoupon(
			@PathVariable("ruleid") Integer ruleId,
			@RequestParam(value = "email", required = false) String email,
			@RequestParam(value = "website", required = false, defaultValue = "10") Integer website) {
		Result result = new Result();
		Coupon coupon = couponService.createCouponByRuleid(email, ruleId,
				website);
		if (null != coupon) {
			result.setData(coupon);
			result.setRet(CommonUtils.SUCCESS_RES);
			return result;
		}
		result.setRet(CommonUtils.ERROR_RES);
		return result;

	}

	/**
	 * 
	  * @Description:  按照传递参数送优惠券|积分
	  * @param @param activeEvent
	  * @param @return  
	  * @return Result  
	  * @author liuyufeng 
	  * @date 2016年4月15日 下午4:54:41
	 */
	@ResponseBody
	@RequestMapping(method = RequestMethod.POST, value = "/v1/coupon/sentGift")
	public Result sentGift(
			@RequestBody SendGift sendGift) {
		Integer amount = sendGift.getAmount();
		Integer ruleid = sendGift.getRuleid();
		String email = sendGift.getEmail();
		Integer point = sendGift.getPoint();
		Integer website = sendGift.getWebsite();
		log.info("---------sendGift="+JSONObject.toJSONString(sendGift));
		Result res = new Result();
		res.setRet(CommonUtils.ERROR_RES);
		if(amount!=null && amount >0 && ruleid!=null && ruleid!=0){
			for(int i=1;i<=amount;i++){
				//赠送优惠券
				Coupon coupon = couponService.createCouponByRuleid(email, ruleid,website);
				if(coupon!=null){
					res.setRet(CommonUtils.SUCCESS_RES);
				}
			}
		}
		
		if(point!=null && point!=0){
			//赠送积分
			MemberEvent memberEvent  = new MemberEvent();
			memberEvent.setEmail(email);
			memberEvent.setWebsite(website);
			String type ="only-send";
			String remark = "regist or subscriber send";
			String source = "regist or subscriber";
			boolean giveOk = pointService.givingPoint(memberEvent, point, type, remark, source);
			if(giveOk){
				res.setRet(CommonUtils.SUCCESS_RES);
			}
		}
		return res;
		
	}
	
	/**
	 * 用户中心查看自己的优惠券--未使用
	 * 
	 * @return
	 */
	@ResponseBody
	@RequestMapping(method = RequestMethod.GET, value = "/v1/coupon/unused/{uuid}")
	public Result getUnUsedCouponsPageByEmail(
			@PathVariable("uuid") String uuid,
			@RequestParam(value = "website", required = false, defaultValue = "10") Integer website,
			@RequestParam(value = "currency", required = false, defaultValue = "USD") String currency,
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
		List<Coupon> list = couponService.getUnusedCouponPageByEmail(realEmail,
				page, size, currency, website);

		// 查询总数
		int total = couponService.getUnusedTotal(realEmail, website);
		Page resultPage = Page.getPage(page, total, size);
		res.setPage(resultPage);
		res.setData(list);
		res.setRet(CommonUtils.SUCCESS_RES);
		return res;
	}

	/**
	 * 用户中心查看自己的优惠券--已使用
	 * 
	 * @return
	 */
	@ResponseBody
	@RequestMapping(method = RequestMethod.GET, value = "/v1/coupon/used/{uuid}")
	public Result getUsedCouponsPageByEmail(
			@PathVariable("uuid") String uuid,
			@RequestParam(value = "website", required = false, defaultValue = "10") Integer website,
			@RequestParam(value = "currency", required = false, defaultValue = "USD") String currency,
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
		List<Coupon> list = couponService.getUsedCouponPageByEmail(realEmail,
				page, size, currency, website);
		// 查询总数
		int total = couponService.getUsedTotal(realEmail, website);
		Page resultPage = Page.getPage(page, total, size);
		res.setPage(resultPage);
		res.setData(list);
		res.setRet(CommonUtils.SUCCESS_RES);
		return res;
	}

	/**
	 * 取消应用coupon
	 * 
	 * @return
	 */
	@ResponseBody
	@RequestMapping(method = RequestMethod.PUT, value = "v1/coupon/cancel")
	public Result cancel(HttpServletRequest request,
			HttpServletResponse response) {
		Result res = new Result();
		CookieUtils.removeCookie(CouponController.LOYALTY_PREFER, request,
				response);
		res.setRet(CommonUtils.SUCCESS_RES);
		return res;
	}

	/**
	 * 获取优惠券总数
	 * 
	 * @return
	 */
	@ResponseBody
	@RequestMapping(method = RequestMethod.GET, value = "v1/coupon/amount")
	public Result getCouponAmountByEmail(
			@RequestParam(value = "website", required = false, defaultValue = "10") Integer website,
			@RequestParam(value = "email", required = true) String email) {
		Result res = new Result();
		String realEmail = "";
		// 验证当前用户
		if (StringUtils.isEmpty(email)) {
			res.setErrMsg("No user");
			res.setRet(CommonUtils.ERROR_RES);
			return res;
		} else {
			realEmail = email;
		}
		Integer amount = 0;
		amount += couponService.getUsedTotal(realEmail, website);
		amount += couponService.getUnusedTotal(realEmail, website);
		res.setRet(CommonUtils.SUCCESS_RES);
		res.setData(amount);
		return res;
	}

}
