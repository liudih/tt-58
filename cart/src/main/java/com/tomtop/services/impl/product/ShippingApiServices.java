package com.tomtop.services.impl.product;

import java.util.List;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.api.client.http.ByteArrayContent;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpContent;
import com.google.api.client.http.HttpHeaders;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.common.collect.Lists;
import com.tomtop.dto.Country;
import com.tomtop.dto.base.Storage;
import com.tomtop.dto.product.ShippingStorage;
import com.tomtop.events.EventBroker;
import com.tomtop.events.order.OrderOpreationEvent;
import com.tomtop.events.order.OrderOpreationEvent.OpreationResult;
import com.tomtop.events.order.OrderOpreationEvent.OpreationType;
import com.tomtop.services.product.IShippingService;
import com.tomtop.utils.FoundationService;
import com.tomtop.valueobjects.BundleCartItem;
import com.tomtop.valueobjects.CartItem;
import com.tomtop.valueobjects.Constants;
import com.tomtop.valueobjects.order.ShippingMethod;
import com.tomtop.valueobjects.order.ShippingMethodMini;

/**
 * api调用方式
 * 
 * @author lijun
 *
 */
@Service
public class ShippingApiServices implements IShippingService {
	private static final Logger Logger = LoggerFactory
			.getLogger(ShippingApiServices.class);

	@Autowired
	HttpRequestFactory requestFactory;

	@Autowired
	FoundationService foundationService;

	@Value("${api.shipMethod}")
	String url;
	
	@Value("${api.shipMethodToken}")
	String token;
	
	@Autowired
	private EventBroker eventBroker;

	public Storage getWebsiteLimitStorage(int siteId) {
		return null;
	}

	public List<ShippingStorage> getStorages(List<String> listingids) {
		return null;
	}

	public Storage getCountryDefaultStorage(Country country) {
		return null;
	}

	public Storage getShippingStorage(int siteId, List<String> listingids) {
		return null;
	}

	public Storage getShippingStorage(int siteId, Country country,
			List<String> listingids) {
		return null;
	}

	@Override
	public boolean isSameStorage(List<String> listingids, String storageId) {
		return false;
	}

	@Override
	public List<ShippingMethod> getShipMethod(String shipToCountryCode,
			int storageId, int language, List<CartItem> items,
			String currencyCode, double totalPrice) {
		if (items == null || items.size() == 0) {
			throw new NullPointerException("items is null");
		}

		try {
			JSONObject paras = new JSONObject();

			paras.put("country", shipToCountryCode);
			paras.put("currency", currencyCode);
			paras.put("language", language);
			paras.put("storageId", storageId);
			paras.put("totalPrice", totalPrice);

			JSONArray array = new JSONArray();
			items.forEach(i -> {
				JSONObject json = new JSONObject();
				json.put("listingId", i.getClistingid());
				json.put("qty", i.getIqty());
				if (i instanceof BundleCartItem) {
					JSONArray childrens = new JSONArray();
					List<String> listings = ((BundleCartItem) i)
							.getChildListingId();
					childrens.addAll(listings);
					json.put("chrd", childrens);
				}

				array.add(json);
			});

			paras.put("shippingCalculateLessParamBase", array);

			String urlStr = url;
			GenericUrl url = new GenericUrl(urlStr);

			String json = paras.toJSONString();

			Logger.debug(json);

			HttpContent content = ByteArrayContent.fromString(
					Constants.TYPE_JSON, json);

			HttpRequest request = requestFactory.buildPostRequest(url, content);

			HttpHeaders headers = new HttpHeaders();
			headers.put("token", token);
			request.setHeaders(headers);
			String result = request.execute().parseAsString();

			Logger.debug(result);
			
			JSONObject resultJson = JSONObject.parseObject(result);
			//可用数量，如果可用数量为0，则记录日志
			int showCount = 0;
			if ("Y".equals(resultJson.getString("status"))) {
				List<ShippingMethod> list = Lists.newLinkedList();
				JSONArray data = resultJson.getJSONArray("data");
				
				for(int i = 0 ; i < data.size() ; i++){
					ShippingMethod shippingMethod = data.getObject(i, ShippingMethod.class);
					list.add(shippingMethod);
					//判断是否可用
					if(shippingMethod.getIsShow()){
						showCount++;
					}
				}
				//如果无物流方式，则记录日志
				if(data.size() <= 0){
					sendEvent(OpreationResult.NONE,json);
				}else if(showCount <= 0){
					sendEvent(OpreationResult.INVALID,json);
				}
				return list;
			}
			sendEvent(OpreationResult.FAILURE,resultJson.toJSONString());
		} catch (Exception e) {
			sendEvent(OpreationResult.FAILURE,e.getMessage());
			Logger.error("get ship method error", e);
		}

		return null;
	}

	/**
	 * 获取一个物流方式信息
	 */
	@Override
	public ShippingMethodMini getShipMethodInfo(String shipCode, Integer lang){
		if(shipCode==null || "".equals(shipCode)){
			return null;
		}
		try {
			StringBuilder urlStr = new StringBuilder(url);
			urlStr.append("/").append(shipCode).append("/name");
			if(lang!=null){
				urlStr.append("?languageid=").append(lang);
			}
			GenericUrl url = new GenericUrl(urlStr.toString());
			HttpRequest request = requestFactory.buildGetRequest(url);

			HttpHeaders headers = new HttpHeaders();
			headers.put("token", token);
			request.setHeaders(headers);
			String result = request.execute().parseAsString();
			Logger.debug(result);
			
			JSONObject resultJson = JSONObject.parseObject(result);
			if ("Y".equals(resultJson.getString("status"))) {
				JSONObject data = resultJson.getJSONObject("data");
				ShippingMethodMini sm = JSON.parseObject(data.toJSONString(),ShippingMethodMini.class);
				return sm;
			}
		} catch (Exception e) {
			Logger.error("get ship method info error", e);
		}
		return null;
	}
	
	/**
	 * 发送事件
	 * @param siteId
	 * @param email
	 * @param result
	 * @param content
	 */
	private void sendEvent(OpreationResult result,String content) {
		OrderOpreationEvent event = new OrderOpreationEvent(
				OpreationType.LOAD_SHIPMETHOD, result, content);
		eventBroker.post(event);
	}
}
