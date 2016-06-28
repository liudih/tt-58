package com.tomtop.services.order;

import java.util.List;

import com.tomtop.bo.order.OrderBo;
import com.tomtop.dto.member.MemberAddress;
import com.tomtop.dto.order.Order;

public interface IOrderUpdateService {

	public static final int SHOW_TYPE_TURE = 1;
	public static final int SHOW_TYPE_RECYCLE = 2;
	public static final int SHOW_TYPE_FALSE = 3;

	public abstract boolean updateShowValidEmail(Integer type,
			List<Integer> ids, String email);

	public abstract boolean updateShow(Integer type, List<Integer> ids);

	public abstract String insert(OrderBo[] orders);

	public abstract String saveBatch(OrderBo[] orders);

	/**
	 * 更新交易号
	 * 
	 * @param orderId
	 * @param paymentId
	 * @return
	 * @author luojiaheng
	 */
	// need recode
	public abstract boolean updateTransactionId(Integer orderId,
			String transactionId);

	public abstract boolean updateTransactionId(String orderId,
			String transactionId);

	public abstract boolean updatePaymentTime(Integer orderId);

	public abstract boolean updatePaymentAccount(Integer orderId, String account);

	public abstract boolean updateOrderPrice(Integer orderId, Double grandTotal);

	public abstract boolean updateOrderShippingPrice(Integer orderId,
			Double grandTotal, Double shippingPrice);

	public abstract boolean updateOrderRemark(Integer orderId, String remark);

	public abstract boolean updateOrderAddress(String orderNumber,
			MemberAddress address);

	public abstract boolean updateShippingMethod(Order order);

	/**
	 * 更新订单的cpaymentid
	 * 
	 * @author lijun
	 * @param paymentId
	 * @param orderNum
	 * @return true ： 更新成功 false ： 更新失败
	 */
	public boolean updatePaymentIdByOrderNum(String paymentId, String orderNum);

	/**
	 * @author lijun
	 * @param orderId
	 * @param status
	 * @param email
	 * @return
	 */
	public boolean replaceOrder(Integer orderId, Integer status,
			String paymentId, String email);

}