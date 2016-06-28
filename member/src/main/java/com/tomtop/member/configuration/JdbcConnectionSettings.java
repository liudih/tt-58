package com.tomtop.member.configuration;


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
	
	private String baseUrl;
	private String baseUsername;
	private String basePassword;

	private String memberUrl;
	private String memberUsername;
	private String memberPassword;
	
	private String trackingUrl;
	private String trackingUsername;
	private String trackingPassword;
	
	private String interactionUrl;
	private String interactionUsername;
	private String interactionPassword;
	
	private String messageUrl;
	private String messageUsername;
	private String messagePassword;
	
	private String loyaltyUrl;
	private String loyaltyUsername;
	private String loyaltyPassword;
	
	private String productUrl;
	private String productUsername;
	private String productPassword;

	private String orderUrl;
	private String orderUsername;
	private String orderPassword;
	
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

	public String getMessageUrl() {
		return messageUrl;
	}

	public void setMessageUrl(String messageUrl) {
		this.messageUrl = messageUrl;
	}

	public String getMessageUsername() {
		return messageUsername;
	}

	public void setMessageUsername(String messageUsername) {
		this.messageUsername = messageUsername;
	}

	public String getMessagePassword() {
		return messagePassword;
	}

	public void setMessagePassword(String messagePassword) {
		this.messagePassword = messagePassword;
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
	

}
