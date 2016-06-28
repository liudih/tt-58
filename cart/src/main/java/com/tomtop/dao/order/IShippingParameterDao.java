package com.tomtop.dao.order;

import java.util.List;

import com.tomtop.dto.order.ShippingParameter;

public interface IShippingParameterDao {
	List<ShippingParameter> getAll();

	ShippingParameter getByKey(String key);
}
