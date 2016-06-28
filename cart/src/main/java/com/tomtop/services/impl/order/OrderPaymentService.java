package com.tomtop.services.impl.order;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.tomtop.dto.order.OrderPayment;
import com.tomtop.forms.PlaceOrderForm;
import com.tomtop.mappers.order.OrderPaymentMapper;

@Service
public class OrderPaymentService {
	@Autowired
	OrderPaymentMapper paymentMapper;

	public boolean saveOrUpdate(OrderPayment op) {
		int i = paymentMapper.update(op);
		if (i != 1) {
			paymentMapper.insert(op);
		}
		return true;
	}

	public boolean createOrderPayment(String orderId, String paymentId,
			PlaceOrderForm from) {
		OrderPayment op = new OrderPayment();
		op.setCjson(JSONObject.toJSONString(from));
		op.setCorderid(orderId);
		op.setCpaymentid(paymentId);
		return saveOrUpdate(op);
	}

	public PlaceOrderForm getForm(String orderId, String paymentId) {
		OrderPayment op = paymentMapper.selectByOrderId(orderId, paymentId);
		if(op == null){
			op = paymentMapper.selectByOrderIdOnly(orderId);
		}
		if (null != op && StringUtils.isNotEmpty(op.getCjson())) {
			return JSONObject.parseObject(op.getCjson(), PlaceOrderForm.class);
		}
		return null;
	}
}
