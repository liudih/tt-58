package com.tomtop.services.payment.ocean;

import org.springframework.stereotype.Service;

import com.tomtop.forms.PlaceOrderForm;

@Service
public class OceanPaymentGiropayPaymentProvider extends AbstractPaymentProvider {

	@Override
	public String id() {
		return "oceanpayment_giropay";
	}

	@Override
	public String name() {
		return "Giropay";
	}

	@Override
	public String getPaymentMethod() {
		return "Giropay";
	}

	@Override
	public boolean validForm(PlaceOrderForm from) {
		return false;
	}

	@Override
	public boolean isNeedExtraInfo() {
		return false;
	}

}
