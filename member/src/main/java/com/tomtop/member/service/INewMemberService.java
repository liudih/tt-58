package com.tomtop.member.service;

import java.util.List;

import com.tomtop.member.models.dto.Statistics;

public interface INewMemberService {

	public List<Statistics> getMemberStatistics(String email, Integer website);

}
