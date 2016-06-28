package com.tomtop.member.service;

import java.util.List;

import com.tomtop.member.models.filter.AddressFilter;
import com.tomtop.member.models.other.MemberAddress;

/**
 * 账单地址
 * @author xiaoch
 *
 */
public interface IBillAddressService {

	public List<MemberAddress> getBillingAddressByPage(AddressFilter address,
			Integer pageIndex, Integer recordPerPage);

	public Integer getOrderAddressCountByEmail(AddressFilter address);

	public boolean updateBillAddress(AddressFilter MemberAddressBo);

	public boolean setDefaultAddress(AddressFilter address);

	public boolean addNewAddress(AddressFilter MemberAddressBo);

	public List<MemberAddress> getOrderAddressByEmail(AddressFilter address);

	public MemberAddress getMemberAddressById(Integer shipaddressid);
	
	public boolean deleteByIds(List<AddressFilter> ids);
	
	/**
	 * 清空所有默认bill地址
	 * @param id
	 * @param email
	 * @return
	 */
	public boolean clearAllDefaultBillAddress(AddressFilter address);

}