package com.tomtop.services.impl.payment;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tomtop.dao.payment.IPaymentCallbackDao;
import com.tomtop.services.payment.IPaymentCallbackService;
import com.tomtop.valueobjects.order.PaymentCallback;

@Service
public class PaymentCallbackService implements IPaymentCallbackService {
	@Autowired
	private IPaymentCallbackDao dao;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * services.order.payment.IPaymentCallbackService#insert(java.lang.String,
	 * java.lang.String, java.lang.String, java.lang.String, java.lang.Integer)
	 */
	@Override
	public boolean insert(String orderNumber, String content, String paymentID,
			String response, Integer siteID) {
		PaymentCallback pc = new PaymentCallback();
		pc.setCcontent(content);
		pc.setCordernumber(orderNumber);
		pc.setCpaymentid(paymentID);
		pc.setCresponse(response);
		pc.setIwebsiteid(siteID);
		return insert(pc);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see services.order.payment.IPaymentCallbackService#insert(dto.order.
	 * PaymentCallback)
	 */
	@Override
	public boolean insert(PaymentCallback pc) {
		int i = dao.insert(pc);
		return i == 1 ? true : false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * services.order.payment.IPaymentCallbackService#getByOrderNumerAndSiteID
	 * (java.lang.String, java.lang.Integer)
	 */
	@Override
	public List<PaymentCallback> getByOrderNumerAndSiteID(String orderNumber,
			Integer siteID) {
		return dao.getByOrderNumberAndSiteID(orderNumber, siteID);
	}
}
