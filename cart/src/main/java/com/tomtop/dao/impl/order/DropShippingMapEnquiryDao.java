package com.tomtop.dao.impl.order;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tomtop.dao.order.IDropShippingMapEnquiryDao;
import com.tomtop.dto.order.DropShippingMap;
import com.tomtop.mappers.order.DropShippingMapMapper;

@Service
public class DropShippingMapEnquiryDao implements IDropShippingMapEnquiryDao {
	@Autowired
	private DropShippingMapMapper mapper;

	@Override
	public List<DropShippingMap> getListByDropShippingID(String id) {
		return mapper.getByDropshippingID(id);
	}

	@Override
	public List<Integer> getDropShippingOrderIDsByDropShippingID(String id) {
		return mapper.getDropShippingOrderIDByDropshippingID(id);
	}

	@Override
	public List<String> getOrderNumbersByID(String dropShippingID) {
		return mapper.getOrderNumbersByID(dropShippingID);
	}

}
