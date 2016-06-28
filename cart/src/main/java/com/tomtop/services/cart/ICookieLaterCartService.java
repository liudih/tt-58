package com.tomtop.services.cart;

import java.util.List;

import com.tomtop.valueobjects.CartItem;

public interface ICookieLaterCartService {

	public abstract void saveCartItem(CartItem cartItem);

	public abstract void deleteItem(List<CartItem> items);

	public abstract void deleteAllItem();

	public abstract CartItem getItem(CartItem item);

	public abstract List<CartItem> getAllItems(Integer siteid, Integer lang,
			String currency);

	public abstract List<CartItem> getAllItemsCurrentStorageid(Integer siteid,
			Integer lang, String currency);
	
	public List<CartItem> getAllLateItemsList(Integer siteid, Integer lang,String currency) ;

}