package com.tomtop.service.impl;

import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.tomtop.common.constants.Cons;
import com.tomtop.common.utils.HttpClientUtil;
import com.tomtop.entry.po.ProductBase;
import com.tomtop.entry.po.ShippingDisplayNamePo;
import com.tomtop.entry.po.ShippingTemplate;
import com.tomtop.entry.po.ShippingTypePo;
import com.tomtop.entry.po.base.Currency;
import com.tomtop.entry.po.base.SystemParamsPo;
import com.tomtop.entry.vo.CacheParamsVo;
import com.tomtop.entry.vo.ShippingTitleDescribeParamsVo;
import com.tomtop.mapper.product.ProductBaseMapper;
import com.tomtop.mapper.shipping.CurrencyMapper;
import com.tomtop.mapper.shipping.ShippingTemplateMapper;
import com.tomtop.service.ICacheManagerService;
@Service
public class CacheManagerService implements ICacheManagerService {

	@Value("${system_params}")
	private String systemParamsUrl;
	
	@Autowired
	ProductBaseMapper productBaseMapper;
	
	@Autowired
	ShippingTemplateMapper shippingTemplateMapper;
	
	@Autowired
	CurrencyMapper ccyMapper;

	
	@Cacheable(value="productBaseCacheManager",keyGenerator = "customKeyGenerator", cacheManager = "productBaseCacheManager")
	@Override
	public ProductBase getProductItem(String listingId,Integer storageId){
		ProductBase productsItem = productBaseMapper.getProductsItem(listingId,storageId);
		return productsItem;
		
	}
	
	
	@Cacheable(value="templateCacheManager",keyGenerator = "customKeyGenerator", cacheManager = "productBaseCacheManager")
	@Override
	public CacheParamsVo getFilterName(Integer filterId) {
		return shippingTemplateMapper.getFilterNameById(filterId);
	}
	
	
	@Cacheable(value="templateCacheManager",keyGenerator = "customKeyGenerator", cacheManager = "productBaseCacheManager")
	@Override
	public ShippingTypePo getTemplateTypeName(Integer shippingTypeId) {
		return  shippingTemplateMapper.getTemplateTypeNameById(shippingTypeId);
	}
	
	
	@Cacheable(value="templateCacheManager",keyGenerator = "customKeyGenerator", cacheManager = "productBaseCacheManager")
	@Override
	public ShippingTitleDescribeParamsVo getShippingTitleDescribe(Integer language,
			Integer tmepLateTypeId) {
		return shippingTemplateMapper.getShippingTitleDescription(tmepLateTypeId, language);
	}
	
	
	@Cacheable(value="templateCacheManager",keyGenerator = "customKeyGenerator", cacheManager = "productBaseCacheManager")
	@Override
	public List<ShippingTemplate> getTemplateListByTemplateId(Integer templateId,
			Integer storageId) {
		 List<ShippingTemplate> templateConfigInfo = shippingTemplateMapper.getTemplateConfigInfo(templateId,storageId);
		
		 return CollectionUtils.isEmpty(templateConfigInfo)?Lists.newArrayList():templateConfigInfo;
	}

	@Cacheable(value="exchangeCacheManager",keyGenerator = "customKeyGenerator", cacheManager = "exchangeCacheManager")
	@Override
	public Double getRate(String ccy) {
		Currency currency = ccyMapper.findByCode(ccy);
		if (currency == null) {
			throw new RuntimeException("Currency Unavailable");
		}
		Double fexchangerate = currency.getFexchangerate();
		return fexchangerate;
	}

	@Cacheable(value="exchangeCacheManager",keyGenerator = "customKeyGenerator", cacheManager = "exchangeCacheManager")
	@Override
	public Double exchange(String originalCCY, String targetCCY) {
		if (originalCCY.equals(targetCCY)) {
			return 1D;
		}
		double temp =getRate(targetCCY) / getRate(originalCCY);
		return temp;
	}

	@Cacheable(value="allShippingCodeName",keyGenerator = "customKeyGenerator", cacheManager = "exchangeCacheManager")
	@Override
	public List<ShippingDisplayNamePo> getAllshippingCodeName() {
		return shippingTemplateMapper.getAllshippingCodeName();
	}

	@Cacheable(value="allShippingCodeName",keyGenerator = "customKeyGenerator", cacheManager = "exchangeCacheManager")
	@Override
	public List<ShippingTypePo> getAllTemplateType() {
		return shippingTemplateMapper.getAllTemplateType();
	}
	@Cacheable(value="systemParams",keyGenerator = "customKeyGenerator", cacheManager = "exchangeCacheManager")
	@Override
	public Map<String,String> getSystemParams() {
		Map<String,String> map=Maps.newHashMap();
		try{
			//?lang=1&client=1&type=FILTER
			String doGet = HttpClientUtil.doGet(systemParamsUrl+"?type="+Cons.C_ERROR_CODE_DESCRIPTION);
			if(StringUtils.isNotEmpty(doGet)){
				JSONObject parseObject = JSONObject.parseObject(doGet);
				Object obj=parseObject.get("ret");
				Object data=parseObject.get("data");
				if(obj!=null && data!=null){
					Integer object = Integer.parseInt(obj.toString());
					if(object!=null && object>0){
						List<SystemParamsPo> list= JSONArray.parseArray(data.toString(), SystemParamsPo.class);
						if(CollectionUtils.isNotEmpty(list)){
							for(SystemParamsPo systemParamsPo : list){
								if(systemParamsPo.getLanguageId()!=null && systemParamsPo.getName()!=null){
									map.put( systemParamsPo.getName(), systemParamsPo.getValue());
								}
							}
						}
					}
				}
			}
		}catch(Exception e){
			
		}
		return map;
	}

}
