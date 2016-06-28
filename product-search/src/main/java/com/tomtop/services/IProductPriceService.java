package com.tomtop.services;

import com.tomtop.entity.ProductPrice;

public interface IProductPriceService {

	public ProductPrice getProductPrice(String listingId,Integer website,String currency);
}
