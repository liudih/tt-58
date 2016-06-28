package com.tomtop.mappers.order;

import java.util.List;

import org.apache.ibatis.annotations.Select;

import com.tomtop.dto.order.ShippingParameter;

public interface ShippingParameterMapper {
	@Select("select * from t_shipping_parameter")
	List<ShippingParameter> getAll();

	@Select("select * from t_shipping_parameter where ckey = #{0}")
	ShippingParameter getByKey(String key);
}
