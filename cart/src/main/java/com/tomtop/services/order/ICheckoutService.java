package com.tomtop.services.order;

import java.util.List;

import com.tomtop.dto.Country;
import com.tomtop.dto.order.ShippingMethodDetail;
import com.tomtop.valueobjects.CartItem;
import com.tomtop.valueobjects.payment.CheckoutDetails;

/**
 * 结算服务类
 * 
 * @author lijun
 *
 */
public interface ICheckoutService {

	/**
	 * 单纯获取商品的subtotal
	 * 
	 * @param items
	 * @return
	 */
	public Double subToatl(List<CartItem> items);

	/**
	 * 最后结算
	 * 
	 * @param items
	 * @param freight
	 *            邮费
	 * @param shipToCountry
	 *            邮寄的国家
	 * @return
	 */
	public CheckoutDetails sum(List<CartItem> items, double freight, String currency);

}
