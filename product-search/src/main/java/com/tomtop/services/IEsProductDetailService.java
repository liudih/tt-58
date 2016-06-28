package com.tomtop.services;

import java.util.List;

import com.tomtop.entity.CollectCount;
import com.tomtop.entity.PrdouctDesc;
import com.tomtop.entity.ProductBase;
import com.tomtop.entity.ProductBaseDepotDtl;
import com.tomtop.entity.ProductBaseDtl;
import com.tomtop.entity.ProductDetails;
import com.tomtop.entity.ProductSeo;
import com.tomtop.entity.index.ReviewStartNum;

public interface IEsProductDetailService {
	
	public ProductBaseDepotDtl getProductBaseDepotDtl(String key,int langId, int siteId, String currency);
	
	public ProductBaseDtl getProductBaseDtlVo(String key,int langId, int siteId, String currency);

	public List<ProductDetails> getProductDetailsBoList(String sku,
			Integer langId, Integer siteId, String currency);

	public PrdouctDesc getPrdouctDescBo(String sku, Integer langId,
			Integer siteId);

	public ProductSeo getProductSeoBo(String sku, Integer langId,
			Integer siteId);
	
	public ReviewStartNum getReviewStartNumBoById(String listingId,Integer langId,
			Integer siteId);
	
	public CollectCount getCollectCountByListingId(String listingIds,Integer langId,
			Integer siteId);
	
	public List<ProductBase> getProductHotBoList(Integer langId, Integer siteId,String currency,Integer size);
	
	public List<ProductBase> getProductHotBoList(Integer langId, Integer siteId,String currency,Integer size,String depotName);
}
