package com.tomtop.member.configuration;


import javax.sql.DataSource;

import liquibase.integration.spring.SpringLiquibase;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.tomtop.member.configuration.global.GlobalConfigSettings;


@Configuration
@EnableConfigurationProperties({ JdbcConnectionSettings.class, GlobalConfigSettings.class })
public class ApplicationConfigurations {

	@Bean
	public SpringLiquibase liquibase(@Qualifier("memberDataSource") DataSource dataSource){
		SpringLiquibase lb = new SpringLiquibase();
		lb.setDataSource(dataSource);
		lb.setChangeLog("classpath:liquibase/mysql/*.xml");
		//contexts specifies the runtime contexts to use.
		return lb;
	}
	
	
}
