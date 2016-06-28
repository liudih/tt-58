package com.tomtop.services.payment.ocean;

import org.springframework.stereotype.Service;

import com.tomtop.forms.PlaceOrderForm;

@Service
public class OceanPaymentTeleingresoPaymentProvider extends
		AbstractPaymentProvider {

	@Override
	public String id() {
		return "oceanpayment_Teleingreso";
	}

	@Override
	public String name() {
		return "Teleingreso";
	}

	@Override
	public boolean validForm(PlaceOrderForm form) {
		return true;
	}

	@Override
	public String getPaymentMethod() {
		return "Teleingreso";
	}

	@Override
	public boolean isNeedExtraInfo() {
		return false;
	}

}
