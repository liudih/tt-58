package com.tomtop.member.service;

import java.util.List;

import com.tomtop.member.models.bo.AddressBo;
import com.tomtop.member.models.bo.BaseBo;

public interface IAddressService {

	public BaseBo insertAddress(AddressBo addBo);
	
	public BaseBo updateAddress(AddressBo addBo);
	
	public BaseBo deleteAddress(List<Integer> ids,Integer website,String email);
	
	public Integer getAddressCount(String email,Integer website,Integer atype);
	
	public int updateDefaultAddress(Integer id, String email, Integer website,Integer atype);
	
	public List<AddressBo> getAddressList(String email,Integer website,Integer atype,Integer page,Integer size);
	
	public AddressBo getAddress(String email,Integer website,Integer atype,Integer id);
}
