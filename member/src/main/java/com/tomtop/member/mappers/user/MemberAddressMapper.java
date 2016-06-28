package com.tomtop.member.mappers.user;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.tomtop.member.models.filter.AddressFilter;
import com.tomtop.member.models.other.MemberAddress;

public interface MemberAddressMapper {

	int insert(AddressFilter address);

	int update(AddressFilter address);

	@Select("select * from t_member_address where iid = #{id} limit 1")
	MemberAddress getMemberAddressById(Integer id);

	@Select("select * from t_member_address where cmemberemail = #{address.cmemberemail} and iwebsiteid = #{address.client}  and iaddressid = 2 order by dupdatedate desc "
			+ " offset (#{1}-1)*#{2} limit #{2}")
	List<MemberAddress> getBillingAddressByPage(@Param("address")AddressFilter address,
			Integer pageIndex, Integer recordPerPage);

	@Select("select count(iid) from t_member_address where cmemberemail = #{address.cmemberemail} and iwebsiteid = #{address.client}  and iaddressid = 2")
	int getOrderAddressCountByEmail(@Param("address")AddressFilter address);

	//@Select("update t_member_address set bdefault = true where iid = #{0}")
	void setDefaultAddress(Map<String,Object> map);

	//@Select("update t_member_address set bdefault = false where iid <> #{id} and cmemberemail = #{email} and iaddressid = 2")
	void setNotDefaultBilladdress(Map<String,Object> map);

	@Select("select * from t_member_address where cmemberemail = #{address.cmemberemail} and iwebsiteid = #{address.website}  and  iaddressid = 2")
	List<MemberAddress> getOrderAddressByEmail(@Param("address") AddressFilter address);

	int deleteByIds(List<AddressFilter> ids);
	/**
	 * 获取用户搜索ship地址
	 * @param email
	 * @return
	 */
	@Select("select * from t_member_address where cmemberemail = #{cmemberemail} and iwebsiteid = #{client}  and  iaddressid = 1")
	List<MemberAddress> getShipAddressByEmail(AddressFilter address);
	
	/**
	 * 清空所有默认ship地址
	 * @param map
	 */
	int clearAllDefaultShipAddress(Map<String,Object> map);
	/**
	 * 获取所有ship地址
	 * @param email
	 * @param pageIndex
	 * @param recordPerPage
	 * @return
	 */
	@Select("select * from t_member_address where cmemberemail = #{address.cmemberemail} and iwebsiteid = #{address.client}  and iaddressid = 1 order by dupdatedate desc " 
			+ "offset (#{1}-1)*#{2} limit #{2}")
	List<MemberAddress> getShipAddressByPage(@Param("address")AddressFilter address,
			Integer pageIndex, Integer recordPerPage);
	/**
	 * 获取ship地址总数
	 * @param email
	 * @return
	 */
	@Select("select count(iid) from t_member_address where cmemberemail = #{address.cmemberemail} and iwebsiteid = #{address.client}  and iaddressid = 1")
	int getShipAddressCountByEmail(@Param("address")AddressFilter address);
	
	/**
	 * 清空所有默认bill地址
	 * @param map
	 */
	int clearAllDefaultBillAddress(Map<String,Object> map);
}