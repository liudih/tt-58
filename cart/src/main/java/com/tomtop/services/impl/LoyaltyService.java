package com.tomtop.services.impl;

import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.api.client.http.ByteArrayContent;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpContent;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.util.Maps;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.tomtop.events.EventBroker;
import com.tomtop.events.order.OrderOpreationEvent;
import com.tomtop.events.order.OrderOpreationEvent.OpreationResult;
import com.tomtop.events.order.OrderOpreationEvent.OpreationType;
import com.tomtop.services.ICurrencyService;
import com.tomtop.utils.CookieUtils;
import com.tomtop.utils.FoundationService;
import com.tomtop.valueobjects.CartItem;
import com.tomtop.valueobjects.Constants;
import com.tomtop.valueobjects.Coupon;
import com.tomtop.valueobjects.Discount;
import com.tomtop.valueobjects.DiscountUsedState;
import com.tomtop.valueobjects.base.LoginContext;
import com.tomtop.valueobjects.product.price.Price;

/**
 * 优惠券 积分 推广码
 * 
 * @author lijun
 *
 */
@Service
public class LoyaltyService {
	private static final Logger Logger = LoggerFactory
			.getLogger(LoyaltyService.class);

	public static final String LOYALTY_PREFER = "loyalty";
	public static final String LOYALTY_TYPE_PROMO = "promo";
	public static final String LOYALTY_TYPE_COUPON = "coupon";
	public static final String LOYALTY_TYPE_POINT = "point";

	@Autowired
	HttpRequestFactory requestFactory;

	@Value("${loyalty.getcoupon}")
	String getcouponUrl;

	@Value("${loyalty.applycoupon}")
	String applycouponUrl;

	@Value("${loyalty.applypromo}")
	String applypromoUrl;

	@Value("${loyalty.applypoint}")
	String applyPointUrl;

	@Value("${loyalty.getpoint}")
	String getpointUrl;

	@Value("${loyalty.lock}")
	String lockUrl;

	@Autowired
	FoundationService foundationService;

	@Autowired
	ICurrencyService currencyService;
	
	@Autowired
	private EventBroker eventBroker;

	/**
	 * 获取可用优惠券
	 * 
	 * @param website
	 * @param client
	 * @param email
	 * @param currency
	 * @param items
	 * @return maybe return null
	 */
	public List<Coupon> getUseableCoupon(int website, int client, String email,
			String currency, List<CartItem> items) {
		Assert.hasLength(email, "email is null");
		Assert.hasLength(currency, "currency is null");
		Assert.notEmpty(items, "items is null");
		
		String storageid = CookieUtils.getCookie("storageid");
		if(storageid==null || "".equals(storageid)){
			storageid = "1";
		}

		StringBuilder sb = new StringBuilder();
		sb.append(this.getcouponUrl);
		sb.append("?email=");
		sb.append(email);
		sb.append("&client=");
		sb.append(client);
		sb.append("&website=");
		sb.append(website);
		sb.append("&currency=");
		sb.append(currency);
		sb.append("&depotId=");
		sb.append(storageid);
		
		String url = sb.toString();

		JSONArray array = new JSONArray();
		// recode 捆绑商品未考虑
		items.forEach(i -> {
			JSONObject json = new JSONObject();
			json.put("listingId", i.getClistingid());
			Price price = i.getPrice();
			json.put("price", price != null ? price.getPrice() : 0);
			json.put("qty", i.getIqty());
			array.add(json);
		});

		Logger.debug("get coupon url:{}", url);
		Logger.debug("get coupon body:{}", array.toJSONString());

		GenericUrl gurl = new GenericUrl(url);

		HttpContent content = ByteArrayContent.fromString(Constants.TYPE_JSON,
				array.toJSONString());
		try {
			HttpRequest request = requestFactory
					.buildPostRequest(gurl, content);
			String feedback = request.execute().parseAsString();

			JSONObject resultJson = JSONObject.parseObject(feedback);

			//System.out.println("resultJson=="+resultJson.toJSONString());
			Integer ret = resultJson.getInteger(Constants.RET);

			if (1 == ret) {
				List<Coupon> coupons = Lists.newLinkedList();
				JSONArray data = resultJson.getJSONArray(Constants.DATA);
				for (int i = 0; i < data.size(); i++) {
					coupons.add(data.getObject(i, Coupon.class));
				}
				return coupons;
			}
			sendEvent(OpreationType.GET_COUPON, OpreationResult.FAILURE,
					array.toJSONString() + resultJson.toJSONString());
		} catch (Exception e) {
			sendEvent(OpreationType.GET_COUPON, OpreationResult.FAILURE,
					array.toJSONString() + e.getMessage());
			Logger.error("get coupon error", e);
		}
		return null;
	}

	/**
	 * 使用优惠券
	 * 
	 * @param code
	 * @param website
	 * @param client
	 * @param email
	 * @param currency
	 * @param items
	 * @param isCoupon
	 *            true:使用的是优惠券 false:使用的是推广码
	 * @return not return null
	 */
	public DiscountUsedState apply(String code, int website, int client,
			String email, String currency, List<CartItem> items, Type type) {

		LoginContext lc = foundationService.getLoginContext();
		Assert.hasLength(code, "code is null");
		Assert.hasLength(currency, "currency is null");
		Assert.notEmpty(items, "items is null");
		if (lc.isLogin() && (Type.coupon == type || Type.point == type)) {
			Assert.hasLength(email, "email is null");
		}
		String storageid = CookieUtils.getCookie("storageid");
		if(storageid==null || "".equals(storageid)){
			storageid = "1";
		}

		StringBuilder sb = new StringBuilder();
		if (Type.coupon == type) {
			sb.append(this.applycouponUrl);
		} else if (Type.promo == type) {
			sb.append(this.applypromoUrl);
		} else if (Type.point == type) {
			sb.append(this.applyPointUrl);
		} else {
			throw new IllegalArgumentException("type error");
		}

		sb.append("/");
		sb.append(code);
		sb.append("?email=");
		if(lc.isLogin()){
			sb.append(email);
		}else{
			sb.append("\"\"");
		}
		sb.append("&client=");
		sb.append(client);
		sb.append("&website=");
		sb.append(website);
		sb.append("&currency=");
		sb.append(currency);
		sb.append("&depotId=");
		sb.append(storageid);

		String url = sb.toString();

		JSONArray array = new JSONArray();
		// recode 捆绑商品未考虑
		items.forEach(i -> {
			if(i!=null && i.getPrice()!=null){
				JSONObject json = new JSONObject();
				json.put("listingId", i.getClistingid());
				json.put("price", i.getPrice().getUnitPrice());
				json.put("qty", i.getIqty());
				array.add(json);
			}
		});

		Logger.debug("get coupon url:{}", url);
		Logger.debug("get coupon body:{}", array.toJSONString());
		// System.out.println("get coupon url:{}"+ url);
		// System.out.println("get coupon body:{}"+ array.toJSONString());
		GenericUrl gurl = new GenericUrl(url);

		HttpContent content = ByteArrayContent.fromString(Constants.TYPE_JSON,
				array.toJSONString());
		try {
			HttpRequest request = requestFactory.buildPutRequest(gurl, content);
			String feedback = request.execute().parseAsString();

			JSONObject resultJson = JSONObject.parseObject(feedback);

			Integer ret = resultJson.getInteger(Constants.RET);

			JSONObject data = resultJson.getJSONObject(Constants.DATA);

			if (1 == ret) {
				DiscountUsedState state = new DiscountUsedState(
						data.getDouble(Constants.VALUE),
						data.getString(Constants.CODE), null, true);
				return state;
			} else {
				sendEvent(getOpreationType(type), OpreationResult.FAILURE,
						array.toJSONString()+resultJson.toJSONString());
				DiscountUsedState state = new DiscountUsedState(null,
						data.getString(Constants.CODE),
						data.getString(Constants.ERR_MSG), false);
				return state;
			}
		} catch (Exception e) {
			sendEvent(getOpreationType(type), OpreationResult.FAILURE,
					array.toJSONString() + e.getMessage());
			Logger.error("apply coupon error", e);
		}
		DiscountUsedState state = new DiscountUsedState(null, code,
				"apply coupon occur error", false);
		return state;
	}

	public Integer getPoint(int website, int client, String email,
			String currency) {

		StringBuilder sb = new StringBuilder();

		sb.append(getpointUrl);
		sb.append("?email=");
		sb.append(email);
		sb.append("&client=");
		sb.append(client);
		sb.append("&website=");
		sb.append(website);
		sb.append("&currency=");
		sb.append(currency);

		String url = sb.toString();

		GenericUrl gurl = new GenericUrl(url);

		try {
			HttpRequest request = requestFactory.buildGetRequest(gurl);

			String feedback = request.execute().parseAsString();

			JSONObject resultJson = JSONObject.parseObject(feedback);

			Integer ret = resultJson.getInteger(Constants.RET);

			if (1 == ret) {
				return resultJson.getInteger(Constants.DATA);
			}
			sendEvent(OpreationType.GET_POINT, OpreationResult.FAILURE, resultJson.toJSONString());
		} catch (Exception e) {
			sendEvent(OpreationType.GET_POINT, OpreationResult.FAILURE, e.getMessage());
			Logger.error("get point error", e);
		}
		return null;
	}

	/**
	 * 锁定(优惠券一旦被锁定就不能再用了,所以该方法一般只会生成订单时会调用)
	 * 
	 * @param code
	 *            优惠券 推广码 积分
	 * @param orderNum
	 *            订单号
	 * @param items
	 * @param type
	 * @return
	 */
	public boolean lock(List<Discount> discount, String orderNum,
			List<CartItem> items, String email, String currency) {
		if (discount == null || discount.size() == 0) {
			return true;
		}
		Assert.notEmpty(discount, "discount is null");
		Assert.notEmpty(items, "items is null");
		Assert.hasLength(orderNum, "orderNum is null");
		JSONObject paras = new JSONObject();
		// recode 需要考虑捆绑商品
		ImmutableList<HashMap<String, Object>> tlist = FluentIterable
				.from(items).transform(i -> {
					HashMap<String, Object> map = Maps.newHashMap();
					map.put("listingId", i.getClistingid());
					map.put("price", i.getPrice().getUnitPrice());
					map.put("qty", i.getIqty());
					return map;
				}).toList();

		ImmutableList<HashMap<String, Object>> discountMap = FluentIterable
				.from(discount).transform(d -> {
					HashMap<String, Object> map = Maps.newHashMap();
					map.put("code", d.getCode());
					map.put("orderNumber", orderNum);
					map.put("preferType", d.getType().toString());
					return map;
				}).toList();

		paras.put("product", tlist);
		paras.put("prefer", discountMap);
		
		String storageid = CookieUtils.getCookie("storageid");
		if(storageid==null || "".equals(storageid)){
			storageid = "1";
		}

		try {
			StringBuilder url = new StringBuilder(lockUrl);
			url.append("?website=").append(this.foundationService.getSiteID());
			if (email != null && !"".equals(email)) {
				url.append("&email=").append(email);
			} else {
				url.append("&email=\"\"");
			}
			url.append("&currency=").append(currency);
			url.append("&depotId=");
			url.append(storageid);
			
			GenericUrl gurl = new GenericUrl(url.toString()+"1");
			HttpContent content = ByteArrayContent.fromString(
					Constants.TYPE_JSON, paras.toJSONString());

			HttpRequest request = requestFactory
					.buildPostRequest(gurl, content);

			String feedback = request.execute().parseAsString();
			JSONObject resultJson = JSONObject.parseObject(feedback);

			Integer ret = resultJson.getInteger(Constants.RET);

			if (1 == ret) {
				return true;
			}
			sendEvent(OpreationType.LOCK_DISCOUNT, OpreationResult.FAILURE,
					paras.toJSONString() + resultJson.toJSONString());
		} catch (Exception e) {
			sendEvent(OpreationType.LOCK_DISCOUNT, OpreationResult.FAILURE,
					paras.toJSONString() + e.getMessage());
			Logger.error("lock error", e);
		}
		return false;
	}

	public List<Discount> getUsedDiscount() {

		List<Discount> list = Lists.newLinkedList();

		String coupon = CookieUtils.getCookie(LOYALTY_PREFER);
		String point = CookieUtils.getCookie(LOYALTY_TYPE_POINT);

		String currency = foundationService.getCurrency();

		LoginContext logCtx = this.foundationService.getLoginContext();
		// 用户未登陆的情况下是不能使用coupon point
		final String email = logCtx.getEmail();

		if (StringUtils.isNotEmpty(coupon)) {
			JSONObject json = null;
			try {
				json = JSONObject.parseObject(coupon);

				String code = json.getString("code");
				String cc = json.getString("currency");
				String type = json.getString("type");

				Type typeEnum = Type.valueOf(type);

				double oprice = json.getDoubleValue("value");
				if (currency != null && !currency.equals(cc)) {
					double nprice = currencyService.exchange(oprice, cc,
							currency);
					oprice = nprice;
				}
				Discount discount = new Discount(code, oprice, currency,
						typeEnum);
				if (StringUtils.isNotEmpty(email)) {
					list.add(discount);
				} else if (Type.promo == typeEnum) {
					list.add(discount);
				}

			} catch (Exception e) {
				Logger.error("parse cookie[loyalty] error:{}", coupon, e);
				CookieUtils.removeCookie(LOYALTY_PREFER);
			}
		}

		if (StringUtils.isNotEmpty(point) && StringUtils.isNotEmpty(email)) {
			try {

				JSONObject json = JSONObject.parseObject(point);
				String code = json.getString("code");
				String cc = json.getString("currency");
				String type = json.getString("type");
				double oprice = json.getDoubleValue("value");
				if (currency != null && !currency.equals(cc)) {
					double nprice = currencyService.exchange(oprice, cc,
							currency);
					oprice = nprice;
				}
				Discount discount = new Discount(code, oprice, currency,
						Type.valueOf(type));
				list.add(discount);
			} catch (Exception e) {
				Logger.error("parse cookie[point] error:{}", point, e);
				CookieUtils.removeCookie(LOYALTY_TYPE_POINT);
			}
		}

		return list;
	}

	public void clearAllDiscount() {
		CookieUtils.removeCookie(LOYALTY_PREFER);
		CookieUtils.removeCookie(LOYALTY_TYPE_POINT);
	}

	public static enum Type {
		coupon, promo, point
	}
	
	
	/**
	 * 发送事件，记录日志
	 * @param opreationType 操作类型
	 * @param result 操作结果
	 * @param content 明细
	 */
	private void sendEvent(OpreationType opreationType, OpreationResult result,String content) {
		OrderOpreationEvent event = new OrderOpreationEvent(opreationType,
				result, content);
		event.setCordernumber("");
		eventBroker.post(event);
	}
	
	/**
	 * 获取操作类型
	 * @param type
	 * @return
	 */
	private OpreationType getOpreationType(Type type) {
		OpreationType opreationType = null;
		if (Type.promo == type) {
			opreationType = OpreationType.APPLY_PROMO;
		} else if (Type.point == type) {
			opreationType = OpreationType.APPLY_POINT;
		}else{
			opreationType = OpreationType.APPLY_COUPON;
		}
		return opreationType;
	}
}
