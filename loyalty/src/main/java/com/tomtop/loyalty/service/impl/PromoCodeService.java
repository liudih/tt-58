package com.tomtop.loyalty.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tomtop.loyalty.mappers.loyalty.PromoCodeMapper;
import com.tomtop.loyalty.models.PromoCode;
import com.tomtop.loyalty.service.IPromoCodeService;

@Service
public class PromoCodeService implements IPromoCodeService {
	@Autowired
	PromoCodeMapper promoCodeMapper;

	@Override
	public PromoCode getPromoCodeByCode(String code, Integer website) {
		if (code == null || code.length() == 0) {
			return null;
		}
		Map<String, Object> paras = new HashMap<String, Object>();
		paras.put("code", code);
		paras.put("website", website);
		PromoCode promoCode = promoCodeMapper.getPromoCodeByCondition(paras);
		return promoCode;
	}

}
