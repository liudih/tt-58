package com.tomtop.services.impl.order;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tomtop.dto.order.Order;
import com.tomtop.dto.order.OrderDetail;
import com.tomtop.events.EventBroker;
import com.tomtop.events.order.PaymentConfirmationEvent;
import com.tomtop.services.base.IWebsiteService;
import com.tomtop.services.order.IOrderService;
import com.tomtop.services.order.IOrderStatusService;
import com.tomtop.services.order.IOrderUpdateService;
import com.tomtop.services.order.IPaymentConfirmationService;
import com.tomtop.valueobjects.order.OrderValue;

@Service
public class PaymentConfirmationService implements IPaymentConfirmationService {
	private static final Logger Logger = LoggerFactory
			.getLogger(PaymentConfirmationService.class);

	@Autowired
	private IOrderService enquiryService;
	@Autowired
	private IOrderStatusService statusSerice;
	@Autowired
	private IOrderUpdateService updateService;
	@Autowired
	private EventBroker eventBroker;
	@Autowired
	private IWebsiteService websiteService;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * services.payment.IPaymentConfirmationService#confirmPayment(java.lang
	 * .String, java.lang.String, int)
	 */
	@Override
	public boolean confirmPayment(String orderId, String transactionId,
			int langId) {
		int id = Integer.parseInt(orderId);
		Order order = enquiryService.getOrderById(id);
		if (null == order) {
			Logger.info(
					"order can not found in confirmPayment, orderID: {}, transactionID: {}",
					orderId, transactionId);
			return false;
		}
		List<OrderDetail> details = enquiryService.getOrderDetails(orderId);

		statusSerice.changeOrdeStatus(order.getIid(),
				IOrderStatusService.PAYMENT_CONFIRMED);

		statusSerice.changeOrdePaymentStatus(order.getCordernumber(), 1);

		updateService.updateTransactionId(order.getIid(), transactionId);
		eventBroker.post(new PaymentConfirmationEvent(new OrderValue(order,
				details), transactionId, langId));
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * services.payment.IPaymentConfirmationService#confirmPayment(java.lang
	 * .Integer, java.lang.String)
	 */
	@Override
	public boolean confirmPayment(Integer orderId, String transactionId) {
		Order order = enquiryService.getOrderById(orderId);
		List<OrderDetail> details = enquiryService.getOrderDetails(orderId);
		if (null == order) {
			Logger.info(
					"order can not found in confirmPayment, orderID: {}, transactionID: {}",
					orderId, transactionId);
			return false;
		}
		statusSerice.changeOrdeStatus(order.getIid(),
				IOrderStatusService.PAYMENT_CONFIRMED);
		updateService.updateTransactionId(order.getIid(), transactionId);
		Integer webisteId = order.getIwebsiteid();
		Integer ilanguageid = websiteService.getWebsite(webisteId)
				.getIlanguageid();
		eventBroker.post(new PaymentConfirmationEvent(new OrderValue(order,
				details), transactionId, ilanguageid));
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * services.payment.IPaymentConfirmationService#confirmPayment(java.lang
	 * .String, java.lang.String, java.lang.String)
	 */
	@Override
	public boolean confirmPayment(String orderId, String transactionId,
			String status) {
		boolean flag = statusSerice.changeOrdeStatus(orderId, status);
		if (!flag) {
			Logger.info(
					"order can not found in confirmPayment, orderID: {}, transactionID: {}, status: {}",
					orderId, transactionId, status);
			return false;
		}
		updateService.updateTransactionId(orderId, transactionId);
		return true;
	}

	@Override
	public boolean confirmPayment(String orderNumber, String transactionId) {
		Order order = enquiryService.getOrderByOrderNumber(orderNumber);
		List<OrderDetail> details = enquiryService.getOrderDetails(orderNumber);
		if (null == order) {
			Logger.info(
					"order can not found in confirmPayment, orderID: {}, transactionID: {}",
					orderNumber, transactionId);
			return false;
		}
		statusSerice.changeOrdeStatus(order.getIid(),
				IOrderStatusService.PAYMENT_CONFIRMED);
		updateService.updateTransactionId(order.getIid(), transactionId);
		Integer webisteId = order.getIwebsiteid();
		Integer ilanguageid = websiteService.getWebsite(webisteId)
				.getIlanguageid();
		eventBroker.post(new PaymentConfirmationEvent(new OrderValue(order,
				details), transactionId, ilanguageid));
		return true;
	}
}
