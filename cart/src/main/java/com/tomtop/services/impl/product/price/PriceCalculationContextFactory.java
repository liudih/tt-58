package com.tomtop.services.impl.product.price;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tomtop.utils.FoundationService;
import com.tomtop.valueobjects.product.price.PriceCalculationContext;

@Service
public class PriceCalculationContextFactory {

	// @Autowired
	// Set<IPriceCalculationContextProvider> ctxProvider;

	@Autowired
	FoundationService foundation;

	public PriceCalculationContext create(int storageId) {
		return create(foundation.getCurrency(),storageId);
	}

	public PriceCalculationContext create(String currency, int storageId) {
		PriceCalculationContext ctx = new PriceCalculationContext(currency,
				storageId);
		// for (IPriceCalculationContextProvider p : ctxProvider) {
		// p.contributeTo(ctx);
		// }
		return ctx;
	}
}
