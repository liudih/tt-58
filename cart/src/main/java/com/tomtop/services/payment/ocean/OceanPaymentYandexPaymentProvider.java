package com.tomtop.services.payment.ocean;

import org.springframework.stereotype.Service;

import com.tomtop.forms.PlaceOrderForm;

@Service
public class OceanPaymentYandexPaymentProvider extends AbstractPaymentProvider {

	@Override
	public String id() {
		return "oceanpayment_yandex";
	}

	@Override
	public String name() {
		return "Yandex.Money";
	}

	@Override
	public boolean validForm(PlaceOrderForm form) {
		return true;
	}

	@Override
	public String getPaymentMethod() {
		return "Yandex";
	}

	@Override
	public boolean isNeedExtraInfo() {
		return false;
	}

}
