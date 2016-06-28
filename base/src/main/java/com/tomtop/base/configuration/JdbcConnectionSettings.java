package com.tomtop.base.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(value = "jdbc")
public class JdbcConnectionSettings extends AbstractSettings {

	private String driver;
	private String url;
	private String username;
	private String password;
	private Integer idleConnectionTestPeriod;
	private Integer idleMaxAge;
	private Integer minConnectionsPerPartition;
	private Integer maxConnectionsPerPartition;
	private Integer partitionCount;
	private Integer acquireIncrement;
	private Integer statementsCacheSize;
	private Integer releaseHelperThreads;

	private String trackinglDriver;
	private String trackingUrl;
	private String trackingUsername;
	private String trackingPassword;

	private String baseUrl;
	private String baseUsername;
	private String basePassword;

	private String orderUrl;
	private String orderUsername;
	private String orderPassword;
	
	private String productUrl;
	private String productUsername;
	private String productPassword;

	private String mysqlDriver;
	private String mysqlUrl;
	private String mysqlUsername;
	private String mysqlPassword;

	public String getTrackinglDriver() {
		return trackinglDriver;
	}

	public String getBaseUrl() {
		return baseUrl;
	}

	public void setBaseUrl(String baseUrl) {
		this.baseUrl = baseUrl;
	}

	public String getBaseUsername() {
		return baseUsername;
	}

	public void setBaseUsername(String baseUsername) {
		this.baseUsername = baseUsername;
	}

	public String getBasePassword() {
		return basePassword;
	}

	public void setBasePassword(String basePassword) {
		this.basePassword = basePassword;
	}

	public String getOrderUrl() {
		return orderUrl;
	}

	public void setOrderUrl(String orderUrl) {
		this.orderUrl = orderUrl;
	}

	public String getOrderUsername() {
		return orderUsername;
	}

	public void setOrderUsername(String orderUsername) {
		this.orderUsername = orderUsername;
	}

	public String getOrderPassword() {
		return orderPassword;
	}

	public void setOrderPassword(String orderPassword) {
		this.orderPassword = orderPassword;
	}

	public void setTrackinglDriver(String trackinglDriver) {
		this.trackinglDriver = trackinglDriver;
	}

	public String getTrackingUrl() {
		return trackingUrl;
	}

	public void setTrackingUrl(String trackingUrl) {
		this.trackingUrl = trackingUrl;
	}

	public String getTrackingUsername() {
		return trackingUsername;
	}

	public void setTrackingUsername(String tracking) {
		this.trackingUsername = tracking;
	}

	public String getTrackingPassword() {
		return trackingPassword;
	}

	public void setTrackingPassword(String trackingPassword) {
		this.trackingPassword = trackingPassword;
	}

	public JdbcConnectionSettings() {

	}

	public String getDriver() {
		return driver;
	}

	public void setDriver(String driver) {
		this.driver = driver;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Integer getIdleConnectionTestPeriod() {
		return idleConnectionTestPeriod;
	}

	public void setIdleConnectionTestPeriod(Integer idleConnectionTestPeriod) {
		this.idleConnectionTestPeriod = idleConnectionTestPeriod;
	}

	public Integer getIdleMaxAge() {
		return idleMaxAge;
	}

	public void setIdleMaxAge(Integer idleMaxAge) {
		this.idleMaxAge = idleMaxAge;
	}

	public Integer getMinConnectionsPerPartition() {
		return minConnectionsPerPartition;
	}

	public void setMinConnectionsPerPartition(Integer minConnectionsPerPartition) {
		this.minConnectionsPerPartition = minConnectionsPerPartition;
	}

	public Integer getMaxConnectionsPerPartition() {
		return maxConnectionsPerPartition;
	}

	public void setMaxConnectionsPerPartition(Integer maxConnectionsPerPartition) {
		this.maxConnectionsPerPartition = maxConnectionsPerPartition;
	}

	public Integer getPartitionCount() {
		return partitionCount;
	}

	public void setPartitionCount(Integer partitionCount) {
		this.partitionCount = partitionCount;
	}

	public Integer getAcquireIncrement() {
		return acquireIncrement;
	}

	public void setAcquireIncrement(Integer acquireIncrement) {
		this.acquireIncrement = acquireIncrement;
	}

	public Integer getStatementsCacheSize() {
		return statementsCacheSize;
	}

	public void setStatementsCacheSize(Integer statementsCacheSize) {
		this.statementsCacheSize = statementsCacheSize;
	}

	public Integer getReleaseHelperThreads() {
		return releaseHelperThreads;
	}

	public void setReleaseHelperThreads(Integer releaseHelperThreads) {
		this.releaseHelperThreads = releaseHelperThreads;
	}

	public String getMysqlDriver() {
		return mysqlDriver;
	}

	public void setMysqlDriver(String mysqlDriver) {
		this.mysqlDriver = mysqlDriver;
	}

	public String getMysqlUrl() {
		return mysqlUrl;
	}

	public void setMysqlUrl(String mysqlUrl) {
		this.mysqlUrl = mysqlUrl;
	}

	public String getMysqlUsername() {
		return mysqlUsername;
	}

	public void setMysqlUsername(String mysqlUsername) {
		this.mysqlUsername = mysqlUsername;
	}

	public String getMysqlPassword() {
		return mysqlPassword;
	}

	public void setMysqlPassword(String mysqlPassword) {
		this.mysqlPassword = mysqlPassword;
	}

	public String getProductUrl() {
		return productUrl;
	}

	public void setProductUrl(String productUrl) {
		this.productUrl = productUrl;
	}

	public String getProductUsername() {
		return productUsername;
	}

	public void setProductUsername(String productUsername) {
		this.productUsername = productUsername;
	}

	public String getProductPassword() {
		return productPassword;
	}

	public void setProductPassword(String productPassword) {
		this.productPassword = productPassword;
	}

}
