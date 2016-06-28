package com.tomtop.controllers.member;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tomtop.dto.ISOCountry;
import com.tomtop.dto.Province;
import com.tomtop.services.impl.base.ProvinceService;
import com.tomtop.utils.FoundationService;

@Controller
@RequestMapping("/country")
public class CountryController {
	
	@Autowired
	FoundationService foundationService;
	
	@Autowired
	ProvinceService provinceService;

	@RequestMapping()
	@ResponseBody
	public Object getAllCountries() {
		List<ISOCountry> clist = foundationService.getAllCountries();
		return clist;
	}
	
	@RequestMapping(value = "/allprovince", method = RequestMethod.POST)
	@ResponseBody
	public Object getAllProvinceByCountryId(@RequestParam("countryid") Integer countryid) {
		List<Province> provinces = provinceService
				.getProvincesByCountryId(countryid);
		return provinces;
	}
}
