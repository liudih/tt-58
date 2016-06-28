package com.tomtop.controllers.cart;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
import com.tomtop.dto.StorageParent;
import com.tomtop.services.cart.ICartService;
import com.tomtop.services.cart.ICookieCartService;
import com.tomtop.services.cart.ICookieLaterCartService;
import com.tomtop.services.impl.LoyaltyService;
import com.tomtop.services.impl.base.StorageService;
import com.tomtop.utils.CookieUtils;
import com.tomtop.utils.FoundationService;
import com.tomtop.utils.HttpClientUtil;
import com.tomtop.valueobjects.BundleCartItem;
import com.tomtop.valueobjects.CartItem;
import com.tomtop.valueobjects.SingleCartItem;
import com.tomtop.valueobjects.base.CommonUrl;
import com.tomtop.valueobjects.base.LoginContext;

@Controller
@RequestMapping("/")
public class CartController {
	@Autowired
	ICookieCartService cookieCartService;
	@Autowired
	ICookieLaterCartService cookieLaterCartService;
	@Autowired
	FoundationService foundationService;
	@Autowired
	ICartService cartEnquiryService;
	@Autowired
	StorageService storageService;
	@Autowired
	CommonUrl commonUrl;
	
	@Value("${cart.imgurl}")
	String imgUrl;

	
	/**
	 * 购物车列表
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping()
	public String cartview(Map<String, Object> model, HttpServletRequest request) {
		int lang = foundationService.getLang();
		int siteid = foundationService.getSiteID();
		String currency = foundationService.getCurrency();
		
		String storageid = CookieUtils.getCookie("storageid");
		model.put("storageid", storageid);
		model.put("currencyBo", foundationService.getCurrencyBo());
		model.put("country", foundationService.getCountryObj());
//		model.put("mainurl", this.foundationService.getMainDomain());
		model.put("commonUrl", commonUrl);
		
		String firstdomain = foundationService.getSubdomains();
		if("m".equals(firstdomain)){
			List<CartItem> cartItemlist = cookieCartService.getAllItems(siteid,lang, currency);
			Multimap<Integer, CartItem> cartItemListIndex = Multimaps.index(
					cartItemlist, c -> c.getStorageID());
			TreeMap<Integer, Collection<CartItem>> cl = new TreeMap<Integer, Collection<CartItem>>();
			cl.putAll(cartItemListIndex.asMap());
			List<Integer> cartStorageids = Lists.newArrayList(cl.keySet());
			List<StorageParent> storageList = storageService.getAllStorageParentList();
			Map<Integer, StorageParent> storageMap = Maps.uniqueIndex(storageList,s -> s.getIid());
			model.put("cartItemlist", cl);
			model.put("cartStorageids", cartStorageids);
			model.put("storageMap", storageMap);
			
			LoginContext loginCtx = foundationService.getLoginContext();
			//收藏的数量
			String collectStr = loginCtx.isLogin() ? foundationService.getCollectsByEmail(loginCtx.getEmail(), siteid) : "";
			model.put("collectlist", collectStr);
			return "/mobile/cart/cartview"; 
		}else{	
			//第三方登录
			Map<String,String> urlmap = foundationService.getLoginUrl();
			//获取购物车商品列表
			Map<String,List<CartItem>> cartItemList = cookieCartService.getAllItemsForMap(siteid, lang, currency);
			//获取late购物车商品列表
			List<CartItem> lateCartItemlist = cookieLaterCartService.getAllLateItemsList( siteid, lang, currency);
			model.put("laterItemlist", lateCartItemlist);
			model.put("cartItemMap", cartItemList);
			model.put("urlmap", urlmap);
			return "/cart/cartview";
		}
	}

	@RequestMapping("/add")
	public String test(Map<String, Object> model) {
		return "cart/carttest";
	}


	@RequestMapping(value = "/savecartitem", method = RequestMethod.POST)
	@ResponseBody
	public Object saveCartItem(Model model, @RequestParam("data") String data) {
		Map<String, Object> mjson = new HashMap<String, Object>();
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
		mjson.put("result", "success");
		return mjson;
	}

	@RequestMapping(value = "/updatecartitem", method = RequestMethod.POST)
	@ResponseBody
	public Object updateCartItem(Model model, @RequestParam("data") String data) {
		Map<String, Object> mjson = new HashMap<String, Object>();
		int lang = foundationService.getLang();
		int siteid = foundationService.getSiteID();
		String currency = foundationService.getCurrency();
		
		CartItem cartItem = packCartItem(data);
		cookieCartService.saveCartItem(cartItem);
		List<CartItem> clist = cartEnquiryService.getCartItems(Lists.newArrayList(cartItem), siteid, lang, currency);
		if(clist.size()>0){
			mjson.put("cartItem", clist.get(0));
		}
		mjson.put("result", "success");
		return mjson;
	}

	@RequestMapping(value = "/delcartitem", method = RequestMethod.POST)
	@ResponseBody
	public Object delCartItem(Model model, @RequestParam("data") String data) {
		Map<String, Object> mjson = new HashMap<String, Object>();
		CartItem cartItem = packCartItem(data);
		List<CartItem> clist = Lists.newArrayList();
		clist.add(cartItem);
		cookieCartService.deleteItem(clist);
		mjson.put("result", "success");
		return mjson;
	}

	@RequestMapping(value = "/savelatercartitem", method = RequestMethod.POST)
	@ResponseBody
	public Object saveLaterCartItem(Model model,
			@RequestParam("data") String data) {
		Map<String, Object> mjson = new HashMap<String, Object>();
		CartItem cartItem = packCartItem(data);
		if (cartItem == null) {
			mjson.put("result", "data is empty");
			return mjson;
		}
		cookieLaterCartService.saveCartItem(cartItem);
		cookieCartService.deleteItem(Lists.newArrayList(cartItem));
		mjson.put("result", "success");
		return mjson;
	}

	/**
	 * 从save for later 到 购物车
	 */
	@RequestMapping(value = "/latertocart", method = RequestMethod.POST)
	@ResponseBody
	public Object laterToCart(Model model, @RequestParam("data") String data) {
		Map<String, Object> mjson = new HashMap<String, Object>();
		CartItem cartItem = packCartItem(data);
		if (cartItem == null) {
			mjson.put("result", "data is empty");
			return mjson;
		}
		cookieLaterCartService.deleteItem(Lists.newArrayList(cartItem));
		cookieCartService.saveCartItem(cartItem);
		mjson.put("result", "success");
		return mjson;
	}

	@RequestMapping(value = "/dellatercart", method = RequestMethod.POST)
	@ResponseBody
	public Object delCartLaterItem(Model model,
			@RequestParam("data") String data) {
		Map<String, Object> mjson = new HashMap<String, Object>();
		CartItem cartItem = packCartItem(data);
		if (cartItem == null) {
			mjson.put("result", "data is empty");
			return mjson;
		}
		cookieLaterCartService.deleteItem(Lists.newArrayList(cartItem));
		mjson.put("result", "success");
		return mjson;
	}

	@RequestMapping(value = "/checkstatus", method = RequestMethod.GET)
	@ResponseBody
	public Object checkCartStatus(Model model,
			@RequestParam("islogin") Integer islogin,
			@RequestParam(value = "storageid", required = false, defaultValue = "1") Integer storageid,
			@RequestParam(value = "listingids", required = false, defaultValue = "") String listingids) {
		
		Map<String, Object> mjson = new HashMap<String, Object>();
		LoginContext loginContext = foundationService.getLoginContext();
		//如果没登录清除积分 优惠券 (不含推广码)cookie
		if(!loginContext.isLogin()){
			String coupon = CookieUtils.getCookie(LoyaltyService.LOYALTY_PREFER);
			if (StringUtils.isNotEmpty(coupon)) {
				JSONObject json = null;
				try {
					json = JSONObject.parseObject(coupon);
					String type = json.getString("type");
					if(type!=null && !"promo".equals(type)){
						CookieUtils.setCookie(LoyaltyService.LOYALTY_PREFER, "");
					}
				} catch (Exception e) {
					CookieUtils.setCookie(LoyaltyService.LOYALTY_PREFER, "");
				}
			}
			CookieUtils.setCookie(LoyaltyService.LOYALTY_TYPE_POINT, "");
		}
		//判断库存
		String hostname = foundationService.getHostName();
		if("tomtop".equals(hostname)){
			boolean ischeck = cartEnquiryService.checkCartItem(listingids, storageid);
			if(ischeck==false){
				mjson.put("result", "no-qty");
				return mjson;
			}
		}
		
		if (islogin != null && islogin == 1 && !loginContext.isLogin()) {
			mjson.put("result", "no-login");
			return mjson;
		}
		mjson.put("result", "success");
		return mjson;
	}
	
	@RequestMapping(value = "/collect", method = RequestMethod.GET)
	@ResponseBody
	public Object addCollect(Model model,@RequestParam("listingId") String listingId,
			@RequestParam("email") String email,
			@RequestParam("type") String type) {
		Map<String, Object> mjson = new HashMap<String, Object>();
		Map<String, Object> dd = new HashMap<String, Object>();
		String cur = "";
		if("add".equals(type)){
			dd.put("listingId", listingId);
			dd.put("email", email);
			dd.put("website", foundationService.getSiteID());
			cur = HttpClientUtil.doPut(FoundationService.PRODUCT_URL + "/ic/v1/product/collect/add", JSON.toJSONString(dd));
		}else if("del".equals(type)){
			dd.put("email", email);
			dd.put("website", foundationService.getSiteID());
			dd.put("ids", listingId);
			//System.out.println(FoundationService.MEMBER_URL + "/collect/v1/collects/delete"+ JSON.toJSONString(dd));
			cur = HttpClientUtil.doPost(FoundationService.MEMBER_URL + "/collect/v1/collects/delete", JSON.toJSONString(dd));
		}
		if(cur==null || "".equals(cur)){
			mjson.put("result", "error");
			return mjson;
		}
		JSONObject object = JSON.parseObject(cur);
		Integer ret = object.getInteger("ret");
		if(ret!=null && ret==1){
			mjson.put("result", "success");
		}else {
			Integer errCode = object.getInteger("errCode");
			if(errCode==-81003){
				mjson.put("result", "This item is already on your wish list!");
			}else{
				mjson.put("result", object.getString("errMsg"));
			}
		}
		return mjson;
	}
	

	/**
	 * json组装CartItem对象
	 */
	protected CartItem packCartItem(String data) {
		JSONArray jarr = JSONObject.parseArray(data);
		if (data == null || "".equals(data) || jarr == null) {
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
	
	@RequestMapping(value = "/addcurrency", method = RequestMethod.GET)
	@ResponseBody
	public Object addCurrency(Model model,@RequestParam("code") String code) {
		Map<String, Object> mjson = new HashMap<String, Object>();
		CookieUtils.removeCookie(FoundationService.COOKIE_CURRENCY);
		CookieUtils.setCookie(FoundationService.COOKIE_CURRENCY, code);
		mjson.put("result", "success");
		return mjson;
	}
	
	
	/**
	 * 热搜产品展示
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/showhotkey", method = RequestMethod.GET)
	@ResponseBody
	public Object showHotKey(Model model) {
		int lang = foundationService.getLang();
		Integer siteid = foundationService.getSiteID();
		Map<String, Object> mjson = new HashMap<String, Object>();
		mjson.put("result","error");		
		StringBuilder url = new StringBuilder(); 
		url.append(foundationService.PRODUCT_URL);
		url.append("/ic/v1/home/search/keyword?category=0");
		url.append("&client=2");
		url.append("&lang=").append(lang);
		url.append("&website=").append(siteid);
		String cur = HttpClientUtil.doGet(url.toString());
		if (cur == null || "".equals(cur)) {
			return mjson;
		}
		JSONObject object = JSON.parseObject(cur);
		Integer ret = object.getInteger("ret");
		if(ret==1){
			mjson.put("result", "success");
			JSONArray ja = object.getJSONArray("data");
			mjson.put("data", ja);
		}
		return mjson;
	}
	
	/**
	 * 显示商品详情
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/showproduct", method = RequestMethod.GET)
	@ResponseBody
	public Object showProductDetail(Model model, @RequestParam("producturl")String producturl) {
		Map<String, Object> mjson = new HashMap<String, Object>();
		mjson.put("result","error");	
		Integer lang = foundationService.getLang();
		Integer siteid = foundationService.getSiteID();
		String currency = foundationService.getCurrency();
		StringBuilder url = new StringBuilder(foundationService.PRODUCT_URL);
		url.append("/ic/v3/product/base");
		url.append("?lang=").append(lang);
		url.append("&client=").append(siteid);
		url.append("&currency=").append(currency);
		url.append("&key=").append(producturl);
		String result = HttpClientUtil.doGet(url.toString());
		if (result == null || "".equals(result)) {
			return mjson;
		}
		JSONObject object = JSON.parseObject(result);
		Integer ret = object.getInteger("ret");
		if(ret==1){
			mjson.put("result", "success");
			JSONObject ja = object.getJSONObject("data");
			mjson.put("data", ja);
		}
		return mjson;
	}
}
