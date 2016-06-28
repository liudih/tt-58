package com.tomtop.services;

import java.util.List;
import java.util.Map;

import com.tomtop.entity.ProductBase;
import com.tomtop.entity.ShoppingCartProduct;

public interface IProductBaseService {

	public List<ProductBase> getProductBaseByListIds(List<String> listingIds,int lang,int website,String currency);
	public ProductBase getProductBaseByListId(String listingId,int lang,int website,String currency);
	public List<ShoppingCartProduct> getProductShopping(String listingIds,int lang,int website,String currency);
	
	public List<ProductBase> getProductBaseByListIds(List<String> listingIds,int lang, int website,String currency,String depotName);
	public ProductBase getProductBaseByListId(String listingId, int lang,int website, String currency,String depotName);
	public Map<Integer,List<ShoppingCartProduct>> getProductShopping(String listingIds,int lang,int website,String currency,String depotName);
}
