package com.tomtop.bo;


public class VhostBo extends BaseBean  {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5457332550853337253L;
	private Integer id;
	private String vhost;
	private Integer languageId;
	private Integer clientId;
	private Integer currencyId;
	private String orderPlaceholder;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getVhost() {
		return vhost;
	}
	public void setVhost(String vhost) {
		this.vhost = vhost;
	}
	public Integer getLanguageId() {
		return languageId;
	}
	public void setLanguageId(Integer languageId) {
		this.languageId = languageId;
	}
	public Integer getClientId() {
		return clientId;
	}
	public void setClientId(Integer clientId) {
		this.clientId = clientId;
	}
	public Integer getCurrencyId() {
		return currencyId;
	}
	public void setCurrencyId(Integer currencyId) {
		this.currencyId = currencyId;
	}
	public String getOrderPlaceholder() {
		return orderPlaceholder;
	}
	public void setOrderPlaceholder(String orderPlaceholder) {
		this.orderPlaceholder = orderPlaceholder;
	}
}
