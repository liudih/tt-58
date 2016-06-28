package com.tomtop.loyalty.service.impl;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tomtop.loyalty.mappers.loyalty.CouponMapper;
import com.tomtop.loyalty.mappers.loyalty.OrderCouponMapper;
import com.tomtop.loyalty.mappers.loyalty.PointMapper;
import com.tomtop.loyalty.models.Coupon;
import com.tomtop.loyalty.models.OrderCoupon;
import com.tomtop.loyalty.models.OrderPoints;
import com.tomtop.loyalty.models.PromoCode;
import com.tomtop.loyalty.models.enums.Status;
import com.tomtop.loyalty.models.enums.Type;
import com.tomtop.loyalty.models.filter.PreferFilter;
import com.tomtop.loyalty.service.IPointService;
import com.tomtop.loyalty.service.IPreferCenterService;
import com.tomtop.loyalty.service.IPromoCodeService;

@Service
public class PreferCenterService implements IPreferCenterService {

	@Autowired
	OrderCouponMapper orderCouponMapper;
	@Autowired
	IPromoCodeService promoCodeService;
	@Autowired
	IPointService pointService;
	@Autowired
	CouponMapper couponMapper;
	@Autowired
	PointMapper pointMapper;

	@Override
	public boolean savePromoOrderPrefer(String email, PreferFilter prefer,
			Integer website) {
		boolean result = false;

		// 向t_member_coupon表保存一条使用记录
		Coupon coupon = new Coupon();
		String code = prefer.getCode();
		PromoCode promo = promoCodeService.getPromoCodeByCode(code,website);
		coupon.setWebsiteId(website);
		coupon.setEmail(email);
		coupon.setRuleId(promo.getRuleId());
		coupon.setCodeId(promo.getId());
		coupon.setType(Type.PROMO_CODE.getCode());
		coupon.setStatus(Status.USED.getCode());
		int couponResult = couponMapper.add(coupon);

		OrderCoupon promoOrder = new OrderCoupon();
		String orderNumber = prefer.getOrderNumber();
		promoOrder.setCcode(code);
		promoOrder.setOrderNumber(orderNumber);
		promoOrder.setCemail(email);
		promoOrder.setIstatus(1);

		if (orderCouponMapper.insert(promoOrder) == 1 && (couponResult == 1)) {
			result = true;
			return result;
		}

		return result;
	}

	@Override
	public boolean saveCouponOrderPrefer(String email, PreferFilter prefer) {
		boolean result = false;
		String code = prefer.getCode();
		if (StringUtils.isNotEmpty(code)) {
			OrderCoupon orderCoupon = new OrderCoupon();
			// int orderId = order.getIid();
			String orderNumber = prefer.getOrderNumber();
			orderCoupon.setCcode(code);
			if (!StringUtils.isEmpty(orderNumber)) {
				orderCoupon.setOrderNumber(orderNumber);
			}
			// 数字2代表已使用,新流程取消从前的锁定状态,调用此方法后coupon直接设置为已使用状态
			orderCoupon.setIstatus(2);
			orderCoupon.setCemail(email);
			result = this.addOrderCoupon(orderCoupon);
		}
		return result;
	}

	@Override
	public boolean savePointsOrderPrefer(String email, PreferFilter prefer,
			Double value, Integer website) {
		Integer point = null;
		try {
			point = Integer.parseInt(prefer.getCode());
		} catch (Exception e) {
			return false;
		}
		// 订单记录的Id
		Integer id = pointService.lockPoints(email, website, point);

		OrderPoints points = new OrderPoints();
		points.setCemail(email);
		points.setFparvalue(value);
		points.setIpointsid(id);
		points.setIstatus(1);
		String remark = "Pay for order." + "No." + prefer.getOrderNumber();
		if (1 == pointMapper.insertOrderPoint(points)
				&& pointService.updateRemarkById(remark, id)) {
			return true;
		}
		return false;
	}

	@Override
	public boolean addOrderCoupon(OrderCoupon orderCoupon) {
		int result = orderCouponMapper.insertOrderCoupon(orderCoupon);
		return result > 0 ? true : false;
	}

}
