package com.tomtop.loyalty.mappers.loyalty;

import java.util.Map;

import com.tomtop.loyalty.models.PromoCode;

public interface PromoCodeMapper {
	
	PromoCode getPromoCodeByCondition(Map<String,Object> map);
}
