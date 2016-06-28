package com.tomtop.services.impl.order;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tomtop.dao.order.IDropShippingUpdateDao;
import com.tomtop.dto.order.DropShipping;

@Service
public class DropShippingUpdateService {
	@Autowired
	private IDropShippingUpdateDao dropShippingUpdate;

	public boolean insert(DropShipping ds) {
		int i = dropShippingUpdate.insert(ds);
		return i == 1 ? true : false;
	}

	public boolean updateByID(DropShipping ds) {
		int i = dropShippingUpdate.updateByID(ds);
		return i == 1 ? true : false;
	}

	public boolean updateByDropShippingID(DropShipping ds) {
		int i = dropShippingUpdate.updateByDropShippingID(ds);
		return i == 1 ? true : false;
	}

	public boolean setUsedByDropShippingID(String dropShippingID) {
		int i = dropShippingUpdate
				.setUsedByDropShippingID(dropShippingID, true);
		return i == 1 ? true : false;
	}
}
