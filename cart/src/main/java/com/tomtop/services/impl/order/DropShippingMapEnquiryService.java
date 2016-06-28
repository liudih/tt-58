package com.tomtop.services.impl.order;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tomtop.dao.order.IDropShippingMapEnquiryDao;
import com.tomtop.dto.order.DropShippingMap;

@Service
public class DropShippingMapEnquiryService {
	@Autowired
	private IDropShippingMapEnquiryDao enquiryDao;

	public List<DropShippingMap> getByDropShippingID(String id) {
		return enquiryDao.getListByDropShippingID(id);
	}

	public List<Integer> getDropShippingOrderIDsByShippingID(String id) {
		return enquiryDao.getDropShippingOrderIDsByDropShippingID(id);
	}

	public List<String> getOrderNumbersByID(String dropShippingID) {
		return enquiryDao.getOrderNumbersByID(dropShippingID);
	}
}
