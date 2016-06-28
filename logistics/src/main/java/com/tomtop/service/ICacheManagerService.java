package com.tomtop.service;

import java.util.List;
import java.util.Map;

import com.tomtop.entry.po.ProductBase;
import com.tomtop.entry.po.ShippingDisplayNamePo;
import com.tomtop.entry.po.ShippingTemplate;
import com.tomtop.entry.po.ShippingTypePo;
import com.tomtop.entry.vo.CacheParamsVo;
import com.tomtop.entry.vo.ShippingTitleDescribeParamsVo;


public interface ICacheManagerService {
	
	ProductBase getProductItem(String listingId, Integer storageId);
	
	
	CacheParamsVo getFilterName(Integer filterId);
	
	ShippingTypePo getTemplateTypeName(Integer shippingTypeId);
	
	ShippingTitleDescribeParamsVo getShippingTitleDescribe(Integer language,Integer tmepLateTypeId);
	
	List<ShippingTemplate> getTemplateListByTemplateId(Integer templateId,Integer storageId);
	
	 Double getRate(String ccy) ;
	 
	 Double exchange(String originalCCY, String targetCCY);
	
	 
	 List<ShippingDisplayNamePo> getAllshippingCodeName();
		
	List<ShippingTypePo> getAllTemplateType();


	Map<String, String> getSystemParams();
}
