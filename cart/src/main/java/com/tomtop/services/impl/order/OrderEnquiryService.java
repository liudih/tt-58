package com.tomtop.services.impl.order;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tomtop.dto.order.Order;
import com.tomtop.mappers.order.OrderMapper;
import com.tomtop.services.order.IOrderStatusService;
import com.tomtop.valueobjects.order.MemberOrderForm;
import com.tomtop.valueobjects.order.OrderCount;


@Service
public class OrderEnquiryService {
	@Autowired
	OrderMapper orderMapper;
	
	@Autowired
	IOrderStatusService statusService;
	
	public static final int SHOW_TYPE_TURE = 1;
	public static final int SHOW_TYPE_RECYCLE = 2;
	public static final int SHOW_TYPE_FALSE = 3;
	
	public OrderCount getCountByEmail(String email, Integer siteId,
			Integer isShow, boolean isNormal) {
		Integer all = this.countByEmailAndStatus(email, null, siteId,
				isShow, isNormal);
		Integer pending = this.countByEmailAndStatus(email,
				IOrderStatusService.PAYMENT_PENDING, siteId, isShow, isNormal);
		Integer confirmed = this.countByEmailAndStatus(email,
				IOrderStatusService.PAYMENT_CONFIRMED, siteId, isShow, isNormal);
		Integer processing = this.countByEmailAndStatus(email,
				IOrderStatusService.PROCESSING, siteId, isShow, isNormal);
		Integer onHold = this.countByEmailAndStatus(email,
				IOrderStatusService.ON_HOLD, siteId, isShow, isNormal);
		Integer dispatched = this.countByEmailAndStatus(email,
				IOrderStatusService.DISPATCHED, siteId, isShow, isNormal);
		Integer cancelled = this.countByEmailAndStatus(email,
				IOrderStatusService.ORDER_CANCELLED, siteId, isShow, isNormal);
		Integer refunded = this.countByEmailAndStatus(email,
				IOrderStatusService.REFUNDED, siteId, isShow, isNormal);
		Integer recycle = this.countByEmailAndStatus(email, null,
				siteId, 2, isNormal);
		//预授权
		Integer paymentProcessing = this.countByEmailAndStatus(email,
				IOrderStatusService.PAYMENT_PROCESSING, siteId, isShow, isNormal);

		OrderCount count = new OrderCount();

		count.setAll(all);
		count.setCancelled(cancelled);
		count.setConfirmed(confirmed);
		count.setDispatched(dispatched);
		count.setOnHold(onHold);
		count.setPending(pending+paymentProcessing);
		count.setProcessing(processing);
		count.setRefunded(refunded);
		count.setRecycle(recycle);

		return count;
	}
	
	private Integer countByEmailAndStatus(String email, String status,
			Integer siteId, Integer isShow, boolean isNormal) {
		Integer statusId = null;
		if (null != status) {
			statusId = statusService.getIdByName(status);
		}
		return orderMapper.getCountByEmailAndStatus(email, statusId, siteId,
				isShow, isNormal);
	}
	
	public List<Order> searchOrders(MemberOrderForm form, boolean isNormal) {
		Date start = null;
		Date end = null;
		if (form.getInterval() != 0) {
			// end = new Date();
			GregorianCalendar calendar = new GregorianCalendar();
			calendar.add(Calendar.MONTH, (form.getInterval() * (-1)));
			start = calendar.getTime();
		}
		return orderMapper.searchOrders(form.getEmail(), form.getStatus(), start, end,
				form.getProductName(), form.getSiteId(), form.getPageSize(),
				form.getPageNum(), form.getIsShow(), form.getOrderNumber(),
				form.getTransactionId(), form.getFirstName(), isNormal);
	}
	
	public Integer searchOrderCount(MemberOrderForm form, boolean isNormal) {
		Date start = null;
		Date end = null;
		if (form.getInterval() != 0) {
			// end = new Date();
			GregorianCalendar calendar = new GregorianCalendar();
			calendar.add(Calendar.MONTH, (form.getInterval() * (-1)));
			start = calendar.getTime();
		}
		return orderMapper.searchOrderCount(form.getEmail(), form.getStatus(), start, end,
				form.getProductName(), form.getSiteId(),
				form.getIsShow(), form.getOrderNumber(),
				form.getTransactionId(), form.getFirstName(), isNormal);
	}
	
	/**
	 * 逻辑删除订单
	 * @param email
	 * @param showType
	 * @param orderNumbers
	 * @return
	 */
	public boolean updateOrderShow(String email, Integer siteid, List<String> orderNumbers,
			Integer showType){
		if(orderNumbers.size()==0 || showType==null){
			return false;
		}
		int flag = orderMapper.updateOrderForDel(email, showType, orderNumbers, siteid);
		return flag>0;
	}
	
	/**
	 * 根据产品ID与邮箱查询未评论的订单ID
	 * @param email
	 * @param listingid
	 * @return
	 */
	public String getorderIdWithoutComment(String email, String listingid){
		return orderMapper.getorderIdWithoutComment(email, listingid);
	}
	
}
