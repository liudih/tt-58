package com.tomtop.loyalty.service.impl;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import com.tomtop.loyalty.mappers.loyalty.CodeMapper;
import com.tomtop.loyalty.models.PreferCode;
import com.tomtop.loyalty.service.IPreferCodeService;

@Service
public class PreferCodeService implements IPreferCodeService {
	@Autowired
	CodeMapper codeMapper;

	public PreferCode getPreferCodeByCode(String code) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * 通过code来查询
	 * 
	 * @author xiaoch
	 * @param code
	 * @return maybe return null
	 */
	public PreferCode getPreferCodeByCouponcode(String code) {
		if (code == null || code.length() == 0) {
			return null;
		}
		return codeMapper.getPreferCodeByCouponcode(code);
	}

}
