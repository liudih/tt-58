package com.tomtop.loyalty.service;

import java.util.List;

import com.tomtop.loyalty.models.Prefer;
import com.tomtop.loyalty.models.Product;
import com.tomtop.loyalty.models.Rule;
import com.tomtop.loyalty.models.bo.RuleBo;

/**
 * 优惠规则
 * 
 * @author xiaoch
 *
 */
public interface IPreferRuleService {
	/**
	 * 规则是否可用
	 * 
	 * @param code
	 * @param listingIds
	 * @param client
	 * @param terminal
	 * @return
	 */
	public Prefer isRuleAvailable(RuleBo rule, List<Product> products,
			Integer client, String terminal, String currency);

	/**
	 * 根据规则id获取优惠规则
	 * 
	 * @param ruleId
	 * @return
	 */
	public Rule getRuleByRuleid(Integer ruleId);

	/**
	 * 获取符合优惠规则的商品
	 * 
	 * @param cartItems
	 * @param ruleBo
	 * @return
	 */
	public List<Product> getAvailableProduct(List<Product> products,
			RuleBo ruleBo);

	/**
	 * 计算商品总价
	 * 
	 * @param validCartItems
	 * @param rule
	 * @return
	 */
	public Double calculatePrefer(List<Product> products, RuleBo ruleBo,
			String currency);

	/**
	 * 根据规则id获取优惠Bo
	 * 
	 * @param ruleId
	 * @return
	 */
	public RuleBo getRuleBoByRuleid(Integer ruleId);

	/**
	 * 根据couponCode查询rule
	 * 
	 * @param ruleId
	 * @return
	 */
	public Rule getRuleByCouponCode(String code);

	public RuleBo getRuleBoByCodeEmail(String code, String email);

	/**
	 * 根据推广码获取rulebo
	 */
	public RuleBo getRuleBoByPromo(String code, Integer website);

	/**
	 * 根据推广码获取rule
	 */
	public Rule getRuleByPromo(String code, Integer website);

	/**
	 * 按照名称查询规则IID
	 */
	public Integer getRuleIidByName(String cname);
}