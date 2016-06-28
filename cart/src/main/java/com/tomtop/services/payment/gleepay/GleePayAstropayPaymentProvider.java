package com.tomtop.services.payment.gleepay;

import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.google.api.client.util.Maps;
import com.tomtop.dto.order.Order;
import com.tomtop.forms.PlaceOrderForm;

@Service
public class GleePayAstropayPaymentProvider extends GleePayPaymentProvider {

	@Override
	public String id() {
		return "gleepay_astropay";
	}

	@Override
	public String name() {
		return "Astropay";
	}

	@Override
	public Map<String, String> getExtraParas(Order order, PlaceOrderForm f) {
		LinkedHashMap<String, String> form = Maps.newLinkedHashMap();

		form.put("pay_userName", f.getQiwiAccount());
		form.put("pay_countryCode", f.getQiwiCountry());
		return form;
	}

	@Override
	public boolean validForm(PlaceOrderForm form) {
		boolean b = true;
		if (StringUtils.isEmpty(form.getQiwiAccount())) {
			Logger.debug("QiwiAccount is null");
			b = false;
		}
		if (StringUtils.isEmpty(form.getQiwiCountry())) {
			Logger.debug("countryCode is null");
			b = false;
		}
		return b;
	}

	@Override
	public String getPaymentMethod() {
		return "Astropay";
	}

}
