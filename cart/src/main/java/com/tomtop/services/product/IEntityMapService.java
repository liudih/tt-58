package com.tomtop.services.product;

import java.util.List;
import java.util.Map;

import org.apache.commons.chain.web.WebContext;

import com.tomtop.dto.product.ProductEntityMap;

public interface IEntityMapService {

	public abstract List<ProductEntityMap> getProductEntityMapByListingId(
			String listingId, Integer lang);

	public abstract Map<String, String> getAttributeMap(String listingID,
			Integer languageID);


//	public Map<String, String> getAttributeMap(String listingID, WebContext context);


}