package com.tomtop.loyalty.configuration;


import javax.sql.DataSource;

import liquibase.integration.spring.SpringLiquibase;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties({ JdbcConnectionSettings.class })
public class ApplicationConfigurations {

	@Bean
	public SpringLiquibase liquibase(@Qualifier("loyaltyDataSource") DataSource dataSource){
		SpringLiquibase lb = new SpringLiquibase();
		lb.setDataSource(dataSource);
		lb.setChangeLog("classpath:liquibase/mysql/*.xml");
		//contexts specifies the runtime contexts to use.
		return lb;
	}
	
}
