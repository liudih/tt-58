package com.tomtop.loyalty.service.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.FluentIterable;
import com.tomtop.loyalty.controllers.CouponController;
import com.tomtop.loyalty.mappers.base.BaseMapper;
import com.tomtop.loyalty.mappers.loyalty.CouponMapper;
import com.tomtop.loyalty.models.Coupon;
import com.tomtop.loyalty.models.Prefer;
import com.tomtop.loyalty.models.Product;
import com.tomtop.loyalty.models.Rule;
import com.tomtop.loyalty.models.bo.RuleBo;
import com.tomtop.loyalty.models.enums.PreferRuleEnum;
import com.tomtop.loyalty.models.enums.PreferRuleEnum.RuleType;
import com.tomtop.loyalty.models.enums.Status;
import com.tomtop.loyalty.models.vo.CouponVo;
import com.tomtop.loyalty.models.vo.CouponVoMore;
import com.tomtop.loyalty.service.ICouponCodeService;
import com.tomtop.loyalty.service.ICouponService;

@Service
public class CouponService implements ICouponService {
	private Logger logger = LoggerFactory.getLogger(CouponService.class);

	@Autowired
	PreferRuleService preferRuleService;
	@Autowired
	PreferCodeService codeService;
	@Autowired
	CouponMapper couponMapper;
	@Autowired
	ICouponCodeService couponCodeService;
	@Autowired
	BaseMapper baseMapper;
	@Autowired
	ThirdService thirdService;
	@Value("${thirdBaseUrl}")
	private String thirdCurrencyUrl;

	@Override
	public Prefer isCouponAvailable(String code, String email,
			List<Product> products, Integer client, String terminal,
			String currency) {
		Prefer prefer = null;

		// 验证用户是否授权
		prefer = this.isMemberAuthorization(code, email,client);
		if (!prefer.isSuccess()) {
			return prefer;
		}
		// 规则验证
		RuleBo ruleBo = preferRuleService.getRuleBoByCodeEmail(code, email);
		prefer = preferRuleService.isRuleAvailable(ruleBo, products, client,
				terminal, currency);
		prefer.setPreferType(CouponController.LOYALTY_TYPE_COUPON);
		prefer.setCode(code);
		if (ruleBo.getItype() == RuleType.CASH.getCode()) {
			prefer.setExtra("cash");
		} else {
			prefer.setExtra("discount");
		}
		return prefer;
	}

	@Override
	public Prefer isMemberAuthorization(String code, String email,Integer website) {

		Prefer prefer = new Prefer();
		if (StringUtils.isEmpty(code) || StringUtils.isEmpty(email)) {
			return prefer;
		}

		Rule rule = preferRuleService.getRuleByCouponCode(code);
		if (null == rule) {
			prefer.setErrMsg("No Rule");
			return prefer;
		}
		boolean isAvailable = this.myCouponUseable(email, code,website);
		if (!isAvailable) {
			prefer.setErrMsg("No Permission");
			return prefer;
		}
		prefer.setIsSuccess(true);
		return prefer;
	}

	@Override
	public boolean myCouponUseable(String userEmail, String code,Integer website) {
		if (StringUtils.isEmpty(userEmail) || StringUtils.isEmpty(code)) {
			return false;
		}

		HashMap<String, Object> paras = new HashMap<String, Object>();
		paras.put("userEmail", userEmail);
		paras.put("code", code);
		paras.put("website", website);
		boolean isOwn = couponMapper.isOwnCoupon(paras) > 0 ? true : false;
		if (!isOwn) {
			return false;
		}
		boolean result = couponMapper.myCouponUseable(paras) > 0 ? false : true;
		return result;
	}

	@Override
	public List<Coupon> getUnusedCouponByEmail(String email,Integer website) {
		HashMap<String, Object> paras = new HashMap<String, Object>();
		paras.put("userEmail", email);
		paras.put("unused", "true");
		paras.put("website", website);
		List<Coupon> list = couponMapper.getUnusedCouponByEmail(paras);
		return list;
	}

	@Override
	public List<Coupon> getUsedCouponPageByEmail(String email, Integer page,
			Integer size, String currency,Integer website) {
		HashMap<String, Object> paras = new HashMap<String, Object>();
		paras.put("page", page);
		paras.put("pageSize", size);
		paras.put("userEmail", email);
		paras.put("used", "true");
		paras.put("website", website);
		List<Coupon> list = couponMapper.getCouponPageByEmail(paras);
		List<Coupon> result = this.convert(list, currency);
		return result;
	}

	@Override
	public List<Coupon> getUnusedCouponPageByEmail(String email, Integer page,
			Integer size, String currency,Integer website) {
		HashMap<String, Object> paras = new HashMap<String, Object>();
		paras.put("page", page);
		paras.put("pageSize", size);
		paras.put("userEmail", email);
		paras.put("unused", "true");
		paras.put("website", website);
		List<Coupon> list = couponMapper.getCouponPageByEmail(paras);
		List<Coupon> result = this.convert(list, currency);
		return result;
	}

	/**
	 * coupon展示转换
	 * 
	 */
	@SuppressWarnings("static-access")
	public List<Coupon> convert(List<Coupon> coupons, String currency) {
		if (StringUtils.isEmpty(currency)) {
			return null;
		}
		List<Coupon> result = new ArrayList<Coupon>();
		// 转换币种
		if (coupons != null) {
			FluentIterable
					.from(coupons)
					.forEach(c -> {
						// 如果是现金券,那么要把优惠券转换成当前币种

							if (!StringUtils.isEmpty(currency)) {
								try {
									int couponCurrencyId = c.getCurrency();
									String couponCurrencyCode = "";
									c.setCurrencyCode(currency);
									couponCurrencyCode = baseMapper
											.getCodeById(couponCurrencyId);
									if (!couponCurrencyCode.equals(currency)) {
										if (c.isCash()) {
											double amount = c.getPar();
											String amountString = thirdService
													.exchangeCurrency(amount,
															couponCurrencyCode,
															currency);
											c.setValue(amountString);
											// c.setValue(Utils.money(amount) +
											// "");
										}
										// 最低消费金额
										double minAmount = c.getMinAmount();
										String minAmountString = thirdService
												.exchangeCurrency(minAmount,
														couponCurrencyCode,
														currency);

										c.setMinAmount(Double
												.valueOf(minAmountString));
									} else {
										if (PreferRuleEnum.RuleType.CASH
												.getCode() == c.getValueType()) {
											c.setValue(c.getPar() + "");
										}
									}
								} catch (Exception e) {
									logger.error("exchange currency failed", e);
									c.setValue(c.getPar() + "");
								}
							}

							if (!c.isCash()) {
								// 折扣券,在值的后面加上%
								Double discount = c.getDiscount();
								if (discount != null) {
									c.setValue(discount + "% OFF");
								}
							}
							// 计算有效期
							if (PreferRuleEnum.TimeType.VALIDITY.getTypeid()
									.equals(c.getTimeType())) {
								try {
									Integer validDays = c.getValidDays();
									Date createDate = c.getCreateDate();
									if (validDays != null) {
										Calendar calendar = Calendar
												.getInstance();
										calendar.setTime(createDate);
										calendar.add(calendar.DATE, validDays);
										c.setValidStartDate(createDate);
										c.setValidEndDate(calendar.getTime());
									}
								} catch (Exception e) {
									logger.error("calculate valid date failed",
											e);
								}
							}
							result.add(c);
						});
		}
		return result;
	}

	@Override
	public Coupon createCouponByRuleid(String email, Integer ruleId,
			Integer website) {
		if (StringUtils.isEmpty(email) || null == ruleId) {
			throw new NullPointerException("email or ruleId is empty,email=="
					+ email + "ruleId==" + ruleId);
		}
		Coupon coupon = new Coupon();
		coupon.setCreator(0);
		coupon.setEmail(email);
		coupon.setRuleId(ruleId);
		coupon.setWebsiteId(website);
		coupon.setStatus(Status.SEND.getCode());
		int codeId = couponCodeService.getCodeIdByRuleId(coupon.getRuleId(),
				false, website, 0);
		coupon.setCodeId(codeId);
		if (codeId == 0) {
			throw new NullPointerException(
					"This rule no longer exists or no publish,email=={}"
							+ email + "ruleId==" + ruleId);
		}
		String code = couponCodeService.getCodeById(codeId);
		coupon.setCode(code);

		int result = this.add(coupon);
		return result > 0 ? coupon : null;
	}

	@Override
	public int add(Coupon c) {
		int result = couponMapper.add(c);
		return result;
	}

	@Override
	public int getTotalByCondion(Map<String, Object> map) {
		int result = couponMapper.getTotalMyCoupon(map);
		return result;
	}

	@Override
	public int getUsedTotal(String email,Integer website) {
		Map<String, Object> paras = new HashMap<String, Object>();
		paras.put("userEmail", email);
		paras.put("used", "true");
		paras.put("website", website);
		int result = this.getTotalByCondion(paras);
		return result;
	}

	@Override
	public int getUnusedTotal(String email,Integer website) {
		Map<String, Object> paras = new HashMap<String, Object>();
		paras.put("userEmail", email);
		paras.put("unused", "true");
		paras.put("website", website);
		int result = this.getTotalByCondion(paras);
		return result;
	}

	@Override
	public List<CouponVo> convertVo(List<Coupon> coupons, String currency) {
		if (StringUtils.isEmpty(currency)) {
			return null;
		}
		List<CouponVo> result = new ArrayList<CouponVo>();
		// 转换币种
		if (coupons != null) {
			FluentIterable.from(coupons).forEach(c -> {
				// 如果是现金券,那么要把优惠券转换成当前币种
					CouponVo vo = new CouponVo();
					vo.setCode(c.getCode());
					if (!StringUtils.isEmpty(currency)) {
						try {
							int couponCurrencyId = c.getCurrency();
							String couponCurrencyCode = "";
							c.setCurrencyCode(currency);
							couponCurrencyCode = baseMapper
									.getCodeById(couponCurrencyId);
							if (!couponCurrencyCode.equals(currency)) {
								if (c.isCash()) {
									double amount = c.getPar();
									String amountString = thirdService
											.exchangeCurrency(amount,
													couponCurrencyCode,
													currency);
									vo.setIsCash(true);
									vo.setValue(amountString);
									vo.setUnit(currency);
									if("JPY".equalsIgnoreCase(currency))vo.setValue(amountString.split("\\.")[0]);
								}

							} else {
								if (c.isCash()) {
									vo.setIsCash(true);
									vo.setValue(c.getPar() + "");
									vo.setUnit(currency);
								}
							}
						} catch (Exception e) {
							logger.error("exchange currency failed", e);
							vo.setIsCash(true);
							vo.setUnit(currency);
							vo.setValue(c.getPar() + "");
						}
					}

					if (!c.isCash()) {
						// 折扣券,在值的后面加上%
						Double discount = c.getDiscount();
						if (discount != null) {
							vo.setIsCash(false);
							vo.setValue(discount + "%");
							vo.setUnit("OFF");
						}
					}
					result.add(vo);
				});
		}
		return result;
	}

	/**
	 * 
	  * @Description: 转换vomore
	  * @param  coupons
	  * @param  currency
	  * @author liuyufeng 
	  * @return 
	  * @date 2016年5月20日 上午9:32:37
	 */
	@Override
	public List<CouponVoMore> convertVoMore(List<Coupon> coupons, String currency) {
		logger.info("------coupons="+JSONObject.toJSONString(coupons));
		if(coupons==null) return null;
		List<CouponVoMore> result = new ArrayList<CouponVoMore>();
		for(Coupon c : coupons){
			CouponVoMore vo = null;
			// 如果是现金券,那么要把优惠券转换成当前币种
			String couponCurrencyCode = baseMapper.getCodeById(c.getCurrency());
			if (!StringUtils.isEmpty(currency)) {
				try {
					vo = new CouponVoMore(c.getCode(), true, c.getPar() + "", currency, c.getValidStartDate(), c.getValidEndDate(), c.getMinAmount(), c.getValidDays(), c.getCreateDate());
					if("Validity".equals(c.getTimeType())){//Validity有效时间/Date有效时间区间
						vo.setValidStartDate(c.getCreateDate()); 
						vo.setValidEndDate(DateUtils.addDays(c.getCreateDate(), c.getValidDays()));
					}
					if(c.isCash()){//现金券
						if(!couponCurrencyCode.equals(currency)){
							String amountString = thirdService.exchangeCurrency(c.getPar(),couponCurrencyCode,currency);
							vo.setValue(amountString);
							if("JPY".equalsIgnoreCase(currency))vo.setValue(amountString.split("\\.")[0]);
						}
					}else{//折扣券
						if (c.getDiscount() != null) {
							vo.setIsCash(false);
							vo.setValue(c.getDiscount() + "%");// 折扣券,在值的后面加上%
							vo.setUnit("OFF");
						}
					}
					if(!couponCurrencyCode.equals(currency)){
						String minAmount = thirdService.exchangeCurrency(c.getMinAmount(),couponCurrencyCode,currency);
						vo.setMinAmount(Double.valueOf(minAmount));
						if("JPY".equalsIgnoreCase(currency))vo.setMinAmount(Double.valueOf(minAmount.split("\\.")[0]));
					}
				} catch (Exception e) {
					logger.error("exchange currency failed", e);
					vo.setIsCash(true);
					vo.setUnit(couponCurrencyCode);
					vo.setValue(c.getPar() + "");
				}
			}
			result.add(vo);
		}
		logger.info("------CouponVoMoreList="+JSONObject.toJSONString(result));
		return result;
	}

}
