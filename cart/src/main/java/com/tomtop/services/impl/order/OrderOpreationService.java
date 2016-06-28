package com.tomtop.services.impl.order;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tomtop.events.order.OrderOpreationEvent;
import com.tomtop.mappers.order.OrderOpreationMapper;

@Service
public class OrderOpreationService {
	
	@Autowired
	private OrderOpreationMapper orderOpreationMapper;
	
	public void insert(OrderOpreationEvent event){
		orderOpreationMapper.insert(event);
	} 
	
	public int getCount(OrderOpreationEvent event){
		Map<String, String> params = new HashMap<String, String>();
		params.put("copreation", event.getCopreation().name());
		params.put("cordernumber", event.getCordernumber());
		params.put("cresult", event.getCresult().getCode());
		return orderOpreationMapper.getCount(params);
	} 
}
