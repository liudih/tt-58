package com.tomtop.loyalty.service;

import java.util.List;
import java.util.Map;

import com.tomtop.loyalty.models.Coupon;
import com.tomtop.loyalty.models.Prefer;
import com.tomtop.loyalty.models.Product;
import com.tomtop.loyalty.models.vo.CouponVo;
import com.tomtop.loyalty.models.vo.CouponVoMore;

/**
 * 优惠券
 * 
 * @author xiaoch
 *
 */
public interface ICouponService {
	/**
	 * 优惠券是否可用
	 * 
	 * @param code
	 * @param listingIds
	 * @param client
	 * @param terminal
	 * @return
	 */
	public Prefer isCouponAvailable(String code, String email,
			List<Product> products, Integer client, String terminal,
			String currency);

	/**
	 * 根据用户邮箱,code获取优惠券规则
	 * 
	 * @param code
	 * @param email
	 * @return
	 */
	// public Rule getRuleByCodeEmail(String code, String email);

	/**
	 * 用户是否授权使用优惠券
	 * 
	 * @param code
	 * @param email
	 * @param listingIds
	 * @param client
	 * @param terminal
	 * @return
	 */
	public Prefer isMemberAuthorization(String code, String email,Integer website);

	/**
	 * 获取规则id
	 * 
	 * @param code
	 * @param email
	 * @return
	 */
	// public Integer getRuleidByCodeEmail(String code, String email);

	/**
	 * 根据code获取ruleBo
	 * 
	 * @param ruleId
	 * @return
	 */
	// public RuleBo getRuleBoByCodeEmail(String code, String email);

	/**
	 * 优惠券对此用户是否可用，同时验证此张优惠券的状态
	 * 
	 * @param userEmail
	 * @param code
	 * @return
	 */
	public boolean myCouponUseable(String userEmail, String code,Integer website);

	/**
	 * 获取所有未使用的优惠券code
	 */

	public List<Coupon> getUnusedCouponByEmail(String email,Integer website);

	/**
	 * 获取所有已经使用的优惠券--分页
	 */

	public List<Coupon> getUsedCouponPageByEmail(String email, Integer page,
			Integer size, String currency,Integer website);

	/**
	 * 获取所有未使用的优惠券--分页
	 */

	public List<Coupon> getUnusedCouponPageByEmail(String email, Integer page,
			Integer size, String currency,Integer website);

	/**
	 * 根据规则id给用户发放优惠券
	 * 
	 * @param email
	 * @param ruleId
	 * @return
	 */
	public Coupon createCouponByRuleid(String email, Integer ruleId,
			Integer client);

	/**
	 * 新增操作
	 * 
	 * @return
	 */
	public int add(Coupon c);

	/**
	 * 根据条件获取优惠券总数
	 * 
	 * @return
	 */
	public int getTotalByCondion(Map<String, Object> map);

	/**
	 * 已使用优惠券总数
	 * 
	 * @return
	 */
	public int getUsedTotal(String email,Integer website);

	/**
	 * 未使用优惠券总数
	 * 
	 * @return
	 */
	public int getUnusedTotal(String email,Integer website);
	
	public List<CouponVo> convertVo(List<Coupon> coupons, String currency);
	
	
	/**
	 * 
	  * @Description: 多属性转换
	  * @param @param coupons
	  * @param @param currency
	  * @param @return  
	  * @return List<CouponVo>  
	  * @author liuyufeng 
	  * @date 2016年5月20日 上午9:32:05
	 */
	public List<CouponVoMore> convertVoMore(List<Coupon> coupons, String currency);
}