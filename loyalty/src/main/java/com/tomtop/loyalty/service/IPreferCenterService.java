package com.tomtop.loyalty.service;

import com.tomtop.loyalty.models.OrderCoupon;
import com.tomtop.loyalty.models.filter.PreferFilter;

public interface IPreferCenterService {

	public boolean savePromoOrderPrefer(String email, PreferFilter prefer,
			Integer website);

	public boolean saveCouponOrderPrefer(String email, PreferFilter prefer);

	public boolean savePointsOrderPrefer(String email, PreferFilter prefer,
			Double value, Integer website);

	/**
	 * 为coupon保存关联关系
	 * 
	 * @param cartCoupon
	 * @return
	 */
	public boolean addOrderCoupon(OrderCoupon orderCoupon);

}
