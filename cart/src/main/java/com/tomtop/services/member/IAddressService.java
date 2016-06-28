package com.tomtop.services.member;

import java.util.List;

import com.tomtop.controllers.member.MemberController;
import com.tomtop.dto.member.MemberAddress;
import com.tomtop.dto.order.Order;

public interface IAddressService {

	public List<MemberAddress> getMemberShippingAddressByEmail(String email, Integer siteId);

	public List<MemberAddress> getShippingAddressByPage(String email,
			Integer pageIndex, Integer recordPerPage);

	public List<MemberAddress> getBillingAddressByPage(String email,
			Integer pageIndex, Integer recordPerPage);
	
	/**
	 * 根据邮件查询所有账单地址
	 * @param email 邮件
	 * @param siteId 站点
	 * @return 地址集合
	 */
	public List<MemberAddress> getAllBillingAddress(String email,
			Integer siteId);
	
	/**
	 * 根据订单收货地址信息查询账单地址
	 * @param order 订单信息
	 * @param icountry 国家id
	 * @return
	 * @author shuliangxing
	 * @date 2016年5月19日 上午9:50:34
	 */
	public MemberAddress getBillAddrByOrderAddrParam(Order order, Integer icountry);
	
	public MemberAddress getMemberAddressById(Integer id);

	public Integer getOrderAddressCountByEmail(String email);

	public boolean deleteAddressById(Integer id);

	public boolean updateMemberShippingAddress(MemberAddress memberAddress);

	public boolean setDefaultShippingaddress(Integer id, String email,
			Integer addressType);

	public MemberAddress getDefaultShippingAddress(String memberEmail);

	public boolean addNewAddress(MemberAddress memberAddress);

	public List<MemberAddress> getOrderAddressByEmail(String email);

	public MemberAddress getDefaultOrderAddress(String email, Integer siteId);

	public Integer getShippingAddressCountByEmail(String email, Integer siteid);

}