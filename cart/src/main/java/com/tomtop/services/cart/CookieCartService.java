package com.tomtop.services.cart;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Collections2;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.tomtop.controllers.Inital;
import com.tomtop.utils.CookieUtils;
import com.tomtop.valueobjects.BundleCartItem;
import com.tomtop.valueobjects.CartItem;
import com.tomtop.valueobjects.SingleCartItem;

@Service
public class CookieCartService implements ICookieCartService {

	private static final Logger logger = LoggerFactory
			.getLogger(CookieCartService.class);
	@Autowired
	ICartService cartEnquiryService;
	@Autowired
	Inital inital;

	public static final String DOMAIN = "chicuu.com";

	public static final String CART_COOKIE_KEY = "plist";

	public static final String STORAGE_ID = "storageid";

	@Override
	public void saveCartItem(CartItem cartItem) {
		if (cartItem == null) {
			return;
		}
		JSONObject jo = null;

		String oplist = getCookie(CART_COOKIE_KEY);
		if (oplist != null) {
			try {
				jo = (JSONObject) JSONObject.parse(oplist);
			} catch (Exception e) {
				logger.error("parse plist to json error,plist is:{}", oplist);
				// 清除plist
				CookieUtils.removeCookie(CART_COOKIE_KEY);
				return;
			}

		} else {
			jo = new JSONObject();
		}
		if (cartItem instanceof SingleCartItem) {
			// 计算hashcode
			int hashCode = cartItem.getClistingid().hashCode();
			String hashCodeStr = hashCode + "";
			// 默认是0
			Integer storageId = 0;
			if (cartItem.getStorageID() != null) {
				storageId = cartItem.getStorageID();
			}

			JSONObject storage = jo.getJSONObject(storageId.toString());
			if (storage == null) {
				storage = new JSONObject();
				jo.put(storageId.toString(), storage);
			}

			JSONArray detailArray = storage.getJSONArray(hashCodeStr);
			if (detailArray == null) {
				detailArray = new JSONArray();

				JSONObject detail = new JSONObject();
				detail.put("listing", cartItem.getClistingid());
				detail.put("qty", cartItem.getIqty());
				detailArray.add(detail);

				storage.put(hashCodeStr, detailArray);
			} else {
				JSONObject detail = detailArray.getJSONObject(0);
				// Integer qty = detail.getInteger("qty");
				// qty = qty + cartItem.getIqty();
				detail.put("qty", cartItem.getIqty());
			}

		} else if (cartItem instanceof BundleCartItem) {
			BundleCartItem bcartiem = ((BundleCartItem) cartItem);
			List<SingleCartItem> slist = bcartiem.getChildList();
			StringBuilder sb = new StringBuilder();
			sb.append(bcartiem.getClistingid());
			// 计算hashcode
			FluentIterable.from(slist).forEach(item -> {
				sb.append(item.getClistingid());
			});
			int hashCode = sb.toString().hashCode();
			String hashCodeStr = hashCode + "";

			// 默认是0
			Integer storageId = 0;
			if (cartItem.getStorageID() != null) {
				storageId = cartItem.getStorageID();
			}

			JSONObject storage = jo.getJSONObject(storageId.toString());
			if (storage == null) {
				storage = new JSONObject();
				jo.put(storageId.toString(), storage);
			}

			JSONArray detailArray = storage.getJSONArray(hashCodeStr);

			if (detailArray == null) {
				detailArray = new JSONArray();
				JSONObject main = new JSONObject();
				main.put("listing", bcartiem.getClistingid());
				main.put("qty", bcartiem.getIqty());
				detailArray.add(main);
				for (SingleCartItem item : slist) {
					JSONObject detail = new JSONObject();
					detail.put("listing", item.getClistingid());
					detail.put("qty", item.getIqty());
					detailArray.add(detail);
				}
				;
				storage.put(hashCodeStr, detailArray);
			} else {
				Map<String, Integer> map = Maps.newHashMap();
				map.put(bcartiem.getClistingid(), bcartiem.getIqty());
				FluentIterable.from(slist).forEach(i -> {
					if (i.getIqty() != null) {
						map.put(i.getClistingid(), i.getIqty());
					}
				});
				Iterator iterator = detailArray.iterator();
				while (iterator.hasNext()) {
					JSONObject detail = (JSONObject) iterator.next();
					String listing = detail.getString("listing");
					Integer qty = map.get(listing);
					if (qty != null) {
						detail.put("qty", qty);
					}
				}
			}

		}
		// String text = JSON.toJSONString(jo,
		// SerializerFeature.UseSingleQuotes);
		String text = jo.toJSONString();
		this.setCookie(CART_COOKIE_KEY, text);
		logger.debug("saveCookies:{}", text);
	}

	@Override
	public void deleteItem(List<CartItem> items) {
		if (items == null || items.size() == 0) {
			return;
		}
		String oplist = getCookie(CART_COOKIE_KEY);

		if (oplist != null) {

			JSONObject jo = null;
			try {
				jo = (JSONObject) JSONObject.parse(oplist);
			} catch (Exception e) {
				logger.error("parse plist to json error,plist is:{}", oplist);
				// 清除plist
				CookieUtils.removeCookie(CART_COOKIE_KEY);
				return;
			}

			for (CartItem item : items) {
				// 0 代表没有仓库
				Integer StorageID = 0;
				if (item.getStorageID() != null) {
					StorageID = item.getStorageID();
				}
				JSONObject storage = null;
				if (jo != null) {
					storage = jo.getJSONObject(StorageID + "");
				}
				if (storage == null) {
					return;
				}
				String hashcode = null;
				if (item instanceof SingleCartItem) {
					// 计算hashcode
					hashcode = item.getClistingid().hashCode() + "";
				} else if (item instanceof BundleCartItem) {
					List<SingleCartItem> child = ((BundleCartItem) item)
							.getChildList();
					StringBuilder sb = new StringBuilder();
					sb.append(((BundleCartItem) item).getClistingid());
					FluentIterable.from(child).forEach(i -> {
						sb.append(i.getClistingid());
					});
					hashcode = sb.toString().hashCode() + "";
				}
				if (storage.get(hashcode) != null && jo != null) {
					storage.remove(hashcode);
				}
				if (jo.get(StorageID + "") == null
						|| "{}".equals(jo.get(StorageID + "").toString())) {
					jo.remove(StorageID + "");
				}
			}
			;
			// String text = JSON.toJSONString(jo,
			// SerializerFeature.UseSingleQuotes);
			String text = jo.toJSONString();
			this.setCookie(CART_COOKIE_KEY, text);
		}
	}

	@Override
	public void deleteAllItem() {
		CookieUtils.removeCookie(CART_COOKIE_KEY);
	}

	@Override
	public CartItem getItem(CartItem item) {
		if (item == null) {
			return null;
		}
		String oplist = getCookie(CART_COOKIE_KEY);

		if (oplist != null) {
			JSONObject jo = null;
			try {
				jo = (JSONObject) JSONObject.parse(oplist);
			} catch (Exception e) {
				logger.error("parse plist to json error,plist is:{}", oplist);
				// 清除plist
				CookieUtils.removeCookie(CART_COOKIE_KEY);
				return null;
			}
			// 0 代表没有仓库
			Integer StorageID = 0;
			if (item.getStorageID() != null) {
				StorageID = item.getStorageID();
			}

			JSONObject storage = jo.getJSONObject(StorageID + "");
			if (storage == null) {
				return null;
			}
			String hashcode = null;
			if (item instanceof SingleCartItem) {
				// 计算hashcode
				hashcode = item.getClistingid().hashCode() + "";
			} else if (item instanceof BundleCartItem) {
				List<SingleCartItem> child = ((BundleCartItem) item)
						.getChildList();
				StringBuilder sb = new StringBuilder();
				sb.append(((BundleCartItem) item).getClistingid());
				FluentIterable.from(child).forEach(i -> {
					sb.append(i.getClistingid());
				});
				hashcode = sb.toString().hashCode() + "";
			}
			// 取一个产品
			JSONArray products = storage.getJSONArray(hashcode);
			if (products == null || products.size() == 0) {
				return null;
			}
			if (products.size() == 1) {
				SingleCartItem sitem = new SingleCartItem();
				if (StorageID != 0) {
					sitem.setStorageID(StorageID);
				}
				JSONObject detail = products.getJSONObject(0);
				String listing = detail.getString("listing");
				Integer qty = detail.getInteger("qty");
				sitem.setClistingid(listing);
				sitem.setIqty(qty);
				return sitem;
			} else {

				LinkedList<SingleCartItem> childs = Lists.newLinkedList();
				JSONObject main = products.getJSONObject(0);

				for (int i = 1; i < products.size(); i++) {
					JSONObject detail = products.getJSONObject(i);
					SingleCartItem child = new SingleCartItem();
					if (StorageID != 0) {
						child.setStorageID(StorageID);
					}
					String listing = detail.getString("listing");
					Integer qty = detail.getInteger("qty");
					child.setClistingid(listing);
					child.setIqty(qty);
					childs.add(child);
				}
				;
				BundleCartItem bitem = new BundleCartItem(childs);
				bitem.setStorageID(StorageID);
				bitem.setIitemtype(2);
				bitem.setIqty(main.getInteger("qty"));
				bitem.setClistingid(main.getString("listing"));
				return bitem;
			}
		}
		return null;
	}
	
	@Override
	public List<CartItem> getCookiesAllItems(Integer siteid, Integer lang,
			String currency) {
		String oplist = getCookie(CART_COOKIE_KEY);
		List<CartItem> clist = Lists.newArrayList();
		if (oplist != null) {
			JSONObject jo = null;
			try {
				jo = JSONObject.parseObject(oplist);
			} catch (Exception e) {
				logger.error("parse plist to json error,plist is:{}", oplist);
				// 清除plist
				CookieUtils.removeCookie(CART_COOKIE_KEY);
				return clist;
			}

			boolean abort = false;

			Iterator<String> iterator = jo.keySet().iterator();

			while (iterator.hasNext()) {
				String store = iterator.next();
				// 如果是不合法的仓库id
				if (StringUtils.isEmpty(store) || !StringUtils.isNumeric(store)) {
					iterator.remove();
					abort = true;
					logger.error("invalid store id:{}, plist is:{}", store,
							oplist);
					continue;
				}
				Integer storeId = Integer.parseInt(store);
				if (storeId == 0) {
					iterator.remove();
					abort = true;
					logger.error("invalid store id 0, plist is:{}", oplist);
					continue;
				}
				JSONObject storeNode = null;
				try {
					storeNode = jo.getJSONObject(store);
				} catch (Exception e) {
					iterator.remove();
					abort = true;
					logger.error("parse store id:{} error, plist is:{}", store,
							oplist);
					continue;
				}

				Iterator<String> piterator = storeNode.keySet().iterator();
				while (piterator.hasNext()) {
					String key = piterator.next();
					try {
						JSONArray products = storeNode.getJSONArray(key);
						if (products.size() == 1) {
							SingleCartItem item = new SingleCartItem();
							JSONObject detail = products.getJSONObject(0);
							String listing = detail.getString("listing");
							Integer qty = detail.getInteger("qty");
							if(StringUtils.isBlank(listing)){
								continue;
							}
							item.setClistingid(listing);
							item.setIqty(qty);
							item.setStorageID(storeId);
							item.setIitemtype(1);
							clist.add(item);
						} else if (products.size() > 1) {

							LinkedList<SingleCartItem> childs = Lists
									.newLinkedList();
							JSONObject main = products.getJSONObject(0);
							for (int i = 1; i < products.size(); i++) {
								JSONObject detail = products.getJSONObject(i);
								SingleCartItem child = new SingleCartItem();
								String listing = detail.getString("listing");
								Integer qty = detail.getInteger("qty");
								child.setClistingid(listing);
								child.setIqty(qty);
								child.setStorageID(storeId);
								childs.add(child);
							}
							BundleCartItem item = new BundleCartItem(childs);
							String listing = main.getString("listing");
							Integer qty = main.getInteger("qty");
							item.setStorageID(storeId);
							item.setIitemtype(2);
							item.setIqty(qty);
							item.setClistingid(listing);
							// 判断是否为空
							if (item.getClistingid() == null
									|| item.getIqty() == null) {
								this.deleteItem(Lists.newArrayList(item));
								continue;
							}
							clist.add(item);
						}
					} catch (Exception e) {
						abort = true;
						piterator.remove();
						logger.error("key:{} error,value:{}", key,
								storeNode.getString(key));
					}
				}
			}
			if (abort) {
				logger.error("============{}", jo.toJSONString());
				this.setCookie(CART_COOKIE_KEY, jo.toJSONString());
			}
		}
		logger.debug("plist:{}", JSON.toJSONString(clist));
		return clist;
	}

	@Override
	public List<CartItem> getAllItems(Integer siteid, Integer lang,
			String currency) {
		List<CartItem> clist = getCookiesAllItems(siteid, lang, currency);
		clist = cartEnquiryService.getCartItems(clist, siteid, lang, currency);
		return clist;
	}

	@Override
	public List<CartItem> getAllItemsCurrentStorageid(Integer siteid,
			Integer lang, String currency) {
		List<CartItem> cartItems = this.getAllItems(siteid, lang, currency);
		String storageid = getCookie("storageid");
		if (storageid != null && StringUtils.isNumeric(storageid)
				&& !"".equals(storageid)) {
			final Integer stid = Integer.parseInt(storageid);
			cartItems = Lists.newArrayList(Collections2.filter(cartItems,
					c -> c.getStorageID() == stid));
		}
		return cartItems;
	}

	private String getCookie(String key) {
		String co = CookieUtils.getCookie(key);
		if (StringUtils.isEmpty(co)) {
			return null;
		}
		String oplist = co;
		try {
			oplist = URLDecoder.decode(oplist, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			logger.error(e.toString());
		}
		return oplist;
	}

	private void setCookie(String key, String value) {
		try {
			value = URLEncoder.encode(value, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			logger.error(e.toString());
		}
		CookieUtils.setCookie(key, value);
	}

	@Override
	public void deleteStorageItems(String storageId) {
		if (StringUtils.isEmpty(storageId)) {
			storageId = this.getCookie(STORAGE_ID);
		}

		if (StringUtils.isEmpty(storageId)) {
			return;
		}

		String oplist = getCookie(CART_COOKIE_KEY);
		if (oplist != null) {
			JSONObject jo = null;
			try {
				jo = JSONObject.parseObject(oplist);
			} catch (Exception e) {
				logger.error(
						"parse plist to json error in deleteStorageItems,plist is:{}",
						oplist);
				// 清除plist
				CookieUtils.removeCookie(CART_COOKIE_KEY);
				return;
			}
			try {
				if (jo.keySet().size() == 1) {
					CookieUtils.removeCookie(CART_COOKIE_KEY);
					return;
				}
				jo.remove(storageId);
				this.setCookie(CART_COOKIE_KEY, jo.toJSONString());
			} catch (Exception e) {
				logger.error(
						"remove store:{} error in deleteStorageItems,plist is:{}",
						storageId, oplist);
			}

		}
	}
	
	@Override
	public Map<String,List<CartItem>> getAllItemsForMap(Integer siteid, Integer lang,String currency) {
		String oplist = getCookie(CART_COOKIE_KEY);
		
		if(StringUtils.isBlank(oplist)){
			return null;
		}
		TreeMap<String,List<CartItem>> storageMap = new TreeMap<String,List<CartItem>>();
		
		JSONObject cartList = null;
		try{
			//购物车商品列表，json字符串
			cartList = JSONObject.parseObject(oplist);
			//获取对应仓库的购物车商品列表
			Iterator<String> iterator = cartList.keySet().iterator();
			while (iterator.hasNext()) {
				String store = iterator.next();
				//判断仓库知否为有效值
				if(StringUtils.isBlank(store) ||  !StringUtils.isNumeric(store)  ||  Integer.parseInt(store)<1){
					logger.error("invalid store>>>>>"+store+",and oplist is >>>>>>",oplist);
					//删除无效的数据，继续下一个循环
					iterator.remove();
					continue;
				}
				List<CartItem> clist = Lists.newArrayList();
				//获取仓库对应的商品列表
				JSONObject storeNode = cartList.getJSONObject(store);
				Iterator<String> piterator = storeNode.keySet().iterator();
				
				Integer storeId = Integer.parseInt(store);
				String storageName = inital.getStorageName(storeId);
				
				
				while (piterator.hasNext()) {
					String key = piterator.next();
					JSONArray products = storeNode.getJSONArray(key);
					if (products == null) {
						continue;
					}
					SingleCartItem item = new SingleCartItem();
					JSONObject detail = products.getJSONObject(0);
					String listing = detail.getString("listing");
					Integer qty = detail.getInteger("qty");
					if(StringUtils.isBlank(listing)){
						continue;
					}
					item.setClistingid(listing);
					item.setIqty(qty);
					item.setStorageID(storeId);
					item.setIitemtype(1);
					clist.add(item);
				}
				clist = cartEnquiryService.getCartItems(clist, siteid, lang, currency,storeId);
				StringBuffer sb = new StringBuffer("");
				sb.append(storageName).append("-").append(store);
				if(clist!=null && clist.size()>0){
					storageMap.put(sb.toString(), clist);
				}
			}
			
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return storageMap;
	}
}


