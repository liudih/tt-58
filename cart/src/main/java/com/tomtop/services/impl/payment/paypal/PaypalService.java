package com.tomtop.services.impl.payment.paypal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tomtop.dto.paypal.PaypalLog;
import com.tomtop.mappers.payment.PaypalReturnLogMapper;

@Service
public class PaypalService {

	@Autowired
	PaypalReturnLogMapper paypalReturnLogMapper;

	/**
	 * @author lijun
	 * @param iwebsiteid
	 * @param orderNum
	 * @param content
	 *            日志内容
	 * @param transactionId
	 *            交易号
	 */
	public void insertLog(Integer websiteId, String orderNum, String content,
			String transactionId) {
		PaypalLog log = new PaypalLog();
		log.setIwebsiteid(websiteId);
		log.setCorderid(orderNum);
		log.setCcontent(content);
		log.setCtransactionid(transactionId);
		paypalReturnLogMapper.insert(log);
	}

}
