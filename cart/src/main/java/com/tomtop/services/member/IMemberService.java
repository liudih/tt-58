package com.tomtop.services.member;

import java.util.Date;
import java.util.List;

import org.apache.commons.chain.web.WebContext;

import com.tomtop.valueobjects.member.MemberBase;
import com.tomtop.valueobjects.member.MemberOtherIdentity;

public interface IMemberService {

	public String getUserNameByMemberEmail(String email);

	public List<MemberBase> getUserNamesByMemberEmail(List<String> list);

//	public MemberBase getMemberByMemberEmail(String email, WebContext context);

//	public MemberBase getMemberByOtherIdentity(MemberOtherIdentity other,
//			WebContext context);

	public Integer searchMemberCountByDate(Date start, Date end);

	/**
	 * 
	 * @author lijun
	 * @return
	 */
	public MemberBase getMemberByUuid(String uuid);
}