package com.tomtop.loyalty.utils;

import java.util.List;

import com.tomtop.loyalty.models.Product;

public class PreferUtil {

	/**
	 * 根据listing集合计算商品总价
	 * 
	 * @param listingIds
	 * @return
	 */
	public static Double subTotal(List<Product> products) {
		DoubleCalculateUtil result = new DoubleCalculateUtil(0.0);
		for (Product p : products) {
			DoubleCalculateUtil duti = new DoubleCalculateUtil(0.0);
			duti = duti.add(p.getPrice());
			duti = duti.multiply(p.getQty());
			result = result.add(duti.doubleValue());
		}
		return result.doubleValue();
	}

	/**
	 * 货币精度转换
	 * 
	 * @param money
	 * @param currency
	 * @return
	 */
	public static String money(Double money, String currency) {
		return null;
	}

}
