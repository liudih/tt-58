package com.tomtop.services.payment.ocean;

import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.google.api.client.util.Maps;
import com.tomtop.dto.order.Order;
import com.tomtop.forms.PlaceOrderForm;

@Service
public class OceanPaymentBoletoPaymentProvider extends AbstractPaymentProvider {
	private static final Logger Logger = LoggerFactory
			.getLogger(OceanPaymentBoletoPaymentProvider.class);

	@Override
	public String id() {
		return "oceanpayment_boleto";
	}

	@Override
	public String name() {
		return "Boleto";
	}

	@Override
	public Map<String, String> getExtraParas(Order order, PlaceOrderForm f) {
		Map<String, String> form = Maps.newLinkedHashMap();

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

		form.put("pay_userName", payUserName.toString());
		form.put("pay_email", order.getCmemberemail());
		form.put("pay_typeCode", f.getPay_typeCode());
		form.put("pay_cpf", f.getPay_cpf());
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
		if (StringUtils.isEmpty(form.getPay_cpf())
				&& "directboleto".equals(form.getPay_typeCode())) {
			Logger.debug("pay_cpf is null");
			b = false;
		}
		return b;
	}

	@Override
	public String getPaymentMethod() {
		return "Ebanx";
	}

}
