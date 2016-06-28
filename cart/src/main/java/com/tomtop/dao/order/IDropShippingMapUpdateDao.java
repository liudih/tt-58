package com.tomtop.dao.order;

import java.util.List;

import com.tomtop.dto.order.DropShippingMap;

public interface IDropShippingMapUpdateDao {
	int batchInsert(List<DropShippingMap> list);

	int updateOrderNumber(String orderNumber, int dsOrderID);
}
