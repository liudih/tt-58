package com.tomtop.member.configuration.mybatis;


import java.io.IOException;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.StandardEnvironment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.util.ClassUtils;

import com.jolbox.bonecp.BoneCPDataSource;
import com.tomtop.member.configuration.JdbcConnectionSettings;

/**
 * product数据库配置
 * 
 * @author lijun
 *
 */
@Configuration
@MapperScan(basePackages = "com.tomtop.member.mappers.product", sqlSessionFactoryRef = "productSqlSessionFactory")
public class ProductMybatisConfig {

	private static Logger log = LoggerFactory
			.getLogger(ProductMybatisConfig.class);

	@Autowired
	private JdbcConnectionSettings jdbcConnectionSettings;

	@SuppressWarnings("deprecation")
	@Bean(name = "productDataSource")
	public DataSource productDataSource() {
		BoneCPDataSource ds = new BoneCPDataSource();
		ds.setDriverClass(jdbcConnectionSettings.getDriver());
		ds.setUsername(jdbcConnectionSettings.getProductUsername());
		ds.setPassword(jdbcConnectionSettings.getProductPassword());
		ds.setJdbcUrl(jdbcConnectionSettings.getProductUrl());
		if (jdbcConnectionSettings.getIdleConnectionTestPeriod() != null) {
			ds.setIdleConnectionTestPeriodInMinutes(jdbcConnectionSettings
					.getIdleConnectionTestPeriod());
		}
		if (jdbcConnectionSettings.getIdleMaxAge() != null) {
			ds.setIdleMaxAgeInMinutes(jdbcConnectionSettings.getIdleMaxAge());
		}
		if (jdbcConnectionSettings.getMaxConnectionsPerPartition() != null) {
			ds.setMaxConnectionsPerPartition(jdbcConnectionSettings
					.getMaxConnectionsPerPartition());
		}
		if (jdbcConnectionSettings.getMinConnectionsPerPartition() != null) {
			ds.setMinConnectionsPerPartition(jdbcConnectionSettings
					.getMinConnectionsPerPartition());
		}
		if (jdbcConnectionSettings.getPartitionCount() != null) {
			ds.setPartitionCount(jdbcConnectionSettings.getPartitionCount());
		}
		if (jdbcConnectionSettings.getAcquireIncrement() != null) {
			ds.setAcquireIncrement(jdbcConnectionSettings.getAcquireIncrement());
		}
		if (jdbcConnectionSettings.getStatementsCacheSize() != null) {
			ds.setStatementsCacheSize(jdbcConnectionSettings
					.getStatementsCacheSize());
		}
		if (jdbcConnectionSettings.getReleaseHelperThreads() != null) {
			ds.setReleaseHelperThreads(jdbcConnectionSettings
					.getReleaseHelperThreads());
		}
		return ds;
	}

	public Resource[] getResource(String basePackage, String pattern)
			throws IOException {
		String packageSearchPath = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX
				+ ClassUtils
						.convertClassNameToResourcePath(new StandardEnvironment()
								.resolveRequiredPlaceholders(basePackage))
				+ "/" + pattern;
		Resource[] resources = new PathMatchingResourcePatternResolver()
				.getResources(packageSearchPath);
		return resources;
	}

	@Bean(name = "productSqlSessionFactory")
	public SqlSessionFactory productSqlSessionFactory(
			@Qualifier("productDataSource") DataSource dataSource)
			throws Exception {
		log.debug("> sqlSessionFactory");
		final SqlSessionFactoryBean sqlSessionFactory = new SqlSessionFactoryBean();
		sqlSessionFactory.setDataSource(dataSource);
		sqlSessionFactory.setConfigLocation(new ClassPathResource(
				"mybatis-config.xml"));
		sqlSessionFactory.setFailFast(true);
		sqlSessionFactory.setMapperLocations(getResource("mapper/product",
				"**/*.xml"));
		// sqlSessionFactory.setTypeHandlersPackage("demo.springboot.configuration.mybatis.typehandler");
		return sqlSessionFactory.getObject();
	}

	// @Bean(name = "productSqlSessionTemplate")
	// public SqlSessionTemplate productSqlSessionTemplate(
	// @Qualifier("productSqlSessionFactory") SqlSessionFactory
	// sqlSessionFactory)
	// throws Exception {
	// return new SqlSessionTemplate(sqlSessionFactory);
	// }

	// @Bean
	// public DataSourceTransactionManager transactionManager() {
	// log.debug("> transactionManager");
	// return new DataSourceTransactionManager(dataSource());
	// }

	@PostConstruct
	public void postConstruct() {
		log.info("jdbc.settings={}", jdbcConnectionSettings);
	}
}
