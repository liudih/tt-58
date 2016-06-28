package com.tomtop.services.impl.order;

import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;
import com.tomtop.exceptions.DiscountException;
import com.tomtop.services.impl.LoyaltyService;
import com.tomtop.services.impl.LoyaltyService.Type;
import com.tomtop.services.order.ICheckoutService;
import com.tomtop.services.order.IFreightService;
import com.tomtop.utils.CookieUtils;
import com.tomtop.utils.DoubleCalculateUtils;
import com.tomtop.utils.FoundationService;
import com.tomtop.valueobjects.CartItem;
import com.tomtop.valueobjects.Discount;
import com.tomtop.valueobjects.DiscountUsedState;
import com.tomtop.valueobjects.base.LoginContext;
import com.tomtop.valueobjects.payment.CheckoutDetails;

/**
 * 结算中心
 * 
 * @author lijun
 *
 */
@Service
public class CheckoutService implements ICheckoutService {

	private static final Logger Logger = LoggerFactory
			.getLogger(CheckoutService.class);

	@Autowired
	IFreightService freightService;

	@Autowired
	FoundationService foundationService;

	@Autowired
	LoyaltyService loyaltyService;

	@Override
	public Double subToatl(List<CartItem> items) {
		DoubleCalculateUtils duti = new DoubleCalculateUtils(0.0);
		for (CartItem ci : items) {
			if(ci!=null && ci.getPrice()!=null){
				duti = duti.add(ci.getPrice().getPrice());
			}
		}
		return duti.doubleValue();
	}

	@Override
	public CheckoutDetails sum(List<CartItem> items, double freight,
			String currency) {
		if (items == null || items.size() == 0) {
			throw new NullPointerException("CartItems is null");
		}
		if (currency == null || currency.length() == 0) {
			throw new NullPointerException("currency is null");
		}

		double subTotal = this.subToatl(items);

		int website = this.foundationService.getSiteID();
		LoginContext logCtx = this.foundationService.getLoginContext();

		final String email = logCtx.getEmail();

		// 非用户会使用推广码
		// email = "guest@guest.com";

		// recode site需考虑
		int site = website;

		// 经验证过的合法优惠券
		List<DiscountUsedState> validDiscount = Lists.newLinkedList();

		// 校验优惠券是否合法
		List<Discount> discount = loyaltyService.getUsedDiscount();
		Iterator<Discount> iterator = discount.iterator();
		while (iterator.hasNext()) {
			Discount d = iterator.next();
			// 用户未登陆的情况下是不允许使用优惠券和积分
			if (StringUtils.isEmpty(email) && Type.promo != d.getType()) {
				throw new DiscountException(d.getCode(), d.getType(),
						"user not login,so can not use coupon or point");
			}
			DiscountUsedState valid = loyaltyService.apply(d.getCode(),
					website, site, email, currency, items, d.getType());
			if (!valid.isSucceed()) {
				// 如果无效则清除cookie
				if (Type.point == d.getType()) {
					CookieUtils.removeCookie(LoyaltyService.LOYALTY_TYPE_POINT);
				} else {
					CookieUtils.removeCookie(LoyaltyService.LOYALTY_PREFER);
				}
				iterator.remove();

			} else {
				d.setDiscount(valid.getDiscount());
				validDiscount.add(valid);
			}

		}

		Double extraTotal = 0.0;
		for (DiscountUsedState cell : validDiscount) {
			// 记录一下日志然后好追踪
			Logger.debug("{}:{}", cell.getCode(), cell.getDiscount());
			extraTotal = extraTotal + cell.getDiscount();
		}
		DoubleCalculateUtils dcu = new DoubleCalculateUtils(subTotal);
		dcu = dcu.add(freight).add(extraTotal);
		double result = dcu.doubleValue();

		if (result < 0.0) {
			result = 0.0;
		}

		CheckoutDetails details = new CheckoutDetails(result, subTotal,
				extraTotal, discount);

		return details;
	}
}
