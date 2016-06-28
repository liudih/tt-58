package com.tomtop.services.payment.ocean;

import org.springframework.stereotype.Service;

import com.tomtop.forms.PlaceOrderForm;

@Service
public class OceanPaymentDirectpayPaymentProvider extends
		AbstractPaymentProvider {

	@Override
	public String id() {
		return "oceanpayment_directpay";
	}

	@Override
	public String name() {
		return "Directpay";
	}

	@Override
	public String getPaymentMethod() {
		return "Directpay";
	}

	@Override
	public boolean validForm(PlaceOrderForm from) {
		return false;
	}

}
