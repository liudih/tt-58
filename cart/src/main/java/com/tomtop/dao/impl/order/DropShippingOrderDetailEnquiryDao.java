package com.tomtop.dao.impl.order;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tomtop.dao.order.IDropShippingOrderDetailEnquiryDao;
import com.tomtop.dto.order.DropShippingOrderDetail;
import com.tomtop.mappers.order.DropShippingOrderDetailMapper;

@Service
public class DropShippingOrderDetailEnquiryDao implements
		IDropShippingOrderDetailEnquiryDao {
	@Autowired
	private DropShippingOrderDetailMapper mapper;

	@Override
	public List<DropShippingOrderDetail> getByDropShippingOrderID(Integer id) {
		return mapper.getByDropShippingOrderID(id);
	}

}
