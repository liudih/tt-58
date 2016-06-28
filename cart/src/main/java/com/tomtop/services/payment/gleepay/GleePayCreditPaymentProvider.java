package com.tomtop.services.payment.gleepay;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.api.client.util.Maps;
import com.tomtop.dto.Country;
import com.tomtop.dto.member.MemberAddress;
import com.tomtop.dto.order.Order;
import com.tomtop.forms.PlaceOrderForm;
import com.tomtop.services.base.ICountryService;
import com.tomtop.services.member.IAddressService;

@Service
public class GleePayCreditPaymentProvider extends GleePayPaymentProvider {
	
	@Autowired
	private IAddressService addressService;
	
	@Autowired
	ICountryService countryService;

	@Override
	public String id() {
		return "gleepay_credit";
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
		
		// 使用账单信息，覆盖父类参数
		form.put("country", country.getCshortname());
		form.put("state", address.getCprovince());
		form.put("city", address.getCcity());
		form.put("address", address.getCstreetaddress());
		form.put("zip", address.getCpostalcode());
		form.put("phone", address.getCtelephone());
		
		//送货信息（可选）
		form.put("shipFirstName", order.getCfirstname());
		form.put("shipLastName", order.getClastname());
		form.put("shipEmail", order.getCmemberemail());
		form.put("shipPhone", order.getCtelephone());
		form.put("shipCountry", order.getCcountrysn());
		form.put("shipState", order.getCprovince());
		form.put("shipCity", order.getCcity());
		form.put("shipAddress", order.getCstreetaddress());
		form.put("shipZip", order.getCpostalcode());
		return form;
	}

	@Override
	public boolean validForm(PlaceOrderForm form) {
		boolean b = true;
		if (form.getBillAddressId() == null) {
			Logger.debug("billAddressId is null");
			return false;
		}
		return b;
	}

	@Override
	public String getPaymentMethod() {
		return "Credit Card";
	}

}
