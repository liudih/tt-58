package com.tomtop.services.impl.order;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tomtop.dao.order.IDropShippingOrderDetailEnquiryDao;
import com.tomtop.dto.order.DropShippingOrderDetail;

@Service
public class DropShippingOrderDetailEnquiryService {
	@Autowired
	private IDropShippingOrderDetailEnquiryDao enquiryDao;

	public List<DropShippingOrderDetail> getByDropShippingOrderID(Integer id) {
		return enquiryDao.getByDropShippingOrderID(id);
	}
}
