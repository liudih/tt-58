package com.tomtop.loyalty.service;

import com.tomtop.loyalty.models.CouponCode;

public interface ICouponCodeService {
	public CouponCode getCouponCodeByCode(String code);
	
	public int getCodeIdByRuleId(int ruleId, boolean status, int websiteId,
			int creatorId) ;
	
	/**
	 * 根据id查询code
	 * 
	 * @param id
	 * @return
	 */
	public String getCodeById(Integer id); 
	
	
}
