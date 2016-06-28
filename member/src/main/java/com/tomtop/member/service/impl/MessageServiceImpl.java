package com.tomtop.member.service.impl;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Maps;
import com.tomtop.member.mappers.message.MessageMapper;
import com.tomtop.member.mappers.user.MemberBaseMapper;
import com.tomtop.member.models.bo.Page;
import com.tomtop.member.models.dto.MemberBase;
import com.tomtop.member.models.dto.Message;
import com.tomtop.member.models.dto.MessageInfo;
import com.tomtop.member.models.dto.MessageStatus;
import com.tomtop.member.service.IMessageService;

@Service
public class MessageServiceImpl implements IMessageService {


	@Autowired
	private MessageMapper messageMapper;
	@Autowired
	MemberBaseMapper memberBaseMapper;
	
	@Override
	public int getMyMessageTotal(String userId,Integer website) {
		HashMap<String, Object> paras = new HashMap<String, Object>(1);
		paras.put("userId", userId);
		paras.put("siteId", website);
		return this.messageMapper.getMyMessageTotal(paras);
	}

	@Override
	public int getMyUnMessageTotal(String userId,Integer website) {
		HashMap<String, Object> paras = new HashMap<String, Object>(1);
		paras.put("userId", userId);
		paras.put("siteId", website);
		return this.messageMapper.getMyUnMessageTotal(paras);
	}
	
	@Override
	public Page<MessageInfo> getMyMessageForPage(String userId,Integer website, int page, int pageSize) {
		Map<String, Object> paras = Maps.newHashMap();
		MemberBase mb = memberBaseMapper.getMemberBaseByEmailAndWebsiteId(userId, website);
		if(mb == null){
			return null;
		}
		paras.put("page", page);
		paras.put("pageSize", pageSize);
		paras.put("userId", userId);
		paras.put("dmemberRegDate", mb.getDcreatedate());
		paras.put("siteId", website);

		List<MessageInfo> messages = this.messageMapper.getMyMessageForPage(paras);
		int total = this.getMyMessageTotal(userId,website);

		Page<MessageInfo> result = new Page<MessageInfo>(messages, total, page,
				pageSize);

		return result;
	}

	@Override
	public int readMessage(String id) {
		HashMap<String, Object> paras = new HashMap<String, Object>(2);
		paras.put("id", id);
		paras.put("status", MessageStatus.READ.getCode());
		return this.messageMapper.updateMessageStatus(paras);
	}

	@Override
	public int deleteMessage(String id) {
		HashMap<String, Object> paras = new HashMap<String, Object>(2);
		paras.put("id", id);
		paras.put("status", MessageStatus.DELETE.getCode());
		return messageMapper.updateMessageStatus(paras);
	}

	@Override
	public Message getDetail(Integer id) {
		return messageMapper.getDetail(id);
	}

	@Override
	public boolean isExistedByBroadcastId(String userId, String broadcastId)
			throws NullPointerException {
		 

		HashMap<String, Object> paras = new HashMap<String, Object>(2);
		paras.put("userId", userId);
		paras.put("broadcastId", broadcastId);
		return this.messageMapper.isExisted(paras) > 1 ?true:false;
	}

	@Override
	public int deleteMessageByBroadcastId(String userId, String broadcastId) {
		HashMap<String, Object> paras = new HashMap<String, Object>(2);

		paras.put("broadcastId", broadcastId);
		paras.put("userId", userId);
		paras.put("status", MessageStatus.DELETE.getCode());
		return this.messageMapper.updateMessageStatus(paras);
	}

	@Override
	public int insert(Message paras) {
		return this.messageMapper.insert(paras);
	}

	
	public boolean sendPersonalMessage(Message info) {				
		int flag = 0; 
		flag = messageMapper.insert(info);
		return (flag > 0);
	}
}
