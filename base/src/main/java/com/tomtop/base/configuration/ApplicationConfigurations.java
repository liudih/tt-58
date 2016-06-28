package com.tomtop.base.configuration;

import javax.sql.DataSource;

import liquibase.integration.spring.SpringLiquibase;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

@Configuration
@EnableCaching
@EnableConfigurationProperties({ JdbcConnectionSettings.class,
		RedisConfigSettings.class })
public class ApplicationConfigurations {
	
	@Autowired
	private RedisConfigSettings settings;

	@Bean(name = "cacheManager")
	@Primary
	public CacheManager getCacheManager(
			@SuppressWarnings("rawtypes") @Qualifier("redisTemplate") RedisTemplate redisTemplate) {
		RedisCacheManager cacheManager = new RedisCacheManager(redisTemplate);
		cacheManager.setDefaultExpiration(settings.getExpireTime());
		return cacheManager;
	}

	@Bean(name = "dayCacheManager")
	public CacheManager getDayCacheManager(
			@SuppressWarnings("rawtypes") @Qualifier("redisTemplate") RedisTemplate redisTemplate) {
		RedisCacheManager cacheManager = new RedisCacheManager(redisTemplate);
		cacheManager.setDefaultExpiration(settings.getDailyExpireTime());
		return cacheManager;
	}

	@Bean(name = "baseCacheManager")
	public CacheManager getBaseCacheManager(
			@SuppressWarnings("rawtypes") @Qualifier("redisTemplate") RedisTemplate redisTemplate) {
		RedisCacheManager cacheManager = new RedisCacheManager(redisTemplate);
		cacheManager.setDefaultExpiration(settings.getBaseExpireTime());
		return cacheManager;
	}
	
	@SuppressWarnings("rawtypes")
	@Bean(name = "redisTemplate")
	public RedisTemplate getRedisTemplate(
			@Qualifier("jedisConnectionFactory") JedisConnectionFactory jedisConnectionFactory) {
		RedisTemplate redisTemplate = new RedisTemplate();
		redisTemplate.setConnectionFactory(jedisConnectionFactory);
		return redisTemplate;
	}
	
	@Bean(name = "jedisConnectionFactory")
	public JedisConnectionFactory getJedisConnectionFactory() {
		JedisConnectionFactory factory = new JedisConnectionFactory();
		factory.setHostName(settings.getIp());
		factory.setPassword(settings.getPassword());
		factory.setPort(settings.getPort());
		factory.setDatabase(settings.getDb());
		factory.setTimeout(settings.getTimeout());
		return factory;
	}
	
	@Bean(name = "restTemplate")
	public RestTemplate getRestTemplate() {
		RestTemplate template = new RestTemplate();
		template.getMessageConverters().add(new FormHttpMessageConverter());
		return template;
	}
	
	@Bean
	public SpringLiquibase liquibase(@Qualifier("mysqlDataSource") DataSource dataSource){
		SpringLiquibase lb = new SpringLiquibase();
		lb.setDataSource(dataSource);
		lb.setChangeLog("classpath:liquibase/mysql/*.xml");
		//contexts specifies the runtime contexts to use.
		return lb;
	}
	
}
