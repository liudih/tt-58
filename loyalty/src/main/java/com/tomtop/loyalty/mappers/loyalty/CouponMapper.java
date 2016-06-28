package com.tomtop.loyalty.mappers.loyalty;

import java.util.List;
import java.util.Map;

import com.tomtop.loyalty.models.Coupon;

/**
 * Coupon 数据库映射类
 * 
 * @author xiaoch
 *
 */
public interface CouponMapper {
	/**
	 * 新增操作
	 * 
	 * @param c
	 * @return
	 */
	public int add(Coupon c);

	/**
	 * 删除操作
	 * 
	 * @param id
	 * @return
	 */
	public int delete(Map<String, Object> paras);

	/**
	 * 获取我的优惠券总数
	 * 
	 * @param paras
	 * @return
	 */
	public int getTotalMyCoupon(Map<String, Object> paras);

	/**
	 * 判断我的优惠券是否可用
	 * 
	 * @param userEmail
	 * @param code
	 * @param siteId
	 * @return
	 */
	public int myCouponUseable(Map<String, Object> paras);

	/**
	 * 判断某人是否拥有这张coupon,不管之前是否已经使用过
	 * 
	 * @param paras
	 * @return
	 */
	public int isOwnCoupon(Map<String, Object> paras);

	/**
	 * 查询所有未使用的优惠券
	 * 
	 * @param paras
	 * @return
	 */
	public List<Coupon> getUnusedCouponByEmail(Map<String, Object> paras);

	/**
	 * 查询所有未使用or已经使用的优惠券--分页
	 * 
	 * @param paras
	 * @return
	 */
	public List<Coupon> getCouponPageByEmail(Map<String, Object> paras);

}
