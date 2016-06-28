package com.tomtop.services.payment.gleepay;

import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.google.api.client.util.Maps;
import com.tomtop.dto.order.Order;
import com.tomtop.forms.PlaceOrderForm;

@Service
public class GleePayQiwiPaymentProvider extends GleePayPaymentProvider {
	
	@Value("${payment.gleepayLocalUrl}")
	String gleepayLocalUrl;
	
	@Override
	public String id() {
		return "gleepay_qiwi";
	}

	@Override
	public String name() {
		return "QIWI Кошелек";
	}

	@Override
	public Map<String, String> getExtraParas(Order order, PlaceOrderForm f) {
		LinkedHashMap<String, String> form = Maps.newLinkedHashMap();

		form.put("qiwiUsername", f.getQiwiCountry()+f.getQiwiAccount());
		form.put("actionUrl", gleepayLocalUrl);
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
		return "Qiwi";
	}
}
