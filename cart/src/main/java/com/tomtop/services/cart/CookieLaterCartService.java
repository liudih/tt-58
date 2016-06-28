package com.tomtop.services.cart;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
public class CookieLaterCartService implements ICookieLaterCartService {

	@Autowired
	private HttpServletRequest request;
	@Autowired
	private HttpServletResponse response;
	@Autowired
	ICartService cartEnquiryService;
	@Autowired
	Inital inital;
	
	private static final Logger logger = LoggerFactory.getLogger(CookieLaterCartService.class);

	public static final String DOMAIN = "chicuu.com";

	public static final String CART_COOKIE_KEY = "glist";

	@Override
	public void saveCartItem(CartItem cartItem) {
		if (cartItem == null) {
			return;
		}
		JSONObject jo = null;

		String oplist = getCookie(CART_COOKIE_KEY);
		if (oplist != null) {
			jo = (JSONObject) JSONObject.parse(oplist);
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
		this.setCookie(CART_COOKIE_KEY, jo.toJSONString());
		logger.debug("saveCookies:" + jo.toJSONString());
	}

	@Override
	public void deleteItem(List<CartItem> items) {
		if (items == null || items.size() == 0) {
			return;
		}
		String oplist = getCookie(CART_COOKIE_KEY);

		if (oplist != null) {
			JSONObject jo = (JSONObject) JSONObject.parse(oplist);

			FluentIterable.from(items).forEach(item -> {
				// 0 代表没有仓库
					Integer StorageID = 0;
					if (item.getStorageID() != null) {
						StorageID = item.getStorageID();
					}

					JSONObject storage = jo.getJSONObject(StorageID + "");
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
					if(storage.get(hashcode)==null){
						jo.remove(StorageID + "");
						logger.info("nofind-hashcode for del=="+hashcode+"==storageid=="+StorageID);
					}else{
						storage.remove(hashcode);
					}
				});
			this.setCookie(CART_COOKIE_KEY, jo.toJSONString());
		}
	}

	@Override
	public void deleteAllItem() {
		this.setCookie(CART_COOKIE_KEY, "{}");
	}

	@Override
	public CartItem getItem(CartItem item) {
		if (item == null) {
			return null;
		}
		String oplist = getCookie(CART_COOKIE_KEY);

		if (oplist != null) {
			JSONObject jo = (JSONObject) JSONObject.parse(oplist);

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
	public List<CartItem> getAllItems(Integer siteid, Integer lang,
			String currency) {
		String oplist = getCookie(CART_COOKIE_KEY);
		List<CartItem> clist = Lists.newArrayList();
		if (oplist != null) {
			JSONObject jo = (JSONObject) JSONObject.parse(oplist);

			Iterator<String> iterator = jo.keySet().iterator();

			while (iterator.hasNext()) {
				String store = iterator.next();
				//如果是不合法的仓库id
				if(!StringUtils.isNumeric(store) || "".equals(store) || "0".equals(store)){
					jo.remove(store);
					this.setCookie(CART_COOKIE_KEY, jo.toJSONString());
					continue;
				}
				Integer storeId = Integer.parseInt(store);
				JSONObject storeNode = jo.getJSONObject(store);

				Iterator<String> piterator = storeNode.keySet().iterator();

				while (piterator.hasNext()) {
					String key = piterator.next();
					JSONArray products = storeNode.getJSONArray(key);
					if (products.size() == 1) {
						SingleCartItem item = new SingleCartItem();
						JSONObject detail = products.getJSONObject(0);
						String listing = detail.getString("listing");
						Integer qty = detail.getInteger("qty");
						
						item.setClistingid(listing);
						item.setIqty(qty);
						item.setStorageID(storeId);
						item.setIitemtype(1);
						clist.add(item);
					} else if(products.size() > 1){

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
						//判断是否为空
						if(item.getClistingid()==null || item.getIqty()==null){
							this.deleteItem(Lists.newArrayList(item));
							continue;
						}
						clist.add(item);
					}
				}
			}
		}
		logger.debug("++++" + JSON.toJSONString(clist));
		clist = cartEnquiryService.getCartItems(clist, siteid, lang, currency);
		return clist;
	}

	@Override
	public List<CartItem> getAllItemsCurrentStorageid(Integer siteid,
			Integer lang, String currency) {
		List<CartItem> cartItems = this.getAllItems(siteid, lang, currency);
		String storageid = getCookie("storageid");
		if (storageid != null) {
			final Integer stid = Integer.parseInt(storageid);
			cartItems = Lists.newArrayList(Collections2.filter(cartItems,
					c -> c.getStorageID() == stid));
		}
		return cartItems;
	}

	public List<CartItem> getAllLateItemsList(Integer siteid, Integer lang,String currency) {
		String oplist = getCookie(CART_COOKIE_KEY);
		
		if(StringUtils.isBlank(oplist)){
			return null;
		}
		
		JSONObject cartList = null;
		List<CartItem> clist = Lists.newArrayList();
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
				//获取仓库对应的商品列表
				JSONObject storeNode = cartList.getJSONObject(store);
				Iterator<String> piterator = storeNode.keySet().iterator();
				
				Integer storeId = Integer.parseInt(store);
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

					item.setClistingid(listing);
					item.setIqty(qty);
					item.setStorageID(storeId);
					item.setIitemtype(1);
					clist.add(item);
				}
			}
			
			//临时处理，当等于100000该值的时候，查询条件不根据仓库id进行查询，主要是为兼容购物车saveorLate处理
			clist = cartEnquiryService.getCartItems(clist, siteid, lang, currency,100000);
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return clist;
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
		CookieUtils.setCookie(key, value);
	}
}
