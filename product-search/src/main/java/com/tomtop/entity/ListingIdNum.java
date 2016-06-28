package com.tomtop.entity;

import java.io.Serializable;

/**
 * 购物车产品信息接口接收类
 * 
 * @author liulj
 *
 */
public class ListingIdNum implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7831582071965020157L;

	public String listingId;

	public int num;
	
	public int depotId;

	public String getListingId() {
		return listingId;
	}

	public void setListingId(String listingId) {
		this.listingId = listingId;
	}

	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}

	public int getDepotId() {
		return depotId;
	}

	public void setDepotId(int depotId) {
		this.depotId = depotId;
	}
	
}
