package com.tomtop.services;

import com.tomtop.entity.BaseBean;

public interface IDropshipProductService {

	public BaseBean addProductDropshipBySku(String email,String sku,Integer siteId);
	public BaseBean addProductDropshipByListingId(String email,String listingId,Integer siteId);
}
