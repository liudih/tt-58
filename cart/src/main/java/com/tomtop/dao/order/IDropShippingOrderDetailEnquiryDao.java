package com.tomtop.dao.order;

import java.util.List;

import com.tomtop.dto.order.DropShippingOrderDetail;

public interface IDropShippingOrderDetailEnquiryDao {
	List<DropShippingOrderDetail> getByDropShippingOrderID(Integer id);
}
