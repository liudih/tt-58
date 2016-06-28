package com.tomtop.controllers;

import java.io.StringWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import liquibase.Liquibase;
import liquibase.exception.LiquibaseException;

import com.google.common.base.Function;
import com.google.common.collect.Maps;
import com.tomtop.services.liquibase.LiquibaseService;

@RestController
@RequestMapping("/liquibase")
public class LiquibaseController {

	private static Logger Logger = LoggerFactory
			.getLogger(LiquibaseController.class);

	private static final String PRODUCTION = "production";

	@Autowired
	LiquibaseService service;

	@RequestMapping
	public Map<String, String> update(@RequestParam Boolean doit)
			throws LiquibaseException {
		// if (!"production".equals(context)) {
		// Map<String, String> map = Maps.newHashMap();
		// map.put("error", "Cannot apply context: " + context);
		// return map;
		// }

		if (!doit) {
			Map<String, String> map = Maps.newHashMap();
			map.put("error", "doit must bu true");
			return map;
		}
		Set<String> names = service.getNames();
		if (names == null || names.size() == 0) {
			Map<String, String> map = Maps.newHashMap();
			map.put("error", "names is null");
			return map;
		}

		DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
		// tag before update, good for rollback
		String lasttag = "last";
		String tag = df.format(new Date());

		Map<String, String> output = Maps.toMap(names,
				new Function<String, String>() {
					@Override
					public String apply(String name) {
						try {
							Liquibase liquibase = service
									.getLiquibaseInstance(name);

							liquibase.tag(lasttag);
							liquibase.update(PRODUCTION);
							liquibase.tag(tag);
							return "OK";
						} catch (LiquibaseException e) {
							Logger.error("Database Change Error", e);
							return e.getMessage();
						}
					}
				});
		return output;

	}

}
