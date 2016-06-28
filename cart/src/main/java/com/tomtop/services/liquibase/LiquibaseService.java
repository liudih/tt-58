package com.tomtop.services.liquibase;

import java.sql.Connection;
import java.util.Map;
import java.util.Set;

import javax.sql.DataSource;

import liquibase.Liquibase;
import liquibase.database.jvm.JdbcConnection;
import liquibase.resource.ClassLoaderResourceAccessor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Service;

import com.google.common.base.Function;
import com.google.common.collect.Maps;

@Service
public class LiquibaseService {

	Map<String, Liquibase> liquibases;

	Map<String, DataSource> datasources;

	@Autowired
	ConfigurableApplicationContext context;
	
	public Liquibase getLiquibaseInstance(String name) {
		if (liquibases == null) {
			initialize();
		}
		return liquibases.get(name);
	}

	public DataSource getDataSource(String name) {
		if (datasources == null) {
			initialize();
		}
		return datasources.get(name);
	}

	protected synchronized void initialize() {
		datasources = context.getBeansOfType(DataSource.class);
		Set<String> names = datasources.keySet();
		liquibases = Maps.toMap(names, new Function<String, Liquibase>() {
			@Override
			public Liquibase apply(String name) {
				try {
					DataSource ds = datasources.get(name);
					Connection conn = ds.getConnection();
					Liquibase lb = new Liquibase("liquibase/" + name + ".xml",
							new ClassLoaderResourceAccessor(),
							new JdbcConnection(conn));
					return lb;
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
			}
		});
	}

	public Set<String> getNames(){
		if (datasources == null) {
			initialize();
		}
		return this.datasources.keySet();
	}
}
