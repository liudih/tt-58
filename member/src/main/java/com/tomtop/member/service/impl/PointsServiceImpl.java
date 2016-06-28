package com.tomtop.member.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tomtop.member.mappers.loyalty.MemberIntegralHistoryMapper;
import com.tomtop.member.service.IPointsService;
@Service
public class PointsServiceImpl implements IPointsService {
	

	@Autowired
	MemberIntegralHistoryMapper historyMapper;
	
	public int getUsefulPoints(String email, int siteId) {
		Integer points = historyMapper.getSumByEmail(email, siteId);
		return points != null ? points : 0;
	}
}
