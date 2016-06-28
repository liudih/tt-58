package com.tomtop.services.impl.order;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Maps;
import com.tomtop.dao.order.IShippingParameterDao;
import com.tomtop.dto.order.ShippingParameter;
import com.tomtop.services.order.IShippingParameterService;

@Service
public class ShippingParameterService implements IShippingParameterService {
	@Autowired
	IShippingParameterDao shippingParameterDao;

	/*
	 * (non-Javadoc)
	 * 
	 * @see services.shipping.IShippingParameterService#getAll()
	 */
	@Override
	public Map<String, String> getAll() {
		List<ShippingParameter> list = shippingParameterDao.getAll();
		return Maps.transformValues(Maps.uniqueIndex(list, e -> e.getCkey()),
				s -> s.getCjsonvalue());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * services.shipping.IShippingParameterService#getByKey(java.lang.String)
	 */
	@Override
	public ShippingParameter getByKey(String key) {
		return shippingParameterDao.getByKey(key);
	}
}
