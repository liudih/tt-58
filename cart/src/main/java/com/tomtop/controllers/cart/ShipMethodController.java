package com.tomtop.controllers.cart;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.tomtop.dto.order.Order;
import com.tomtop.dto.order.OrderDetail;
import com.tomtop.mappers.order.DetailMapper;
import com.tomtop.mappers.order.OrderMapper;
import com.tomtop.services.cart.ICookieCartService;
import com.tomtop.services.product.IShippingService;
import com.tomtop.utils.CookieUtils;
import com.tomtop.utils.FoundationService;
import com.tomtop.valueobjects.CartItem;
import com.tomtop.valueobjects.Constants;
import com.tomtop.valueobjects.SingleCartItem;
import com.tomtop.valueobjects.order.ShippingMethod;

@RestController
@RequestMapping(value = "/ship-method", method = RequestMethod.POST)
public class ShipMethodController {

	@Autowired
	IShippingService shippingService;

	@Autowired
	FoundationService foundationService;

	@Autowired
	ICookieCartService cartService;
	
	@Autowired
	OrderMapper orderMapper;
	
	@Autowired
	DetailMapper detailMapper;

	@RequestMapping()
	public String getShipMethod(@RequestBody String body) {

		JSONObject feedback = new JSONObject();

		JSONObject json = null;

		try {
			json = JSONObject.parseObject(body);
		} catch (Exception e) {
			feedback.put(Constants.SUCCEED, false);
			feedback.put(Constants.ERROR, "data must be json");
			return feedback.toJSONString();
		}

		if (!json.containsKey("country")) {
			feedback.put(Constants.SUCCEED, false);
			feedback.put(Constants.ERROR, "country is null");
			return feedback.toJSONString();
		}
		if (!json.containsKey("totalPrice")) {
			feedback.put(Constants.SUCCEED, false);
			feedback.put(Constants.ERROR, "totalPrice is null");
			return feedback.toJSONString();
		}

		String country = json.getString("country");
		if (StringUtils.isEmpty(country)) {
			feedback.put(Constants.SUCCEED, false);
			feedback.put(Constants.ERROR, "country is null");
			return feedback.toJSONString();
		}
		Double totalPrice = null;
		try {
			totalPrice = json.getDouble("totalPrice");
		} catch (Exception e) {
			feedback.put(Constants.SUCCEED, false);
			feedback.put(Constants.ERROR, "totalPrice must number");
			return feedback.toJSONString();
		}
		int lang = this.foundationService.getLang();
		int site = this.foundationService.getSiteID();
		String currency = this.foundationService.getCurrency();
		
		List<CartItem> items = Lists.newArrayList();
		String storage = CookieUtils.getCookie("storageid");
		int storageId = 1;
		try{
			storageId = Integer.parseInt(storage);
		}catch(Exception ex){
			ex.printStackTrace();
			feedback.put(Constants.ERROR, "storage not exists");
			return feedback.toJSONString();
		}
		//如果是ec支付，不从cookie中取
		String ordernumber = json.getString("ordernumber");
		if(ordernumber!=null && !"".equals(ordernumber)){
			Order order = orderMapper.getOrderByOrderNumber(ordernumber);
			if(order!=null){
				List<OrderDetail> details = detailMapper.getOrderDetailByOrderId(order
						.getIid());
				for(OrderDetail od : details){
					SingleCartItem ci = new SingleCartItem();
					ci.setClistingid(od.getClistingid());
					ci.setIqty(od.getIqty());
					ci.setStorageID(order.getIstorageid());
					items.add(ci);
				}
				storageId = order.getIstorageid();
				site = order.getIwebsiteid();
				currency = order.getCcurrency();
			}
		}else{
			items = cartService.getAllItemsCurrentStorageid(site,
					lang, currency);
		}

		
		// ship method
		List<ShippingMethod> method = this.shippingService.getShipMethod(
				country, storageId, lang, items, currency, totalPrice);

		JSONArray result = new JSONArray();

		if (method != null) {
			result.addAll(method);
		}

		return result.toJSONString();
	}
}
