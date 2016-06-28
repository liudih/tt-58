package com.tomtop.member.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tomtop.member.mappers.user.MemberAddressMapper;
import com.tomtop.member.models.filter.AddressFilter;
import com.tomtop.member.models.other.MemberAddress;
import com.tomtop.member.service.IBillAddressService;

@Service
public class BillAddressService implements IBillAddressService {

	@Autowired
	MemberAddressMapper memberAddressMapper;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * services.member.address.IAddressService#getBillingAddressByPage(java.
	 * lang.String, java.lang.Integer, java.lang.Integer)
	 */
	public List<MemberAddress> getBillingAddressByPage(AddressFilter address,
			Integer pageIndex, Integer recordPerPage) {
		return memberAddressMapper.getBillingAddressByPage(address, pageIndex,
				recordPerPage);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * services.member.address.IAddressService#getMemberAddressById(java.lang
	 * .Integer)
	 */
	public MemberAddress getMemberAddressById(Integer id) {
		return memberAddressMapper.getMemberAddressById(id);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * services.member.address.IAddressService#getOrderAddressCountByEmail(java
	 * .lang.String)
	 */
	public Integer getOrderAddressCountByEmail(AddressFilter address) {
		return memberAddressMapper.getOrderAddressCountByEmail(address);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * services.member.address.IAddressService#updateMemberShippingAddress(dto
	 * .member.MemberAddressBo)
	 */
	public boolean updateBillAddress(AddressFilter memberAddress) {
		if (null != memberAddress.getBdefault()
				&& true == memberAddress.getBdefault()) {
			this.setDefaultAddress(memberAddress);
		}
		int result = memberAddressMapper.update(memberAddress);
		return result > 0 ? true : false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see services.member.address.IAddressService#addNewAddress(dto.member.
	 * MemberAddressBo)
	 */
	public boolean addNewAddress(AddressFilter MemberAddressBo) {

		int result = memberAddressMapper.insert(MemberAddressBo);

		return result > 0 ? true : false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * services.member.address.IAddressService#getOrderAddressByEmail(java.lang
	 * .String)
	 */
	public List<MemberAddress> getOrderAddressByEmail(AddressFilter address) {
		return memberAddressMapper.getOrderAddressByEmail(address);
	}

	@Override
	public boolean setDefaultAddress(AddressFilter address) {
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("id", address.getIid());
			map.put("email", address.getCmemberemail());
			map.put("website", address.getClient());
			memberAddressMapper.setDefaultAddress(map);
			memberAddressMapper.setNotDefaultBilladdress(map);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	@Override
	public boolean deleteByIds(List<AddressFilter> ids) {
		int result = memberAddressMapper.deleteByIds(ids);
		return result > 0 ? true : false;
	}

	@Override
	public boolean clearAllDefaultBillAddress(AddressFilter address) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("email", address.getCmemberemail());
		map.put("website", address.getClient());
		int result = memberAddressMapper.clearAllDefaultBillAddress(map);
		return result > 0 ? true : false;
	}

}
