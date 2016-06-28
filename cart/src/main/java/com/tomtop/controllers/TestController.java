package com.tomtop.controllers;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.google.api.client.http.HttpRequestFactory;
import com.tomtop.events.EventBroker;
import com.tomtop.events.order.ReduceQtyEvent;
import com.tomtop.services.product.IProductService;
import com.tomtop.utils.CookieUtils;
import com.tomtop.valueobjects.product.Product4API;

@RestController
@RequestMapping("/test")
public class TestController {

	@Autowired
	HttpRequestFactory requestFactory;

	@Autowired
	private EventBroker eventBroker;

	@Autowired
	IProductService productService;

	@Autowired
	RedisTemplate<String, String> redisTemplate;

	@RequestMapping("")
	public String ok(Map<String, Object> model) {
		return "test";
	}

	@RequestMapping("/null")
	public String test(Map<String, Object> model) {
		throw new NullPointerException();
	}

	@RequestMapping("/cookie")
	public String cookie(Map<String, Object> model)
			throws UnsupportedEncodingException {
		JSONObject json = new JSONObject();
		json.put("key", "value");
		String text = JSON
				.toJSONString(json, SerializerFeature.UseSingleQuotes);
		System.out.println(text);
		CookieUtils.setCookie("test", URLEncoder.encode(text, "UTF-8"));
		CookieUtils.setCookie("test1", "1234566");
		model.put("testparam", "test11369...");
		return "test";
	}

	@RequestMapping("/read-cookie")
	public String readCookie(Map<String, Object> model) {
		String key = CookieUtils.getCookie("test");
		JSONObject json = JSONObject.parseObject(key);

		model.put("testparam", "test11369...");
		return "test";
	}

	@RequestMapping("/reduce-qty-event")
	public String testReduceQtyEvent() {
		try {
			ReduceQtyEvent event = new ReduceQtyEvent("CAN16E16O5445-8Y6YRI",
					10);
			this.eventBroker.post(event);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return "ok";
	}

	@RequestMapping("/testProduct")
	public List<Product4API> testProduct() {
		String listingIds = "e3861149-c954-454c-8611-49c954354c48,4f4be87c-890f-4cab-8be8-7c890f9cab59";
		List<Product4API> result = productService.getProductListFromAPI(
				listingIds, 1, 1, 1);
		return result;
	}

}
