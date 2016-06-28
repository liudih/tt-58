package com.tomtop.services.payment.ocean;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Maps;
import com.tomtop.dto.Country;
import com.tomtop.dto.member.MemberAddress;
import com.tomtop.dto.order.Order;
import com.tomtop.forms.PlaceOrderForm;
import com.tomtop.services.base.ICountryService;
import com.tomtop.services.member.IAddressService;

@Service
public class OceanPaymentCreditPaymentProvider extends AbstractPaymentProvider {

	private static final Logger Logger = LoggerFactory
			.getLogger(OceanPaymentCreditPaymentProvider.class);

	public final static String credit_3D_payment = "oceanpayment_credit_3D";
	public final static String OCEAN_PAYMENT_CREDIT = "oceanpayment_credit";

	@Autowired
	IAddressService addressService;

	@Autowired
	ICountryService countryService;

	@Override
	public String id() {
		return OCEAN_PAYMENT_CREDIT;
	}

	@Override
	public String name() {
		return "Credit Card";
	}

	@Override
	public Map<String, String> getExtraParas(Order order, PlaceOrderForm f) {
		if(f==null){
			return null;
		}
		Map<String, String> form = Maps.newLinkedHashMap();

		MemberAddress address = addressService.getMemberAddressById(f
				.getBillAddressId());

		Country country = countryService.getCountryByCountryId(address
				.getIcountry());

		form.put("billing_firstName", address.getCfirstname());
		form.put("billing_lastName", address.getClastname());
		form.put("billing_email", address.getCmemberemail());
		form.put("billing_phone", address.getCtelephone());
		form.put("billing_country", country.getCshortname());
		form.put("billing_state", address.getCprovince());
		form.put("billing_city", address.getCcity());
		form.put("billing_address", address.getCstreetaddress());
		form.put("billing_zip", address.getCpostalcode());

		return form;
	}

	@Override
	public boolean validForm(PlaceOrderForm form) {
		if (form.getBillAddressId() == null) {
			Logger.debug("billAddressId is null");
			return false;
		}
		return true;
	}

	@Override
	public String getPaymentMethod() {
		return "Credit Card";
	}

}
