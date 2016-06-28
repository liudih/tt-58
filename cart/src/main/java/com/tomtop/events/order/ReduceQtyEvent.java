package com.tomtop.events.order;

/**
 * 修改库存事件
 * 
 * @author lijun
 *
 */
public class ReduceQtyEvent {

	final String orderNum;

	final int websiteID;

	public ReduceQtyEvent(String orderNum, int websiteID) {
		this.orderNum = orderNum;
		this.websiteID = websiteID;
	}

	public String getOrderNum() {
		return orderNum;
	}

	public int getWebsiteID() {
		return websiteID;
	}

}
