package com.tomtop.member.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tomtop.framework.core.utils.Page;
import com.tomtop.framework.core.utils.Result;
import com.tomtop.member.models.bo.AddressBo;
import com.tomtop.member.models.bo.BaseBo;
import com.tomtop.member.service.IAddressService;
import com.tomtop.member.utils.CommonUtils;

/**
 * 用户地址管理
 * 
 * @author renyy
 *
 */
@RestController
@RequestMapping(value = "/member")
public class AddressController {

	@Autowired
	IAddressService addressService;
	/**
	 * 添加账单地址
	 * 
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/v1/address/add")
	public Result add(@RequestBody AddressBo address) {
		Result res = new Result();
		BaseBo bb = addressService.insertAddress(address);
		if(bb.getRes() == CommonUtils.SUCCESS_RES){
			res.setRet(CommonUtils.SUCCESS_RES);
		}else{
			res.setRet(CommonUtils.ERROR_RES);
			res.setErrMsg(bb.getMsg());
		}
		return res;
	}
	
	/**
	 * 更新账单地址
	 * 
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/v1/address/update")
	public Result update(@RequestBody AddressBo address) {
		Result res = new Result();
		BaseBo bb = addressService.updateAddress(address);
		if(bb.getRes() == CommonUtils.SUCCESS_RES){
			res.setRet(CommonUtils.SUCCESS_RES);
		}else{
			res.setRet(CommonUtils.ERROR_RES);
			res.setErrMsg(bb.getMsg());
		}
		return res;
	}
	
	/**
	 * 删除账单地址
	 * 
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/v1/address/delete")
	public Result del(@RequestParam(value = "ids") List<Integer> ids,
			@RequestParam(value = "website", required = false, defaultValue = "1") Integer website,
			@RequestParam(value = "email") String email) {
		Result res = new Result();
		BaseBo bb = addressService.deleteAddress(ids, website, email);
		if(bb.getRes() == CommonUtils.SUCCESS_RES){
			res.setRet(CommonUtils.SUCCESS_RES);
		}else{
			res.setRet(CommonUtils.ERROR_RES);
			res.setErrMsg(bb.getMsg());
		}
		return res;
	}
	
	/**
	 * 设置默认地址
	 * 
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/v1/address/setting")
	public Result setting(@RequestBody AddressBo address) {
		Result res = new Result();
		int re = addressService.updateDefaultAddress(address.getId(), address.getEmail(), address.getWebsite(), address.getAtype());
		if(re >= CommonUtils.SUCCESS_RES){
			res.setRet(CommonUtils.SUCCESS_RES);
		}else{
			res.setRet(CommonUtils.ERROR_RES);
			res.setErrMsg("set error");
		}
		return res;
	}
	
	/**
	 * 获取用户所有账单地址
	 *
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/v1/address/list")
	public Result getList(
			@RequestParam("email") String email,
			@RequestParam("atype") Integer atype,
			@RequestParam(value = "website", required = false, defaultValue = "1") Integer website,
			@RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
			@RequestParam(value = "size", required = false, defaultValue = "10") Integer size) {
		Result res = new Result();
		List<AddressBo> addressList = addressService.getAddressList(email, website, atype, page, size);
		Integer total = addressService.getAddressCount(email, website, atype);
		Page resultpage = Page.getPage(page, total, size);
		res.setRet(CommonUtils.SUCCESS_RES);
		res.setData(addressList);
		res.setPage(resultpage);
		return res;

	}
	
	/**
	 * 获取账单地址详情
	 *
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/v1/address/dtl")
	public Result getById(
			@RequestParam("id") Integer id,
			@RequestParam("email") String email,
			@RequestParam("atype") Integer atype,
			@RequestParam(value = "website", required = false, defaultValue = "1") Integer website) {
		Result res = new Result();
		
		AddressBo address = addressService.getAddress(email, website, atype, id);
		if (null != address) {
			res.setRet(CommonUtils.SUCCESS_RES);
			res.setData(address);
		} else {
			res.setRet(CommonUtils.ERROR_RES);
			res.setErrMsg("No Permission");
		}
		
		return res;

	}
}
