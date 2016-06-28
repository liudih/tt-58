package com.tomtop.services.impl.product.price;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.ListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimaps;
import com.google.common.collect.Ordering;
import com.tomtop.dao.product.IProductSalePriceDao;
import com.tomtop.dto.product.ProductSalePrice;
import com.tomtop.services.ICurrencyService;
import com.tomtop.utils.PriceUtils;
import com.tomtop.valueobjects.product.price.Price;
import com.tomtop.valueobjects.product.price.PriceBuilder;
import com.tomtop.valueobjects.product.price.PriceCalculationContext;

@Service
public class SaleDiscountProvider implements IDiscountProvider {

	private static Logger logger = LoggerFactory.getLogger(SaleDiscountProvider.class);
	
	@Autowired
	IProductSalePriceDao productSalePriceEnquityDao;

	@Autowired
	ICurrencyService exchg;

	@Override
	public int getPriority() {
		return 10;
	}

	@Override
	public List<Price> decorate(List<Price> originalPrices,
			PriceCalculationContext context) {
		List<String> checkIDs = PriceUtils
				.extractSingleProductSpecListingIDs(originalPrices);
		if (checkIDs == null || checkIDs.size() == 0) {
			return originalPrices;
		}
		int storageId = context.getStorageId();
		List<ProductSalePrice> onSales = this.productSalePriceEnquityDao
				.getProductSalePrice(context.getPriceAt(), checkIDs,storageId);
		final ListMultimap<String, ProductSalePrice> salesIndex = Multimaps
				.index(onSales, s -> s.getClistingid());
		return Lists.transform(
				originalPrices,
				p -> {
					if (salesIndex.containsKey(p.getSpec().getListingID())) {
						List<ProductSalePrice> sps = salesIndex.get(p.getSpec()
								.getListingID());
						if (!sps.isEmpty()) {
							// if more than one sale price, get the lowest
							ProductSalePrice sp = Ordering
									.natural()
									.onResultOf(
											(ProductSalePrice pp) -> pp
													.getFsaleprice()).min(sps);
							// discount price even higher than original price?
							// no way, return original price
							double salePriceInTargetCcy = exchg.exchange(
									sp.getFsaleprice(), context.getCurrency());
							if (salePriceInTargetCcy >= p.getUnitPrice()) {
								return p;
							}
							return PriceBuilder
									.change(p)
									.withNewPrice(sp.getFsaleprice(),
											sp.getDbegindate(),
											sp.getDenddate()).get();
						}
					}
					return p;
				});
	}

}
