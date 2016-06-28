package com.tomtop.controllers.payment;

import java.io.IOException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.alibaba.fastjson.JSON;
import com.tomtop.dto.order.Order;
import com.tomtop.events.EventBroker;
import com.tomtop.events.order.OrderOpreationEvent;
import com.tomtop.events.order.OrderOpreationEvent.OpreationResult;
import com.tomtop.events.order.OrderOpreationEvent.OpreationType;
import com.tomtop.events.order.ReduceQtyEvent;
import com.tomtop.events.payment.PaymentCompletedEvent;
import com.tomtop.exceptions.BadRequestException;
import com.tomtop.services.order.IOrderService;
import com.tomtop.services.order.IOrderStatusService;
import com.tomtop.services.payment.gleepay.GleePalService;
import com.tomtop.valueobjects.payment.gleepay.GleePayResponse;

@Controller
@RequestMapping("/gleepay")
public class GleePayController extends BasePaymentController {
	private static final String REDIRECT = "/payment-result/succeed/gleepay/";
	@Autowired
	private IOrderService orderService;
	@Autowired
	private EventBroker eventBroker;

	@Autowired
	private IOrderStatusService statusService;

	@Autowired
	GleePalService gleePalService;

	@RequestMapping(value = "/return")
	public String back(GleePayResponse from, HttpServletResponse response)
			throws IOException {

		String orderNum = from.getOrderNo();

		Order order = orderService.getOrderByOrderNumber(orderNum);
		if (order == null) {
			throw new BadRequestException("can not found order with orderNum:"
					+ orderNum);
		}

		// 签名正确且支付成功
		if (gleePalService.validateSign(from) && from.isSuccess()) {

			boolean flag = statusService.payOrder(orderNum, from.getTradeNo(),
					from.getMerNo());
			if (flag) {
				// 发送事件修改库存
				ReduceQtyEvent reduceQtyEvent = new ReduceQtyEvent(orderNum,
						order.getIwebsiteid());
				eventBroker.post(reduceQtyEvent);

				// 发送支付完成事件
				PaymentCompletedEvent completedEvent = new PaymentCompletedEvent(
						orderNum);
				eventBroker.post(completedEvent);

				// 发送记录支付成功日志事件
				OrderOpreationEvent opreationEvent = new OrderOpreationEvent(
						OpreationType.GLEEPAY_RESPONSE,
						OpreationResult.SUCCESS, JSON.toJSONString(from),
						orderNum);
				eventBroker.post(opreationEvent);
			}

			// 为了广告联盟,需要重定向
			response.sendRedirect(REDIRECT + orderNum);
			return null;
		}

		OrderOpreationEvent opreationEvent = new OrderOpreationEvent(
				OpreationType.GLEEPAY_RESPONSE, OpreationResult.FAILURE,
				JSON.toJSONString(from), orderNum);
		eventBroker.post(opreationEvent);

		// 跳到失败页面
		String details = from.getOrderInfo();
		String[] str = details.split(":");
		// 跳转到支付失败页面
		StringBuilder errorurl = new StringBuilder("/payment-result/error");
		errorurl.append("?orderNum=").append(orderNum);
		// 鼎付测试环境支付失败没有错误码
		if (str != null && str.length > 1) {
			errorurl.append("&errorCode=").append(str[0]);
			errorurl.append("&error=").append(str[1]);
		} else {
			errorurl.append("&error=").append(str[0]);
		}
		errorurl.append("&returnWhere=").append("cart");
		errorurl.append("&storageid=").append(order.getIstorageid());
		response.sendRedirect(errorurl.toString());
		return null;
	}

	@RequestMapping(value = "/doPayment", method = RequestMethod.GET)
	public String paymentToGo(String orderNum, HttpServletRequest request,
			HttpServletResponse response, Map<String, Object> model) {
		return super.doPayment(orderNum, response, model,
				OpreationType.GLEEPAY_REQUEST);
	}
}
