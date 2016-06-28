package com.tomtop.services.payment.ocean;

import org.springframework.stereotype.Service;

@Service
public class OceanPaymentInstantTransferPaymentProvider extends
		AbstractPaymentProvider {

	@Override
	public String id() {
		return "oceanpayment_InstantTransfer";
	}

	@Override
	public String name() {
		return "InstantTransfer";
	}

	@Override
	public String getPaymentMethod() {
		return "InstantTransfer";
	}

	@Override
	public boolean isNeedExtraInfo() {
		return false;
	}

}
