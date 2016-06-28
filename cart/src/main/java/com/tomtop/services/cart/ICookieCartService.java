package com.tomtop.services.cart;

import java.util.List;
import java.util.Map;

import com.tomtop.valueobjects.CartItem;

public interface ICookieCartService {

	public abstract void saveCartItem(CartItem cartItem);

	public abstract void deleteItem(List<CartItem> items);

	public abstract void deleteAllItem();

	public abstract CartItem getItem(CartItem item);
	
	public abstract List<CartItem> getCookiesAllItems(Integer siteid, Integer lang,
			String currency);

	public abstract List<CartItem> getAllItems(Integer siteid, Integer lang, String currency);
	
	public Map<String,List<CartItem>> getAllItemsForMap(Integer siteid, Integer lang,String currency) ;
	
	public abstract List<CartItem> getAllItemsCurrentStorageid(Integer siteid,
			Integer lang, String currency);

	/**
	 * 清除某个仓库的商品
	 * 
	 * @author lijun
	 * @param StorageId
	 */
	public abstract void deleteStorageItems(String StorageId);
}