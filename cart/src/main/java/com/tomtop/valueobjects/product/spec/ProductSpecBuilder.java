package com.tomtop.valueobjects.product.spec;

import java.util.List;

import com.google.common.collect.Lists;

public class ProductSpecBuilder {

	String mainListingID;
	List<String> allListingIDs = Lists.newArrayList();
	int qty = 1;

	protected ProductSpecBuilder(String mainListingID) {
		this.mainListingID = mainListingID;
		this.allListingIDs.add(mainListingID);
	}

	public ProductSpecBuilder bundleWith(String listingID) {
		allListingIDs.add(listingID);
		return this;
	}

	public ProductSpecBuilder bundleWith(List<String> bundleListingIDs) {
		allListingIDs.addAll(bundleListingIDs);
		//Iterables.addAll(allListingIDs, bundleListingIDs);
		return this;
	}

	public ProductSpecBuilder setQty(int qty) {
		this.qty = qty;
		return this;
	}

	public IProductSpec get() {
		if (allListingIDs.size() > 1) {
			return new BundleProductSpec(mainListingID, allListingIDs, qty);
		} else {
			return new SingleProductSpec(mainListingID, qty);
		}
	}

	public static ProductSpecBuilder build(String mainListingID) {
		return new ProductSpecBuilder(mainListingID);
	}

}
