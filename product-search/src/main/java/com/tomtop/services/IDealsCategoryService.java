package com.tomtop.services;

import java.util.List;

import com.tomtop.entity.DealsCategory;
import com.tomtop.entity.ProductBaseDepotSearchKeyword;

public interface IDealsCategoryService {

	public List<DealsCategory> getDealsCategoryList(Integer client);
	
	public ProductBaseDepotSearchKeyword getDealsProducts(String keyword,
			Integer website, Integer lang, String currency, Integer categoryId,
			Integer page, Integer size, String sort, String depotName);
}
