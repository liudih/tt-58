package com.tomtop.services.payment.ocean;

import org.springframework.stereotype.Service;

@Service
public class OceanPaymentWebmoneyPaymentProvider extends
		AbstractPaymentProvider {

	@Override
	public String id() {
		return "oceanpayment_webmoney";
	}

	@Override
	public String name() {
		return "WebMoney";
	}

	@Override
	public String getPaymentMethod() {
		return "WebMoney";
	}

	@Override
	public boolean isNeedExtraInfo() {
		return false;
	}

}
