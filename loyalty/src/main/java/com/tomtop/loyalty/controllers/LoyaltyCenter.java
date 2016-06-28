package com.tomtop.loyalty.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.tomtop.framework.core.utils.Result;
import com.tomtop.loyalty.models.Prefer;
import com.tomtop.loyalty.models.Product;
import com.tomtop.loyalty.models.filter.PreferFilter;
import com.tomtop.loyalty.models.filter.ProductFilter;
import com.tomtop.loyalty.service.IPointService;
import com.tomtop.loyalty.service.IPromoService;
import com.tomtop.loyalty.service.impl.CouponService;
import com.tomtop.loyalty.service.impl.PreferCenterService;
import com.tomtop.loyalty.service.impl.ThirdService;
import com.tomtop.loyalty.utils.CommonUtils;
import com.tomtop.loyalty.utils.CookieUtils;
import com.tomtop.loyalty.utils.TerminalUtil;

/**
 * 优惠中心
 * 
 * @author xiaoch
 *
 */
@RestController
@RequestMapping(value = "/loyalty")
public class LoyaltyCenter {

	@Value("${thirdMemberUrl}")
	private String thirdMemberUrl;

	@Autowired
	ThirdService thirdService;

	@Autowired
	CouponService couponService;
	@Autowired
	PreferCenterService preferCenterService;
	@Autowired
	IPromoService promoService;
	@Autowired
	IPointService pointService;

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	/**
	 * 获取当前所有优惠
	 * 
	 * @return
	 */
	@ResponseBody
	@RequestMapping(method = RequestMethod.POST, value = "/v1/prefercenter/allprefer")
	public Result getAllPrefer(
			HttpServletRequest request,
			HttpServletResponse response,
			@RequestBody List<ProductFilter> productFilters,
			@RequestParam(value = "email", required = true) String email,
			@RequestParam(value = "depotId", required = true) Integer depotId,
			@RequestParam(value = "client", required = false, defaultValue = "5") String terminal,
			@RequestParam(value = "website", required = false, defaultValue = "10") Integer website,
			@RequestParam(value = "currency", required = false, defaultValue = "USD") String currency) {
		logger.info("---getAllPrefer,productFilters="+JSONObject.toJSONString(productFilters)+
				",email="+email+",depotId="+depotId+",terminal="+terminal+",website="+website+",currency="+currency);
		terminal = TerminalUtil.getTerminalById(terminal);
		Result res = new Result();
		List<Prefer> prefers = new ArrayList<Prefer>(5);
		String realEmail = "";
		// 验证当前用户
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
		String loyaltyCookies = CookieUtils.getCookie(
				CouponController.LOYALTY_PREFER, request, response);
		// 商品对象转换
		List<Product> products = thirdService.convert(productFilters,website,depotId);
		
		if (StringUtils.isNotEmpty(loyaltyCookies)) {

			String[] loyaltyArray = org.apache.commons.lang3.StringUtils.split(
					loyaltyCookies, ":");

			if (null != loyaltyArray && loyaltyArray.length == 2) {

				String loyaltyType = loyaltyArray[0];
				String loyaltyCode = loyaltyArray[1];
				if (CouponController.LOYALTY_TYPE_COUPON.equals(loyaltyType)) {
					Prefer couponLoyaltyPrefer = couponService
							.isCouponAvailable(loyaltyCode, realEmail,
									products, website, terminal, currency);
					if (couponLoyaltyPrefer.isSuccess()) {
						prefers.add(couponLoyaltyPrefer);
					} else {
						this.undoCurrentPrefer(request, response);
					}
				} else if (CouponController.LOYALTY_TYPE_PROMO
						.equals(loyaltyType)) {
					Prefer couponLoyaltyPrefer = promoService.isPromoAvailable(
							loyaltyCode, realEmail, products, website,
							terminal, currency);
					if (couponLoyaltyPrefer.isSuccess()) {
						prefers.add(couponLoyaltyPrefer);
					} else {
						this.undoCurrentPrefer(request, response);
					}
				}
			}
		}
		if (StringUtils.isNotEmpty(CookieUtils.getCookie(
				CouponController.LOYALTY_TYPE_POINT, request, response))) {
			CookieUtils.removeCookie(CouponController.LOYALTY_TYPE_POINT,
					request, response);
		}
		res.setRet(CommonUtils.SUCCESS_RES);
		res.setData(prefers);
		return res;
	}

	/**
	 * 订单已经生成,保存所有优惠
	 * 
	 * @return
	 */
	@ResponseBody
	@RequestMapping(method = RequestMethod.POST, value = "/v1/prefercenter/saveprefer")
	public Result saveAllPrefer(
			@RequestParam(value = "email", required = true) String email,
			@RequestParam(value = "depotId", required = true) Integer depotId,
			@RequestParam(value = "client", required = false, defaultValue = "5") String terminal,
			@RequestParam(value = "website", required = false, defaultValue = "10") Integer website,
			@RequestParam(value = "currency", required = false, defaultValue = "USD") String currency,
			@RequestBody Map<String, Object> map) {
		logger.info("saveAllPrefer --email:"+email+",client="+terminal+",website="+website+",currency="+currency+",map="+JSONObject.toJSONString(map));
		Result res = new Result();
		terminal = TerminalUtil.getTerminalById(terminal);
		Prefer applyResult = new Prefer();
		List<PreferFilter> preferFilters=null;
		List<ProductFilter> productFilters=null;
		try{
		preferFilters =JSON.parseArray(JSON.toJSONString(map.get("prefer")), PreferFilter.class);
		String object  = JSON.toJSONString(map.get("product"));
		productFilters = JSON.parseArray(object, ProductFilter.class);
//		productFilters=JSON.parseArray(JSON.toJSONString(map.get("product")), List<ProductFilter>.class);
		}catch(Exception e){
			e.printStackTrace();
			res.setRet(CommonUtils.ERROR_RES);
			res.setErrMsg("Parameter passing there is an error, converting objects fail");
			return res;
		}
		
		if(website==1 && (depotId==null || depotId==0)){//tomtop 站点 depotId 必传
			res.setErrMsg("depotId is null");
			res.setRet(CommonUtils.ERROR_RES);
			return res;
		}
		// 商品对象转换
		List<Product> products = thirdService.convert(productFilters,website,depotId);
		
		if (CollectionUtils.isNotEmpty(preferFilters)) {
			for (int i = 0; i < preferFilters.size(); i++) {
				PreferFilter loyaltyPrefer = preferFilters.get(i);
				if (null == loyaltyPrefer) {
					logger.error("Preferential information is empty,email=={}",
							email);
					res.setRet(CommonUtils.ERROR_RES);
					return res;
				}
				String preferType = loyaltyPrefer.getPreferType();
				if (StringUtils.isEmpty(preferType)) {
					logger.error(
							"Preferential information type is empty,email=={}",
							email);
					res.setRet(CommonUtils.ERROR_RES);
					res.setErrMsg("Preferential information type is empty");
					return res;
				}
				if (preferType.equals(CouponController.LOYALTY_TYPE_COUPON)) {
					logger.debug("Lock coupon preferential,email=={}", email);
					if (StringUtils.isEmpty(email)) {
						logger.error("Lock coupon preferential,code==",
								loyaltyPrefer.getCode());
						res.setErrMsg("No user");
						res.setRet(CommonUtils.ERROR_RES);
						return res;
					}
					applyResult = couponService.isCouponAvailable(
							loyaltyPrefer.getCode(), email, products, website,
							terminal, currency);
					logger.info("----applyResult1="+JSONObject.toJSONString(applyResult));
					if (!applyResult.isSuccess()) {
						res.setRet(CommonUtils.ERROR_RES);
						res.setData(JSONObject.toJSONString(applyResult));
						return res;
					}
					boolean result = preferCenterService.saveCouponOrderPrefer(
							email, loyaltyPrefer);
					if (!result) {
						logger.error(
								"Save the coupon discount information failure,email=={},code==",
								email, loyaltyPrefer.getCode());
						res.setRet(CommonUtils.ERROR_RES);
						res.setErrMsg("Save the coupon discount information failure");
						return res;
					}
				} else if (preferType
						.equals(CouponController.LOYALTY_TYPE_PROMO)) {
					logger.debug("Lock promo preferential,email=={}", email);
					applyResult = promoService.isPromoAvailable(
							loyaltyPrefer.getCode(), email, products, website,
							terminal, currency);
					logger.info("----applyResult2="+JSONObject.toJSONString(applyResult));
					if (!applyResult.isSuccess()) {
						res.setRet(CommonUtils.ERROR_RES);
						res.setData(JSONObject.toJSONString(applyResult));
						return res;
					}
					boolean result = preferCenterService.savePromoOrderPrefer(
							email, loyaltyPrefer, website);
					if (!result) {
						logger.error(
								"Save the preferential promotion code information failure,email=={}",
								email);
						res.setRet(CommonUtils.ERROR_RES);
						res.setErrMsg("Save the preferential promotion code information failure");
						return res;
					}
				} else if (preferType
						.equals(CouponController.LOYALTY_TYPE_POINT)) {
					applyResult = pointService.isPointAvailable(email,
							Integer.valueOf(loyaltyPrefer.getCode()), website,
							currency);
					logger.info("----applyResult3="+JSONObject.toJSONString(applyResult));
					if (!applyResult.isSuccess()) {
						res.setRet(CommonUtils.ERROR_RES);
						res.setData(JSONObject.toJSONString(applyResult));
						return res;
					}
					boolean result = preferCenterService.savePointsOrderPrefer(
							email, loyaltyPrefer, applyResult.getValue(),
							website);
					if (!result) {
						logger.error(
								"Save the preferential point code information failure,email=={}",
								email);
						res.setRet(CommonUtils.ERROR_RES);
						res.setErrMsg("Save the preferential point code information failure");
						return res;
					}
				}
			}
			res.setRet(CommonUtils.SUCCESS_RES);
			return res;
		}
		logger.error("Currently there is no discount information,email=={}",
				email);
		res.setRet(CommonUtils.SUCCESS_RES);
		return res;
	}

	private void undoCurrentPrefer(HttpServletRequest request,
			HttpServletResponse response) {

		if (StringUtils.isNotEmpty(CookieUtils.getCookie(
				CouponController.LOYALTY_PREFER, request, response))) {
			CookieUtils.removeCookie(CouponController.LOYALTY_PREFER, request,
					response);
		}
	}

}
