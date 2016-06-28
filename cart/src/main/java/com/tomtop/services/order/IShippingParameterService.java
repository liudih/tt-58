package com.tomtop.services.order;

import java.util.Map;

import com.tomtop.dto.order.ShippingParameter;

public interface IShippingParameterService {

	public abstract Map<String, String> getAll();

	public abstract ShippingParameter getByKey(String key);

}