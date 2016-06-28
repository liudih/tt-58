package com.tomtop.services.payment.gleepay;

import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.google.api.client.util.Maps;
import com.tomtop.dto.order.Order;
import com.tomtop.forms.PlaceOrderForm;

@Service
public class GleePayIdealPaymentProvider extends GleePayPaymentProvider {

	@Override
	public String id() {
		return "gleepay_ideal";
	}

	@Override
	public String name() {
		return "IDEAL";
	}

	@Override
	public Map<String, String> getExtraParas(Order order, PlaceOrderForm f) {
		LinkedHashMap<String, String> form = Maps.newLinkedHashMap();
		form.put("icIssuer", f.getIcIssuer());
		return form;
	}
	
	@Override
	public boolean validForm(PlaceOrderForm form) {
		boolean b = true;
		if (StringUtils.isEmpty(form.getIcIssuer())) {
			Logger.debug("icIssuer is null");
			b = false;
		}
		return b;
	}

	@Override
	public String getPaymentMethod() {
		return "IDEAL";
	}

}
