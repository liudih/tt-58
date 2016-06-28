package com.tomtop.loyalty.service;

import com.tomtop.loyalty.models.PromoCode;

/**
 * 推广码
 * 
 * @author xiaoch
 *
 */
public interface IPromoCodeService {

	/**
	 * 通过code查询数据
	 * 
	 * @param code
	 * @return
	 */
	public PromoCode getPromoCodeByCode(String code, Integer website);

}
