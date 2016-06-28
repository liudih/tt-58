package com.tomtop.configuration;

import java.util.Locale;

import javax.servlet.Filter;
import javax.sql.DataSource;

import liquibase.integration.spring.SpringLiquibase;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
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
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;

import redis.clients.jedis.JedisPoolConfig;

import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.tomtop.customizer.MyCustomizer;
import com.tomtop.iterceptors.ViewModelInterceptor;

@Configuration
@EnableCaching
@EnableConfigurationProperties({ JdbcConnectionSettings.class,
		PaymentSettings.class, CartSettings.class, RedisConfigSettings.class })
public class ApplicationConfigurations {

	@Autowired
	private ViewModelInterceptor interceptor;

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

		JedisPoolConfig poolConfig = new JedisPoolConfig();
		// 设置最大连接数
		poolConfig.setMaxTotal(settings.getMaxTotal());
		// 设置最大空闲数
		poolConfig.setMaxIdle(settings.getMaxIdle());
		// 当池内没有返回对象时，最大等待时间
		poolConfig.setMaxWaitMillis(settings.getMaxWaitMillis());
		// 当调用borrow Object方法时，是否进行有效性检查
		poolConfig.setTestOnBorrow(settings.isTestOnBorrow());

		factory.setPoolConfig(poolConfig);
		return factory;
	}

	@Bean(name = "restTemplate")
	public RestTemplate getRestTemplate() {
		RestTemplate template = new RestTemplate();
		template.getMessageConverters().add(new FormHttpMessageConverter());
		return template;
	}

	@Bean
	public SpringLiquibase liquibase(DataSource dataSource) {
		SpringLiquibase lb = new SpringLiquibase();
		lb.setDataSource(dataSource);
		lb.setChangeLog("classpath:liquibase/order.xml");
		return lb;
	}


	@Bean
	public LocaleResolver localeResolver() {
		CookieLocaleResolver resolver = new CookieLocaleResolver();
		resolver.setDefaultLocale(new Locale("en"));
		resolver.setCookieName("locale");
		resolver.setCookieMaxAge(60 * 60 * 24 * 365);
		return resolver;
	}

	@Bean
	public Filter shallowEtagHeaderFilter() {
		return new ApplicationContextHeaderFilter();
	}

	@Bean
	public HttpRequestFactory httpRequestFactory() {
		NetHttpTransport transport = new NetHttpTransport();
		HttpRequestFactory factory = transport.createRequestFactory();
		return factory;
	}

	@Bean
	public WebMvcConfigurerAdapter webMvcConfigurerAdapter() {
		return new WebMvcConfigurerAdapter() {
			@Override
			public void addInterceptors(InterceptorRegistry registry) {
				registry.addInterceptor(interceptor);
			}
		};
	}


	@Bean
	public EmbeddedServletContainerCustomizer containerCustomizer() {
		return new MyCustomizer();
//		return new CustomizerErrorPage();
	}
}
