package com.tomtop.services.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tomtop.entity.Currency;
import com.tomtop.entity.Price;
import com.tomtop.entity.ProductPrice;
import com.tomtop.entity.ProductSalePrice;
import com.tomtop.mappers.product.ProductDtlMapper;
import com.tomtop.mappers.product.ProductSalePriceMapper;
import com.tomtop.services.IProductPriceService;
import com.tomtop.utils.ProductComputeUtil;

/**
 * 数据库获取商品价格
 * @author renyy
 *
 */
@Service
public class ProductPriceServiceImpl extends BaseService implements IProductPriceService {

	@Autowired
	ProductSalePriceMapper productSalePriceMapper;
	@Autowired
	ProductDtlMapper productDtlMapper;
	@Autowired
	ProductComputeUtil pcutil;
	/**
	 * 获取单个商品价格
	 * 
	 * @param listingId
	 * @param website
	 * @param currency
	 */
	@Override
	public ProductPrice getProductPrice(String listingId, Integer website,
			String currency) {
		int res = productSalePriceMapper.isOffPrice(listingId);
		Price pp = productDtlMapper.getProductPriceByListingId(listingId, website);
		if(pp != null){
			Currency cbo = this.getCurrencyBean(currency);
			if(res > 0){
				List<ProductSalePrice> psplist = productSalePriceMapper.getAllProductSalePriceByListingId(listingId);
				if(psplist != null && psplist.size() > 0){
					ProductPrice pc = pcutil.getProductPrice(pp.getCostPrice(), pp.getPrice(), psplist, cbo);
					return pc;
				}
			}else{
				ProductPrice pc = pcutil.getProductPrice(pp.getCostPrice(), pp.getPrice(), cbo);
				return pc;
			}
		}
		return null;
	}

}
