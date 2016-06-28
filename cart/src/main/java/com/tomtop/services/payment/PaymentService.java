package com.tomtop.services.payment;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.tomtop.services.impl.base.SystemParameterService;
import com.tomtop.utils.FoundationService;

@Service
public class PaymentService implements IPaymentService {

	@Autowired
	Set<IPaymentProvider> providers;

	@Autowired
	private SystemParameterService parameterService;

	@Autowired
	private FoundationService foundation;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * services.order.payment.IPaymentService#getPaymentById(java.lang.String)
	 */
	@Override
	public IPaymentProvider getPaymentById(String id) {
		if (StringUtils.isEmpty(id)) {
			return null;
		}
		Map<String, IPaymentProvider> map = Maps.uniqueIndex(providers,
				e -> e.id());
		return map.get(id);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see services.order.payment.IPaymentService#getMap()
	 */
	@Override
	public Map<String, IPaymentProvider> getMap() {
		return Maps.uniqueIndex(providers, e -> e.id());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * services.order.payment.IPaymentService#filterByOrderTag(java.util.List)
	 */
	@Override
	public List<String> filterByOrderTag(List<String> tags) {
		StringBuffer str = new StringBuffer();
		for (String string : tags) {
			String p = parameterService.getSystemParameter(
					foundation.getSiteID(), foundation.getLanguage(), string
							+ "_payment");
			if (StringUtils.isNotEmpty(p)) {
				str.append(p + ",");
			}
		}
		if (StringUtils.isNotEmpty(str.toString())) {
			List<String> payments = Lists.newArrayList(str.toString()
					.split(","));
			payments = Lists.newArrayList(Collections2.filter(payments,
					p -> StringUtils.isNotEmpty(p)));
			return payments;
		}
		return Lists.newArrayList();
	}
}
