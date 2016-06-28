package com.tomtop.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * redis配置设置
 * 
 * @author liulj
 *
 */
@ConfigurationProperties(value = "redis")
public class RedisConfigSettings extends AbstractSettings {
	private String password;
	private String ip;
	private Integer port;
	private Integer timeout;
	private Integer db = 1;
	/**
	 * 缓存管理器失效时间
	 */
	private Integer expireTime = 0;
	/**
	 * 每天失效时间
	 */
	private Integer dailyExpireTime;
	/**
	 * 产品缓冲的过期时间
	 */
	private Integer baseExpireTime;

	/** 设置最大连接数 */
	private int maxTotal;

	/** 设置最大空闲数 */
	private int maxIdle;

	/** 当池内没有返回对象时，最大等待时间 */
	private long maxWaitMillis;

	/** 当调用borrow Object方法时，是否进行有效性检查 */
	private boolean testOnBorrow;

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public Integer getPort() {
		return port;
	}

	public void setPort(Integer port) {
		this.port = port;
	}

	public Integer getTimeout() {
		return timeout;
	}

	public void setTimeout(Integer timeout) {
		this.timeout = timeout;
	}

	public Integer getExpireTime() {
		return expireTime;
	}

	public void setExpireTime(Integer expireTime) {
		this.expireTime = expireTime;
	}

	public Integer getDailyExpireTime() {
		return dailyExpireTime;
	}

	public void setDailyExpireTime(Integer dailyExpireTime) {
		this.dailyExpireTime = dailyExpireTime;
	}

	public Integer getBaseExpireTime() {
		return baseExpireTime;
	}

	public void setBaseExpireTime(Integer baseExpireTime) {
		this.baseExpireTime = baseExpireTime;
	}

	public Integer getDb() {
		return db;
	}

	public void setDb(Integer db) {
		this.db = db;
	}

	public int getMaxTotal() {
		return maxTotal;
	}

	public void setMaxTotal(int maxTotal) {
		this.maxTotal = maxTotal;
	}

	public int getMaxIdle() {
		return maxIdle;
	}

	public void setMaxIdle(int maxIdle) {
		this.maxIdle = maxIdle;
	}

	public long getMaxWaitMillis() {
		return maxWaitMillis;
	}

	public void setMaxWaitMillis(long maxWaitMillis) {
		this.maxWaitMillis = maxWaitMillis;
	}

	public boolean isTestOnBorrow() {
		return testOnBorrow;
	}

	public void setTestOnBorrow(boolean testOnBorrow) {
		this.testOnBorrow = testOnBorrow;
	}
}
