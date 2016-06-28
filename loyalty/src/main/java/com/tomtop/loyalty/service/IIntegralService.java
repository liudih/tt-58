package com.tomtop.loyalty.service;


import com.tomtop.loyalty.models.IntegralModel;
import com.tomtop.loyalty.models.Page;
import com.tomtop.loyalty.models.Pageable;

/**
 * 用户积分流水业务接口
 * @author zhangxiaangquan	
 *
 */
public interface IIntegralService {

	
	/**
	 * 添加用户积分流水
	 * @param model
	 * @return 成功数量
	 */
	public int  addIntegralForUser(IntegralModel model,String type);
	
	/**
	 * 获取用户积分具体数字
	 * @param email 用户的email
	 * @param webSiteId 站点Id
	 * @return
	 */
	public int countIntegralForUser(String email,int webSiteId);
	

	
	
	/**
	 * 查询用户积分流水记录
	 * @param model
	 * @param page
	 * @param begin
	 * @param end
	 * @return
	 */
	public Page<IntegralModel> searchIntegralForUser(Pageable condition);

}
