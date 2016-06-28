package com.tomtop.services;


import java.util.List;
import java.util.Map;

import com.tomtop.entity.CallProductLableSaleSku;
import com.tomtop.entity.OrderProduct;
import com.tomtop.entity.ReportProductData;

public interface ICallProductService {
	
	public Map<String,CallProductLableSaleSku> getCallProductLableSaleSkuBo(List<String> listingIds,Integer lang,Integer website);
	public Map<String,CallProductLableSaleSku> getCallProductLableSaleSkuBoV2(List<String> listingIds, Integer lang, Integer website,Integer depotId);
    public ReportProductData getReportProductDataVo(Integer categoryId,Integer lang, Integer siteId,String currency,Boolean bmain,Integer page,Integer size,String status,Integer storage);
	public List<OrderProduct> getOrderProducts(List<String> listingIds,Integer lang,Integer website,Integer storage);
}
