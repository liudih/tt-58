package com.tomtop.services.impl.product.price;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tomtop.services.product.IPriceProvider;
import com.tomtop.valueobjects.product.price.Price;
import com.tomtop.valueobjects.product.price.PriceCalculationContext;
import com.tomtop.valueobjects.product.spec.IProductSpec;
import com.tomtop.valueobjects.product.spec.SingleProductSpec;

/**
 * 基础价格提供，适合找出单个商品的价格
 * 
 * @author kmtong
 *
 */
@Service
public class BasicPriceProvider implements IPriceProvider {

	@Autowired
	BasePriceCalculator basePriceCalculator;

	@Override
	public List<Price> getPrice(List<IProductSpec> items,
			PriceCalculationContext ctx) {

		return basePriceCalculator.getPrice(items, ctx);

	}

	@Override
	public boolean match(IProductSpec item) {
		return (item instanceof SingleProductSpec);
	}

}
