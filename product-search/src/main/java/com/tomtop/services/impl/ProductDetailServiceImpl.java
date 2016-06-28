package com.tomtop.services.impl;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.tomtop.entity.ProductExplain;
import com.tomtop.mappers.product.ProductDtlMapper;
import com.tomtop.mappers.product.ProductExplainMapper;
import com.tomtop.services.IProductDetailService;

/**
 * 商品详情业务逻辑类
 * 
 * @author renyy
 *
 */
@Service
public class ProductDetailServiceImpl implements IProductDetailService {

	@Autowired
	ProductDtlMapper productDtlMapper;

	@Autowired
	ProductExplainMapper productExplainMapper;
	
	/**
	 * 获取商品的explanin
	 * 
	 * @param type
	 * 
	 * @param langId
	 * 
	 * @param siteId
	 * 
	 * @return String
	 * @author renyy
	 */
	@Cacheable(value = "product_explain", keyGenerator = "customKeyGenerator")
	@Override
	public String getProductExplainByType(String type, Integer siteId,
			Integer langId) {
		String context = "";
		if(type == null || "".equals(type.trim())){
			return context;
		}
		ProductExplain pedto = productExplainMapper.getProductExplainByType(type, siteId, langId);
		if(pedto == null || pedto.getCcontent() == null){
			return context;
		}
		context = pedto.getCcontent();
		return context;
	}
	
	/**
	 * 获取根据listingId获取库存
	 * 
	 * @param listingId
	 * 
	 * @return Integer
	 * @author renyy
	 */
	@Override
	public Integer getProductQty(String listingId,Integer website) {
		return productDtlMapper.getProductQtyByListingId(listingId,website);
	}

}
