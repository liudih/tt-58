package com.tomtop.loyalty.mappers.loyalty;

import org.apache.ibatis.annotations.Select;

import com.tomtop.loyalty.models.CouponCode;
import com.tomtop.loyalty.models.PreferCode;

public interface CodeMapper {

	@Select("select * from t_coupon_code where ccode = #{0}")
	PreferCode getPreferCodeByCouponcode(String code);
	
	@Select("select icouponruleid from t_coupon_code where ccode = #{0}")
	Integer getRuleIdByCode(String code);
	
	int add(CouponCode couponCode);
	
	@Select("select ccode from t_coupon_code where iid = #{0}")
	String getCodeById(Integer id);
}
