package com.tomtop.member.controllers;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.tomtop.framework.core.utils.Page;
import com.tomtop.framework.core.utils.Result;
import com.tomtop.member.models.bo.MemberUidBo;
import com.tomtop.member.models.filter.AddressFilter;
import com.tomtop.member.models.other.MemberAddress;
import com.tomtop.member.service.IMemberService;
import com.tomtop.member.service.IShipAddressService;
import com.tomtop.member.utils.CommonUtils;

/**
 * 收货地址
 * 
 * @author xiaoch
 *
 */
@RestController
@RequestMapping(value = "/member")
public class ShipAddressController {

	@Autowired
	IMemberService memberService;

	@Autowired
	IShipAddressService addressService;

	final static int SHIPPING_ADDRESS_TYPE = 1;

	/**
	 * 添加收货地址
	 * 
	 */
	@ResponseBody
	@RequestMapping(method = RequestMethod.POST, value = "/v1/shipaddress/{uuid}")
	public Result add(
			@PathVariable("uuid") String uuid,
			@RequestParam(value = "website", required = false, defaultValue = "10") Integer website,
			@RequestBody AddressFilter shipAddressVo) {
		Result res = new Result();
		// 权限验证
		String email = "";
		MemberUidBo member = memberService.getMemberEmailByUUid(uuid, website);
		Integer re = member.getRes();
		if (re == CommonUtils.SUCCESS_RES) {
			email = member.getEmail();
		} else {
			String msg = member.getMsg();
			res.setRet(CommonUtils.ERROR_RES);
			res.setErrCode(re.toString());
			res.setErrMsg(msg);
			return res;
		}
		// 添加地址
		shipAddressVo.setCmemberemail(email);
		shipAddressVo.setIaddressid(SHIPPING_ADDRESS_TYPE);
		List<MemberAddress> shipaddresscount = addressService
				.getShipAddressByEmail(shipAddressVo);

		// 判断当前有多少地址
		if (CollectionUtils.isEmpty(shipaddresscount)) {
			shipAddressVo.setBdefault(true);
		} else {
			shipAddressVo
					.setBdefault(null == shipAddressVo.getBdefault() ? false
							: shipAddressVo.getBdefault());
			if (true == shipAddressVo.getBdefault()) {
				addressService.clearAllDefaultShipAddress(shipAddressVo);
			}
		}

		boolean saveOrderAddressResult = addressService.add(shipAddressVo);

		if (saveOrderAddressResult == false) {
			res.setRet(CommonUtils.ERROR_RES);
			res.setErrMsg("server_error");
			return res;
		}
		res.setRet(CommonUtils.SUCCESS_RES);
		return res;
	}

	/**
	 * 更新收货地址
	 * 
	 */
	@ResponseBody
	@RequestMapping(method = RequestMethod.PUT, value = "/v1/shipaddress/{id}/{uuid}")
	public Result update(
			@PathVariable("id") Integer id,
			@PathVariable("uuid") String uuid,
			@RequestParam(value = "website", required = false, defaultValue = "10") Integer website,
			@RequestBody AddressFilter shipAddressVo) {
		Result res = new Result();
		// 权限验证
		String email = "";
		MemberUidBo member = memberService.getMemberEmailByUUid(uuid, website);
		Integer re = member.getRes();
		if (re == CommonUtils.SUCCESS_RES) {
			email = member.getEmail();
		} else {
			String msg = member.getMsg();
			res.setRet(CommonUtils.ERROR_RES);
			res.setErrCode(re.toString());
			res.setErrMsg(msg);
			return res;
		}

		// 更新地址
		MemberAddress orderAddress = addressService.getMemberAddressById(id);
		if (null != orderAddress
				&& email.equals(orderAddress.getCmemberemail())) {
			shipAddressVo.setIid(id);
			shipAddressVo.setIaddressid(SHIPPING_ADDRESS_TYPE);
			boolean result = addressService
					.updateMemberShippingAddress(shipAddressVo);
			if (result == false) {
				res.setRet(CommonUtils.ERROR_RES);
				res.setErrMsg("server_error");
				return res;
			}
		} else {
			res.setRet(CommonUtils.ERROR_RES);
			res.setErrMsg("No Permission");
			return res;
		}
		res.setRet(CommonUtils.SUCCESS_RES);
		return res;
	}

	/**
	 * 批量删除收货地址
	 * 
	 */
	@ResponseBody
	@RequestMapping(method = RequestMethod.POST, value = "/v1/shipaddress/del/{uuid}")
	public Result delete(
			@PathVariable("uuid") String uuid,
			@RequestParam(value = "website", required = false, defaultValue = "10") Integer website,
			@RequestBody List<AddressFilter> id) {
		Result res = new Result();
		// 权限验证
		String email = "";
		MemberUidBo member = memberService.getMemberEmailByUUid(uuid, website);
		Integer re = member.getRes();
		if (re == CommonUtils.SUCCESS_RES) {
			email = member.getEmail();
		} else {
			String msg = member.getMsg();
			res.setRet(CommonUtils.ERROR_RES);
			res.setErrCode(re.toString());
			res.setErrMsg(msg);
			return res;
		}
		if (CollectionUtils.isNotEmpty(id)) {
			for (int i = 0; i < id.size(); i++) {
				MemberAddress address = addressService.getMemberAddressById(id
						.get(i).getIid());
				if (null == address
						|| (!email.equals(address.getCmemberemail()))) {
					res.setRet(CommonUtils.ERROR_RES);
					res.setErrMsg("No Permission");
					return res;
				}
			}
		}

		boolean result = addressService.deleteByIds(id);
		if (result) {
			res.setRet(CommonUtils.SUCCESS_RES);
			return res;
		} else {
			res.setRet(CommonUtils.ERROR_RES);
			return res;
		}
	}

	/**
	 * 设置默认收货地址
	 *
	 */
	@ResponseBody
	@RequestMapping(method = RequestMethod.PUT, value = "/v1/shipaddress/default/{id}/{uuid}")
	public Result setDefault(
			@PathVariable("uuid") String uuid,
			@PathVariable(value = "id") Integer id,
			@RequestParam(value = "website", required = false, defaultValue = "10") Integer website) {
		Result res = new Result();
		// 权限验证
		String email = "";
		MemberUidBo member = memberService.getMemberEmailByUUid(uuid, website);
		Integer re = member.getRes();
		if (re == CommonUtils.SUCCESS_RES) {
			email = member.getEmail();
		} else {
			String msg = member.getMsg();
			res.setRet(CommonUtils.ERROR_RES);
			res.setErrCode(re.toString());
			res.setErrMsg(msg);
			return res;
		}
		MemberAddress address = addressService.getMemberAddressById(id);
		AddressFilter addressFilter = new AddressFilter();
		addressFilter.setIid(id);
		addressFilter.setCmemberemail(email);
		if (null != address && address.getCmemberemail().equals(email)) {
			boolean result = addressService.setDefaultAddress(addressFilter);
			if (true == result) {
				res.setRet(CommonUtils.SUCCESS_RES);
				return res;
			} else {
				return res;
			}
		} else {
			res.setRet(CommonUtils.ERROR_RES);
			return res;
		}
	}

	/**
	 * 获取所有收货地址
	 *
	 */
	@ResponseBody
	@RequestMapping(method = RequestMethod.GET, value = "/v1/shipaddress/{uuid}")
	public Result get(
			@PathVariable("uuid") String uuid,
			@RequestParam(value = "website", required = false, defaultValue = "10") Integer website,
			@RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
			@RequestParam(value = "size", required = false, defaultValue = "10") Integer size) {
		Result res = new Result();
		// 权限验证
		String email = "";
		MemberUidBo member = memberService.getMemberEmailByUUid(uuid, website);
		Integer re = member.getRes();
		if (re == CommonUtils.SUCCESS_RES) {
			email = member.getEmail();
		} else {
			String msg = member.getMsg();
			res.setRet(CommonUtils.ERROR_RES);
			res.setErrCode(re.toString());
			res.setErrMsg(msg);
			return res;
		}
		AddressFilter address = new AddressFilter();
		address.setCmemberemail(email);
		address.setClient(website);
		List<MemberAddress> addressList = addressService.getShipAddressByPage(
				address, page, size);

		Integer total = addressService.getShipAddressCountByEmail(address);
		Page resultpage = new Page();
		resultpage.setCurrentPage(page);
		resultpage.setPageSize(size);
		resultpage.setTotalRecord(total);
		res.setRet(CommonUtils.SUCCESS_RES);
		res.setData(addressList);
		res.setPage(resultpage);
		return res;

	}

	/**
	 * 根据主键id获取账单地址
	 * 
	 */
	@ResponseBody
	@RequestMapping(method = RequestMethod.GET, value = "/v1/shipaddress/{id}/{uuid}")
	public Result getById(
			@PathVariable("id") Integer id,
			@RequestParam(value = "website", required = false, defaultValue = "10") Integer website,
			@PathVariable("uuid") String uuid) {
		Result res = new Result();
		// 用户登录验证
		String email = "";
		MemberUidBo member = memberService.getMemberEmailByUUid(uuid, website);
		Integer re = member.getRes();
		if (re == CommonUtils.SUCCESS_RES) {
			email = member.getEmail();
		} else {
			String msg = member.getMsg();
			res.setRet(CommonUtils.ERROR_RES);
			res.setErrCode(re.toString());
			res.setErrMsg(msg);
			return res;
		}
		MemberAddress orderAddress = addressService.getMemberAddressById(id);
		if (null != orderAddress
				&& email.equals(orderAddress.getCmemberemail())) {
			res.setRet(CommonUtils.SUCCESS_RES);
			res.setData(orderAddress);
			return res;
		} else {
			res.setRet(CommonUtils.ERROR_RES);
			res.setErrMsg("No Permission");
			return res;
		}

	}
}
