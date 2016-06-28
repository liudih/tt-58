package com.tomtop.services.impl.order;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tomtop.dto.order.OrderPack;
import com.tomtop.mappers.order.OrderPackMapper;
import com.tomtop.services.order.IOrderPackService;

@Service
public class OrderPackService implements IOrderPackService {
	@Autowired
	OrderPackMapper orderPackMapper;
	@Autowired
	OrderEnquiryService orderEnquiryService;
	
	@Override
	public boolean saveOrUpdateOrderPack(OrderPack orderPack) {
		if (orderPack.getIid() != null) {
			return orderPackMapper.updateByPrimaryKeySelective(orderPack) > 0;
		} else {
			return orderPackMapper.insert(orderPack) > 0;
		}
	}

	@Override
	public boolean batchInsertOrderPack(List<OrderPack> orderPacks) {
		List<OrderPack> orderPackList = new ArrayList<OrderPack>();
		for (OrderPack orderPack : orderPacks) {
			boolean checkOrderPack = checkOrderPack(orderPack.getIorderid(),
					orderPack.getCtrackingnumber());
			if (checkOrderPack) {
				continue;
			}
			orderPackList.add(orderPack);
		}
		if (orderPackList.size() <= 0) {
			return true;
		}
		return orderPackMapper.batchInsert(orderPackList) > 0;
	}

	@Override
	public boolean checkOrderPack(Integer orderId, String tackNum) {
		return orderPackMapper.ckeckOrderPackByOrderIdAndTrackNum(orderId,
				tackNum) > 0;
	}


	@Override
	public boolean insertOrderPack(OrderPack orderPack) {
		boolean checkOrderPack = checkOrderPack(orderPack.getIorderid(),
				orderPack.getCtrackingnumber());
		if (checkOrderPack) {
			return true;
		}

		return saveOrUpdateOrderPack(orderPack);
	}

	@Override
	public List<OrderPack> getByOrderId(Integer orderId) {
		return orderPackMapper.getOrderPacksByOrderId(orderId);
	}

	@Override
	public OrderPack getOrderPackByOrderIdAndSku(Integer orderId, String sku) {
		return orderPackMapper.getOrderPackByOrderIdAndSku(orderId, sku);
	}
	
	@Override
	public OrderPack getOneByOrderId(Integer orderId){
		return orderPackMapper.getOneByOrderId(orderId);
	}
}
