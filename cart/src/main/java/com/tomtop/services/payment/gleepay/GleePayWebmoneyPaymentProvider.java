package com.tomtop.services.payment.gleepay;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.google.api.client.util.Maps;
import com.tomtop.dto.order.Order;
import com.tomtop.forms.PlaceOrderForm;

@Service
public class GleePayWebmoneyPaymentProvider extends GleePayPaymentProvider {

	@Value("${payment.gleepayLocalUrl}")
	String gleepayLocalUrl;
	
	@Override
	public String id() {
		return "gleepay_webmoney";
	}

	@Override
	public String name() {
		return "Webmoney";
	}

	@Override
	public Map<String, String> getExtraParas(Order order, PlaceOrderForm f) {
		LinkedHashMap<String, String> form = Maps.newLinkedHashMap();
		form.put("actionUrl", gleepayLocalUrl);
		return form;
	}

	@Override
	public String getPaymentMethod() {
		return "Webmoney(new)";
	}
	
	@Override
	public boolean isNeedExtraInfo() {
		return false;
	}
}
