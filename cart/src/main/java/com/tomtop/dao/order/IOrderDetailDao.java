package com.tomtop.dao.order;

import java.util.List;

import com.tomtop.valueobjects.CartItem;

public interface IOrderDetailDao {

	/**
	 * 反序列化order成caritems
	 * 
	 * @author lijun
	 * @param orderNum
	 * @return
	 */
	public List<CartItem> selectCartItemsByOrderNum(String orderNum);

}
