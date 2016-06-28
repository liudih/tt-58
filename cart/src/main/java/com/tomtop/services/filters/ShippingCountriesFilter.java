package com.tomtop.services.filters;

import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;
import com.tomtop.entity.order.ShippingMethodRequst;
import com.tomtop.services.base.ISystemParameterService;
import com.tomtop.valueobjects.order.ShippingMethodInformation;

/**
 * 国家过滤，有些国家不能寄送
 * 
 * @author Administrator
 *
 */
@Service
public class ShippingCountriesFilter implements IFreightFilter {

	private static final Logger Logger = LoggerFactory
			.getLogger(ShippingCountriesFilter.class);

	@Autowired
	ISystemParameterService iSystemParameterService;

	@Override
	public List<ShippingMethodInformation> processing(
			List<ShippingMethodInformation> list, ShippingMethodRequst requst) {

		String countries = iSystemParameterService.getSystemParameter(
				requst.getWebsiteId(), requst.getLang(),
				"ship_countries_fliter");
		if (null == countries || countries.length() == 0) {
			Logger.error("not set sys paramter name: {}, site: {}, langID: {}",
					"ship_countries_fliter", requst.getWebsiteId(),
					requst.getLang());
			return list;
		}
		Logger.debug("{} <-----shipping----> {} ", countries,
				requst.getCountry());
		countries = countries.toUpperCase();
		List<String> countryList = Arrays.asList(countries.split(","));
		if (countryList.contains(requst.getCountry().toUpperCase())) {
			return Lists.newArrayList();
		}
		return list;
	}

	@Override
	public int order() {
		return 10;
	}

}
