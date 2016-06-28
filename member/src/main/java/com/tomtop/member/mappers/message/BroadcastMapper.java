package com.tomtop.member.mappers.message;


import java.util.List;
import java.util.Map;

import com.tomtop.member.models.dto.Broadcast;


/**
 * 表t_message_broadcast映射类
 * 
 * @author lijun
 *
 */
public interface BroadcastMapper {

	public List<Broadcast> selectBroadcasts();

	/**
	 * 分页查询
	 * 
	 * @param paras
	 * @return
	 */
	public List<Broadcast> selectBroadcastsForPage(Map<String, Object> paras);

	/**
	 * 获取总页数
	 * 
	 * @param paras
	 * @return
	 */
	public int getTotal(Map<String, Object> paras);

	/**
	 * 标记我的消息(删除 已阅)
	 * 
	 * @param paras
	 * @return 0-成功 1-失败
	 */
	public int markMyBroadcastMessage(Map<String, Object> paras);

	/**
	 * 获取详情
	 * 
	 * @param paras
	 * @return
	 */
	public Broadcast getDetail(Map<String, Object> paras);

	/**
	 * 新增消息
	 * 
	 * @param m
	 * @return
	 */
	public int add(Broadcast m);

	/**
	 * 更新操作
	 * 
	 * @param m
	 * @return
	 */
	public int update(Broadcast m);

	/**
	 * 推送消息
	 * 
	 * @param m
	 * @return
	 */
	public int publish(Broadcast m);

	/**
	 * 删除操作
	 * 
	 * @param paras
	 * @return
	 */
	public int delete(Map<String, Object> paras);
}
