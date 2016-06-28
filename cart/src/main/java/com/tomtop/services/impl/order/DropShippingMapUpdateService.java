package com.tomtop.services.impl.order;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tomtop.dao.order.IDropShippingMapUpdateDao;
import com.tomtop.dto.order.DropShippingMap;

@Service
public class DropShippingMapUpdateService {
	@Autowired
	private IDropShippingMapUpdateDao updateDao;

	public boolean batchInsert(List<DropShippingMap> list) {
		if (list == null || list.isEmpty()) {
			return true;
		}
		int i = updateDao.batchInsert(list);
		return i == list.size() ? true : false;
	}

	public boolean updateOrderNumber(String orderNumber, int dsOrderID) {
		int i = updateDao.updateOrderNumber(orderNumber, dsOrderID);
		return i == 1 ? true : false;
	}
}
