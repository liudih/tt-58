package com.tomtop.controllers.member;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.tomtop.services.ICurrencyService;
import com.tomtop.services.cart.ICartService;
import com.tomtop.services.cart.ICookieCartService;
import com.tomtop.services.impl.LoyaltyService;
import com.tomtop.services.impl.LoyaltyService.Type;
import com.tomtop.services.order.ICheckoutService;
import com.tomtop.utils.CookieUtils;
import com.tomtop.utils.FoundationService;
import com.tomtop.utils.Utils;
import com.tomtop.valueobjects.CartItem;
import com.tomtop.valueobjects.Coupon;
import com.tomtop.valueobjects.Discount;
import com.tomtop.valueobjects.DiscountUsedState;
import com.tomtop.valueobjects.base.LoginContext;

@RestController
@RequestMapping("/loyalty")
public class LoyaltyController {
	private static final Logger Logger = LoggerFactory
			.getLogger(LoyaltyController.class);

	@Autowired
	FoundationService foundationService;

	@Autowired
	ICookieCartService cookieCartService;

	@Autowired
	ICurrencyService currencyService;

	@Autowired
	ICartService cartService;

	@Autowired
	LoyaltyService loyaltyService;

	@Autowired
	ICheckoutService checkoutService;

	/**
	 * 获取优惠券
	 * 
	 * @param storageid
	 * @return
	 */
	@RequestMapping(value = "/coupon", method = RequestMethod.GET)
	public String getCoupon(@RequestParam("storageid") String storageid) {
		LoginContext loginContext = foundationService.getLoginContext();
		JSONObject result = new JSONObject();

		if (!loginContext.isLogin()) {
			result.put("ret", -1);
			return result.toJSONString();
		}
		String email = loginContext.getEmail();
		int lang = foundationService.getLang();
		int siteid = foundationService.getSiteID();
		String currency = foundationService.getCurrency();
		List<CartItem> cartItems = cookieCartService
				.getAllItemsCurrentStorageid(siteid, lang, currency);

		List<Coupon> coupons = null;
		if (cartItems != null && cartItems.size() > 0) {
			coupons = loyaltyService.getUseableCoupon(siteid, 5, email,
					currency, cartItems);
		}

		if (coupons != null) {
			result.put("ret", 1);
			result.put("data", coupons);
		} else {
			result.put("ret", -1);
		}
		return result.toJSONString();

	}

	/**
	 * 应用优惠卷
	 * 
	 * @param storageid
	 * @param code
	 * @return
	 */
	@RequestMapping(value = "/applycoupon", method = RequestMethod.GET)
	@ResponseBody
	public Object applyCoupon(@RequestParam("storageid") String storageid,
			@RequestParam("code") String code, String typeStr) {
		LoginContext loginContext = foundationService.getLoginContext();
		Map<String, Object> mjson = new HashMap<String, Object>();
		String email = "";
		if ((typeStr == null || "point".equals(typeStr)) && !loginContext.isLogin()) {
			mjson.put("result", "no login");
			return mjson;
		}else{
			email = loginContext.getEmail();
		}
		int lang = foundationService.getLang();
		int siteid = foundationService.getSiteID();
		String currency = foundationService.getCurrency();
		List<CartItem> cartItems = cookieCartService
				.getAllItemsCurrentStorageid(siteid, lang, currency);
		// 购物车金额
		double total = checkoutService.subToatl(cartItems);

		LoyaltyService.Type type = null;
		if (typeStr == null) {
			type = LoyaltyService.Type.coupon;
		} else if ("promo".equals(typeStr)) {
			type = LoyaltyService.Type.promo;
		} else if ("point".equals(typeStr)) {
			type = LoyaltyService.Type.point;
		}
		DiscountUsedState state = loyaltyService.apply(code, siteid, 5, email,
				currency, cartItems, type);

		if (state.isSucceed()) {
			double discountTotal = state.getDiscount();
			if ((discountTotal + total) < 0) {
				mjson.put("result",
						"The discount amount is greater than the total price");
				return mjson;
			}
			JSONObject data = new JSONObject();
			data.put("currency", currency);
			data.put("code", code);
			data.put("type", type.name());
			data.put("value", Utils.moneyNoComma(state.getDiscount(), currency));
			if (Type.point == type) {
				CookieUtils.setCookie(LoyaltyService.LOYALTY_TYPE_POINT,
						data.toJSONString());
			} else {
				CookieUtils.setCookie(LoyaltyService.LOYALTY_PREFER,
						data.toJSONString());
			}
			mjson.put("result", "success");
			mjson.put("data", data);
		} else {
			mjson.put("result", state.getErrMsg());
		}
		return mjson;

	}

	/**
	 * 应用推广码
	 * 
	 * @param storageid
	 * @param code
	 * @return
	 */
	@RequestMapping(value = "/applypromo", method = RequestMethod.GET)
	@ResponseBody
	public Object applyPromo(@RequestParam("storageid") String storageid,
			@RequestParam("code") String code) {
		return this.applyCoupon(storageid, code, "promo");
	}

	/**
	 * 应用积分
	 * 
	 * @param storageid
	 * @param code
	 * @return
	 */
	@RequestMapping(value = "/applypoint", method = RequestMethod.GET)
	@ResponseBody
	public Object applyPoint(@RequestParam("storageid") String storageid,
			@RequestParam("point") String point) {
		return this.applyCoupon(storageid, point, "point");
	}

	/**
	 * 获取用户所有优惠
	 * @return
	 */
	@RequestMapping(value = "/allprefer", method = RequestMethod.GET)
	public Object allprefer() {
		LoginContext loginContext = foundationService.getLoginContext();
		Map<String, Object> mjson = new HashMap<String, Object>();
		String email = "";
		if (loginContext.isLogin()) {
			email = loginContext.getEmail();
		}
		
		int lang = foundationService.getLang();
		int siteid = foundationService.getSiteID();
		String currency = foundationService.getCurrency();
		List<CartItem> cartItems = cookieCartService
				.getAllItemsCurrentStorageid(siteid, lang, currency);
		// 购物车金额
		final double total = this.checkoutService.subToatl(cartItems);

		List<Discount> discounts = this.loyaltyService.getUsedDiscount();
		
		Discount pointDiscount = null;
		Discount couponAndPromo = null;
		boolean isPoint = true;
		boolean isCoupon = true;
		for(Discount dis :discounts){
			if("point".equals(dis.getType().toString())){
				pointDiscount = dis;
			}else if("coupon".equals(dis.getType().toString()) || "promo".equals(dis.getType().toString())){
				couponAndPromo = dis;
			}
		}
		
		//验证优惠卷
		if(couponAndPromo!=null){
			DiscountUsedState state = loyaltyService.apply(couponAndPromo.getCode(),
					siteid, 5, email, currency, cartItems, couponAndPromo.getType());
			if(!state.isSucceed()){
				isCoupon = false;
				CookieUtils.setCookie(LoyaltyService.LOYALTY_PREFER, "");
			}else{
				JSONObject data = new JSONObject();
				data.put("currency", currency);
				data.put("code", couponAndPromo.getCode());
				data.put("type", couponAndPromo.getType().toString());
				data.put("value", Utils.moneyNoComma(state.getDiscount(), currency));
				couponAndPromo.setDiscount(state.getDiscount());
				CookieUtils.setCookie(LoyaltyService.LOYALTY_PREFER,
						data.toJSONString());
			}
		}
		//验证积分
		if(pointDiscount!=null){
			DiscountUsedState state = loyaltyService.apply(pointDiscount.getCode(),
					siteid, 5, email, currency, cartItems, pointDiscount.getType());
			if(!state.isSucceed()){
				isPoint = false;
				CookieUtils.setCookie(LoyaltyService.LOYALTY_TYPE_POINT, "");
			}
		}
		for(Discount d : discounts){
			Double dPrice = d.getDiscount();
			if ((total + dPrice) <= 0) {
				if (Type.point == d.getType()) {
					isPoint = false;
					CookieUtils
							.setCookie(LoyaltyService.LOYALTY_TYPE_POINT, "");
				} else {
					isCoupon = false;
					CookieUtils.setCookie(LoyaltyService.LOYALTY_PREFER, "");
				}
			}
		};
		
		List<Discount> filterDiscounts = Lists.newArrayList();
		if(isPoint && pointDiscount!=null){
			filterDiscounts.add(pointDiscount);
		}
		if(isCoupon && couponAndPromo!=null){
			filterDiscounts.add(couponAndPromo);
		}

		if (discounts.size() > 0) {
			mjson.put("data", filterDiscounts);
			mjson.put("result", "success");
		} else {
			mjson.put("result", "no data");
		}
		return mjson;
	}

	/**
	 * 取消应用推广码 和 优惠卷
	 * 
	 * @return
	 */
	@RequestMapping(value = "/undoprefer", method = RequestMethod.GET)
	public Object undoPrefer() {
		Map<String, Object> mjson = new HashMap<String, Object>();
		CookieUtils.setCookie(LoyaltyService.LOYALTY_PREFER, "");
		mjson.put("result", "success");
		return mjson;
	}

	/**
	 * 取消应用积分
	 * 
	 * @return
	 */
	@RequestMapping(value = "/undopoint", method = RequestMethod.GET)
	@ResponseBody
	public Object undoPoint() {
		Map<String, Object> mjson = new HashMap<String, Object>();
		CookieUtils.setCookie(LoyaltyService.LOYALTY_TYPE_POINT, "");
		mjson.put("result", "success");
		return mjson;
	}

	/**
	 * 获取积分
	 * 
	 * @return
	 */
	@RequestMapping(value = "/getpoint", method = RequestMethod.GET)
	public String getPoint() {
		LoginContext loginContext = foundationService.getLoginContext();

		JSONObject result = new JSONObject();

		if (!loginContext.isLogin()) {
			result.put("result", "no login");
			return result.toJSONString();
		}
		String email = loginContext.getEmail();
		int siteid = foundationService.getSiteID();
		String currency = foundationService.getCurrency();
		Integer point = loyaltyService.getPoint(siteid, 5, email, currency);
		if (point != null) {
			result.put("ret", 1);
			result.put("data", point);
		} else {
			result.put("ret", 0);
			result.put("data", 0);
		}

		return result.toJSONString();
	}

}
