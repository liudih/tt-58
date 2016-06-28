package com.tomtop.member.service;

import java.util.List;

import com.tomtop.member.models.filter.AddressFilter;
import com.tomtop.member.models.other.MemberAddress;

/**
 * 账单地址
 * @author xiaoch
 *
 */
public interface IShipAddressService {

	/**
	 * 获取所有ship地址
	 * @param email
	 * @param pageIndex
	 * @param recordPerPage
	 * @return
	 */
	public List<MemberAddress> getShipAddressByPage(AddressFilter address,
			Integer pageIndex, Integer recordPerPage);
	/**
	 * 获取收货地址总数
	 * @param email
	 * @return
	 */
	public Integer getShipAddressCountByEmail(AddressFilter address);

	public boolean updateMemberShippingAddress(AddressFilter address);
	/**
	 * 设置默认ship地址
	 * @param id
	 * @param email
	 * @return
	 */
	public boolean setDefaultAddress(AddressFilter address);

	public boolean add(AddressFilter address);
	/**
	 * 获取用户的所有地址
	 * @param email
	 * @return
	 */
	public List<MemberAddress> getShipAddressByEmail(AddressFilter address);
	/**
	 * 根据主键查询
	 * @param shipaddressid
	 * @return
	 */
	public MemberAddress getMemberAddressById(Integer shipaddressid);
	/**
	 * 根据id集合删除
	 * @param ids
	 * @return
	 */
	public boolean deleteByIds(List<AddressFilter> ids);
	
	/**
	 * 清空所有默认地址
	 * @param id
	 * @param email
	 * @return
	 */
	public boolean clearAllDefaultShipAddress(AddressFilter address);

}