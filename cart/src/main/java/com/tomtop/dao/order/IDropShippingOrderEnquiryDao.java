package com.tomtop.dao.order;

import java.util.List;

import com.tomtop.dto.order.DropShippingOrder;

public interface IDropShippingOrderEnquiryDao {
	List<DropShippingOrder> getListByIDs(List<Integer> ids);
}
