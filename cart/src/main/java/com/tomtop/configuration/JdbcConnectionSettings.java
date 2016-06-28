package com.tomtop.configuration;

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

	private String baseDriver;
	private String baseUrl;
	private String baseUsername;
	private String basePassword;
	private Integer baseMinConnectionsPerPartition;
	private Integer baseMaxConnectionsPerPartition;

	private String memberDriver;
	private String memberUrl;
	private String memberUsername;
	private String memberPassword;
	private Integer memberMinConnectionsPerPartition;
	private Integer memberMaxConnectionsPerPartition;

	private String productDriver;
	private String productUrl;
	private String productUsername;
	private String productPassword;
	private Integer productMinConnectionsPerPartition;
	private Integer productMaxConnectionsPerPartition;

	private String paypalDriver;
	private String paypalUrl;
	private String paypalUsername;
	private String paypalPassword;
	private Integer paypalMinConnectionsPerPartition;
	private Integer paypalMaxConnectionsPerPartition;

	public String getBaseDriver() {
		return baseDriver;
	}

	public void setBaseDriver(String baseDriver) {
		this.baseDriver = baseDriver;
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

	public Integer getBaseMinConnectionsPerPartition() {
		return baseMinConnectionsPerPartition;
	}

	public void setBaseMinConnectionsPerPartition(
			Integer baseMinConnectionsPerPartition) {
		this.baseMinConnectionsPerPartition = baseMinConnectionsPerPartition;
	}

	public Integer getBaseMaxConnectionsPerPartition() {
		return baseMaxConnectionsPerPartition;
	}

	public void setBaseMaxConnectionsPerPartition(
			Integer baseMaxConnectionsPerPartition) {
		this.baseMaxConnectionsPerPartition = baseMaxConnectionsPerPartition;
	}

	public String getMemberDriver() {
		return memberDriver;
	}

	public void setMemberDriver(String memberDriver) {
		this.memberDriver = memberDriver;
	}

	public String getMemberUrl() {
		return memberUrl;
	}

	public void setMemberUrl(String memberUrl) {
		this.memberUrl = memberUrl;
	}

	public String getMemberUsername() {
		return memberUsername;
	}

	public void setMemberUsername(String memberUsername) {
		this.memberUsername = memberUsername;
	}

	public String getMemberPassword() {
		return memberPassword;
	}

	public void setMemberPassword(String memberPassword) {
		this.memberPassword = memberPassword;
	}

	public Integer getMemberMinConnectionsPerPartition() {
		return memberMinConnectionsPerPartition;
	}

	public void setMemberMinConnectionsPerPartition(
			Integer memberMinConnectionsPerPartition) {
		this.memberMinConnectionsPerPartition = memberMinConnectionsPerPartition;
	}

	public Integer getMemberMaxConnectionsPerPartition() {
		return memberMaxConnectionsPerPartition;
	}

	public void setMemberMaxConnectionsPerPartition(
			Integer memberMaxConnectionsPerPartition) {
		this.memberMaxConnectionsPerPartition = memberMaxConnectionsPerPartition;
	}

	public String getProductDriver() {
		return productDriver;
	}

	public void setProductDriver(String productDriver) {
		this.productDriver = productDriver;
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

	public Integer getProductMinConnectionsPerPartition() {
		return productMinConnectionsPerPartition;
	}

	public void setProductMinConnectionsPerPartition(
			Integer productMinConnectionsPerPartition) {
		this.productMinConnectionsPerPartition = productMinConnectionsPerPartition;
	}

	public Integer getProductMaxConnectionsPerPartition() {
		return productMaxConnectionsPerPartition;
	}

	public void setProductMaxConnectionsPerPartition(
			Integer productMaxConnectionsPerPartition) {
		this.productMaxConnectionsPerPartition = productMaxConnectionsPerPartition;
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

	public String getPaypalDriver() {
		return paypalDriver;
	}

	public void setPaypalDriver(String paypalDriver) {
		this.paypalDriver = paypalDriver;
	}

	public String getPaypalUrl() {
		return paypalUrl;
	}

	public void setPaypalUrl(String paypalUrl) {
		this.paypalUrl = paypalUrl;
	}

	public String getPaypalUsername() {
		return paypalUsername;
	}

	public void setPaypalUsername(String paypalUsername) {
		this.paypalUsername = paypalUsername;
	}

	public String getPaypalPassword() {
		return paypalPassword;
	}

	public void setPaypalPassword(String paypalPassword) {
		this.paypalPassword = paypalPassword;
	}

	public Integer getPaypalMinConnectionsPerPartition() {
		return paypalMinConnectionsPerPartition;
	}

	public void setPaypalMinConnectionsPerPartition(
			Integer paypalMinConnectionsPerPartition) {
		this.paypalMinConnectionsPerPartition = paypalMinConnectionsPerPartition;
	}

	public Integer getPaypalMaxConnectionsPerPartition() {
		return paypalMaxConnectionsPerPartition;
	}

	public void setPaypalMaxConnectionsPerPartition(
			Integer paypalMaxConnectionsPerPartition) {
		this.paypalMaxConnectionsPerPartition = paypalMaxConnectionsPerPartition;
	}

}
