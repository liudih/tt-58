package com.tomtop.dao.impl.order;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tomtop.dao.order.IShippingParameterDao;
import com.tomtop.dto.order.ShippingParameter;
import com.tomtop.mappers.order.ShippingParameterMapper;

@Service
public class ShippingParameterDao implements IShippingParameterDao {
	@Autowired
	ShippingParameterMapper mapper;

	@Override
	public List<ShippingParameter> getAll() {
		return mapper.getAll();
	}

	@Override
	public ShippingParameter getByKey(String key) {
		return mapper.getByKey(key);
	}

}
