package com.tomtop.services.payment;

import java.util.List;

import com.tomtop.valueobjects.order.PaymentCallback;

public interface IPaymentCallbackService {

	public abstract boolean insert(String orderNumber, String content,
			String paymentID, String response, Integer siteID);

	public abstract boolean insert(PaymentCallback pc);

	public abstract List<PaymentCallback> getByOrderNumerAndSiteID(
			String orderNumber, Integer siteID);

}