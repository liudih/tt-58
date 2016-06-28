package com.tomtop.member.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tomtop.member.mappers.interaction.InteractionMapper;
import com.tomtop.member.mappers.loyalty.MemberIntegralHistoryMapper;
import com.tomtop.member.models.dto.Statistics;
import com.tomtop.member.service.INewMemberService;

/**
 * 新的会员中心服务
 * @author Administrator
 *
 */
@Service
public class NewMemberServiceImpl implements INewMemberService {

	@Autowired
	InteractionMapper interactionMapper;
	@Autowired
	MemberIntegralHistoryMapper integralMapper;
	
	/**
	 * 获取会员统计数量
	 */
	@Override
	public List<Statistics> getMemberStatistics(String email, Integer website) {
		List<Statistics> stlist = interactionMapper.getCount(email, website);
		List<Statistics> loylist = integralMapper.getStatisticsAvailableLoyalty(email, website);
		stlist.addAll(loylist);
		return stlist;
	}

}
