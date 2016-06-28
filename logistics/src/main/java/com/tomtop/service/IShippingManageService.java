package com.tomtop.service;

import java.util.List;

import com.tomtop.entry.bo.ShippingPriceBo;
import com.tomtop.entry.form.ShippingPriceCalculateDto;
import com.tomtop.entry.form.ShippingPriceParamsDto;
import com.tomtop.entry.po.ShippingDisplayNamePo;
import com.tomtop.entry.po.ShippingPriceCalculate;

public interface IShippingManageService {

	public  ShippingPriceBo getShippingPrice(ShippingPriceCalculate shippingPriceCalculate);
	
	public  ShippingPriceBo getShippingPriceSimple(ShippingPriceCalculate shippingPriceCalculate);

	List<ShippingPriceCalculateDto> shippingCalculateService(ShippingPriceParamsDto shippingPriceParamsDto);

	ShippingDisplayNamePo shippingCodeName(String code, Integer languageId);
	
}
