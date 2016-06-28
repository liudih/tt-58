package com.tomtop.handlers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.eventbus.Subscribe;
import com.tomtop.entity.payment.paypal.PaymentLogEvent;
import com.tomtop.services.impl.payment.paypal.PaypalService;

/**
 * 记录交易日志
 * 
 * @author lijun
 *
 */
@Service
public class PaymentLogEventHandler implements IEventHandler {

	@Autowired
	PaypalService service;

	@Subscribe
	public void log(PaymentLogEvent payload) throws InterruptedException {
		service.insertLog(payload.getWebsiteId(), payload.getOrderNum(),
				payload.getContent(), payload.getCtransactionId());
	}
}
