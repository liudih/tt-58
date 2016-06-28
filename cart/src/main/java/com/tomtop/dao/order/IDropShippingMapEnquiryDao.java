package com.tomtop.dao.order;

import java.util.List;

import com.tomtop.dto.order.DropShippingMap;

public interface IDropShippingMapEnquiryDao {
	List<DropShippingMap> getListByDropShippingID(String id);

	List<Integer> getDropShippingOrderIDsByDropShippingID(String id);

	List<String> getOrderNumbersByID(String dropShippingID);
}
