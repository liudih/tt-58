package com.tomtop.entity.order;

import java.io.Serializable;
import java.util.List;

import com.tomtop.valueobjects.CartItem;

/**
 * 
 * @author lijun
 *
 */
public class CreateOrderRequest implements Serializable {

	private static final long serialVersionUID = 1L;

	final List<CartItem> items;
	final Integer siteId;
	final Integer addressId;
	final String shipCode;
	//广告联盟AID
	final String origin;
	final String message;
	final String ip;
	final Integer langID;
	final String currency;
	final String vhost;
	final Integer storage;
	// 支付类型
	private String cpaymenttype;
	

	public CreateOrderRequest(List<CartItem> items, Integer siteId,
			Integer addressId, String shipCode, String origin, String message,
			String ip, Integer langID, String currency, String vhost,
			Integer storage) {
		this.items = items;
		this.siteId = siteId;
		this.addressId = addressId;
		this.shipCode = shipCode;
		this.origin = origin;
		this.message = message;
		this.ip = ip;
		this.langID = langID;
		this.currency = currency;
		this.vhost = vhost;
		this.storage = storage;
	}

	public Integer getStorage() {
		return storage;
	}

	public List<CartItem> getItems() {
		return items;
	}

	public Integer getSiteId() {
		return siteId;
	}

	public Integer getAddressId() {
		return addressId;
	}

	public String getShipCode() {
		return shipCode;
	}

	public String getOrigin() {
		return origin;
	}

	public String getMessage() {
		return message;
	}

	public String getIp() {
		return ip;
	}

	public Integer getLangID() {
		return langID;
	}

	public String getCurrency() {
		return currency;
	}

	public String getVhost() {
		return vhost;
	}

	public String getCpaymenttype() {
		return cpaymenttype;
	}

	public void setCpaymenttype(String cpaymenttype) {
		this.cpaymenttype = cpaymenttype;
	}

}
