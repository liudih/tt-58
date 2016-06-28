package com.tomtop.events;

import java.util.Collection;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Service;

import com.google.common.eventbus.EventBus;
import com.tomtop.handlers.IEventHandler;

/**
 * 事件经纪人
 * 
 * @author lijun
 *
 */
@Service
public class EventBroker {
	private static final Logger LOGGER = LoggerFactory
			.getLogger(EventBroker.class);

	EventBus eventBus;

	@Autowired
	ConfigurableApplicationContext context;

	public EventBroker() {
		eventBus = new EventBus();
	}

	@PostConstruct
	public void register() {

		Map<String, IEventHandler> handlers = context
				.getBeansOfType(IEventHandler.class);
		Collection<IEventHandler> values = handlers.values();
		values.forEach(v -> {
			LOGGER.debug("{}", v.getClass());
			eventBus.register(v);
		});

	}

	public void post(Object event) {
		eventBus.post(event);
	}
}
