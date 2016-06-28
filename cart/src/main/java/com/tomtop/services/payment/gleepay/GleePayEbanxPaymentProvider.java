package com.tomtop.services.payment.gleepay;

import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.google.api.client.util.Maps;
import com.tomtop.dto.order.Order;
import com.tomtop.forms.PlaceOrderForm;

@Service
public class GleePayEbanxPaymentProvider extends GleePayPaymentProvider {

	@Override
	public String id() {
		return "gleepay_ebanx";
	}

	@Override
	public String name() {
		return "Ebanx";
	}

	@Override
	public Map<String, String> getExtraParas(Order order, PlaceOrderForm f) {
		LinkedHashMap<String, String> form = Maps.newLinkedHashMap();

		StringBuilder payUserName = new StringBuilder();
		if (order.getCfirstname() != null) {
			payUserName.append(order.getCfirstname());
			payUserName.append(" ");
		}

		if (order.getCmiddlename() != null) {
			payUserName.append(order.getCmiddlename());
			payUserName.append(" ");
		}

		if (order.getClastname() != null) {
			payUserName.append(order.getClastname());
		}
		
		form.put("ebanxName", payUserName.toString());
		form.put("ebanxEmail", order.getCmemberemail());
		form.put("ebanxTypeCode", f.getPay_typeCode());
		return form;
	}

	@Override
	public boolean validForm(PlaceOrderForm form) {
		boolean b = true;
		if (StringUtils.isEmpty(form.getUserName())) {
			Logger.debug("userName is null");
			b = false;
		}
		if (StringUtils.isEmpty(form.getUserEmail())) {
			Logger.debug("userEmail is null");
			b = false;
		}
		if (StringUtils.isEmpty(form.getPay_typeCode())) {
			Logger.debug("pay_typeCode is null");
			b = false;
		}
		return b;
	}

	@Override
	public String getPaymentMethod() {
		return "Ebanx";
	}

}
