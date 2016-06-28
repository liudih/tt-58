package com.tomtop.services.impl.member;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tomtop.controllers.member.MemberController;
import com.tomtop.dto.member.MemberAddress;
import com.tomtop.dto.order.Order;
import com.tomtop.mappers.member.MemberAddressMapper;
import com.tomtop.services.member.IAddressService;

@Service
public class AddressService implements IAddressService {

	private static final Logger Logger = LoggerFactory
			.getLogger(AddressService.class);

	@Autowired
	MemberAddressMapper memberAddressMapper;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * services.member.address.IAddressService#getMemberShippingAddressByEmail
	 * (java.lang.String)
	 */
	public List<MemberAddress> getMemberShippingAddressByEmail(String email, Integer siteId) {
		return memberAddressMapper.getAllShippingAddressByEmail(email, siteId);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * services.member.address.IAddressService#getShippingAddressByPage(java
	 * .lang.String, java.lang.Integer, java.lang.Integer)
	 */
	public List<MemberAddress> getShippingAddressByPage(String email,
			Integer pageIndex, Integer recordPerPage) {
		return memberAddressMapper.getShippingAddressByPage(email, pageIndex,
				recordPerPage);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * services.member.address.IAddressService#getBillingAddressByPage(java.
	 * lang.String, java.lang.Integer, java.lang.Integer)
	 */
	public List<MemberAddress> getBillingAddressByPage(String email,
			Integer pageIndex, Integer recordPerPage) {
		return memberAddressMapper.getBillingAddressByPage(email, pageIndex,
				recordPerPage);
	}
	
	/**
	 * 根据邮件查询所有账单地址
	 * @param email 邮件
	 * @param siteId 站点
	 * @return 地址集合
	 */
	public List<MemberAddress> getAllBillingAddress(String email,
			Integer siteId){
		return memberAddressMapper.getAllBillingAddress(email, siteId);
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
	public Integer getOrderAddressCountByEmail(String email) {
		return memberAddressMapper.getOrderAddressCountByEmail(email);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * services.member.address.IAddressService#deleteAddressById(java.lang.Integer
	 * )
	 */
	public boolean deleteAddressById(Integer id) {
		int result = memberAddressMapper.deleteByPrimaryKey(id);
		return result > 0 ? true : false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * services.member.address.IAddressService#updateMemberShippingAddress(dto
	 * .member.MemberAddress)
	 */
	public boolean updateMemberShippingAddress(MemberAddress memberAddress) {
		memberAddress.setBdefault(true);
		int result = memberAddressMapper
				.updateByPrimaryKeySelective(memberAddress);
		if (null != memberAddress.getBdefault()
				&& true == memberAddress.getBdefault()) {

			memberAddressMapper.setNotDefaultShippingaddress(
					memberAddress.getIid(), memberAddress.getCmemberemail(),
					memberAddress.getIaddressid());
		}
		return result > 0 ? true : false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * services.member.address.IAddressService#setDefaultShippingaddress(java
	 * .lang.Integer, java.lang.String, java.lang.Integer)
	 */
	public boolean setDefaultShippingaddress(Integer id, String email,
			Integer addressType) {
		try {
			memberAddressMapper.setDefaultShippingaddress(id, email,
					addressType);
			memberAddressMapper.setNotDefaultShippingaddress(id, email,
					addressType);
			return true;
		} catch (Exception e) {
			Logger.error("set default shipping address error", e);
			return false;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * services.member.address.IAddressService#getDefaultShippingAddress(java
	 * .lang.String)
	 */
	public MemberAddress getDefaultShippingAddress(String memberEmail) {
		return memberAddressMapper.getDefaultShippingAddress(memberEmail);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see services.member.address.IAddressService#addNewAddress(dto.member.
	 * MemberAddress)
	 */
	public boolean addNewAddress(MemberAddress memberAddress) {

		int result = memberAddressMapper.insertSelective(memberAddress);
		if (memberAddress.getBdefault() != null
				&& true == memberAddress.getBdefault()
				&& memberAddress.getIaddressid() != null) {
			memberAddressMapper.setNotDefaultShippingaddress(
					memberAddress.getIid(), memberAddress.getCmemberemail(),
					memberAddress.getIaddressid());
		}
		return result > 0 ? true : false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * services.member.address.IAddressService#getOrderAddressByEmail(java.lang
	 * .String)
	 */
	public List<MemberAddress> getOrderAddressByEmail(String email) {
		return memberAddressMapper.getOrderAddressByEmail(email);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * services.member.address.IAddressService#getDefaultOrderAddress(java.lang
	 * .String)
	 */
	public MemberAddress getDefaultOrderAddress(String email, Integer siteId) {
		return memberAddressMapper.getDefaultOrderAddress(email,siteId);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * services.member.address.IAddressService#getShippingAddressCountByEmail
	 * (java.lang.String)
	 */
	public Integer getShippingAddressCountByEmail(String email, Integer siteid) {
		return memberAddressMapper.getShippingAddressCountByEmail(email, siteid);
	}

	public Integer getBillAddressCountByEmailAndShipAddressId(String email,
			Integer ishipAddressId) {
		return memberAddressMapper.getBillAddressCountByEmailAndShipAddressId(
				email, ishipAddressId);
	}

	public MemberAddress getBillAddressByEmailAndShipAddressId(String email,
			Integer ishipAddressId) {
		return memberAddressMapper.getBillAddressByEmailAndShipAddressId(email,
				ishipAddressId);
	}

	
	@Override
	public MemberAddress getBillAddrByOrderAddrParam(Order order, Integer icountry) {
		return memberAddressMapper.getBillAddrByOrderAddrParam(order, icountry);
	}
	
}
