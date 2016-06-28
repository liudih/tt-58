package com.tomtop.loyalty.service;

import com.tomtop.loyalty.models.PreferCode;

public interface IPreferCodeService {
	/**
	 * 根据code获取优惠码对象
	 */
	public PreferCode getPreferCodeByCode(String code);
}
