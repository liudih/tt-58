package com.tomtop.loyalty.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tomtop.loyalty.controllers.CouponController;
import com.tomtop.loyalty.models.Prefer;
import com.tomtop.loyalty.models.Product;
import com.tomtop.loyalty.models.bo.RuleBo;
import com.tomtop.loyalty.service.IPreferRuleService;
import com.tomtop.loyalty.service.IPromoCodeService;
import com.tomtop.loyalty.service.IPromoService;

@Service
public class PromoService implements IPromoService {
	@Autowired
	IPreferRuleService ruleService;
	@Autowired
	IPromoCodeService promoCodeService;

	@Override
	public Prefer isPromoAvailable(String code, String email,
			List<Product> products, Integer client, String terminal,
			String currency) {
		Prefer prefer = new Prefer();
		RuleBo ruleBo = ruleService.getRuleBoByPromo(code,client);
		if (null == ruleBo) {
			prefer.setErrMsg("No Rule");
			return prefer;
		}
		prefer = ruleService.isRuleAvailable(ruleBo, products, client,
				terminal, currency);
		prefer.setPreferType(CouponController.LOYALTY_TYPE_PROMO);
		prefer.setCode(code);
		return prefer;
	}

}
