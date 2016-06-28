package com.tomtop.services.product;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.tomtop.valueobjects.CartItem;
import com.tomtop.valueobjects.product.price.Price;
import com.tomtop.valueobjects.product.price.PriceCalculationContext;
import com.tomtop.valueobjects.product.spec.IProductSpec;

public interface IPriceService {

	Price getPrice(String listingID,int storageId);

	Price getPrice(String listingID, String ccy,int storageId);

	Price getPrice(IProductSpec item,int storageId);

	Price getPrice(IProductSpec item, String ccy,int storageId);

	List<Price> getPrice(List<IProductSpec> items,int storageId);

	List<Price> getPrice(List<IProductSpec> items, Date priceAt,int storageId);

	List<Price> getPrice(List<IProductSpec> items, Date priceAt, String currency,int storageId);

	List<Price> getPrice(List<IProductSpec> items, PriceCalculationContext ctx);

	Map<IPriceProvider, List<Price>> getPriceByProvider(List<IProductSpec> items,int storageId);
	
	/**
	 * 新的查询产品价格方法
	 * @param items 购物车产品
	 * @param ccy 当前货币
	 * @param storageId 当前仓库
	 * @return 根据listingid分组的价格
	 */
	Map<String, Price> getPriceByProvider(List<CartItem> items, String ccy,Integer storageId);

}
