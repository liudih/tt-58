package com.tomtop.service;

import com.tomtop.entry.bo.ShippingCalculateLessParam;
import com.tomtop.entry.po.ProductBase;
import com.tomtop.entry.po.ShippingPriceCalculate;


public interface IProductBaseInfoService {
	
	public ShippingPriceCalculate getShippingCalculateBase(ShippingCalculateLessParam ShippingCalculateLessParam);

	
}
