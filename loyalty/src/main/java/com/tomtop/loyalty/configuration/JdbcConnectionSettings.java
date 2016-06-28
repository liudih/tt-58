package com.tomtop.loyalty.configuration;

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

	private String memberUrl;
	private String memberUsername;
	private String memberPassword;

	private String trackingUrl;
	private String trackingUsername;
	private String trackingPassword;

	private String interactionUrl;
	private String interactionUsername;
	private String interactionPassword;

	private String loyaltyUrl;
	private String loyaltyUsername;
	private String loyaltyPassword;

	private String baseUrl;
	private String baseUsername;
	private String basePassword;

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

	public String getTrackingUrl() {
		return trackingUrl;
	}

	public void setTrackingUrl(String trackingUrl) {
		this.trackingUrl = trackingUrl;
	}

	public String getTrackingUsername() {
		return trackingUsername;
	}

	public void setTrackingUsername(String trackingUsername) {
		this.trackingUsername = trackingUsername;
	}

	public String getTrackingPassword() {
		return trackingPassword;
	}

	public void setTrackingPassword(String trackingPassword) {
		this.trackingPassword = trackingPassword;
	}

	public String getInteractionUrl() {
		return interactionUrl;
	}

	public void setInteractionUrl(String interactionUrl) {
		this.interactionUrl = interactionUrl;
	}

	public String getInteractionUsername() {
		return interactionUsername;
	}

	public void setInteractionUsername(String interactionUsername) {
		this.interactionUsername = interactionUsername;
	}

	public String getInteractionPassword() {
		return interactionPassword;
	}

	public void setInteractionPassword(String interactionPassword) {
		this.interactionPassword = interactionPassword;
	}

	public String getLoyaltyUrl() {
		return loyaltyUrl;
	}

	public void setLoyaltyUrl(String loyaltyUrl) {
		this.loyaltyUrl = loyaltyUrl;
	}

	public String getLoyaltyUsername() {
		return loyaltyUsername;
	}

	public void setLoyaltyUsername(String loyaltyUsername) {
		this.loyaltyUsername = loyaltyUsername;
	}

	public String getLoyaltyPassword() {
		return loyaltyPassword;
	}

	public void setLoyaltyPassword(String loyaltyPassword) {
		this.loyaltyPassword = loyaltyPassword;
	}

}
