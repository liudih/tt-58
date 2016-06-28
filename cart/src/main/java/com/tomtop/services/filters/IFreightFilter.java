package com.tomtop.services.filters;

import java.util.List;

import com.tomtop.entity.order.ShippingMethodRequst;
import com.tomtop.valueobjects.order.ShippingMethodInformation;

public interface IFreightFilter {
	/**
	 * 处理过程
	 * 
	 * @param list
	 *            滤后的List<ShippingMethodInformation>
	 * @param requst
	 * @return 返回处理后的List<ShippingMethodInformation>
	 */
	public List<ShippingMethodInformation> processing(
			List<ShippingMethodInformation> list, ShippingMethodRequst requst);

	/**
	 * 处理顺序，越大越后处理
	 * 
	 * @return
	 */
	public int order();
}