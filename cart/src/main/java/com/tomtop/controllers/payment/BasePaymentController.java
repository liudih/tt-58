package com.tomtop.controllers.payment;

import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.tomtop.dto.order.Order;
import com.tomtop.entity.payment.paypal.PaymentLogEvent;
import com.tomtop.events.EventBroker;
import com.tomtop.events.order.OrderOpreationEvent;
import com.tomtop.events.order.OrderOpreationEvent.OpreationResult;
import com.tomtop.events.order.OrderOpreationEvent.OpreationType;
import com.tomtop.exceptions.BadRequestException;
import com.tomtop.exceptions.UserNoLoginException;
import com.tomtop.forms.PlaceOrderForm;
import com.tomtop.mappers.order.DetailMapper;
import com.tomtop.services.ICurrencyService;
import com.tomtop.services.impl.order.OrderPaymentService;
import com.tomtop.services.order.IOrderService;
import com.tomtop.services.order.IOrderStatusService;
import com.tomtop.services.payment.IPaymentCallbackService;
import com.tomtop.services.payment.IPaymentProvider;
import com.tomtop.services.payment.IPaymentService;
import com.tomtop.services.payment.ocean.OceanPaymentService;
import com.tomtop.utils.FoundationService;
import com.tomtop.valueobjects.base.LoginContext;
import com.tomtop.valueobjects.order.PaymentContext;

@Controller
public class BasePaymentController {
	@Autowired
	private OceanPaymentService oceanService;
	@Autowired
	private IPaymentCallbackService callbackService;
	@Autowired
	private IOrderService orderService;
	@Autowired
	private EventBroker eventBroker;

	@Autowired
	FoundationService foundation;

	@Autowired
	private OrderPaymentService orderPaymentService;

	@Autowired
	IPaymentService paymentService;

	@Autowired
	private IOrderStatusService statusService;

	@Autowired
	private IPaymentCallbackService oceanLogService;

	@Autowired
	ICurrencyService currencyService;

	@Autowired
	DetailMapper detailMapper;

	public String doPayment(String orderNum, HttpServletResponse response,
			Map<String, Object> model,OpreationType opreationType) {
		LoginContext loginCtx = this.foundation.getLoginContext();
		if (loginCtx == null) {
			throw new UserNoLoginException();
		}
		String email = loginCtx.getEmail();

		PaymentContext paymentContext = orderService.getPaymentContext(
				orderNum, foundation.getLanguage());
		Order order = paymentContext.getOrder().getOrder();

		//获取支付提供方
		IPaymentProvider provider = paymentService.getPaymentById(order
				.getCpaymentid());
		//是否有额外属性
		if (provider.isNeedExtraInfo()) {
			PlaceOrderForm form = orderPaymentService.getForm(
					order.getCordernumber(), order.getCpaymentid());
			paymentContext.setForm(form);
		}
		if (order == null
				|| !order.getCmemberemail().equals(email.toLowerCase())
				&& statusService
						.getIdByName(IOrderStatusService.PAYMENT_PENDING) == order
						.getIstatus()) {
			throw new BadRequestException("order email invalid");
		}

		// 校验库存
		orderService.validateInventory(order.getIstorageid(), paymentContext
				.getOrder().getDetails());

		//获取支付参数
		Map<String, String> paras = provider.getDoPaymentParas(paymentContext);
		Assert.notEmpty(paras, "create ocean payment paras error");
		// 记录日志
		int site = this.foundation.getSiteID();

		JSONObject json = new JSONObject();
		json.putAll(paras);
		json.put("from", "OceanController.doPayment");

		//记录支付日志
		PaymentLogEvent event = new PaymentLogEvent(site, orderNum,
				json.toJSONString(), null);
		eventBroker.post(event);

		// 发送订单操作日志
		OrderOpreationEvent orderOpreationEvent = new OrderOpreationEvent(
				opreationType, OpreationResult.SUCCESS,
				JSON.toJSONString(paras), orderNum);	
		eventBroker.post(orderOpreationEvent);

		model.put("actionUrl", paras.get("actionUrl"));
		paras.remove("actionUrl");
		model.put("form", paras);
		model.put("step", 2);

		String firstdomain = foundation.getSubdomains();
		if ("m".equals(firstdomain)) {
			return "/mobile/payment/payment_redirect";
		} else {
			return "/payment/payment_redirect";
		}
	}
}
