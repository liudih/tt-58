package com.tomtop.controllers.order;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.api.client.util.Maps;
import com.google.common.collect.Lists;
import com.tomtop.services.cart.ICookieCartService;
import com.tomtop.services.order.IOrderService;
import com.tomtop.services.product.IPriceService;
import com.tomtop.utils.FoundationService;
import com.tomtop.utils.PropertyReader;
import com.tomtop.valueobjects.BundleCartItem;
import com.tomtop.valueobjects.CartItem;
import com.tomtop.valueobjects.SingleCartItem;
import com.tomtop.valueobjects.product.price.Price;
import com.tomtop.valueobjects.product.spec.IProductSpec;
import com.tomtop.valueobjects.product.spec.ProductSpecBuilder;

@Controller
@RequestMapping("/order")
public class OrderTestController {
	private static final Logger Logger = LoggerFactory
			.getLogger(OrderTestController.class);

	@Autowired
	IOrderService orderService;

	@Autowired
	IPriceService priceService;

	@Autowired
	ICookieCartService cookieCartService;
	@Autowired
	FoundationService foundationService;

	@RequestMapping("/1")
	public String view1() {
		return "/order/checkout_step1";
	}

	@RequestMapping("/2")
	public String view2(Map<String, Object> model, HttpServletRequest request) {
		int lang = foundationService.getLang();
		int siteid = foundationService.getSiteID();
		String currency = foundationService.getCurrency();

		List<CartItem> cartItemlist = cookieCartService
				.getAllItemsCurrentStorageid(siteid, lang, currency);
		model.put("cartItemlist", cartItemlist);
		Properties config = PropertyReader
				.getProperties("/application.properties");
		model.put("currencyBo", foundationService.getCurrencyBo());
		model.put("imgurl", config.getProperty("cart.imgurl"));
		return "/order/checkout_step2";
	}

	@RequestMapping("/3")
	public String view3(Map<String, Object> model, HttpServletRequest request) {
		int lang = foundationService.getLang();
		int siteid = foundationService.getSiteID();
		String currency = foundationService.getCurrency();

		List<CartItem> cartItemlist = cookieCartService
				.getAllItemsCurrentStorageid(siteid, lang, currency);
		model.put("cartItemlist", cartItemlist);
		Properties config = PropertyReader
				.getProperties("/application.properties");
		model.put("currencyBo", foundationService.getCurrencyBo());
		model.put("imgurl", config.getProperty("cart.imgurl"));
		return "/order/confirm_order";
	}

	@RequestMapping("/4")
	public String view4() {
		return "/order/pay_fail";
	}

	@RequestMapping("/5")
	public String view5() {
		return "/order/pay_success";
	}

	@RequestMapping(value = "/_create", method = RequestMethod.POST)
	public Map<String, Object> test(@RequestBody String jsonStr) {
		Map<String, Object> feedback = Maps.newHashMap();
		Integer storageid = 1;
		List<JSONObject> listJson = null;
		try {
			listJson = JSONObject.parseArray(jsonStr, JSONObject.class);
		} catch (Exception e) {
			Logger.error("parse json error");
			feedback.put("succeed", false);
			feedback.put("errorInfo", "parse json error");
			return feedback;
		}

		List<CartItem> items = Lists.newArrayList();

		listJson.forEach(i -> {
			Integer qty = i.getInteger("qty");
			String listing = i.getString("listing");
			JSONArray childrens = i.getJSONArray("childrens");

			if (childrens == null || childrens.size() == 0) {
				SingleCartItem item = new SingleCartItem();
				item.setIqty(qty);
				item.setClistingid(listing);

				IProductSpec spec = ProductSpecBuilder.build(listing)
						.setQty(qty).get();
				item.setPrice(this.priceService.getPrice(spec, "USD", storageid));

				items.add(item);
			} else {

				List<SingleCartItem> children = Lists.newLinkedList();

				Iterator<Object> iterator = childrens.iterator();
				while (iterator.hasNext()) {
					JSONObject json = (JSONObject) iterator.next();
					Integer q = json.getInteger("qty");
					String l = json.getString("listing");
					SingleCartItem cell = new SingleCartItem();
					cell.setIqty(q);
					cell.setClistingid(l);

					IProductSpec spec = ProductSpecBuilder.build(l).setQty(q)
							.get();
					cell.setPrice(this.priceService.getPrice(spec, "USD", storageid));

					children.add(cell);
				}

				BundleCartItem item = new BundleCartItem(children);
				item.setIqty(qty);
				item.setClistingid(listing);

				IProductSpec spec = ProductSpecBuilder.build(listing)
						.bundleWith(item.getChildListingId()).setQty(qty).get();
				Price bprice = priceService.getPrice(spec, "USD", storageid);
				item.setPrice(bprice);

				items.add(item);

			}
		});

		return feedback;
	}
}
