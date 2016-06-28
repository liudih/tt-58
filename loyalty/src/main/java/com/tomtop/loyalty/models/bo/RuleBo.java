package com.tomtop.loyalty.models.bo;

import java.io.Serializable;
import java.util.List;

import com.tomtop.loyalty.models.Rule;
import com.tomtop.loyalty.models.enums.PreferRuleEnum;
import com.tomtop.loyalty.models.enums.PreferRuleEnum.TimeType;

/**
 * 优惠规则
 * 
 * @author xiaoch
 *
 */
public class RuleBo extends Rule implements Serializable {

	private static final long serialVersionUID = 1L;
	// 有效时间类型(优惠券产生之日起有效天数 or 有效日期段)
	private PreferRuleEnum.TimeType timeType;

	// 优惠券类型(现金券 or 折扣券)
	// private CouponRuleBack.CouponType type;
	// 排除的产品类型
	private List<String> excludeProductTypeIds;

	// 排除的品类过滤ID
	private List<Integer> excludeCategoryIds;
	
	// 站点id
	private List<String> websiteIds;

	// 终端类型
	private List<String> useTerminal;

	// 允许使用的sku
	private List<String> skus;

	public List<String> getWebsiteIds() {
		return websiteIds;
	}

	public void setWebsiteIds(List<String> websiteIds) {
		this.websiteIds = websiteIds;
	}

	public PreferRuleEnum.TimeType getTimeType() {
		return timeType;
	}

	public void setCtimetype(String timeType) {
		this.timeType = TimeType.get(timeType);
	}

	public List<String> getExcludeProductTypeIds() {
		return excludeProductTypeIds;
	}

	public void setExcludeProductTypeIds(List<String> excludeProductTypeIds) {
		this.excludeProductTypeIds = excludeProductTypeIds;
	}

	public List<Integer> getExcludeCategoryIds() {
		return excludeCategoryIds;
	}

	public void setExcludeCategoryIds(List<Integer> excludeCategoryIds) {
		this.excludeCategoryIds = excludeCategoryIds;
	}

	public List<String> getUseTerminal() {
		return useTerminal;
	}

	public void setUseTerminal(List<String> useTerminal) {
		this.useTerminal = useTerminal;
	}

	public List<String> getSkus() {
		return skus;
	}

	public void setSkus(List<String> skus) {
		this.skus = skus;
	}

}
