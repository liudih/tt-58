package com.tomtop.dao.impl.payment;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tomtop.dao.payment.IPaymentCallbackDao;
import com.tomtop.mappers.order.PaymentCallbackMapper;
import com.tomtop.valueobjects.order.PaymentCallback;

@Service
public class PaymentCallbackDao implements IPaymentCallbackDao {
	@Autowired
	PaymentCallbackMapper mapper;

	@Override
	public int insert(PaymentCallback pc) {
		return mapper.insert(pc);
	}

	@Override
	public List<PaymentCallback> getByOrderNumberAndSiteID(String orderNumber,
			Integer site) {
		return mapper.getByOrderNumberAndSiteID(orderNumber, site);
	}
}
