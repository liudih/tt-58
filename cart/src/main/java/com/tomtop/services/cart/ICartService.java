package com.tomtop.services.cart;

import java.util.List;

import com.tomtop.valueobjects.CartItem;

public interface ICartService {

	public abstract List<CartItem> getCartItems(List<CartItem> items,
			int siteID, int languageID, String ccy, int storageId);

	public abstract List<CartItem> getCartItems(List<CartItem> items,
			int siteID, int languageID, String ccy);

	public abstract double getTotal(List<CartItem> items);

	/**
	 * 判断是否够库存
	 * 
	 * @param itemID
	 * @param qty
	 * @param listingids
	 * @return
	 */
	public abstract boolean isEnoughQty(CartItem cartItem);
	
	public boolean checkCartItem(String listingids, Integer storageid);

}