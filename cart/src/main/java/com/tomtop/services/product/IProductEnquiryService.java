package com.tomtop.services.product;

import java.util.List;

import com.tomtop.valueobjects.CartItem;
import com.tomtop.valueobjects.product.ProductLite;

public interface IProductEnquiryService {

	public List<ProductLite> getProductLiteByListingIDs( List<String> listingIDs, int websiteID, int languageID);
	
	
	public List<ProductLite> getProductLiteForListingIDsAndStorageId( List<CartItem> items, int websiteID, int languageID,int storageId);
}
