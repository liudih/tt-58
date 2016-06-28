package com.tomtop.services.order;

import java.util.List;

import com.tomtop.dto.order.BillDetail;
import com.tomtop.dto.order.Order;
import com.tomtop.dto.order.OrderDetail;
import com.tomtop.dto.order.OrderDiscount;
import com.tomtop.entity.order.CreateOrderRequest;
import com.tomtop.valueobjects.CartItem;
import com.tomtop.valueobjects.order.OrderItem;
import com.tomtop.valueobjects.order.PaymentContext;
import com.tomtop.valueobjects.order.ShippingMethod;

public interface IOrderService {

	/**
	 * 验证取得的运送方式的ID当前订单是否可使用
	 *
	 * @param storageId
	 * @param country
	 *            国家的英文缩写
	 * @param cunrrency
	 *            TODO
	 * @param langID
	 *            TODO
	 * @return 如果验证通过会返回该ShippingMethod,通过该返回值去取邮费,如果验证不通过返回null
	 * @author luojiaheng
	 */
	public ShippingMethod checkShippingMethodCorrect(int storageId,
			String country, String shipCode, Double subTotal,
			List<CartItem> items, String cunrrency, int langID);

	/**
	 * 检查订单是否已经支付(订单状态为 ：付款处理中 和 收款成功 视为已经支付)
	 * 
	 * @author lijun
	 * @param orderNum
	 * @return true : 已经支付过 false : 未支付过
	 */
	public boolean isAlreadyPaid(String orderNum);

	/**
	 * @author lijun
	 * @param order
	 * @return
	 */
	public boolean isAlreadyPaid(Order order);

	/**
	 * 反序列化order成cartitems
	 * 
	 * @author lijun
	 * @param orderNum
	 *            订单号
	 * @return List<CartItem>
	 */
	public List<CartItem> deserializeOrder(String orderNum);

	/**
	 * 更新ship地址和邮费 这个方法只会是paypal调到主站时用户修改了邮寄地址和邮寄方式
	 * 
	 * @author lijun
	 * @return
	 */
	public boolean updateShipAddressAndShipPrice(Order order) throws Exception;

	/**
	 * 重新计算已有订单用新的邮寄方式的邮费
	 * 用户未登陆的情况下直接去paypal付款,待paypal返回到我们网站上待用户去选择邮寄方式的时候要重新计算邮费
	 * 
	 * @author lijun
	 * @param orderNum
	 * @param shipMethodId
	 * @param shipToCountryCode
	 *            邮寄地址国家的code
	 */
	public double getFreight(String orderNum, String shipMethodId,
			String shipToCountryCode);

	/**
	 * 生成普通订单号(V2.0)
	 * 
	 * @author lijun
	 * @param shipToCountryCode
	 * @return
	 */
	public String createGeneralOrderNumberV2(String shipToCountryCode);

	/**
	 * 生成游客订单
	 * 
	 * @author lijun
	 * @param shipToCountryCode
	 * @return
	 */
	public String createGuestOrderNumberV2(String shipToCountryCode);

	/**
	 * 生成代理订单号
	 * 
	 * @author lijun
	 * @param shipToCountryCode
	 * @return
	 */
	public String createAgentOrderNumberV2(String shipToCountryCode);

	/**
	 * 创建订单
	 * 
	 * @author lijun
	 * @param request
	 * @return
	 */
	public Order createOrderInstance(CreateOrderRequest request);

	/**
	 * 生成订单号
	 */
	public String generateOrderNum();

	public BillDetail parseBill(List<BillDetail> bills, OrderDetail orderDetail);

	public boolean insertOrder(com.tomtop.dto.order.Order order) ;

	public BillDetail getShippingBill(Order order);

	public boolean insertDetail(List<OrderDetail> details);

	public List<OrderItem> getOrderDetailByOrder(Order order, int langID);

	public List<OrderItem> getOrderDetailByOrder(Order order);

	public Order getOrderByOrderNumber(String orderNumber);

	public Order getOrderById(int orderId);

	public List<OrderDetail> getOrderDetails(Integer orderId);

	public List<OrderDetail> getOrderDetails(String orderId);

	public PaymentContext getPaymentContext(String orderNum, int langId);

	public boolean updateOrderPaymentId(Integer iid, String paymentId);

	public List<OrderDiscount> getOrderDiscountList(String orderNumber,
			Integer siteid);

	/**
	 * @author lijun
	 * @param orderNum
	 *            the String orderNum is not null
	 * @return
	 */
	public List<OrderDetail> getOrderDetailsByOrderNum(String orderNum);
	
	/**
	 * 校验是否有库存
	 * @param storageid 仓库ID
	 * @param details 订单详情
	 */
	public boolean validateInventory(Integer storageid,List<OrderDetail> details);
}