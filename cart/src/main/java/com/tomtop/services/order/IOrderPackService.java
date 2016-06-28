package com.tomtop.services.order;

import java.util.List;

import com.tomtop.dto.order.OrderPack;
public abstract interface IOrderPackService {
	public abstract boolean saveOrUpdateOrderPack(
			OrderPack paramOrderPack);

	public abstract boolean batchInsertOrderPack(
			List<OrderPack> paramList);

	public abstract boolean checkOrderPack(Integer paramInteger,
			String paramString);

	public abstract boolean insertOrderPack(OrderPack paramOrderPack);

	public abstract List<OrderPack> getByOrderId(Integer paramInteger);

	public abstract OrderPack getOrderPackByOrderIdAndSku(
			Integer paramInteger, String paramString);
	
	public OrderPack getOneByOrderId(Integer orderId);
}