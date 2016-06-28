package com.tomtop.mappers.order;

import java.util.Map;

import com.tomtop.events.order.OrderOpreationEvent;


public interface OrderOpreationMapper {
	int insert(OrderOpreationEvent event);
	int getCount(Map<String, String> param);
}
