package com.tomtop.handlers;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.google.common.eventbus.Subscribe;
import com.tomtop.dto.order.Order;
import com.tomtop.dto.order.OrderDetail;
import com.tomtop.dto.product.InventoryHistory;
import com.tomtop.enums.InventoryTypeEnum;
import com.tomtop.events.order.ReduceQtyEvent;
import com.tomtop.services.impl.product.InventoryUpdateService;
import com.tomtop.services.order.IOrderService;
import com.tomtop.utils.FoundationService;
import com.tomtop.utils.HttpClientUtil;



/**
 * 
 * @author lijun
 *
 */
@Service
public class ReduceQtyEventHandler implements IEventHandler {

	@Autowired
	IOrderService orderService;

	@Autowired
	FoundationService foundationService;

	@Autowired
	InventoryUpdateService inventoryService;
	
	@Value("${api.search.importUrl}")
	String importUrl;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ReduceQtyEventHandler.class);

	@Subscribe
	public void reduce(ReduceQtyEvent envent) {
		String orderNum = envent.getOrderNum();

		Order order = orderService.getOrderByOrderNumber(orderNum);
		if (order == null) {
			return;
		}
		List<OrderDetail> details = orderService
				.getOrderDetailsByOrderNum(orderNum);
		if (details == null) {
			return;
		}

		Integer storageid = order.getIstorageid() == null ? 1 : order.getIstorageid();
		
		details.forEach(d -> {
			InventoryHistory ih = new InventoryHistory();
			ih.setClistingid(d.getClistingid());
			ih.setIwebsiteid(envent.getWebsiteID());
			ih.setIqty(d.getIqty() * -1);
			ih.setCtype(InventoryTypeEnum.SALE.getValue());
			ih.setIstorageid(storageid);
			ih.setCreference("sale for order: " + order.getIid());
			inventoryService.insert(ih);
			//更新搜索引擎
			try{
				HttpClientUtil.doGet(importUrl + d.getClistingid());
			}catch (Exception e) {
				LOGGER.error("call search api error",e);
			}
		});

	}
}
