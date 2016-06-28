package com.tomtop.services;

import java.util.Map;

public interface IProductUrlService {

	public Map<String, String> updateProductUrl(Integer website);
	public Map<String, String> updateProductUrlByListingId(String listingId,Integer website);
}
