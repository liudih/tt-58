package com.tomtop.configuration;


import java.net.InetAddress;

import javax.sql.DataSource;

import liquibase.integration.spring.SpringLiquibase;

import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.embedded.FilterRegistrationBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.context.web.ErrorPageFilter;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import com.alibaba.fastjson.JSONArray;
import com.tomtop.framework.core.utils.Result;

@Configuration
@EnableCaching
@EnableConfigurationProperties({ JdbcConnectionSettings.class,
		RedisConfigSettings.class, EsConfigSettings.class })
public class ApplicationConfigurations {

	private static final Logger logger = LoggerFactory
			.getLogger(ApplicationConfigurations.class);

	@Autowired
	private RedisConfigSettings settings;

	@Autowired
	private EsConfigSettings esSetting;

	/**
	 * 创建一个redis模板
	 * @param jedisConnectionFactory
	 * @return
	 */
	@Bean(name = "redisTemplate")
	public RedisTemplate<Object, Object> getRedisTemplate(
			@Qualifier("jedisConnectionFactory") JedisConnectionFactory jedisConnectionFactory) {
		RedisTemplate<Object, Object> redisTemplate = new RedisTemplate<Object, Object>();
		redisTemplate.setConnectionFactory(jedisConnectionFactory);
		return redisTemplate;
	}
	
	/**
	 * 默认缓存管理
	 * @param redisTemplate
	 * @return
	 */
	@Bean(name = "cacheManager")
	@Primary
	public CacheManager getCacheManager(
			@SuppressWarnings("rawtypes") @Qualifier("redisTemplate") RedisTemplate redisTemplate) {
		RedisCacheManager cacheManager = new RedisCacheManager(redisTemplate);
		cacheManager.setDefaultExpiration(settings.getExpireTime());
		return cacheManager;
	}
	
	/**
	 * 只能主动清缓存,无法自动失效
	 * @param redisTemplate
	 * @return
	 */
	@Bean(name = "noCacheManager")
	public CacheManager getNoCacheManager(
			@SuppressWarnings("rawtypes") @Qualifier("redisTemplate") RedisTemplate redisTemplate) {
		RedisCacheManager cacheManager = new RedisCacheManager(redisTemplate);
		return cacheManager;
	}
	
	@Bean(name = "dayCacheManager")
	public CacheManager getDayCacheManager(
			@SuppressWarnings("rawtypes") @Qualifier("redisTemplate") RedisTemplate redisTemplate) {
		RedisCacheManager cacheManager = new RedisCacheManager(redisTemplate);
		cacheManager.setDefaultExpiration(settings.getDailyExpireTime());
		return cacheManager;
	}
	
	//目前没有使用 保留 by 160613
	@Bean(name = "productPageCacheManager")
	public CacheManager getProductPageCacheManager(
			@SuppressWarnings("rawtypes") @Qualifier("redisTemplate") RedisTemplate redisTemplate) {
		RedisCacheManager cacheManager = new RedisCacheManager(redisTemplate);
		cacheManager.setDefaultExpiration(settings.getProductPageExpireTime());
		return cacheManager;
	}
	//目前没有使用 保留 by 160613
	@Bean(name = "recentOrdersCacheManager")
	public CacheManager getRecentOrdersCacheManager(
			@SuppressWarnings("rawtypes") @Qualifier("redisTemplate") RedisTemplate redisTemplate) {
		RedisCacheManager cacheManager = new RedisCacheManager(redisTemplate);
		cacheManager.setDefaultExpiration(settings
				.getRecentOrdersPageExpireTime());
		return cacheManager;
	}

	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Bean(name = "redisResult")
	public RedisTemplate<Object, Object> getRedisResult(
			@Qualifier("jedisConnectionFactory") JedisConnectionFactory jedisConnectionFactory) {
		RedisTemplate<Object, Object> redisTemplate = new RedisTemplate<Object, Object>();
		redisTemplate.setConnectionFactory(jedisConnectionFactory);
		redisTemplate.setKeySerializer(new StringRedisSerializer());
		redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer(Result.class));
		return redisTemplate;
	}
	
	/**
	 * 只能主动清缓存,无法自动失效
	 * @param redisTemplate
	 * @return
	 */
	@Bean(name = "noCacheResultManager")
	public CacheManager getNoCacheResultManager(
			@SuppressWarnings("rawtypes") @Qualifier("redisResult") RedisTemplate redisTemplate) {
		RedisCacheManager cacheManager = new RedisCacheManager(redisTemplate);
		return cacheManager;
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

	@Bean(name = "productLiquibase")
	@Primary
	public SpringLiquibase liquibase(
			@Qualifier("mysqlProductDataSource") DataSource dataSource) {
		SpringLiquibase lb = new SpringLiquibase();
		lb.setDataSource(dataSource);
		lb.setChangeLog("classpath:liquibase/mysql/*.xml");
		// contexts specifies the runtime contexts to use.
		return lb;
	}
	
	@Bean(name = "productDataLiquibase")
	public SpringLiquibase productLiquibase(
			@Qualifier("productDataSource") DataSource dataSource) {
		SpringLiquibase lb = new SpringLiquibase();
		lb.setDataSource(dataSource);
		lb.setChangeLog("classpath:liquibase/product/*.xml");
		// contexts specifies the runtime contexts to use.
		return lb;
	}
	@Bean(name = "indexClient")
	public TransportClient indexClient() throws Exception {

		Settings settings = Settings
				.settingsBuilder()
				.put("number_of_shards", esSetting.getNumberOfShards())
				.put("number_of_replicas", esSetting.getNumberOfReplicas())
				.put("client.transport.sniff",
						esSetting.getClientTransportSniff())
				.put("cluster.name", esSetting.getClusterName())
				.put("client.transport.ping_timeout",
						esSetting.getClientTransportPingTimeout()).build();

		TransportClient indexClient = TransportClient.builder()
				.settings(settings).build();
		JSONArray ips = esSetting.getClientNodesIp();
		JSONArray ports = esSetting.getClientNodesPort();

		if (ips == null || ports == null || ips.size() != ports.size()) {
			logger.error("ClientNodesIp && ClientNodesPort config error");
			throw new RuntimeException(
					"ClientNodesIp && ClientNodesPort config error");
		}

		for (int i = 0; i < ips.size(); i++) {
			InetAddress host = InetAddress.getByName(ips.getString(i));
			int portNo = ports.getInteger(i);
			InetSocketTransportAddress transportAddress = new InetSocketTransportAddress(
					host, portNo);
			indexClient.addTransportAddress(transportAddress);
		}

		return indexClient;

	}

	@Bean(name = "transportClient")
	public TransportClient transportClient() throws Exception {

		Settings settings = Settings
				.settingsBuilder()
				.put("client.transport.sniff",
						esSetting.getClientTransportSniff())
				.put("cluster.name", esSetting.getClusterName())
				.put("client.transport.ping_timeout",
						esSetting.getClientTransportPingTimeout()).build();

		TransportClient indexClient = TransportClient.builder()
				.settings(settings).build();
		JSONArray ips = esSetting.getClientNodesIp();
		JSONArray ports = esSetting.getClientNodesPort();

		if (ips == null || ports == null || ips.size() != ports.size()) {
			logger.error("ClientNodesIp && ClientNodesPort config error");
			throw new RuntimeException(
					"ClientNodesIp && ClientNodesPort config error");
		}

		for (int i = 0; i < ips.size(); i++) {
			InetAddress host = InetAddress.getByName(ips.getString(i));
			int portNo = ports.getInteger(i);
			InetSocketTransportAddress transportAddress = new InetSocketTransportAddress(
					host, portNo);
			indexClient.addTransportAddress(transportAddress);
		}

		return indexClient;

	}
	
	@Bean(name = "errorPageFilter")
	public ErrorPageFilter errorPageFilter() {
	    return new ErrorPageFilter();
	}

	@Bean(name = "disableSpringBootErrorFilter")
	public FilterRegistrationBean disableSpringBootErrorFilter(ErrorPageFilter filter) {
	    FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
	    filterRegistrationBean.setFilter(filter);
	    filterRegistrationBean.setEnabled(false);
	    return filterRegistrationBean;
	}
}
