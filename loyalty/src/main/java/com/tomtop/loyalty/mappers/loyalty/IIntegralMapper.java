package com.tomtop.loyalty.mappers.loyalty;

import java.util.List;

import com.tomtop.loyalty.models.IntegralModel;
import com.tomtop.loyalty.models.Pageable;

/**
 * 积分流水DAO
 * @author ztiny
 */

public interface IIntegralMapper {
	
	/**
	 * 根据用户查询用户积分流水记录
	 * @param page
	 * @return List<IntegralModel>
	 */
	public List<IntegralModel> searchByUser(Pageable conditon);
	
	/**
	 * 获取总数
	 * @param condition
	 * @return
	 */
	public int searchTotalByUser(Pageable condition);
	
	/**
	 * 根据用户名查询用户总积分
	 * @param email 账号地址
	 * @param webSiteId 站点id
	 * @return
	 */
	public int searchUserIntegralNumByUser(String email,int webSiteId);
	
	/**
	 * 添加用户积分流水
	 * @param integralModel
	 */
	public int save(IntegralModel integralModel);
	
	/**
	 * 修改用户积分流水记录
	 * @param model
	 * @return
	 */
	public IntegralModel update(IntegralModel model);
	

}
