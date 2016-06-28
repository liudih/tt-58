package com.tomtop.mappers.member;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.tomtop.dto.member.MemberAddress;
import com.tomtop.dto.order.Order;

public interface MemberAddressMapper {
	int deleteByPrimaryKey(Integer iid);

	int insert(MemberAddress record);

	int insertSelective(MemberAddress record);

	MemberAddress selectByPrimaryKey(Integer iid);

	int updateByPrimaryKeySelective(MemberAddress record);

	int updateByPrimaryKey(MemberAddress record);

	@Select("select * from t_member_address where iid = #{id}  limit 1")
	MemberAddress getMemberAddressById(Integer id);

	@Select({"<script>",
			"select * from t_member_address where cmemberemail = #{email} and  iaddressid = 1 ",
			"<if test=\"siteid!=null and siteid==1\" >",
			"and (iwebsiteid=1 or iwebsiteid is null) ",
			"</if>",
			"<if test=\"siteid!=null and siteid!=1\" >",
			"and iwebsiteid=#{siteid} ",
			"</if>",
			"order by bdefault desc,iid desc ",
			"</script>"})
	List<MemberAddress> getAllShippingAddressByEmail(@Param("email") String email,
			@Param("siteid")Integer siteId);

	@Select("select * from t_member_address where cmemberemail = #{0} and  iaddressid = 1 "
			+ "offset (#{1}-1)*#{2} limit #{2}")
	List<MemberAddress> getShippingAddressByPage(String email,
			Integer pageIndex, Integer recordPerPage);

	@Select("select * from t_member_address where cmemberemail = #{0} and iaddressid = 2 "
			+ "offset (#{1}-1)*#{2} limit #{2}")
	List<MemberAddress> getBillingAddressByPage(String email,
			Integer pageIndex, Integer recordPerPage);
	
	/**
	 * 根据邮件查询所有账单地址
	 * @param email 邮件
	 * @param siteId 站点
	 * @return 地址集合
	 */
	@Select({"<script>",
		"select * from t_member_address where cmemberemail = #{email} and  iaddressid = 2 ",
		"<if test=\"siteid!=null and siteid==1\" >",
		"and (iwebsiteid=1 or iwebsiteid is null) ",
		"</if>",
		"<if test=\"siteid!=null and siteid!=1\" >",
		"and iwebsiteid=#{siteid} ",
		"</if>",
		"order by bdefault desc,iid desc ",
		"</script>"})
	List<MemberAddress> getAllBillingAddress(@Param("email") String email,
			@Param("siteid") Integer siteId);

	@Select("select count(iid) from t_member_address where cmemberemail = #{0} and iaddressid = 2")
	int getOrderAddressCountByEmail(String email);

	@Select("update t_member_address set bdefault = true where iid = #{0} and cmemberemail = #{1} and iaddressid =#{2}")
	void setDefaultShippingaddress(Integer id, String email, Integer addressType);

	@Select("update t_member_address set bdefault = false where iid <> #{0} and cmemberemail = #{1} and iaddressid = #{2}")
	void setNotDefaultShippingaddress(Integer id, String email,
			Integer addressType);

	@Select("select * from t_member_address where cmemberemail = #{memberemail} and bdefault = true and iaddressid =1 limit 1")
	MemberAddress getDefaultShippingAddress(String memberEmail);

	int insertBatch(List<MemberAddress> list);

	@Delete({ "<script> delete from t_member_address where cmemberemail  in <foreach item=\"item\" index=\"index\" collection=\"list\" open=\"(\" "
			+ " separator=\",\" close=\")\">#{item}</foreach> </script>" })
	int deleteByEmail(List<String> email);

	@Select("select * from t_member_address where cmemberemail = #{0} and  iaddressid = 2")
	List<MemberAddress> getOrderAddressByEmail(String email);

	@Select("select * from t_member_address where cmemberemail = #{0}"
			+ " and iaddressid=2 and iwebsiteid=#{1} order by  bdefault desc  limit 1")
	MemberAddress getDefaultOrderAddress(String email, Integer siteId);

	@Select({"<script>",
			"select count(iid) from t_member_address where cmemberemail = #{email} and  iaddressid = 1 ",
			"<if test=\"siteid!=null and siteid==1\" >",
			"and (iwebsiteid=1 or iwebsiteid is null) ",
			"</if>",
			"<if test=\"siteid!=null and siteid!=1\" >",
			"and iwebsiteid=#{siteid} ",
			"</if>",
		"</script>"})
	Integer getShippingAddressCountByEmail(@Param("email")String email, @Param("siteid")Integer siteid);

	@Select("select count(iid) from t_member_address where cmemberemail = #{0} and  iaddressid = 2 and ishipaddressid=#{1} ")
	Integer getBillAddressCountByEmailAndShipAddressId(String email,
			Integer ishipAddressId);

	@Select("select * from t_member_address where cmemberemail = #{0} and  iaddressid = 2 and ishipaddressid=#{1} limit 1")
	MemberAddress getBillAddressByEmailAndShipAddressId(String email,
			Integer ishipAddressId);

	/**
	 * 根据订单收货地址信息查询账单地址
	 * @param order 订单信息
	 * @param icountry 国家id
	 * @return
	 * @author shuliangxing
	 * @date 2016年5月19日 上午9:40:27
	 */
	@Select({"<script>",
		"select * from t_member_address where cmemberemail = #{order.cemail} and  iaddressid = 2 ",
		"<if test=\"order.iwebsiteid !=null and order.iwebsiteid == 1\" >",
		"and (iwebsiteid=1 or iwebsiteid is null) ",
		"</if>",
		"<if test=\"order.iwebsiteid!=null and order.iwebsiteid!=1\" >",
		"and iwebsiteid=#{order.iwebsiteid} ",
		"</if>",
		"and cfirstname = #{order.cfirstname}",
		"and clastname = #{order.clastname}",
		"and cstreetaddress = #{order.cstreetaddress}",
		"and ccity = #{order.ccity}",
		"and icountry = #{icountry}",
		"and cprovince = #{order.cprovince}",
		"and cpostalcode = #{order.cpostalcode}",
		"and ctelephone = #{order.ctelephone}",
		"limit 1",
		"</script>"})
	MemberAddress getBillAddrByOrderAddrParam(@Param("order") Order order, @Param("icountry") Integer icountry);
}