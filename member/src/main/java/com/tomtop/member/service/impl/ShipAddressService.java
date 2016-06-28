package com.tomtop.member.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tomtop.member.mappers.user.MemberAddressMapper;
import com.tomtop.member.models.filter.AddressFilter;
import com.tomtop.member.models.other.MemberAddress;
import com.tomtop.member.service.IShipAddressService;

@Service
public class ShipAddressService implements IShipAddressService {

	@Autowired
	MemberAddressMapper memberAddressMapper;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * services.member.address.IAddressService#getBillingAddressByPage(java.
	 * lang.String, java.lang.Integer, java.lang.Integer)
	 */
	public List<MemberAddress> getShipAddressByPage(AddressFilter address,
			Integer pageIndex, Integer recordPerPage) {
		return memberAddressMapper.getShipAddressByPage(address, pageIndex,
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
	public Integer getShipAddressCountByEmail(AddressFilter address) {
		return memberAddressMapper.getShipAddressCountByEmail(address);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * services.member.address.IAddressService#updateMemberShippingAddress(dto
	 * .member.MemberAddressBo)
	 */
	public boolean updateMemberShippingAddress(AddressFilter address) {
		if (null != address.getBdefault()
				&& true == address.getBdefault()) {
			this.setDefaultAddress(address);
		}
		int result = memberAddressMapper.update(address);
		return result > 0 ? true : false;
	}


	/*
	 * (non-Javadoc)
	 * 
	 * @see services.member.address.IAddressService#addNewAddress(dto.member.
	 * MemberAddressBo)
	 */
	public boolean add(AddressFilter address) {

		int result = memberAddressMapper.insert(address);

		return result > 0 ? true : false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * services.member.address.IAddressService#getOrderAddressByEmail(java.lang
	 * .String)
	 */
	public List<MemberAddress> getShipAddressByEmail(AddressFilter address) {
		return memberAddressMapper.getShipAddressByEmail(address);
	}

	@Override
	public boolean setDefaultAddress(AddressFilter address) {
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("id", address.getIid());
			map.put("email", address.getCmemberemail());
			map.put("website", address.getClient());
			memberAddressMapper.setDefaultAddress(map);
			memberAddressMapper.clearAllDefaultShipAddress(map);
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
	public boolean clearAllDefaultShipAddress(AddressFilter address) {

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("email", address.getCmemberemail());
		map.put("website", address.getClient());
		int result = memberAddressMapper.clearAllDefaultShipAddress(map);
		return result > 0 ? true : false;
	}

}
