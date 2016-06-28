package com.tomtop.services.impl.order;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tomtop.dao.order.IDropShippingOrderEnquiryDao;
import com.tomtop.dto.order.DropShippingOrder;

@Service
public class DropShippingOrderEnquiryService {
	@Autowired
	private IDropShippingOrderEnquiryDao enquiryDao;

	public List<DropShippingOrder> getListByIDs(List<Integer> ids) {
		return enquiryDao.getListByIDs(ids);
	}
}
