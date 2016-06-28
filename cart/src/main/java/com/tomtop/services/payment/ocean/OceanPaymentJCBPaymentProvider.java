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
public class OceanPaymentJCBPaymentProvider extends AbstractPaymentProvider {

	private static final Logger Logger = LoggerFactory
			.getLogger(OceanPaymentJCBPaymentProvider.class);

	@Autowired
	ICountryService countryService;

	@Autowired
	IAddressService addressService;

	@Override
	public String id() {
		return "oceanpayment_jcb";
	}

	@Override
	public String name() {
		return "JCB";
	}

	@Override
	public Map<String, String> getExtraParas(Order order, PlaceOrderForm f) {
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
			Logger.debug("BillAddressId is null");
			return false;
		}
		return true;
	}

	@Override
	public String getPaymentMethod() {
		return "Credit Card";
	}

}
