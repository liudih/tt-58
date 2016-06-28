package com.tomtop.controllers.payment;

import java.io.IOException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.tomtop.dto.order.Order;
import com.tomtop.entity.payment.paypal.PaymentLogEvent;
import com.tomtop.events.EventBroker;
import com.tomtop.events.order.OrderOpreationEvent;
import com.tomtop.events.order.OrderOpreationEvent.OpreationResult;
import com.tomtop.events.order.OrderOpreationEvent.OpreationType;
import com.tomtop.events.order.ReduceQtyEvent;
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
import com.tomtop.valueobjects.payment.ocean.OceanPaymentResult;

@Controller
@RequestMapping("/ocean")
public class OceanController {
	private static final String REDIRECT = "/payment-result/succeed/ocean/";
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

	@Value("${payment.oceanEndPoint}")
	String oceanEndPoint;

	@RequestMapping(value = "/return", method = RequestMethod.POST)
	public String userPOST(@ModelAttribute OceanPaymentResult from,
			Map<String, Object> model, HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		
		OceanPaymentResult result = from;
		String orderNum = result.getOrder_number();

		String signValue = oceanService.getOceanValidSignValue(result, orderNum);
		
		int siteId = this.foundation.getSiteID();
		LoginContext loginCtx = this.foundation.getLoginContext();
		String email = loginCtx != null ? loginCtx.getEmail() : "";
		
		if (signValue.toLowerCase().equals(result.getSignValue().toLowerCase())) {
			if ("1".equals(result.getPayment_status())
					|| "-1".equals(result.getPayment_status())) {
				int site = foundation.getSiteID();
				// 发送事件去修改库存
				ReduceQtyEvent event = new ReduceQtyEvent(orderNum, site);
				this.eventBroker.post(event);
				
				//发送记录支付成功日志事件
				OrderOpreationEvent resultEvent = new OrderOpreationEvent(siteId,
						email, orderNum, OpreationType.OCEAN_RESPONSE, JSON.toJSONString(result));
				eventBroker.post(resultEvent);
			}
		}
		Order order = orderService.getOrderByOrderNumber(orderNum);
		if (order == null) {
			throw new BadRequestException("can not found order with orderNum:"
					+ orderNum);
		}

		if ("1".equals(result.getPayment_status())
				|| "-1".equals(result.getPayment_status())) {

			// 为了广告联盟,需要重定向
			response.sendRedirect(REDIRECT + orderNum);
			return null;
		}
		
		OrderOpreationEvent resultEvent = new OrderOpreationEvent(siteId,
				email, orderNum, OpreationType.OCEAN_RESPONSE, JSON.toJSONString(result));
		resultEvent.setCresult(OpreationResult.FAILURE);
		eventBroker.post(resultEvent);

		// 跳到失败页面
		String details = result.getPayment_details();
		String[] str = details.split(":");
		// 跳转到支付失败页面
		StringBuilder errorurl = new StringBuilder("/payment-result/error");
		errorurl.append("?orderNum=").append(orderNum);
		errorurl.append("&errorCode=").append(str[0]);
		errorurl.append("&error=").append(str[1]);
		errorurl.append("&returnWhere=").append("cart");
		errorurl.append("&storageid=").append(order.getIstorageid());
		response.sendRedirect(errorurl.toString());
		return null;
	}

	@RequestMapping(value = "/doPayment", method = RequestMethod.GET)
	public String paymentToGo(String orderNum, HttpServletRequest request,
			HttpServletResponse response, Map<String, Object> model) {
		LoginContext loginCtx = this.foundation.getLoginContext();
		if (loginCtx == null) {
			throw new UserNoLoginException();
		}
		String email = loginCtx.getEmail();

		PaymentContext paymentContext = orderService.getPaymentContext(
				orderNum, foundation.getLanguage());
		Order order = paymentContext.getOrder().getOrder();

		IPaymentProvider provider = paymentService.getPaymentById(order
				.getCpaymentid());
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
		
		//校验库存
		orderService.validateInventory(order.getIstorageid(),paymentContext.getOrder().getDetails());
		
		Map<String, String> paras = provider.getDoPaymentParas(paymentContext);
		Assert.notEmpty(paras, "create ocean payment paras error");
		// 记录日志
		int site = this.foundation.getSiteID();

		JSONObject json = new JSONObject();
		json.putAll(paras);
		json.put("from", "OceanController.doPayment");

		PaymentLogEvent event = new PaymentLogEvent(site, orderNum,
				json.toJSONString(), null);
		eventBroker.post(event);
		
		//发送记录支付成功日志事件
		OrderOpreationEvent orderOpreationEvent = new OrderOpreationEvent(site,
				email, orderNum, OpreationType.OCEAN_REQUEST, JSON.toJSONString(paras));
		eventBroker.post(orderOpreationEvent);

		model.put("form", paras);
		model.put("step", 2);
		model.put("actionUrl", oceanEndPoint);
		
		String firstdomain = foundation.getSubdomains();
		if("m".equals(firstdomain)){
			return "/mobile/payment/payment_redirect";
		}else{
			return "/payment/payment_redirect";
		}
	}
}
