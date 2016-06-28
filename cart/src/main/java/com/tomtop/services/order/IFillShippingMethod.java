package com.tomtop.services.order;

import java.util.List;

import com.tomtop.dto.order.ShippingMethod;

public interface IFillShippingMethod {

	public abstract List<ShippingMethod> fill(List<ShippingMethod> methods);

}