package com.tomtop.services;

import com.tomtop.entity.BaseBean;


public interface IWholeSaleProductService {

	public BaseBean addWholeSaleProduct(String email,String sku,Integer qty,Integer siteId);
}
