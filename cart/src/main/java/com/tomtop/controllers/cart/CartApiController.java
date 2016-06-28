package com.tomtop.controllers.cart;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Collection;
import java.util.List;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
import com.tomtop.services.cart.ICartService;
import com.tomtop.services.cart.ICookieCartService;
import com.tomtop.services.impl.base.StorageService;
import com.tomtop.utils.FoundationService;
import com.tomtop.utils.Result;
import com.tomtop.valueobjects.BundleCartItem;
import com.tomtop.valueobjects.CartItem;
import com.tomtop.valueobjects.SingleCartItem;

@Controller
@RequestMapping("/cart-api")
public class CartApiController {
	private static final Logger logger = LoggerFactory.getLogger(CartApiController.class);
	@Autowired
	ICookieCartService cookieCartService;
	@Autowired
	FoundationService foundationService;
	@Autowired
	ICartService cartEnquiryService;
	@Autowired
	StorageService storageService;


	@RequestMapping(value = "/savecartitem/jsonp", method = RequestMethod.GET)
	@ResponseBody
	public Object saveCartItem(Model model, @RequestParam("data") String data,
			HttpServletRequest request, HttpServletResponse response) {
		Result res = new Result();
		CartItem cartItem = packCartItem(data);
		// 判断是否存在已有的商品，存在则更新数量
		CartItem ocartItem = cookieCartService.getItem(cartItem);

		if (ocartItem != null) {
			if (ocartItem instanceof SingleCartItem) {
				if (cartItem.getIqty() != null && ocartItem.getIqty() != null) {
					cartItem.setIqty(cartItem.getIqty() + ocartItem.getIqty());
				}
			} else if (ocartItem instanceof BundleCartItem) {
				List<SingleCartItem> blist = ((BundleCartItem) cartItem)
						.getChildList();
				List<SingleCartItem> oblist = ((BundleCartItem) ocartItem)
						.getChildList();
				for (int i = 0; i < blist.size(); i++) {
					if (blist.size() == oblist.size()
							&& blist.get(i).getIqty() != null
							&& oblist.get(i).getIqty() != null) {
						blist.get(i).setIqty(
								blist.get(i).getIqty()
										+ oblist.get(i).getIqty());
					}
				}
			}
		}
		cookieCartService.saveCartItem(cartItem);
		int lang = foundationService.getLang();
		int siteid = foundationService.getSiteID();
		String currency = foundationService.getCurrency();
		
		List<CartItem> cartItemlist = cookieCartService.getAllItems(siteid,
				lang, currency);
		res.setRet(Result.SUCCESS);
		res.setData(cartItemlist.size());
		String callbackName = request.getParameter("jsoncallback");
		if(callbackName!=null){
			return callbackName + "(" + JSON.toJSONString(res) + ")";
		}
		return res;
	}

	@RequestMapping(value = "/delcartitem", method = RequestMethod.POST)
	@ResponseBody
	public Object delCartItem(Model model, @RequestBody String data) {
		Result res = new Result();
		CartItem cartItem = packCartItem(data);
		List<CartItem> clist = Lists.newArrayList();
		clist.add(cartItem);
		cookieCartService.deleteItem(clist);
		res.setRet(Result.SUCCESS);
		return res;
	}
	
	@RequestMapping(value = "/cartitem", method = RequestMethod.GET)
	@ResponseBody
	public Object showCartItem(Model model) {
		Result res = new Result();
		int lang = foundationService.getLang();
		int siteid = foundationService.getSiteID();
		String currency = foundationService.getCurrency();

		List<CartItem> cartItemlist = cookieCartService.getAllItems(siteid,
				lang, currency);

		Multimap<Integer, CartItem> cartItemListIndex = Multimaps.index(
				cartItemlist, c -> c.getStorageID());
		TreeMap<Integer, Collection<CartItem>> cl = new TreeMap<Integer, Collection<CartItem>>();
		cl.putAll(cartItemListIndex.asMap());
		
		res.setRet(Result.SUCCESS);
		res.setData(cl);
		return res;
	}
	

	/**
	 * json组装CartItem对象
	 */
	protected CartItem packCartItem(String data) {
		if (data == null || "".equals(data)) {
			return null;
		}
		try {
			data = URLDecoder.decode(data, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			logger.error(e.toString());
		}
		JSONArray jarr = JSONObject.parseArray(data);
		if (jarr == null) {
			return null;
		}
		CartItem cartItem = new CartItem();
		if (jarr.size() == 1) {
			cartItem = new SingleCartItem();
			JSONObject jsonNode = jarr.getJSONObject(0);
			Integer storageID = jsonNode.getInteger("storageid");
			Integer qty = jsonNode.getInteger("qty");
			String clistingid = jsonNode.getString("clistingid");
			cartItem.setStorageID(storageID);
			cartItem.setIqty(qty);
			cartItem.setClistingid(clistingid);
		} else if (jarr.size() > 1) {
			List<SingleCartItem> childList = Lists.newArrayList();
			Integer mainStorageId = null;
			Integer mainQty = null;
			String mainListingid = null;
			for (int i = 0; i < jarr.size(); i++) {
				JSONObject jsonNode = jarr.getJSONObject(i);
				Integer storageID = jsonNode.getInteger("storageid");
				Integer qty = jsonNode.getInteger("qty");
				String clistingid = jsonNode.getString("clistingid");
				if (i == 0) {
					mainStorageId = storageID;
					mainQty = qty;
					mainListingid = clistingid;
					continue;
				}
				SingleCartItem singleCartItem = new SingleCartItem();
				singleCartItem.setStorageID(storageID);
				singleCartItem.setIqty(qty);
				singleCartItem.setClistingid(clistingid);
				childList.add(singleCartItem);
			}
			cartItem = new BundleCartItem(childList);
			cartItem.setStorageID(mainStorageId);
			cartItem.setIqty(mainQty);
			cartItem.setClistingid(mainListingid);
		}
		return cartItem;
	}
}
