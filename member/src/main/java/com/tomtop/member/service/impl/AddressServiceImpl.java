package com.tomtop.member.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tomtop.member.mappers.user.AddressMapper;
import com.tomtop.member.models.bo.AddressBo;
import com.tomtop.member.models.bo.BaseBo;
import com.tomtop.member.service.IAddressService;
import com.tomtop.member.utils.CommonUtils;
/**
 * 用户地址管理业务
 * @author renyy
 *
 */
@Service
public class AddressServiceImpl implements IAddressService {

	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	AddressMapper addressMapper;
	
	/**
	 * 添加地址
	 */
	@Override
	public BaseBo insertAddress(AddressBo addBo) {
		BaseBo bb = new BaseBo();
		int res = 0;
		Integer acount = this.getAddressCount(addBo.getEmail(), addBo.getWebsite(), addBo.getAtype());
		if(acount == null || acount == 0){
			addBo.setIsDef(true);
		}else if(acount >= 5){
			bb.setRes(CommonUtils.MEMBER_ADDRESS_OVER_ERROR);
			bb.setMsg("It can not be more than 5");
			return bb;
		}else{
			if (true == addBo.getIsDef()) {
				//清除默认地址
				res = addressMapper.updateAllNotDefault(addBo.getWebsite(), addBo.getEmail(), addBo.getAtype());
				if(res <= 0){
					logger.error("updateAllNotDefault error [" + addBo.getEmail() + "]-[" +  addBo.getAtype() + "]" );
				}
			}
		}
		res = addressMapper.insertAddress(addBo);
		if(res <= 0){
			logger.error("insertAddress error [" + addBo.getEmail() + "]-[" +  addBo.getAtype() + "]" );
			bb.setRes(CommonUtils.ERROR_RES);
			bb.setMsg("insert address error");
		}else{
			bb.setRes(CommonUtils.SUCCESS_RES);
		}
		bb.setRes(CommonUtils.SUCCESS_RES);
		
		return bb;
	}
	/**
	 * 更新地址
	 */
	@Override
	public BaseBo updateAddress(AddressBo addBo) {
		BaseBo bb = new BaseBo();
		int res = 0;
		if (true == addBo.getIsDef()) {
			//清除默认地址
			res = addressMapper.updateAllNotDefault(addBo.getWebsite(), addBo.getEmail(), addBo.getAtype());
			if(res <= 0){
				logger.error("updateAllNotDefault error [" + addBo.getEmail() + "]-[" +  addBo.getAtype() + "]" );
			}
		}
		res = addressMapper.updateAddress(addBo);
		if(res <= 0){
			logger.error("updateAddress error [" + addBo.getEmail() + "]-[" +  addBo.getAtype() + "]" );
			bb.setRes(CommonUtils.ERROR_RES);
			bb.setMsg("update address error");
		}else{
			bb.setRes(CommonUtils.SUCCESS_RES);
		}
		return bb;
	}
	/**
	 * 删除地址
	 */
	@Override
	public BaseBo deleteAddress(List<Integer> ids,Integer website,String email) {
		BaseBo bb = new BaseBo();
		int res = 0;
		res = addressMapper.deleteAddress(ids, website,email);
		if(res <= 0){
			logger.error("delete address error [" + email + "]-[" +  ids + "]" );
			bb.setRes(CommonUtils.ERROR_RES);
			bb.setMsg("delete address error");
		}else{
			bb.setRes(CommonUtils.SUCCESS_RES);
		}
		return bb;
	}
	
	/**
	 * 设置为默认地址
	 */
	@Override
	public int updateDefaultAddress(Integer id, String email, Integer website,Integer atype) {
		int res = 0;
		res = addressMapper.updateAllNotDefault(website, email, atype);
		if(res <= 0){
			logger.error("updateAllNotDefault error [" + email + "]-[" +  atype + "]" );
		}
		res = addressMapper.updateDefaultAddress(id, email, website, atype);
		if(res <= 0){
			logger.error("updateDefaultAddress error [" + email + "]-[" +  atype + "]" );
		}
		return res;
	}
	
	/**
	 * 获取用户有多少个地址数
	 */
	@Override
	public Integer getAddressCount(String email,Integer website,Integer atype){
		
		return addressMapper.getAddressCount(website, email, atype);
	}
	
	/**
	 * 获取地址列表
	 */
	@Override
	public List<AddressBo> getAddressList(String email, Integer website,Integer atype,
			Integer page, Integer size) {
		return addressMapper.getAddressList(email, website, atype, page, size);
	}
	
	/**
	 * 根据ID获取详细
	 */
	@Override
	public AddressBo getAddress(String email, Integer website, Integer atype,Integer id) {
		return addressMapper.getAddressById(id,email, website, atype);
	}

}
