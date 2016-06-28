package com.tomtop.services;

import com.tomtop.entity.ProductBaseSearchKeyword;

public interface IFreeShippingService {

	public ProductBaseSearchKeyword getProductFreeShipping(String keyword,Integer website,
			Integer lang,String currency,Integer categoryId,Integer page,Integer size,
			String sort,String depotName,String brand,String yjprice);
}
