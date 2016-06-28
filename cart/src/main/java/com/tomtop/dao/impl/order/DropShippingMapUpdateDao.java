package com.tomtop.dao.impl.order;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tomtop.dao.order.IDropShippingMapUpdateDao;
import com.tomtop.dto.order.DropShippingMap;
import com.tomtop.mappers.order.DropShippingMapMapper;

@Service
public class DropShippingMapUpdateDao implements IDropShippingMapUpdateDao {
	@Autowired
	private DropShippingMapMapper mapper;

	@Override
	public int batchInsert(List<DropShippingMap> list) {
		return mapper.batchInsert(list);
	}

	@Override
	public int updateOrderNumber(String orderNumber, int dsOrderID) {
		return mapper.updateOrderID(orderNumber, dsOrderID);
	}

}
