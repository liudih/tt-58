package com.tomtop.dao.payment;

import java.util.List;

import com.tomtop.valueobjects.order.PaymentCallback;

public interface IPaymentCallbackDao {
	int insert(PaymentCallback pc);

	List<PaymentCallback> getByOrderNumberAndSiteID(String orderNumber,
			Integer site);
}
