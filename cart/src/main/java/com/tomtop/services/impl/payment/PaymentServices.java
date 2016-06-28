package com.tomtop.services.impl.payment;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.tomtop.dto.order.Order;
import com.tomtop.entity.payment.paypal.PaymentBase;
import com.tomtop.entity.payment.paypal.PaypaiReturn;
import com.tomtop.entity.payment.paypal.PaymentLogEvent;
import com.tomtop.mappers.payment.PaymentMapper;
import com.tomtop.mappers.payment.PaypaiReturnLogMapper;
import com.tomtop.services.ICurrencyService;

@Service
public class PaymentServices {

	@Autowired
	PaymentMapper paymentmapper;

	@Autowired
	PaypaiReturnLogMapper paypaiReturnLogMapper;

	@Autowired
	ICurrencyService currencyService;

	public PaymentBase GetPayment(Order order) {
		double dprice = currencyService.exchange(order.getFgrandtotal(),
				order.getCcurrency(), "USD");
		return paymentmapper.getPaymentAccountBase(order.getIwebsiteid(),
				dprice);
	}

	public void InsertLog(Integer iwebsiteid, String corderid, String content) {
		paypaiReturnLogMapper.Insert(iwebsiteid, corderid, content, null);
	}

	/**
	 * @author lijun
	 * @param iwebsiteid
	 * @param corderid
	 *            orderid 或者 orderNum
	 * @param content
	 *            日志内容
	 * @param transactionId
	 *            交易号
	 */
	public void InsertLog(Integer iwebsiteid, String corderid, String content,
			String transactionId) {
		paypaiReturnLogMapper.Insert(iwebsiteid, corderid, content,
				transactionId);
	}

	public PaypaiReturn getPaypaiReturnByOrderId(String orderId) {
		PaymentLogEvent paypaiReturnLog = paypaiReturnLogMapper
				.getPaypaiReturnLogByOrderId(orderId);
		if (paypaiReturnLog != null) {
			PaypaiReturn paypaiReturn = JSONObject.parseObject(
					paypaiReturnLog.getContent(), PaypaiReturn.class);
			return paypaiReturn;
		} else {
			return null;
		}
	}

	/**
	 * 根据订单号，获取数据信息
	 * 
	 * @param corderid
	 * @return
	 */
	public List<PaymentLogEvent> getPaypaiReturnLogByOrderIds(String corderid) {
		return paypaiReturnLogMapper.getPaypaiReturnLogByOrderIds(corderid);
	}
}
