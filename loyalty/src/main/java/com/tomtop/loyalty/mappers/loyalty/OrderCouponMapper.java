package com.tomtop.loyalty.mappers.loyalty;

import org.apache.ibatis.annotations.Insert;

import com.tomtop.loyalty.models.OrderCoupon;

public interface OrderCouponMapper {

	int insertOrderCoupon(OrderCoupon orderCoupon);
	
	@Insert("INSERT INTO t_order_coupon ( cemail, ccode, istatus, cordernumber) values "
			+ "( #{cemail}, #{ccode}, #{istatus},#{orderNumber})")
	int insert(OrderCoupon coupon);

}
