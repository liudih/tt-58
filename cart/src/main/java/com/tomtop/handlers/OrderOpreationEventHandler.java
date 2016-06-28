package com.tomtop.handlers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.eventbus.Subscribe;
import com.tomtop.events.order.OrderOpreationEvent;
import com.tomtop.services.impl.order.OrderOpreationService;
import com.tomtop.utils.FoundationService;
import com.tomtop.valueobjects.base.LoginContext;



/**
 * 订单操作事件处理
 * @author liuchengqiang
 * @Date 2016-06-01
 *
 */
@Service
public class OrderOpreationEventHandler implements IEventHandler {
	
	@Autowired
	private OrderOpreationService service;
	
	@Autowired
	private FoundationService foundationService;

	@Subscribe
	public void insert(OrderOpreationEvent event) {
		int siteId = foundationService.getSiteID();
		LoginContext loginCtx = foundationService.getLoginContext();
		String email = loginCtx != null ? loginCtx.getEmail() : "";
		String vhost = foundationService.getVhost();
		event.setIwebsiteid(siteId);
		event.setCemail(email);
		event.setVhost(vhost);
		//如果已经保存则，不添加
		int count = service.getCount(event);
		if(count > 0){
			return;
		}
		service.insert(event);
	}
}
