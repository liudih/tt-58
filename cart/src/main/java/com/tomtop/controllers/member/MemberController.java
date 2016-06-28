package com.tomtop.controllers.member;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.common.collect.Collections2;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.Lists;
import com.tomtop.dto.Country;
import com.tomtop.dto.Currency;
import com.tomtop.dto.base.Storage;
import com.tomtop.dto.member.MemberAddress;
import com.tomtop.exceptions.MemberAddressException;
import com.tomtop.exceptions.UserNoLoginException;
import com.tomtop.services.ICurrencyService;
import com.tomtop.services.base.ICountryService;
import com.tomtop.services.base.IStorageService;
import com.tomtop.services.cart.ICookieCartService;
import com.tomtop.services.impl.member.AddressService;
import com.tomtop.utils.FoundationService;
import com.tomtop.utils.Result;
import com.tomtop.valueobjects.CartItem;
import com.tomtop.valueobjects.Constants;
import com.tomtop.valueobjects.base.LoginContext;
import com.tomtop.valueobjects.member.ShippingAddressForm;

@Controller
@RequestMapping("/member")
public class MemberController {
	
	@Autowired
	FoundationService foundationService;
	
	@Autowired
	AddressService addressService;
	
	@Autowired
	ICountryService countryService;
	
	@Autowired
	ICookieCartService cookieCartService;
	
	@Autowired
	IStorageService iStorageService;
	
	@Autowired
	ICurrencyService currencyService;
	
	
	Logger logger = Logger.getLogger(MemberController.class);
	
	final static int SHIPPING_ADDRESS_TYPE = 1;

	final static int ORDER_ADDRESS_TYPE = 2;
	
	
	/**
	 * 添加 修改邮寄地址
	 * @param addressForm
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/addshipaddress", method = RequestMethod.POST)
	@ResponseBody
	public Object addShipAddress(ShippingAddressForm addressForm, HttpServletRequest request) {
		Map<String, Object> resultMap = new HashMap<String, Object>();

		// 是否登录
		LoginContext loginCtx = foundationService.getLoginContext();
		boolean isLogin = loginCtx.isLogin();
		if (!isLogin) {
			resultMap.put("result", "no-login");
			return resultMap;
		}
		String email = loginCtx.getEmail();

		MemberAddress shippingAddress = new MemberAddress();
		BeanUtils.copyProperties(addressForm, shippingAddress);
		shippingAddress.setCmemberemail(email);
		shippingAddress.setIwebsiteid(foundationService.getSiteID());
		Integer id = shippingAddress.getIid();
		boolean result;
		Integer siteid = foundationService.getSiteID();
		if (id == null) {
			
			if(shippingAddress.getBdefault()==null){
				Integer sCount = addressService
						.getShippingAddressCountByEmail(email, siteid);
				// 当第一次添加地址时，不管客户是否选择默认，都设置为默认
				if (sCount == 0) {
					shippingAddress.setBdefault(true);
				} else {
					shippingAddress.setBdefault(false);
				}
			}
			
			shippingAddress.setIaddressid(SHIPPING_ADDRESS_TYPE);
			result = addressService.addNewAddress(shippingAddress);
			// 添加bill地址
			MemberAddress billAddress = new MemberAddress();
			BeanUtils.copyProperties(shippingAddress, billAddress);
			billAddress.setIaddressid(ORDER_ADDRESS_TYPE);
			billAddress.setIid(null);
			result = addressService.addNewAddress(billAddress);
		} else {
			MemberAddress md = addressService.getMemberAddressById(id);
			String cemail = md.getCmemberemail();
			if (!email.equals(cemail)) {
				resultMap.put("result", "no the same user!");
				return resultMap;
			}
			result = addressService
					.updateMemberShippingAddress(shippingAddress);
		}

		if (!result) {
			resultMap.put("result", "no-login");
		}else{
			resultMap.put("result", "success");
		}
		resultMap.put("id", shippingAddress.getIid());
		return resultMap;
	}
	
	/**
	 * 设置默认邮寄地址
	 * @param request
	 * @param addressid
	 * @return
	 */
	@RequestMapping(value="/setDefaultShippingaddress", method = RequestMethod.POST)
	@ResponseBody
	public Object setDefaultShippingaddress(HttpServletRequest request, 
			@RequestParam("addressid") Integer addressid) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		// 是否登录
		LoginContext loginCtx = foundationService.getLoginContext();
		if (!loginCtx.isLogin()) {
			resultMap.put("result", "no-login");
			return resultMap;
		}
		MemberAddress address = addressService.getMemberAddressById(addressid);
		if (null != address && address.getCmemberemail().equals(loginCtx.getEmail())) {
			boolean result = addressService.setDefaultShippingaddress(addressid,
					loginCtx.getEmail(), address.getIaddressid());
			if (result) {
				resultMap.put("result", "success");
				return resultMap;
			}
		}
		resultMap.put("result", "error");
		return resultMap;
	}
	
	@RequestMapping(value="/get-sm", method = RequestMethod.GET)
	@ResponseBody
	public Object getShipMethod(@RequestParam("countrycode") String shipToCountryCode, 
			@RequestParam("storageid") Integer storageid) {
		Map<String, Object> mjson = new HashMap<String, Object>();
		if (shipToCountryCode == null || shipToCountryCode.length() == 0) {
			mjson.put("result", "shipToCountryCode is null");
			return mjson;
		}
		// 某些国家被屏蔽
		Country country = countryService
				.getCountryByShortCountryName(shipToCountryCode);
		Boolean isShow = country.getBshow();
		if (isShow != null && !isShow) {
			logger.debug("country:{} is not show,so can not get ship method"+
					shipToCountryCode);
			mjson.put("result", "country:" + shipToCountryCode + " is not show");
			return mjson;
		}
		int site = this.foundationService.getSiteID();
		int lang = this.foundationService.getLanguage();
		String currencyCode = this.foundationService.getCurrency();

		
		List<CartItem> items = cookieCartService.getAllItemsCurrentStorageid(site,
				lang, currencyCode);
		if (items == null || items.size() == 0) {
			mjson.put("result", "Cart items is null");
			return mjson;
		}
		// 过滤仓库id
		Integer stid = storageid;
		items = Lists.newArrayList(Collections2.filter(items,
				c -> c.getStorageID() == stid));
		if (items.size() == 0) {
			mjson.put("result", "Cart items is null");
			return mjson;
		}
		// 转为真实仓
		List<Storage> storagelist = iStorageService.getAllStorages();
		List<Storage> newstoragelist = Lists.newArrayList(Collections2
				.filter(storagelist, c -> c.getIparentstorage() == stid));
		newstoragelist.sort((p1, p2) -> p1.getIid().compareTo(p2.getIid()));
		if (newstoragelist != null && newstoragelist.size() > 0) {
			storageid = newstoragelist.get(0).getIid();
			logger.debug("get real storage -- >"+stid +"--"+ storageid);
			int tstorageid = storageid;
			items = Lists.transform(items, p -> {
				p.setStorageID(tstorageid);
				return p;
			});
		}
		try {
			// 判断所有商品是否是同一个仓库
			Integer firstStorage = items.get(0).getStorageID();

			Currency currency = currencyService.getCurrencyByCode(currencyCode);

//			ShippingMethodInformations fragment = (ShippingMethodInformations) prvider
//					.getFragment(items, shipToCountryCode, firstStorage);
			
			mjson.put("result", "success");
//			mjson.put("html", views.html.order.shipping_method_information
//					.render(fragment, currency).toString());
			return mjson;
		} catch (Exception e) {
			logger.error("ShippingAddress refreshShippingMethod" + e);
		}
		mjson.put("result", "error");
		return mjson;
	}
	
	
	/**
	 * 添加修改账单地址
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/addbilladdress", method = RequestMethod.POST)
	@ResponseBody
	public Object addOrUpdateBillAddress(HttpServletRequest request,
			@RequestParam("shipaddressid") Integer shipaddressid) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		// 是否登录
		LoginContext loginCtx = foundationService.getLoginContext();
		if (!loginCtx.isLogin()) {
			resultMap.put("result", "no-login");
			return resultMap;
		}

		Integer billaddresscount = addressService
				.getBillAddressCountByEmailAndShipAddressId(loginCtx.getEmail(),
						shipaddressid);
		
		if (billaddresscount == 0) {
			MemberAddress orderAddress = addressService
					.getMemberAddressById(shipaddressid);
			orderAddress.setIid(null);
			orderAddress.setIaddressid(ORDER_ADDRESS_TYPE);
			orderAddress.setBdefault(true);
			orderAddress.setIshipAddressId(shipaddressid);
			boolean saveOrderAddressResult = addressService
					.addNewAddress(orderAddress);

			if (saveOrderAddressResult == false) {
				resultMap.put("result", "error");
				return resultMap;
			}
			resultMap.put("result", "success");
			resultMap.put("iid", orderAddress.getIid());
		} else {
			MemberAddress shipAddressClone = addressService
					.getMemberAddressById(shipaddressid);

			MemberAddress billAddress = addressService
					.getBillAddressByEmailAndShipAddressId(loginCtx.getEmail(), shipaddressid);
			
			shipAddressClone.setIid(billAddress.getIid());
			shipAddressClone.setIaddressid(2);
			shipAddressClone.setIshipAddressId(shipaddressid);
			addressService.updateMemberShippingAddress(shipAddressClone);
			resultMap.put("iid", shipAddressClone.getIid());
			resultMap.put("result", "success");
		}
		return resultMap;
	}
	
	/**
	 * 新增地址(包括账单地址和收货地址)
	 * @param address
	 * @param request
	 * @return
	 * @author shuliangxing
	 * @date 2016年5月17日 下午3:32:22
	 */
	@ResponseBody
	@RequestMapping(value="/addAddress", method = RequestMethod.POST)
	public Result addAddress(MemberAddress address, HttpServletRequest request){
		// 是否登录
		LoginContext loginCtx = foundationService.getLoginContext();
		if (!loginCtx.isLogin()) {
			throw new UserNoLoginException();
		}
		
		//访问终端的类型
		//String terminalType = (String)request.getAttribute(Constants.TERMINAL_TYPE);
		//站点id
		Integer siteId = foundationService.getSiteID();
		
		address.setCmemberemail(loginCtx.getEmail());
		address.setBdefault(true);
		address.setIwebsiteid(siteId);
		
		addressService.addNewAddress(address);
		
		return new Result(Result.SUCCESS, null);
	}
	
	/**
	 * 获取用户对应站点下所有账单地址
	 * @param request
	 * @return
	 * @author shuliangxing
	 * @date 2016年5月17日 下午4:11:04
	 */
	@ResponseBody
	@RequestMapping(value="/getAllBillAddr", method = RequestMethod.GET)
	public Result getAllBillAddr(HttpServletRequest request){
		// 是否登录
		LoginContext loginCtx = foundationService.getLoginContext();
		if (!loginCtx.isLogin()) {
			throw new UserNoLoginException();
		}
		
		//访问终端的类型
		//String terminalType = (String)request.getAttribute(Constants.TERMINAL_TYPE);
		//站点id
		Integer siteId = foundationService.getSiteID();
		//账单地址集合
		List<MemberAddress> billAddressList = addressService.getAllBillingAddress(loginCtx.getEmail(), siteId);
		for (MemberAddress billAddr : billAddressList) {
			if (billAddr.getIcountry() != null) {
				Country billcountry = countryService
						.getCountryByCountryId(billAddr.getIcountry());
				if (billcountry == null) {
					throw new MemberAddressException("bill country error");
				}
				billAddr.setCountryFullName(billcountry.getCname());
				billAddr.setCountryCode(billcountry.getCshortname());
			}
		}
		
		return new Result(Result.SUCCESS, billAddressList);
	}
	
	/**
	 * 获取用户默认账单地址
	 * @param request
	 * @return
	 * @author shuliangxing
	 * @date 2016年5月17日 下午4:40:55
	 */
	@ResponseBody
	@RequestMapping(value="/getUserDefaultBillAddr", method = RequestMethod.GET)
	public MemberAddress getUserDefaultBillAddr(HttpServletRequest request){
		int siteId = foundationService.getSiteID();
		// 是否登录
		LoginContext loginCtx = foundationService.getLoginContext();
		if (!loginCtx.isLogin()) {
			throw new UserNoLoginException();
		}
		
		// 用户默认账单地址
		MemberAddress billAddress = addressService
				.getDefaultOrderAddress(loginCtx.getEmail(), siteId);
		if (billAddress.getIcountry() != null) {
			Country billcountry = countryService
					.getCountryByCountryId(billAddress.getIcountry());
			if (billcountry == null) {
				throw new MemberAddressException("bill country error");
			}
			billAddress.setCountryFullName(billcountry.getCname());
			billAddress.setCountryCode(billcountry.getCshortname());
		}
		
		return billAddress;
	}

	
	/**
	 * 添加修改账单地址，不依赖收货地址
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/savebilladdress", method = RequestMethod.POST)
	@ResponseBody
	public Object saveBillAddress(HttpServletRequest request,ShippingAddressForm addressForm) {
		Map<String, Object> resultMap = new HashMap<String, Object>();

		// 是否登录
		LoginContext loginCtx = foundationService.getLoginContext();
		boolean isLogin = loginCtx.isLogin();
		if (!isLogin) {
			resultMap.put("result", "no-login");
			return resultMap;
		}
		String email = loginCtx.getEmail();

		MemberAddress billAddress = new MemberAddress();
		BeanUtils.copyProperties(addressForm, billAddress);
		billAddress.setCmemberemail(email);
		billAddress.setIwebsiteid(foundationService.getSiteID());
		Integer id = billAddress.getIid();
		boolean result;
		if (id == null) {
			billAddress.setBdefault(true);
			billAddress.setIaddressid(ORDER_ADDRESS_TYPE);
			billAddress.setIid(null);
			result = addressService.addNewAddress(billAddress);
		} else {
			MemberAddress md = addressService.getMemberAddressById(id);
			String cemail = md.getCmemberemail();
			if (!email.equals(cemail)) {
				resultMap.put("result", "no the same user!");
				return resultMap;
			}
			result = addressService.updateMemberShippingAddress(billAddress);
		}
		if (!result) {
			resultMap.put("result", "no-login");
		}else{
			resultMap.put("result", "success");
		}
		resultMap.put("id", billAddress.getIid());
		return resultMap;
	}
	
	/**
	 * 查询账单地址
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/billaddresses", method = RequestMethod.GET)
	@ResponseBody
	public Object getBillAddress() {
		Map<String, Object> resultMap = new HashMap<String, Object>();

		// 是否登录
		LoginContext loginCtx = foundationService.getLoginContext();
		boolean isLogin = loginCtx.isLogin();
		if (!isLogin) {
			resultMap.put("result", "no-login");
			return resultMap;
		}
		String email = loginCtx.getEmail();
		List<MemberAddress> orderAddresses = addressService.getOrderAddressByEmail(email);
		FluentIterable.from(orderAddresses).forEach(
			a -> {
				if (a.getIcountry() != null) {
					Country country = countryService
							.getCountryByCountryId(a.getIcountry());
					a.setCountryFullName(country.getCname());
					a.setCountryCode(country.getCshortname());
				}
		});
		resultMap.put("result", "success");
		resultMap.put("addresses", orderAddresses);
		return resultMap;
	}
}
