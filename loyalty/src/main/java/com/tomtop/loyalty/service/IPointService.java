package com.tomtop.loyalty.service;

import java.util.List;
import java.util.Map;

import com.tomtop.loyalty.models.MemberEvent;
import com.tomtop.loyalty.models.MemberIntegralHistory;
import com.tomtop.loyalty.models.Point;
import com.tomtop.loyalty.models.Prefer;

public interface IPointService {
	/**
	 * 根据email查询用户有多少积分
	 * 
	 * @param
	 * @return
	 */
	public Integer getUserTotalPointByEmail(String email, Integer website);
	
	/**
	 * 
	  * @Description: 获取用户积分信息
	  * @param email
	  * @param website
	  * @return Map<String,Integer>  
	  * @author liuyufeng 
	  * @date 2016年5月17日 下午3:08:37
	 */
	public Map<String,Integer> getUserPointInfo(String email, Integer website);

	/**
	 * 应用积分
	 * 
	 * @param email
	 * @param website
	 * @return
	 */
	public Prefer isPointAvailable(String email, Integer point,
			Integer website, String currency);

	/**
	 * 根据积分兑换相应货币
	 * 
	 * @param email
	 * @param code
	 * @param website
	 * @return
	 */
	public Double convertPointToMoney(Integer point, String currency);

	/**
	 * 未使用积分历史记录
	 * 
	 * @param point
	 * @param currency
	 * @return
	 */
	public List<Point> getUnusedPointHistory(String email, Integer website,
			int page, int size);

	/**
	 * 已使用积分流水记录
	 * 
	 * @param point
	 * @param currency
	 * @return
	 */
	public List<Point> getUsedPointHistory(String email, Integer website,
			int page, int size);

	/**
	 * 未使用总数
	 * 
	 * @param email
	 * @param website
	 * @return
	 */
	public Integer getUnusedTotalCountByEmail(String email, Integer website);

	/**
	 * 已使用总数
	 * 
	 * @param email
	 * @param website
	 * @return
	 */
	public Integer getUsedTotalCountByEmail(String email, Integer website);

	/**
	 * 锁定积分
	 * 
	 * @param email
	 * @param siteID
	 * @param points
	 * @return
	 */
	public Integer lockPoints(String email, Integer website, Integer point);

	public boolean insertIntegralHistory(
			MemberIntegralHistory memberIntegralHistory);

	/**
	 * 积分状态更新
	 * 
	 * @param remark
	 * @param id
	 * @return
	 */
	public boolean updateRemarkById(String remark, Integer id);
	
	public Boolean givingPoint(MemberEvent member,Integer point,
			String type, String remark, String source);
	
	public Integer getPointByType(Point point);
	
	/**
	 * 
	  * @Description: 按照备注查询是否有数据
	  * @param @param remark
	  * @param @return  
	  * @return int  
	  * @author liuyufeng 
	  * @date 2016年5月13日 上午9:44:31
	 */
	public int getByRemark(String remark);

}
