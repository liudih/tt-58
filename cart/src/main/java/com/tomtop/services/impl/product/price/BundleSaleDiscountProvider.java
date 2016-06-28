package com.tomtop.services.impl.product.price;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.tomtop.dao.product.IProductSalePriceDao;
import com.tomtop.dto.product.ProductSalePrice;
import com.tomtop.utils.PriceUtils;
import com.tomtop.valueobjects.product.price.BundlePrice;
import com.tomtop.valueobjects.product.price.Price;
import com.tomtop.valueobjects.product.price.PriceBuilder;
import com.tomtop.valueobjects.product.price.PriceCalculationContext;

@Service
public class BundleSaleDiscountProvider implements IDiscountProvider {

	@Autowired
	IProductSalePriceDao productSalePriceEnquityDao;

	@Override
	public int getPriority() {
		return 10;
	}

	@Override
	public List<Price> decorate(List<Price> originalPrices,
			PriceCalculationContext context) {

		List<String> checkIDs = PriceUtils
				.extractBundleProductSpecListingIDs(originalPrices);
		if (checkIDs == null || checkIDs.size() == 0) {
			return originalPrices;
		}
		int storageId = context.getStorageId();
		List<ProductSalePrice> onSales = this.productSalePriceEnquityDao.getProductSalePrice(
				context.getPriceAt(), checkIDs,storageId);
		final Map<String, ProductSalePrice> salesIndex = Maps.uniqueIndex(
				onSales, s -> s.getClistingid());
		return Lists.transform(originalPrices, (Price p) -> {
			if (p instanceof BundlePrice) {
				return this.convertPrice((BundlePrice) p, salesIndex);
			}
			return p;
		});
	}

	private Price convertPrice(BundlePrice bprice,
			Map<String, ProductSalePrice> salesIndex) {

		List<Price> newpricelist = Lists.transform(
				bprice.getBreakdown(),
				p -> {
					if (salesIndex.containsKey(p.getSpec().getListingID())) {
						ProductSalePrice sp = salesIndex.get(p.getSpec()
								.getListingID());
						return PriceBuilder
								.change(p)
								.withNewPrice(sp.getFsaleprice(),
										sp.getDbegindate(), sp.getDenddate())
								.get();
					}
					return p;
				});
		double unitBasePrice = 0;
		double unitPrice = 0;
		for (Price child : newpricelist) {
			unitBasePrice += child.getUnitBasePrice();
			unitPrice += child.getUnitPrice();
		}
		BundlePrice result = new BundlePrice(bprice.getSpec(), newpricelist);
		result.setUnitBasePrice(unitBasePrice);
		result.setUnitPrice(unitPrice);
		result.setCurrency(bprice.getCurrency());
		result.setSymbol(bprice.getSymbol());
		result.setRate(bprice.getRate());
		if (unitBasePrice != unitPrice) {
			result.setDiscount((unitBasePrice-unitPrice) / unitBasePrice);
		}
		return result;
	}
}
