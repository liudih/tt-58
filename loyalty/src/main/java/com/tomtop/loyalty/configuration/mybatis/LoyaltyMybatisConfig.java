package com.tomtop.loyalty.configuration.mybatis;

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
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.StandardEnvironment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.util.ClassUtils;

import com.jolbox.bonecp.BoneCPDataSource;
import com.tomtop.loyalty.configuration.JdbcConnectionSettings;

/**
 * loyalty数据库配置
 * 
 * @author xiaoch
 *
 */
@Configuration
@MapperScan(basePackages = "com.tomtop.loyalty.mappers.loyalty", sqlSessionFactoryRef = "loyaltySqlSessionFactory")
public class LoyaltyMybatisConfig {

	private static Logger log = LoggerFactory
			.getLogger(LoyaltyMybatisConfig.class);

	@Autowired
	private JdbcConnectionSettings jdbcConnectionSettings;

	@SuppressWarnings("deprecation")
	@Primary
	@Bean(name = "loyaltyDataSource")
	public DataSource loyaltyDataSource() {
		BoneCPDataSource ds = new BoneCPDataSource();
		ds.setDriverClass(jdbcConnectionSettings.getDriver());
		ds.setUsername(jdbcConnectionSettings.getLoyaltyUsername());
		ds.setPassword(jdbcConnectionSettings.getLoyaltyPassword());
		ds.setJdbcUrl(jdbcConnectionSettings.getLoyaltyUrl());
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

	@Bean(name = "loyaltySqlSessionFactory")
	public SqlSessionFactory loyaltySqlSessionFactory(
			@Qualifier("loyaltyDataSource") DataSource dataSource)
			throws Exception {
		log.debug("> sqlSessionFactory");
		final SqlSessionFactoryBean sqlSessionFactory = new SqlSessionFactoryBean();
		sqlSessionFactory.setDataSource(dataSource);
		sqlSessionFactory.setConfigLocation(new ClassPathResource(
				"mybatis-config.xml"));
		sqlSessionFactory.setFailFast(true);
		sqlSessionFactory.setMapperLocations(getResource("mapper/loyalty",
				"**/*.xml"));
		return sqlSessionFactory.getObject();
	}

	@PostConstruct
	public void postConstruct() {
		log.info("jdbc.settings={}", jdbcConnectionSettings);
	}
}
