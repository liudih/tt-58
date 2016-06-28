package com.tomtop.services;

import java.util.LinkedHashMap;
import java.util.List;

import com.tomtop.entity.ProductBaseDepotSearchKeyword;
import com.tomtop.entity.ProductBaseRecommend;
import com.tomtop.entity.SearchDepotProduct;
import com.tomtop.entity.TopSellersProduct;

public interface ITopSellersRecService {

	public List<TopSellersProduct> getTopSellersProductList(Integer client);
	public List<TopSellersProduct> putTopSellersProductList(Integer client);
	public LinkedHashMap<String, List<SearchDepotProduct>> getProductMap(Integer website,Integer client,Integer lang,String currency);
	public ProductBaseDepotSearchKeyword getProductTopSellers(String keyword,
			Integer website, Integer lang, String currency, Integer categoryId,
			Integer page, Integer size, String sort);
	public List<ProductBaseRecommend> getHotSellersProduct(Integer website, Integer lang, String currency,Integer categoryId,Integer page,Integer size,String depotName);
}
