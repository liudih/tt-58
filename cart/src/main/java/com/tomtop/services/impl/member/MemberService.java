package com.tomtop.services.impl.member;

import java.util.Date;
import java.util.List;

import org.apache.commons.chain.web.WebContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tomtop.dto.member.MemberOtherId;
import com.tomtop.mappers.member.MemberBaseMapper;
import com.tomtop.mappers.member.MemberOtherIdMapper;
import com.tomtop.services.member.IMemberService;
import com.tomtop.utils.FoundationService;
import com.tomtop.valueobjects.member.MemberBase;
import com.tomtop.valueobjects.member.MemberOtherIdentity;

@Service
public class MemberService implements IMemberService {

	@Autowired
	MemberBaseMapper memberBaseMapper;

	@Autowired
	MemberOtherIdMapper otherIdMapper;

	@Autowired
	FoundationService foundation;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * services.IMemberEnquiryService#getUserNameByMemberEmail(java.lang.String)
	 */
	public String getUserNameByMemberEmail(String email) {
		return memberBaseMapper.getUserName(email);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * services.IMemberEnquiryService#getUserNamesByMemberEmail(java.util.List)
	 */
	public List<MemberBase> getUserNamesByMemberEmail(List<String> list) {

		return memberBaseMapper.getUserNames(list);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * services.IMemberEnquiryService#getMemberByMemberEmail(java.lang.String,
	 * context.WebContext)
	 */
//	public MemberBase getMemberByMemberEmail(String email, WebContext context) {
//		int site = foundation.getSiteID(context);
//		MemberBase jjBase = memberBaseMapper.getUserByEmail(email, site);
//		return jjBase;
//	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * services.IMemberEnquiryService#getMemberByOtherIdentity(valueobjects.
	 * member.MemberOtherIdentity, context.WebContext)
	 */
//	public MemberBase getMemberByOtherIdentity(MemberOtherIdentity other,
//			WebContext context) {
//		MemberOtherId otherId = otherIdMapper.getBySource(other.getSource(),
//				other.getId());
//		if (otherId != null) {
//			int site = foundation.getSiteID(context);
//			return memberBaseMapper.getUserByEmail(other.getEmail(), site);
//		}
//		return null;
//	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * services.IMemberEnquiryService#searchMemberCountByDate(java.util.Date,
	 * java.util.Date)
	 */
	public Integer searchMemberCountByDate(Date start, Date end) {
		return memberBaseMapper.getMemberCountByDate(start, end);
	}

	/**
	 * 通过uuid获取用户
	 * 
	 * @author lijun
	 * @return
	 */
	public MemberBase getMemberByUuid(String uuid) {
		if (uuid == null || uuid.length() == 0) {
			return null;
		}
		return this.memberBaseMapper.getUserByUuid(uuid);
	}
}
