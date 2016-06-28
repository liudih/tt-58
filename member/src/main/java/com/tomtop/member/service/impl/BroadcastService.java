package com.tomtop.member.service.impl;


import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tomtop.member.mappers.message.BroadcastMapper;
import com.tomtop.member.models.bo.Page;
import com.tomtop.member.models.dto.Broadcast;
import com.tomtop.member.models.dto.MessageStatus;
import com.tomtop.member.service.IBroadcastService;


@Service
public class BroadcastService implements IBroadcastService {
	@Autowired
	private BroadcastMapper brodcastMapper;

	public List<Broadcast> selectBroadcasts() {
		return brodcastMapper.selectBroadcasts();
	}

	@Override
	public Page<Broadcast> selectBroadcastsForPage(String userId, int page, int pageSize) {

		HashMap<String, Object> paras = new HashMap<String, Object>(3);
		paras.put("page", page);
		paras.put("pageSize", pageSize);
		paras.put("userId", userId);

		List<Broadcast> messages = this.brodcastMapper.selectBroadcastsForPage(paras);

		HashMap<String, Object> totalParas = new HashMap<String, Object>(1);
		totalParas.put("userId", userId);
		int total = this.brodcastMapper.getTotal(totalParas);

		Page<Broadcast> result = new Page<Broadcast>(messages, total, page,
				pageSize);

		return result;
	}

	@Override
	public int deleteMyBroadcastMessage(String userId, int broadcastId) {
		int status = MessageStatus.DELETE.getCode();

		HashMap<String, Object> paras = new HashMap<String, Object>();
		paras.put("broadcastId", broadcastId);
		paras.put("userId", userId);
		paras.put("status", status);
		return this.brodcastMapper.markMyBroadcastMessage(paras);
	}

	@Override
	public int readMyBroadcastMessage(String userId, String broadcastId) {
		 
		int status = MessageStatus.READ.getCode();

		HashMap<String, Object> paras = new HashMap<String, Object>();
		paras.put("broadcastId", broadcastId);
		paras.put("userId", userId);
		paras.put("status", status);
		return this.brodcastMapper.markMyBroadcastMessage(paras);
	}

	@Override
	public Broadcast getDetail(String broadcastId) {
		HashMap<String, Object> paras = new HashMap<String, Object>(1);
		paras.put("id", broadcastId);
		return this.brodcastMapper.getDetail(paras);
	}

	@Override
	public int add(Broadcast m) {
		return this.brodcastMapper.add(m);
	}

	@Override
	public int update(Broadcast m) {
		return this.brodcastMapper.update(m);
	}

	@Override
	public int publish(Broadcast m) {
		return this.brodcastMapper.publish(m);
	}

	@Override
	public int delete(int id) {
		HashMap<String, Object> paras = new HashMap<String, Object>(1);
		paras.put("id", id);
		return this.brodcastMapper.delete(paras);
	}
}
