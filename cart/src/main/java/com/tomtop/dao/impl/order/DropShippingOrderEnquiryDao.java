package com.tomtop.dao.impl.order;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tomtop.dao.order.IDropShippingOrderEnquiryDao;
import com.tomtop.dto.order.DropShippingOrder;
import com.tomtop.mappers.order.DropShippingOrderMapper;

@Service
public class DropShippingOrderEnquiryDao implements
		IDropShippingOrderEnquiryDao {
	@Autowired
	private DropShippingOrderMapper mapper;

	@Override
	public List<DropShippingOrder> getListByIDs(List<Integer> ids) {
		return mapper.getListByIDs(ids);
	}

}
