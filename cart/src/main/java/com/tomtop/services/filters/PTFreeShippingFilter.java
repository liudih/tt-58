package com.tomtop.services.filters;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tomtop.entity.order.ShippingMethodRequst;
import com.tomtop.services.ICurrencyService;
import com.tomtop.services.order.IShippingMethodService;
import com.tomtop.valueobjects.order.ShippingMethodInformation;

/**
 * 如果存在免费，且订单中商品全为免邮，总价超过 30 USD，则免邮的物流费用为0
 * 
 * @author luoJH
 *
 */
@Service
public class PTFreeShippingFilter implements IFreightFilter {

	@Autowired
	ICurrencyService currencyService;

	@Autowired
	IShippingMethodService shippingMethodService;

	@Override
	public List<ShippingMethodInformation> processing(
			List<ShippingMethodInformation> list, ShippingMethodRequst requst) {
		double usdTotal = currencyService.exchange(requst.getGrandTotal(),
				requst.getCurrency(), "USD");

		boolean isAllfree = shippingMethodService.checkIsAllfree(requst
				.getListingIds());
		List<String> pcodelist = shippingMethodService.getPcodeList();

		for (ShippingMethodInformation sm : list) {

			if ((isAllfree && pcodelist.contains(sm.getCode()) && requst
					.getShippingWeight() == 0)
					&& (!sm.isBistracking() || (sm.isBistracking() && usdTotal >= 30))) {
				sm.setFreight(0.0);
			}
		}
		return list;
	}

	@Override
	public int order() {
		return 100;
	}

}
