package com.tomtop.services.payment.gleepay;

import org.springframework.stereotype.Service;

@Service
public class GleePayPoliPaymentProvider extends GleePayPaymentProvider {

	@Override
	public String id() {
		return "gleepay_poli";
	}

	@Override
	public String name() {
		return "POLI";
	}

	@Override
	public String getPaymentMethod() {
		return "POLI";
	}
	
	@Override
	public boolean isNeedExtraInfo() {
		return false;
	}

}
