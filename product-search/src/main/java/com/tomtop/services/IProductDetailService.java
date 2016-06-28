package com.tomtop.services;

public interface IProductDetailService {

	public String getProductExplainByType(String type,Integer website,Integer langId);
	
	public Integer getProductQty(String listingId,Integer website);
}
