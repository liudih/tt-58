package com.tomtop.services;

import com.tomtop.entity.ProductBaseDepotSearchKeyword;
import com.tomtop.entity.ProductBaseSearchKeyword;
import com.tomtop.entity.ProductDepotSearchKeywordAggSort;


public interface IEsProductSearchKeywordService {

	public ProductBaseSearchKeyword getProductBaseSearch(String keyword,Integer website,
			Integer lang,String currency,Integer categoryId,Integer level,Integer page,Integer size,
			String sort,boolean bmain,String tagName,String depotName,String brand,String yjprice,String type);

	public ProductBaseDepotSearchKeyword getProductBaseDepotSearch(String keyword, Integer website, 
			Integer lang,String currency,Integer categoryId,Integer level,Integer page,Integer size,
			String sort,String tagName,String depotName,String brand,String yjprice,String type,Double startPrice,Double endPrice);
	
	public ProductDepotSearchKeywordAggSort getProductBaseDepotSearchScroll(String keyword, Integer website, 
			Integer lang,String currency,Integer categoryId,Integer level,Integer page,Integer size,
			String sort,String tagName,String depotName,String brand,String yjprice,String type,Double startPrice,Double endPrice);
}
