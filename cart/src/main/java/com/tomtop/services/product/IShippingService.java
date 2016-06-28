package com.tomtop.services.product;

import java.util.List;

import com.tomtop.dto.Country;
import com.tomtop.dto.base.Storage;
import com.tomtop.dto.product.ShippingStorage;
import com.tomtop.valueobjects.CartItem;
import com.tomtop.valueobjects.order.ShippingMethod;
import com.tomtop.valueobjects.order.ShippingMethodMini;

public interface IShippingService {

	/**
	 * 当站点有默认发货仓库时，忽略其它，直接按默认仓库发货
	 * 
	 * @param siteId
	 * @return
	 */
	public Storage getWebsiteLimitStorage(int siteId);

	/**
	 * 获取listing的所有仓库
	 * 
	 * @param listingids
	 * @return
	 */
	public List<ShippingStorage> getStorages(List<String> listingids);

	/**
	 * 根据目的发货地国家，获取这个国家优先从那个仓库发货 1.如果站点有默认发货仓库,那么默认仓库，这是为了一些站点会让一些海外子公司自主经营
	 * 2.如果所有商品未设置发货仓库，那么根据目的地国家取相应的发货仓库
	 * 
	 * @param country
	 *            目的发货国家
	 * @return 仓库
	 */
	public Storage getCountryDefaultStorage(Country country);

	public Storage getShippingStorage(int siteId, List<String> listingids);

	public Storage getShippingStorage(int siteId, Country country,
			List<String> listingids);

	/**
	 * 判断是否同仓库
	 * 
	 * @author lijun
	 * @param listingids
	 * @param storageId
	 * @return
	 */
	public boolean isSameStorage(List<String> listingids, String storageId);

	/**
	 * 获取邮寄方式
	 * 
	 * @author lijun
	 * @param shipToCountryCode
	 *            寄往的国家code
	 * @param storageId
	 *            发货仓库id
	 * @param language
	 *            语言id
	 * @param items
	 * @return
	 */
	public List<ShippingMethod> getShipMethod(
			String shipToCountryCode, int storageId, int language,
			List<CartItem> items,String currencyCode,double totalPrice);
	
	
	public ShippingMethodMini getShipMethodInfo(String shipCode, Integer lang);
}