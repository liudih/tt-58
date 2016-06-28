package com.tomtop.handlers;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.eventbus.Subscribe;
import com.tomtop.dto.order.OrderCurrencyRate;
import com.tomtop.events.order.OrderConfirmationEvent;
import com.tomtop.mappers.order.OrderCurrencyRateMapper;
import com.tomtop.services.ICurrencyService;

@Service
public class OrderConfirmationEventHandler implements IEventHandler {
	private static Logger logger = LoggerFactory
			.getLogger(OrderConfirmationEventHandler.class);
	@Autowired
	private ICurrencyService currencyService;

	@Autowired
	private OrderCurrencyRateMapper rateService;

	@Subscribe
	public void saveOrderCurrencyRate(OrderConfirmationEvent event) {
		if (event != null) {
			String currency = event.getCurrency();
			if (StringUtils.isEmpty(currency)) {
				logger.error("currency is null,so ignore event. orderNum is {}",event.getOrderNum());
				return;
			}
			double frate = currencyService.getRate(currency);
			OrderCurrencyRate rate = new OrderCurrencyRate();
			rate.setCcurrency(currency);
			rate.setCordernumber(event.getOrderNum());
			rate.setFrate(frate);
			rateService.insert(rate);
		}
	}

}
