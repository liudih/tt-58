package com.tomtop.services;

import java.util.List;
import java.util.Map;

import com.tomtop.entity.index.IndexEntity;
import com.tomtop.entity.index.PageBean;


public interface ISearchService {

	public IndexEntity getSearchProduct(String listingId,Integer langId,Integer siteId);
	
	public List<IndexEntity> getSearchProductList(PageBean bean,Integer langId);
	
	public List<IndexEntity> getSearchProductList(String listingIds,Integer langId,Integer siteId);

	public Map<String,IndexEntity> getSearchProductMap(String listingIds,Integer langId, Integer siteId);
	
	public List<IndexEntity> getSearchProductHotList(Integer langId, Integer siteId,Integer size);
	
	public PageBean getSearchPageBean(PageBean bean, Integer langId);
	
	public PageBean getSearchPageBeanRecommendByType(String listingId,Integer langId, Integer siteId,String type,Integer size);

}
