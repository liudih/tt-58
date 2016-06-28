package com.tomtop.services;

import java.util.List;

import com.tomtop.entity.NewArrivalsAgg;
import com.tomtop.entity.ProductBaseDepotSearchKeyword;
import com.tomtop.entity.ProductBaseSearchKeyword;

public interface INewArrivalsService {

	public ProductBaseSearchKeyword getProductNewArrivals(String keyword,Integer website,
			Integer lang,String currency,Integer categoryId,Integer page,Integer size,
			String sort,String tagName,String depotName,String brand,String yjprice,String type,String releaseTime);
	
	public List<NewArrivalsAgg> getNewAgg(Integer website);
	
	public ProductBaseDepotSearchKeyword getProductNewArrivalsDepot(String keyword,
			Integer website, Integer lang, String currency, Integer categoryId,
			Integer page, Integer size, String sort, String tagName,
			String depotName, String brand, String yjprice, String type,String releaseTime);
}
