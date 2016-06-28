package com.tomtop.valueobjects.product.price;

import java.util.Date;
import java.util.Map;

import com.google.common.collect.Maps;

public class PriceCalculationContext {

	String currency;
	Map<String, Object> context;
	Date priceAt = new Date();

	final int storageId;

	public PriceCalculationContext(String currency, int storageId) {
		this.currency = currency;
		this.context = Maps.newHashMap();
		this.storageId = storageId;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public Date getPriceAt() {
		return priceAt;
	}

	public void setPriceAt(Date priceAt) {
		this.priceAt = priceAt;
	}

	public Object get(String contextVar) {
		return context.get(contextVar);
	}

	public void put(String contextVar, Object value) {
		context.put(contextVar, value);
	}

	public int getStorageId() {
		return storageId;
	}

	@Override
	public String toString() {
		return "PriceCalculationContext(" + currency + ", " + context + ")";
	}

}
