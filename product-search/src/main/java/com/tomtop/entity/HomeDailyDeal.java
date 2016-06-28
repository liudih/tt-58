package com.tomtop.entity;


/**
 * 每日倒计时商品实体类
 * 
 * @author renyy
 *
 */
public class HomeDailyDeal extends ProductBase {
	
	private static final long serialVersionUID = -5213006380115849087L;

	/**
	 * 折扣
	 */
	private Float discount;

	public Float getDiscount() {
		if(discount == null){
			discount = 0f;
		}
		return discount;
	}

	public void setDiscount(Float discount) {
		this.discount = discount;
	}

}