package com.tomtop.dao.order;

import com.tomtop.dto.order.DropShipping;


public interface IDropShippingUpdateDao {
	int insert(DropShipping ds);

	int updateByID(DropShipping ds);

	int updateByDropShippingID(DropShipping ds);

	int setUsedByDropShippingID(String dropShippingID, boolean bused);
}
