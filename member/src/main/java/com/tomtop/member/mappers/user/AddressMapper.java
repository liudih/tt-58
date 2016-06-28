package com.tomtop.member.mappers.user;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.tomtop.member.models.bo.AddressBo;

public interface AddressMapper {

	Integer insertAddress(AddressBo address);

	Integer updateAddress(AddressBo address);
	
	Integer deleteAddress(@Param("list") List<Integer> ids,@Param("website") Integer website,@Param("email") String email);
	
	//@Update("update t_member_address set bdefault=true,dupdatedate=now() "
	//		+ "where iid=#{0} and cmemberemail=#{1} and iwebsiteid=#{2} and iaddressid=#{3}")
	Integer updateDefaultAddress(@Param("id") Integer id,@Param("email") String email,
			@Param("website") Integer website,@Param("atype") Integer atype);
	
	Integer updateAllNotDefault(@Param("website") Integer website,@Param("email")String email,@Param("atype")Integer atype);
	
	@Select("select count(*) from t_member_address where iwebsiteid=#{0} and cmemberemail=#{1} and iaddressid=#{2}")
	Integer getAddressCount(Integer website,String email,Integer atype);
	
	@Select("select iid as id,cmemberemail email,iaddressid atype,bdefault isDef,cfirstname fname,"
			+ "clastname lname,ccompany company,cstreetaddress street,ccity city,icountry country,"
			+ "cprovince province,cpostalcode postalcode,ctelephone tel,iwebsiteid website "
			+ "from t_member_address where "
			+ "cmemberemail=#{0} and iwebsiteid=#{1} and iaddressid=#{2} order by dupdatedate desc "
			+ "offset (#{3}-1)*#{4} limit #{4}")
	List<AddressBo> getAddressList(String email,Integer website,Integer atype,Integer page,Integer size);
	
	
	@Select("select iid as id,cmemberemail email,iaddressid atype,bdefault isDef,cfirstname fname,"
			+ "clastname lname,ccompany company,cstreetaddress street,ccity city,icountry country,"
			+ "cprovince province,cpostalcode postalcode,ctelephone tel,iwebsiteid website "
			+ "from t_member_address where iid=#{0} and "
			+ "cmemberemail=#{1} and iwebsiteid=#{2} and iaddressid=#{3} ")
	AddressBo getAddressById(Integer id,String email,Integer website,Integer atype);
}
