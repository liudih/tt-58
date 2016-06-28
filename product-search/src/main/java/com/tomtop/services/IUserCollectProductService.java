package com.tomtop.services;

import com.tomtop.entity.UserCollectProduct;



public interface IUserCollectProductService {

	public UserCollectProduct getUserCollectProductList(
			String email,Integer categoryId,String sort,String productKey,Integer lang,
			Integer website,String currency,Integer page,Integer size);
}
