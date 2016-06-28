package com.tomtop.loyalty.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.FluentIterable;
import com.tomtop.framework.core.utils.BeanUtils;
import com.tomtop.loyalty.mappers.base.BaseMapper;
import com.tomtop.loyalty.mappers.loyalty.CodeMapper;
import com.tomtop.loyalty.mappers.loyalty.RuleMapper;
import com.tomtop.loyalty.models.Coupon;
import com.tomtop.loyalty.models.Prefer;
import com.tomtop.loyalty.models.PreferCode;
import com.tomtop.loyalty.models.Product;
import com.tomtop.loyalty.models.PromoCode;
import com.tomtop.loyalty.models.Rule;
import com.tomtop.loyalty.models.bo.RuleBo;
import com.tomtop.loyalty.models.enums.PreferRuleEnum;
import com.tomtop.loyalty.models.enums.PreferRuleEnum.RuleType;
import com.tomtop.loyalty.service.IPreferRuleService;
import com.tomtop.loyalty.service.IPromoCodeService;
import com.tomtop.loyalty.utils.PreferUtil;

/**
 * 优惠规则
 * 
 * @author xiaoch
 *
 */
@Service
public class PreferRuleService implements IPreferRuleService {
	@Autowired
	CodeMapper codeMapper;
	@Autowired
	RuleMapper ruleMapper;
	@Autowired
	BaseMapper baseMapper;
	@Autowired
	ThirdService thirdService;
	@Autowired
	IPromoCodeService promoCodeService;

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Override
	public Prefer isRuleAvailable(RuleBo rule, List<Product> products,
			Integer client, String terminal, String currency) {
		Prefer preferResult = new Prefer();
		if (null == rule) {
			preferResult.setErrMsg("No Rule");
			return preferResult;
		}
//		logger.info("---products1="+JSONObject.toJSONString(products));
		//筛选优惠券是否为促销价商品
		if("OffPrice".equals(rule.getCproducttype())){
			//如果商品标签中包含OffPrice，则将这个商品过滤掉
			products = FluentIterable.from(products).filter(c -> {
						return !c.getLables().contains("OffPrice");
					}).toList();
		}
//		logger.info("---products2="+JSONObject.toJSONString(products));
		
		// 筛选符合规则的产品
		List<Product> availableProducts = this.getAvailableProduct(products,
				rule);
		if (CollectionUtils.isNotEmpty(availableProducts)) {
			// 将优惠产品做最后校验
			preferResult = this.matchRule(availableProducts, rule, client,
					terminal, currency);
			if (!preferResult.isSuccess()) {
				return preferResult;
			}
			Double value = this.calculatePrefer(availableProducts, rule,
					currency);
			preferResult.setIsSuccess(true);
			// preferResult.setCode(code);
			preferResult.setValue(value);
			// preferResult.setPreferType(LOYALTY_TYPE_COUPON);
			// if (rule.getType() == CouponType.CASH) {
			// loyaltyPrefer.setExtra("cash");
			// } else {
			// loyaltyPrefer.setExtra("discount");
			// }
			return preferResult;
		} else {
			preferResult.setErrMsg("There is no available goods");
			return preferResult;
		}

	}

	@Override
	public Rule getRuleByRuleid(Integer ruleId) {
		Rule couponRule = ruleMapper.get(ruleId);
		List<Integer> checkIds = getTreeCheckByRuleId(ruleId);
		couponRule.setExcludeCategoryIds(checkIds);
		return couponRule;
	}

	/**
	 * 根据规则id获取所有选中的品类id
	 * 
	 * @param ruleId
	 * @return
	 */
	public List<Integer> getTreeCheckByRuleId(Integer ruleId) {
		List<Integer> checkIds = ruleMapper.getTreeCheckByRuleId(ruleId);
		return checkIds;
	}
	
	/**
	 * @function 根据规则名获取规则iid
	 * @param cname
	 * @return
	 */
	public Integer getRuleIidByName(String cname){
		return ruleMapper.getRuleIidByName(cname);
	}

	@Override
	public Double calculatePrefer(List<Product> products, RuleBo rule,
			String currency) {
		String code = null;
		try {
			Double amount = rule.getFcouponamount();
			Integer originCurrencyId = rule.getCcurrency();

			double value = 0;
			if (null != rule.getItype()
					&& (rule.getItype() == RuleType.CASH.getCode())
					&& amount != null && currency != null) {
				try {
					String originCurrencyCode = baseMapper
							.getCodeById(originCurrencyId);
					value = Double.valueOf(thirdService.exchangeCurrency(
							amount, originCurrencyCode, currency));
					// 针对一些货币精度做处理
					// String couponValue = PreferUtil.money(value, currency)
					// .replaceAll(",", "");
					// value = Double.valueOf(couponValue);
				} catch (Exception e) {
					logger.error("exchange currency failed", e);
					return 0D;
				}
			} else {
				Float rate = rule.getFdiscount();
				if (rate != null) {
					if (CollectionUtils.isNotEmpty(products)) {
						double total = PreferUtil.subTotal(products);
						double rd = rate / 100;
						if (rd >= 0.0 && rd <= 1.0) {
							value = total * rd;
						}
					} else {
						logger.debug("计算prepareOrderInstance折扣时该购物车里面没有符合规则的商品");
					}
				}
			}
			if (value > 0) {
				return -value;
			}
		} catch (Exception e) {
			logger.error("coupon:{} use failed", code, e);
		}
		return null;
	}

	/**
	 * 针对code规则判断是否匹配
	 * 
	 * @param cartItems
	 * @param rule
	 * @param webContext
	 * @return
	 */
	@SuppressWarnings("static-access")
	private Prefer matchRule(List<Product> cartItems, RuleBo rule,
			Integer client, String terminal, String currency) {
		Prefer preferResult = new Prefer();
		if (rule == null) {
			preferResult.setErrMsg("noRule");
			return preferResult;
		}
		// 终端类型做判断
		if (null == terminal) {
			preferResult.setErrMsg("No Terminal");
			return preferResult;
		}
		if (CollectionUtils.isEmpty(rule.getUseTerminal())) {
			preferResult.setErrMsg("No Available Terminal");
			return preferResult;
		}
		if (!(rule.getUseTerminal().contains(terminal))) {

			String warn = "Coupon can be only used on xxx platform";
			warn = warn.replace("xxx", rule.getUseTerminal().toString());
			preferResult.setErrMsg(warn);
			return preferResult;
		}
		// 站点判断
		if (CollectionUtils.isNotEmpty(rule.getWebsiteIds())) {
			if (!rule.getWebsiteIds().contains(String.valueOf(client))) {
				preferResult.setErrMsg("This site can not be applied");
				return preferResult;

			}
		}
		// Cart cart = (Cart) context.getActionOn();
		// 总金额金额
		double totalAmount = 0;

		Date now = new Date();
		// 最低消费金额
		Double limitAmount = rule.getForderamountlimit();
		// 把最低消费金额转换成用户所在国家的金额
		if (rule.getTimeType() != null) {

			switch (rule.getTimeType()) {
			case VALIDITY:
				// 优惠券生成日期=有效日期的开始时间
				Date start = rule.getDcreatedate();
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(start);
				Integer validity = rule.getIvalidity();
				calendar.add(calendar.DATE, validity);
				// 结束时间
				Date end = calendar.getTime();
				if (end.getTime() < now.getTime()) {
					preferResult
							.setErrMsg("Coupon is not within the scope of use period");
					return preferResult;
				}
				break;

			case DATE:
				// 开始时间
				Date startDate = rule.getStartdate();
				Date endDate = rule.getEnddate();
				if (endDate.getTime() < now.getTime()
						|| now.getTime() < startDate.getTime()) {
					preferResult
							.setErrMsg("Coupon is not within the scope of use period");
					return preferResult;
				}
				break;
			}
		} else {
			preferResult.setErrMsg("Validity period has a problem");
			return preferResult;
		}
		// 计算最低消费金额
		totalAmount = PreferUtil.subTotal(cartItems);
		// limitAmount不为null则代表有最小消费金额限制
		if (limitAmount != null) {
			// 把面值转换成用户所在国家币值

			if (StringUtils.isEmpty(currency)) {
				preferResult.setErrMsg("currency is null!");
				return preferResult;
			}
			String ruleCurrencyCode = baseMapper.getCodeById(rule
					.getCcurrency());
			if (!currency.equals(ruleCurrencyCode)) {
				try {
					limitAmount = Double.valueOf(thirdService.exchangeCurrency(
							limitAmount, ruleCurrencyCode, currency));

				} catch (Exception e) {
					preferResult.setErrMsg("exchange currency failed!");
					return preferResult;
				}

			}

			if (limitAmount > totalAmount) {
				String warn = "Your order is less than xxx , you can not use Coupon";
				warn = warn.replaceAll("xxx", currency + limitAmount);
				preferResult.setErrMsg(warn);
				return preferResult;
			}
		}

		preferResult.setIsSuccess(true);
		return preferResult;
	}

	@Override
	public RuleBo getRuleBoByRuleid(Integer ruleId) {
		return null;
	}

	@Override
	public Rule getRuleByCouponCode(String code) {
		Integer ruleId = codeMapper.getRuleIdByCode(code);
		if (null == ruleId) {
			return null;
		}
		Rule couponRule = this.getRuleByRuleid(ruleId);
		return couponRule;
	}

	@Override
	public RuleBo getRuleBoByCodeEmail(String code, String email) {
		Rule rule = this.getRuleByCouponCode(code);
		RuleBo ruleBo = new RuleBo();
		BeanUtils.copyPropertys(rule, ruleBo);
		// 获取创建时间 把优惠券的创建时间写入rule
		PreferCode cc = codeMapper.getPreferCodeByCouponcode(code);
		if (null != cc) {
			ruleBo.setDcreatedate(cc.getDcreatdate());
		}
		// 设置站点ids
		String websites = rule.getCwebsiteid();
		String[] arraySites = org.apache.commons.lang3.StringUtils.split(
				websites, ",");
		if (null != arraySites && arraySites.length > 0) {
			List<String> ruleWebsite = Arrays.asList(arraySites);
			ruleBo.setWebsiteIds(ruleWebsite);
		}
		// 设置允许使用sku
		String skus = rule.getCsku();
		String[] arraySku = org.apache.commons.lang3.StringUtils.split(skus,
				",");
		if (null != arraySku && arraySku.length > 0) {
			List<String> rulesku = Arrays.asList(arraySku);
			ruleBo.setSkus(rulesku);
		}
		// 设置允许使用终端
		String terminals = rule.getCuseterminal();
		String[] arrayTerminal = org.apache.commons.lang3.StringUtils.split(
				terminals, ",");
		List<String> list = new ArrayList<String>(5);
		if (null != arrayTerminal && arrayTerminal.length > 0) {
			for (int i = 0; i < arrayTerminal.length; i++) {
				PreferRuleEnum.UseTerminal useTerminal = PreferRuleEnum.UseTerminal
						.getUseTerminal(arrayTerminal[i]);
				list.add(useTerminal.getTerminalEN());
			}
			ruleBo.setUseTerminal(list);
		}
		return ruleBo;
	}

	@Override
	public List<Product> getAvailableProduct(List<Product> products,
			RuleBo ruleBo) {
		final List<Integer> excludeCategoryIds = ruleBo.getExcludeCategoryIds();
		final List<String> excludeLabelType = ruleBo.getExcludeProductTypeIds();
		logger.debug("购物车里有{}件商品", products.size());

		// 满足规则的产品
		List<Product> validProduct = FluentIterable
				.from(products)
				.filter(c -> {
					Product listingid = c;
					// 该商品最细级别类目id
					List<Integer> productCategoryIds = c.getCategoryIds();
					// 改商品的商品标签
					List<String> productLabels = c.getLables();

					// 判断sku是否满足,当sku存在时,所有的排除商品品类以及排除商品标签都失效
					List<String> skus = ruleBo.getSkus();
					if (null != skus && skus.size() > 0) {
						String sku = c.getSku();
						if (skus.contains(sku)) {
							return true;
						} else {
							logger.error("sku does not meet the rules,sku=={}",
									sku);
							return false;
						}
					}

					// if (lc != null) {
					// if(CollectionUtils.isNotEmpty(excludeCategoryIds)){

					// 判断产品目录是否满足规则
					// 获取最小级别目录
					logger.debug("category != null:{}",
							productCategoryIds != null);
					logger.debug("excludeCategoryIds != null:{}",
							excludeCategoryIds != null);
					logger.debug("{}", excludeCategoryIds);
					if (CollectionUtils.isNotEmpty(excludeCategoryIds)
							&& CollectionUtils.isNotEmpty(productCategoryIds)) {
						logger.debug("==========开始判断商品Category===========");
						List<Integer> invalidCategory = FluentIterable
								.from(productCategoryIds)
								.filter(l -> {

									logger.debug("商品{}所属Category:{}",
											listingid, l);
									if (excludeCategoryIds.contains(l)) {
										logger.debug("商品{}属于排除Category{}",
												listingid, l);
										return true;
									}
									return false;
								}).toList();
						if (invalidCategory.size() > 0) {
							logger.debug(
									"产品{}属于规则排除Category内,所以该产品不参与最低消费金额的计算",
									listingid);
							return false;
						}
					}
					// }
					// 判断产品类别是否满足规则
					if (CollectionUtils.isNotEmpty(productLabels)
							&& CollectionUtils.isNotEmpty(excludeLabelType)) {
						logger.debug("==========开始判断商品标签===========");
						List<String> invalidLabel = FluentIterable
								.from(productLabels)
								.filter(l -> {
									logger.debug("商品{}标签:{}", listingid, l);
									if (excludeLabelType.contains(l)) {
										logger.debug("商品{}属于排除标签{}", listingid,
												l);
										return true;
									}
									return false;
								}).toList();
						logger.debug("==========结束判断商品标签===========");
						if (invalidLabel.size() > 0) {
							logger.debug("产品{}属于规则排除标签内,所以该产品不参与最低消费金额的计算",
									listingid);
							return false;
						}
					}

					// }// **************************************

					return true;
				}).toList();
		return validProduct;
	}

	@Override
	public RuleBo getRuleBoByPromo(String code, Integer website) {
		Rule rule = this.getRuleByPromo(code, website);
		if (null == rule) {
			return null;
		}
		RuleBo ruleBo = new RuleBo();
		BeanUtils.copyPropertys(rule, ruleBo);
		// 获取创建时间 把优惠券的创建时间写入rule
		PreferCode cc = codeMapper.getPreferCodeByCouponcode(code);
		if (null != cc) {
			ruleBo.setDcreatedate(cc.getDcreatdate());
		}
		// 设置站点ids
		String websites = rule.getCwebsiteid();
		String[] arraySites = org.apache.commons.lang3.StringUtils.split(
				websites, ",");
		if (null != arraySites && arraySites.length > 0) {
			List<String> ruleWebsite = Arrays.asList(arraySites);
			ruleBo.setWebsiteIds(ruleWebsite);
		}
		// 设置允许使用sku
		String skus = rule.getCsku();
		String[] arraySku = org.apache.commons.lang3.StringUtils.split(skus,
				",");
		if (null != arraySku && arraySku.length > 0) {
			List<String> rulesku = Arrays.asList(arraySku);
			ruleBo.setSkus(rulesku);
		}
		// 设置允许使用终端
		String terminals = rule.getCuseterminal();
		String[] arrayTerminal = org.apache.commons.lang3.StringUtils.split(
				terminals, ",");
		List<String> list = new ArrayList<String>(5);
		if (null != arrayTerminal && arrayTerminal.length > 0) {
			for (int i = 0; i < arrayTerminal.length; i++) {
				PreferRuleEnum.UseTerminal useTerminal = PreferRuleEnum.UseTerminal
						.getUseTerminal(arrayTerminal[i]);
				list.add(useTerminal.getTerminalEN());
			}
			ruleBo.setUseTerminal(list);
		}
		return ruleBo;
	}

	@Override
	public Rule getRuleByPromo(String code, Integer website) {
		PromoCode promoCode = promoCodeService
				.getPromoCodeByCode(code, website);
		if (null == promoCode) {
			return null;
		}
		Integer ruleId = promoCode.getRuleId();
		if (null == ruleId) {
			return null;
		}
		Rule couponRule = this.getRuleByRuleid(ruleId);
		return couponRule;
	}

}
