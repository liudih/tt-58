package com.tomtop.loyalty.controllers;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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

import com.alibaba.fastjson.JSONObject;
import com.tomtop.framework.core.utils.Result;
import com.tomtop.loyalty.models.Prefer;
import com.tomtop.loyalty.models.Product;
import com.tomtop.loyalty.models.filter.ProductFilter;
import com.tomtop.loyalty.service.IPromoService;
import com.tomtop.loyalty.service.impl.ThirdService;
import com.tomtop.loyalty.utils.CommonUtils;
import com.tomtop.loyalty.utils.CookieUtils;
import com.tomtop.loyalty.utils.TerminalUtil;

@RestController
@RequestMapping(value = "/loyalty")
public class PromoController {

	@Autowired
	IPromoService promoService;

	@Value("${thirdMemberUrl}")
	private String thirdMemberUrl;

	@Autowired
	ThirdService thirdService;

	private Logger logger = LoggerFactory.getLogger(this.getClass());
	/**
	 * 应用推广码
	 * 
	 * @return
	 */
	@ResponseBody
	@RequestMapping(method = RequestMethod.PUT, value = "v1/promo/apply/{code}")
	public Result apply(
			HttpServletRequest request,
			HttpServletResponse response,
			@PathVariable("code") String code,
			@RequestBody List<ProductFilter> productFilters,
			@RequestParam(value = "depotId", required = true) Integer depotId,
			@RequestParam(value = "email", required = false, defaultValue = "") String email,
			@RequestParam(value = "client", required = false, defaultValue = "5") String terminal,
			@RequestParam(value = "website", required = false, defaultValue = "10") Integer website,
			@RequestParam(value = "currency", required = false, defaultValue = "USD") String currency) {
		logger.info("---getAllPrefer,productFilters="+JSONObject.toJSONString(productFilters)+
				",email="+email+",depotId="+depotId+",terminal="+terminal+",website="+website+",currency="+currency);
		terminal = TerminalUtil.getTerminalById(terminal);
		Result res = new Result();
		Prefer result = new Prefer();
		
		if(website==1 && (depotId==null || depotId==0)){//tomtop 站点 depotId 必传
			res.setErrMsg("depotId is null");
			res.setRet(CommonUtils.ERROR_RES);
			return res;
		}
		// 商品对象转换
		List<Product> products = thirdService.convert(productFilters,website,depotId);
		result = promoService.isPromoAvailable(code, email, products, website,
				terminal, currency);
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
	 * 取消应用推广码
	 * 
	 * @return
	 */
	@ResponseBody
	@RequestMapping(method = RequestMethod.PUT, value = "v1/promo/cancel")
	public Result cancel(HttpServletRequest request,
			HttpServletResponse response) {
		Result res = new Result();
		CookieUtils.removeCookie(CouponController.LOYALTY_PREFER, request,
				response);
		res.setRet(CommonUtils.SUCCESS_RES);
		return res;
	}

	/**
	 * 查看当前优惠
	 * 
	 * @return
	 */
	// private String getCurrentPreferCooike(HttpServletRequest request,
	// HttpServletResponse response) {
	//
	// String cookie = CookieUtils.getCookie(CouponController.LOYALTY_PREFER,
	// request, response);
	// return cookie;
	// }

}
