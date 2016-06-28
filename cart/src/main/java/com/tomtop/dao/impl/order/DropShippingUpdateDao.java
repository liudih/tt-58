package com.tomtop.dao.impl.order;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tomtop.dao.order.IDropShippingUpdateDao;
import com.tomtop.dto.order.DropShipping;
import com.tomtop.mappers.order.DropShippingMapper;

@Service
public class DropShippingUpdateDao implements IDropShippingUpdateDao {
	@Autowired
	DropShippingMapper mapper;

	@Override
	public int insert(DropShipping ds) {
		return mapper.insert(ds);
	}

	@Override
	public int updateByID(DropShipping ds) {
		return mapper.updateByID(ds);
	}

	@Override
	public int updateByDropShippingID(DropShipping ds) {
		return mapper.updateByDropShippingID(ds);
	}

	@Override
	public int setUsedByDropShippingID(String dropShippingID, boolean bused) {
		return mapper.setUsedByDropShippingID(dropShippingID, bused);
	}

}
