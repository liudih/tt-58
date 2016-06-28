package com.tomtop.controllers.cart;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tomtop.dto.Country;
import com.tomtop.dto.member.MemberAddress;
import com.tomtop.services.base.ICountryService;
import com.tomtop.services.cart.ICartService;
import com.tomtop.services.member.IAddressService;
import com.tomtop.utils.CookieUtils;
import com.tomtop.utils.FoundationService;
import com.tomtop.valueobjects.base.LoginContext;

@Controller
@RequestMapping("/")
public class BaseController {
	
	@Autowired
	ICartService cartService;
	
	@Autowired
	IAddressService addressService;

	@Autowired
	ICountryService countryService;
	
	@Autowired
	FoundationService foundationService;
	
	/**
	 * 保存仓库id到cookies
	 * @param model
	 * @param storageid
	 * @return
	 */
	@RequestMapping(value = "/setstorageid", method = RequestMethod.GET)
	@ResponseBody
	public Object setCookieStorage(Model model, 
		@RequestParam(value = "storageid", required = false, defaultValue = "1") String storageid) {
		Map<String, Object> mjson = new HashMap<String, Object>();
		CookieUtils.setCookie("storageid", storageid);
		mjson.put("result", "success");
		return mjson;
	}
	
	/**
	 * 校验商品的状态
	 * @param model
	 * @param storageid
	 * @param listingids
	 * @return
	 */
	@RequestMapping(value = "/checkcartitem", method = RequestMethod.GET)
	@ResponseBody
	public Object checkCartItem(Model model, 
		@RequestParam(value = "storageid", required = false, defaultValue = "1") Integer storageid,
		@RequestParam(value = "listingids", required = true) String listingids) {
		
		Map<String, Object> mjson = new HashMap<String, Object>();
		boolean ischeck = cartService.checkCartItem(listingids, storageid);
		
		mjson.put("result", "success");
		mjson.put("ischeck", ischeck);
		return mjson;
	}
	
	@RequestMapping(value = "/showBillAddress", method = RequestMethod.GET)
	@ResponseBody
	public Object showBillAddress(Model model) {
		Map<String, Object> mjson = new HashMap<String, Object>();
		int site  = foundationService.getSiteID();
		LoginContext lc = foundationService.getLoginContext();
		if(!lc.isLogin()){
			mjson.put("result", "no-login");
			return mjson;
		}
		String email = lc.getEmail();
		//显示默认账单地址
		MemberAddress billAddress = addressService.getDefaultOrderAddress(email, site);
		if (billAddress != null && billAddress.getIcountry() != null) {
			Country billcountry = countryService.getCountryByCountryId(billAddress.getIcountry());
			if (billcountry != null) {
				billAddress.setCountryFullName(billcountry.getCname());
				billAddress.setCountryCode(billcountry.getCshortname());
				mjson.put("result", "success");
				mjson.put("billAddress", billAddress);
			}else{
				mjson.put("result", "Country is null");
			}
		}else{
			mjson.put("result", "billAddress is null");
		}
		return mjson;
	}
	
	
}
