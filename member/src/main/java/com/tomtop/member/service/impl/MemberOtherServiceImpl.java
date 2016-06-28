package com.tomtop.member.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tomtop.member.mappers.user.MemberBaseMapper;
import com.tomtop.member.mappers.user.MemberOtherMapper;
import com.tomtop.member.models.bo.BaseBo;
import com.tomtop.member.models.bo.RegisterMemberBo;
import com.tomtop.member.models.dto.MemberBase;
import com.tomtop.member.models.dto.MemberOtherId;
import com.tomtop.member.service.IMemberOtherService;
import com.tomtop.member.utils.CommonUtils;

@Service
public class MemberOtherServiceImpl implements IMemberOtherService {

	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	MemberBaseMapper memberBaseMapper;
	@Autowired
	MemberOtherMapper memberOtherMapper;
	@Autowired
	MemberServiceImpl memberService;
	
	/**
	 * 第三方登陆时操作
	 * @param email
	 * 		                邮箱(必填)
	 * @param source
	 * 		               来源(必填)
	 * @param sourceId
	 * 		               账号Id(必填)
	 * @param siteId
	 * 		               站点Id(默认为1)
	 * @param country
	 * 		              国家
	 * 
	 * @return Boolean
	 * 
	 * @author renyy
	 * @throws Exception 
	 */
	@Override
	public BaseBo loginOrRegistration(String email, String source,
			String sourceId,Integer siteId,String country) throws Exception {
		BaseBo bb = new BaseBo();
		if(email == null || "".equals(email)){
			bb.setRes(-1014);
			bb.setMsg("loginOrRegistration email is null");
			return bb;
		}
		if(source == null){
			bb.setRes(-1015);
			bb.setMsg("loginOrRegistration source is null");
			return bb;
		}
		if(sourceId == null){
			bb.setRes(-1016);
			bb.setMsg("loginOrRegistration sourceId is null");
			return bb;
		}
		if(siteId == null){
			siteId = 1;
		}
		if(country == null){
			country = "US";
		}
		MemberOtherId otherId = memberOtherMapper.getBySource(source,
				sourceId);
		MemberBase mb = null;
		//用户
		if (otherId != null) {
			MemberBase mbw = new MemberBase();
			mbw.setCemail(email);
			mbw.setIwebsiteid(siteId);
			mb = memberBaseMapper.getMemberBaseWhere(mbw);
		}
		if (mb != null) {
			//用户已登陆注册过
			bb.setRes(CommonUtils.SUCCESS_RES);
			return bb;
		}else{
			MemberOtherId other = memberOtherMapper.getBySource(source, sourceId);
			if(other == null){
				other = new MemberOtherId();
				other.setBvalidated(false);
				other.setCemail(email);
				other.setCsource(source);
				other.setCsourceid(sourceId);
				
				memberOtherMapper.insert(other);
				logger.debug("Member {} Registration done by {}", email,
						source);
				//用户注册
				RegisterMemberBo rmb = memberService.memberRegister(email, sourceId, country, siteId, source);
				if(rmb.getRes() != CommonUtils.SUCCESS_RES){
					bb.setRes(rmb.getRes());
					bb.setMsg(rmb.getMsg());
					return rmb;
				}
			}
		}
		bb.setRes(CommonUtils.SUCCESS_RES);
		return bb;
	}
}
