package com.tomtop.handlers;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.google.api.client.http.ByteArrayContent;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpContent;
import com.google.api.client.http.HttpHeaders;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.common.eventbus.Subscribe;
import com.tomtop.dto.order.Order;
import com.tomtop.events.payment.PaymentCompletedEvent;
import com.tomtop.services.impl.order.OrderService;
import com.tomtop.services.impl.order.OrderStatusService;


/**
 * 订单支付完成事件
 * @author liuchengqiang
 * @time 2016年6月4日 上午11:53:40
 */
@Service
public class PaymentCompletedEventHandler implements IEventHandler {

	private static Logger Logger = LoggerFactory
			.getLogger(PaymentCompletedEventHandler.class);

	@Autowired
	OrderService orderService;
	
	@Autowired
	OrderStatusService orderStatusService;

	@Value("${producer.register.eventUrl}")
	String eventUrl;
	
	@Value("${producer.register.eventToken}")
	String eventToken;

	@Subscribe
	public void execute(PaymentCompletedEvent event) {
		Order order = orderService.getOrderByOrderNumber(event.getOrderNum());
		if(order == null || order.getIid() == null){
			Logger.warn("订单不存在,orderNumber={}",event.getOrderNum());
			return;
		}
		//添加订单状态变更历史记录
		try {
			orderStatusService.saveStatusHistory(order.getIid(), order.getIstatus());
		} catch (Exception e) {
			Logger.error("addOrderStatusHistory:{} error",event,e);
		}
		//注册订单完成时间
		try {
			NetHttpTransport transport = new NetHttpTransport();

			JSONObject jsonObj = new JSONObject();
			jsonObj.put("code", "EVENT_CODE_ORDER_COMPLETE");
			JSONObject param = new JSONObject();
			param.put("email", order.getCmemberemail());
			param.put("website", order.getIwebsiteid());
			param.put("currency", order.getCcurrency());
			param.put("orderNumber", order.getCordernumber());
			param.put("money", order.getFgrandtotal());
			jsonObj.put("param", param);
			
			HttpContent content = new ByteArrayContent("application/json", jsonObj
					.toJSONString().getBytes());

			GenericUrl url = new GenericUrl(eventUrl);

			HttpRequest request = transport.createRequestFactory()
					.buildPostRequest(url, content);
			HttpHeaders headers = new HttpHeaders();
			headers.set("token", eventToken);
			request.setHeaders(headers);
			request.execute();

		} catch (IOException e) {
			Logger.error("send PaymentCompletedEvent to tomtop error", e);
		}
	}
}
