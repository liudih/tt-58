package com.tomtop.member.service;

import com.tomtop.member.models.bo.Page;
import com.tomtop.member.models.dto.Message;
import com.tomtop.member.models.dto.MessageInfo;

public interface IMessageService {

	/**
	 * 获取我的消息总条数,分页的时候会要用到
	 * 
	 * @return 我的
	 */
	public int getMyMessageTotal(String userId,Integer website);

	/**
	 * 获取我的未读取消息总条数
	 * 
	 * @return 
	 */
	public int getMyUnMessageTotal(String userId,Integer website);
	
	
	/**
	 * 分页获取我的消息
	 * 
	 * @param page
	 *            当前页
	 * @param pageSize
	 *            每页数据
	 * @return
	 */
	public Page<MessageInfo> getMyMessageForPage(String userId,Integer website, int page, int pageSize);

	/**
	 * 把我的消息设置为已阅状态
	 * 
	 * @param id
	 * @return
	 */
	public int readMessage(String id);

	public int deleteMessageByBroadcastId(String userId,String broadcastId);

	public int deleteMessage(String id);

	/**
	 * 获取详情
	 * 
	 * @param id
	 * @return
	 */
	public Message getDetail(Integer id);

	/**
	 * 表t_message_info中是否已经存在Broadcast消息
	 * 
	 * @param broadcastId
	 * @return
	 */
	public boolean isExistedByBroadcastId(String userId,String broadcastId);

	/**
	 * 插入操作
	 * 
	 * @param paras
	 * @return
	 */
	public int insert(Message paras);
}
