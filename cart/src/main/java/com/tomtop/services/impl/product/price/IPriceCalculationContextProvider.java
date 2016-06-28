package com.tomtop.services.impl.product.price;

import com.tomtop.valueobjects.product.price.PriceCalculationContext;

/**
 * Introduce variables base on the current environment, e.g. Login Group, etc.
 * that will affect the pricing.
 * 
 * @author kmtong
 *
 */
public interface IPriceCalculationContextProvider {

	/**
	 * 提供相应的环境变量到PriceCalculationContext里
	 * 
	 * @param context
	 */
	void contributeTo(PriceCalculationContext context);

}
