package com.tomtop.member.controllers;

import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tomtop.member.configuration.global.GlobalConfigSettings;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tomtop.framework.core.utils.Result;
import com.tomtop.member.models.bo.BaseBo;
import com.tomtop.member.models.bo.LoginMemberBo;
import com.tomtop.member.models.bo.MemberBaseBo;
import com.tomtop.member.models.bo.MemberData;
import com.tomtop.member.models.bo.MemberPhotoBo;
import com.tomtop.member.models.bo.MemberUidBo;
import com.tomtop.member.models.bo.RegisterMemberBo;
import com.tomtop.member.models.bo.TokenUUidBo;
import com.tomtop.member.models.dto.MemberBase;
import com.tomtop.member.models.filter.LoginMemberFilter;
import com.tomtop.member.models.filter.RegisterMemberFilter;
import com.tomtop.member.service.IMemberEdmService;
import com.tomtop.member.service.IMemberPhotoService;
import com.tomtop.member.service.IMemberService;
import com.tomtop.member.utils.CommonUtils;

/**
 * 用户操作类
 * 
 * @author renyy
 *
 */
@RestController
@RequestMapping(value = "/member")
public class MemberController {

	@Autowired
	IMemberService memberService;
	
	@Autowired
	IMemberEdmService memberEdmService;

	@Autowired
	IMemberPhotoService memerPhotoServie;
	
	@Value("${base_api_url}")
	private String base_api_url;
	
	@Autowired
	private GlobalConfigSettings setting;
	
	/**
	 * 用户登录验证
	 * 
	 * @param LoginMemberFilter
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/v1/login")
	public Result checkLogin(@RequestBody LoginMemberFilter lfilter) {
		Result res = new Result();
		String email = lfilter.getEmail();
		String pwd = lfilter.getPwd();
		Integer websiteid = lfilter.getClient();
		LoginMemberBo lmb = memberService.memberLogin(email, pwd, websiteid);
		Integer re = lmb.getRes();
		if (re == CommonUtils.SUCCESS_RES) {
			TokenUUidBo tub = memberService.getToken(email, pwd, websiteid);
			res.setRet(CommonUtils.SUCCESS_RES);
			lmb.setRes(null);
			lmb.setToken(tub.getToken());
			lmb.setUuid(tub.getUuid());
			res.setData(lmb);
		} else {
			String msg = lmb.getMsg();
			res.setRet(CommonUtils.ERROR_RES);
			res.setErrCode(re.toString());
			res.setErrMsg(msg);
		}
		return res;
	}
	
	/**
	 * 用户登录验证
	 * 第二版升级 调整client --> website
	 * 
	 * @param LoginMemberFilter
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/v2/login")
	public Result checkLoginV2(@RequestBody LoginMemberFilter lfilter) {
		Result res = new Result();
		String email = lfilter.getEmail();
		String pwd = lfilter.getPwd();
		Integer websiteid = lfilter.getWebsite();
		LoginMemberBo lmb = memberService.memberLogin(email, pwd, websiteid);
		Integer re = lmb.getRes();
		if (re == CommonUtils.SUCCESS_RES) {
			TokenUUidBo tub = memberService.getToken(email, pwd, websiteid);
			res.setRet(CommonUtils.SUCCESS_RES);
			lmb.setRes(null);
			lmb.setToken(tub.getToken());
			lmb.setUuid(tub.getUuid());
			res.setData(lmb);
		} else {
			String msg = lmb.getMsg();
			res.setRet(CommonUtils.ERROR_RES);
			res.setErrCode(re.toString());
			res.setErrMsg(msg);
		}
		return res;
	}

	/**
	 * 用户注册
	 * 
	 * @param RegisterMemberFilter
	 */
	@RequestMapping(method = RequestMethod.PUT, value = "/v1/register")
	public Result registeredMember(HttpServletRequest request,
			@RequestBody RegisterMemberFilter rgmfilter) {
		Result res = new Result();
		try {
			String email = rgmfilter.getEmail();
			String pwd = rgmfilter.getPwd();
			String country = rgmfilter.getCountryShort();
			Integer siteId = rgmfilter.getClient();
			String vhost = request.getRemoteHost();
			RegisterMemberBo rmb = memberService.memberRegister(email, pwd,
					country, siteId, vhost);
			Integer re = rmb.getRes();
			String msg = rmb.getMsg();
			if (re == CommonUtils.SUCCESS_RES) {
				res.setRet(CommonUtils.SUCCESS_RES);
			} else {
				res.setRet(CommonUtils.ERROR_RES);
				res.setErrCode(re.toString());
				res.setErrMsg(msg);
			}

		} catch (Exception e) {
			res.setRet(CommonUtils.ERROR_RES);
			res.setErrCode("500");
			res.setErrMsg("registeredMember faile"+e.getMessage());
			e.printStackTrace();
		}
		return res;
	}

	/**
	 * 用户注册
	 * 第二版升级 调整client --> website
	 * @param RegisterMemberFilter
	 */
	@RequestMapping(method = RequestMethod.PUT, value = "/v2/register")
	public Result registeredMemberV2(HttpServletRequest request,
			@RequestBody RegisterMemberFilter rgmfilter) {
		Result res = new Result();
		try {
			String email = rgmfilter.getEmail();
			String pwd = rgmfilter.getPwd();
			String country = rgmfilter.getCountryShort();
			Integer siteId = rgmfilter.getWebsite();
			String vhost = request.getRemoteHost();
			RegisterMemberBo rmb = memberService.memberRegister(email, pwd,
					country, siteId, vhost);
			Integer re = rmb.getRes();
			String msg = rmb.getMsg();
			if (re == CommonUtils.SUCCESS_RES) {
				TokenUUidBo tub = memberService.getToken(email, pwd, siteId);
				rmb.setUuid(tub.getUuid());
				rmb.setToken(tub.getToken());
				res.setRet(CommonUtils.SUCCESS_RES);
				res.setData(rmb);
			} else {
				res.setRet(CommonUtils.ERROR_RES);
				res.setErrCode(re.toString());
				res.setErrMsg(msg);
			}

		} catch (Exception e) {
			res.setRet(CommonUtils.ERROR_RES);
			res.setErrCode("500");
			res.setErrMsg("registeredMember faile"+e.getMessage());
			e.printStackTrace();
		}
		return res;
	}
	
	/**
	 * 根据uuid获取邮件账号
	 * 
	 * @param uuid
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/v1/email/{uuid}")
	public Result getEmailByUid(
			@PathVariable("uuid") String uuid,
			@RequestParam(value = "client", required = false, defaultValue = "1") Integer client) {
		Result res = new Result();
		MemberUidBo member = memberService.getMemberEmailByUUid(uuid, client);
		Integer re = member.getRes();
		if (re == CommonUtils.SUCCESS_RES) {
			res.setRet(CommonUtils.SUCCESS_RES);
			member.setRes(null);
			res.setData(member);
		} else {
			String msg = member.getMsg();
			res.setRet(CommonUtils.ERROR_RES);
			res.setErrCode(re.toString());
			res.setErrMsg(msg);
		}
		return res;
	}

	/**
	 * 根据uuid获取邮件账号
	 * 
	 * @param uuid
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/v2/email/{uuid}")
	public Result getEmailByUid(
			@PathVariable("uuid") String uuid,
			@RequestParam(value = "client", required = false, defaultValue = "1") Integer client,
			@RequestParam(value = "website", required = false, defaultValue = "1") Integer website) {
		Result res = new Result();
		MemberUidBo member = memberService.getMemberEmailByUUid(uuid, website);
		Integer re = member.getRes();
		if (re == CommonUtils.SUCCESS_RES) {
			res.setRet(CommonUtils.SUCCESS_RES);
			member.setRes(null);
			res.setData(member);
		} else {
			String msg = member.getMsg();
			res.setRet(CommonUtils.ERROR_RES);
			res.setErrCode(re.toString());
			res.setErrMsg(msg);
		}
		return res;
	}
	
	/**
	 * 用户email站点信息获取会员信息
	 * 
	 * @param uuid
	 */
	@RequestMapping(value = "/v1/memberbase", method = RequestMethod.GET)
	public Result getMemberBaseByEmailAndWebsiteId(
			@RequestParam(value = "client", required = false, defaultValue = "1") Integer client,
			@RequestParam(value = "website", required = false, defaultValue = "1") Integer website,
			@RequestParam(value = "uuid") String uuid,
			@RequestParam(value = "email") String email) {
		Result res = new Result();
		try {
			boolean flagV = memberService.validate(website, email, uuid);
			if (flagV) {
				MemberBaseBo base = this.memberService
						.getMemberBaseByEmailAndWebsiteId(email, website);
				Integer re = base.getRes();
				if (re == CommonUtils.SUCCESS_RES) {
					res.setRet(CommonUtils.SUCCESS_RES);
					res.setData(JSON.toJSON(base));
				} else {
					String msg = base.getMsg();
					res.setRet(CommonUtils.ERROR_RES);
					res.setErrCode(re.toString());
					res.setErrMsg(msg);
				}
			} else {
				res.setRet(CommonUtils.ERROR_RES);
				res.setErrCode("500");
				res.setErrMsg("email is not valid");
			}

		} catch (Exception e) {
			res.setRet(CommonUtils.ERROR_RES);
			res.setErrCode("500");
			res.setErrMsg("get memeberbase faile" + e.getMessage());
			e.printStackTrace();
		}
		return res;
	}
	/**
	 * 获取会员头像
	 * 
	 * @param uuid
	 */
	@RequestMapping(value = "/v1/memberphoto", method = RequestMethod.GET)
	public Result getMemberPhotoByEmailAndWebsiteId(
			@RequestParam(value = "client", required = false, defaultValue = "1") Integer client,
			@RequestParam(value = "website", required = false, defaultValue = "1") Integer website,
			@RequestParam(value = "uuid") String uuid,
			@RequestParam(value = "email") String email) {
		Result res = new Result();
		try {
			boolean flag = memberService.validate(website, email, uuid);

			if (flag) {
				MemberPhotoBo mp = this.memerPhotoServie.getMemberPhoto(email,
						website);
				Integer re = mp.getRes();
				if (re == CommonUtils.SUCCESS_RES) {
					res.setRet(CommonUtils.SUCCESS_RES);
					mp.setRes(null);
					res.setData(JSON.toJSON(mp));
				} else {
					String msg = mp.getMsg();
					res.setRet(CommonUtils.ERROR_RES);
					res.setErrCode(re.toString());
					res.setErrMsg(msg);
				}
			} else {
				res.setRet(CommonUtils.ERROR_RES);
				res.setErrCode("500");
				res.setErrMsg("email is not valid");
			}

		} catch (Exception e) {
			res.setRet(CommonUtils.ERROR_RES);
			res.setErrCode("500");
			res.setErrMsg("update memeberbase faile");
			e.printStackTrace();
		}
		return res;
	}

	/**
	 * 更新会员基本信息
	 * 
	 * @param websiteId
	 * @param email
	 * @param jsonParam
	 * @return
	 */
	@RequestMapping(value = "/v1/memberbase/update", method = RequestMethod.POST)
	public Result updateMemberBaseByEmailAndWebsiteId(
			@RequestBody String jsonParam) {
		Result res = new Result();
		try {
			ObjectMapper om = new ObjectMapper();
			JsonNode jsonNode = om.readTree(jsonParam);
			MemberBaseBo cbase = om.convertValue(jsonNode, MemberBaseBo.class);
			MemberBase mbb = new MemberBase();

			String uuid = cbase.getCuuid();
			Integer websiteId = cbase.getClient();
			String email = cbase.getCemail();

			boolean flagV = memberService.validate(websiteId, email, uuid);
			if (flagV) {
				BeanUtils.copyProperties(mbb, cbase);
				mbb.setIwebsiteid(websiteId);
				boolean flag = this.memberService
						.updateMemberBaseByEmailAndWebsiteId(mbb);
				if (flag) {
					res.setRet(CommonUtils.SUCCESS_RES);
					// base.setRes(null);
					res.setData(flag);
				} else {
					res.setRet(CommonUtils.ERROR_RES);
					res.setErrCode("500");
					res.setErrMsg("update memeberbase faile");
				}
			} else {
				res.setRet(CommonUtils.ERROR_RES);
				res.setErrCode("500");
				res.setErrMsg("email is not valid");
			}

		} catch (Exception e) {
			res.setRet(CommonUtils.ERROR_RES);
			res.setErrCode("500");
			res.setErrMsg("update memeberbase faile");
			e.printStackTrace();
		}

		return res;
	}
	
	/**
	 * 更新会员基本信息  .V2
	 * 
	 * @param websiteId
	 * @param email
	 * @return
	 */
	@RequestMapping(value = "/v2/memberbase/update", method = RequestMethod.POST)
	public Result updateMemberBaseByEmailAndWebsiteIdV2(
			@RequestBody MemberData memberData) {
		Result res = new Result();
		try {
			int ret = this.memberService.updateMemberData(memberData);
			if (ret > 0) {
				res.setRet(CommonUtils.SUCCESS_RES);
			} else {
				res.setRet(CommonUtils.ERROR_RES);
				res.setErrCode("500");
				res.setErrMsg("update memeberbase faile");
			}

		} catch (Exception e) {
			res.setRet(CommonUtils.ERROR_RES);
			res.setErrCode("500");
			res.setErrMsg("update memeberbase faile");
			e.printStackTrace();
		}

		return res;
	}

	/**
	 * 获取会员登录上下文
	 * 
	 * @param websiteId
	 * @param email
	 * @param jsonParam
	 * @return
	 */
	@RequestMapping(value = "/v1/login_context", method = { RequestMethod.GET,
			RequestMethod.POST })
	public Result getLoginContext(HttpServletRequest request) {
		Result res = new Result();
		try {
			Cookie[] cookies = request.getCookies();
			JSONObject json = new JSONObject();
			if (null == cookies) {
				res.setRet(CommonUtils.ERROR_RES);
				res.setData("cookie is null");
				res.setErrCode("-1001");
				return res;
			} else {
				String uuid = null;
				String token = null;
				for (Cookie c : cookies) {
					if (c.getName().equals("TT_UUID")) {
						uuid = c.getValue();
					}
					if (c.getName().equals("TT_TOKEN")) {
						token = c.getValue();
					}
				}
				if (null == token) {
					res.setRet(CommonUtils.ERROR_RES);
					res.setData("token is null");
					res.setErrCode("-1002");
					return res;
				}
				if (null == uuid) {
					res.setRet(CommonUtils.ERROR_RES);
					res.setData("uuid is null");
					res.setErrCode("-1003");
					return res;
				}
				MemberBase member = this.memberService
						.getMemberBaseByUuid(uuid);

				if (member == null || member.getCemail() == null) {
					res.setRet(CommonUtils.ERROR_RES);
					res.setData("email is null");
					res.setErrCode("-1004");
					return res;
				}
				TokenUUidBo tokenBo = this.memberService
						.getTokenByEmailAndPwdAndSiteId(member.getCemail(),
								member.getCpasswd(), member.getIwebsiteid());
				if (tokenBo == null || tokenBo.getToken() == null) {
					res.setRet(CommonUtils.ERROR_RES);
					res.setData("token is null");
					res.setErrCode("-1005");
					return res;
				}
				// TT_LANG TT_UUID TT_TOKEN TT_COUN国家 TT_CURR货币
				if (tokenBo.getToken().equals(token)) {
					for (Cookie c : cookies) {
						if (c.getName().equals("TT_CURR")) {
							json.put("currencyCode", c.getValue());
						}
						if (c.getName().equals("TT_COUN")) {
							json.put("countryCode", c.getValue());
						}
					}
				}
				json.put("email", member.getCemail());
				json.put("groupId", member.getIgroupid());

				res.setRet(CommonUtils.SUCCESS_RES);
				res.setData(json.toJSONString());
				return res;

			}

		} catch (Exception e) {
			res.setRet(CommonUtils.ERROR_RES);
			res.setData("get login_context faile" + e.getMessage());
			res.setErrCode("500");
			e.printStackTrace();
			return res;
		}
	}

	/**
	 * 會員中心統計信息
	 * 
	 * @param uuid
	 */
	@RequestMapping(value = "/v1/member_center_statistics", method = RequestMethod.GET)
	public Result getMemberStatisticsByEmailAndWebsiteId(
			@RequestParam(value = "client", required = false, defaultValue = "1") Integer client,
			@RequestParam(value = "uuid") String uuid,
			@RequestParam(value = "email") String email) {
		Result res = new Result();
		try {
			boolean flagV = memberService.validate(client, email, uuid);
			if (flagV) {

				JSONObject json = this.memberService
						.getMemberStatisticsByEmailAndWebsiteId(email, client,
								uuid);
				res.setRet(CommonUtils.SUCCESS_RES);
				res.setData(json.toJSONString());
			} else {
				res.setRet(CommonUtils.ERROR_RES);
				res.setErrCode("-1001");
				res.setData("email is not valid");
			}

		} catch (Exception e) {
			res.setRet(CommonUtils.ERROR_RES);
			res.setErrCode("500");
			res.setData("get MemberStatistics faile" + e.getMessage());

			e.printStackTrace();
		}
		return res;
	}
	
	
	
	
	/**
	 * 用户激活
	 * 
	 * @param uuid
	 */
	@RequestMapping(value = "/v1/activate", method = RequestMethod.GET)
	public Result activate(
			@RequestParam(value = "website") Integer website,
			@RequestParam(value = "code") String code) {
		Result res = new Result();
		try {
			String flagV = memberService.activation(code, website);
			//激活成功发送邮件
			if ("success".equals(flagV)) {
				
				res.setRet(CommonUtils.SUCCESS_RES);
			} else {
				res.setRet(CommonUtils.ERROR_RES);
				res.setErrCode("500");
				res.setErrMsg(flagV);
			}

		} catch (Exception e) {
			res.setRet(CommonUtils.ERROR_RES);
			res.setErrCode("500");
			res.setErrMsg("member activate faile" + e.getMessage());
			e.printStackTrace();
		}
		return res;
	}

	
	/**
	 * 用户发送激活邮件
	 * 
	 * @param client
	 *  @param email
	 */
	@RequestMapping(value = "/v1/activate/email", method = RequestMethod.GET)
	public Result againSendActivateEmail(
			@RequestParam(value = "website") Integer website,
			@RequestParam(value = "email") String email) {
		Result res = new Result();
		try {
			BaseBo bb = new BaseBo();
			MemberBaseBo member = this.memberService.getMemberBaseByEmailAndWebsiteId(
					email, website);
			if(member == null){
				res.setRet(-1);
				res.setErrMsg("email Invalid");
				return res;
			}
			
			//根据国家找到默认语言，再根据默认语言找邮件模板，如果没有默认语言的模板，默认用英语
			Integer lang = 1;
			if(member.getCcountry() != null && !"".equals(member.getCcountry())){
				lang = this.memberService.getDefaultLangId(member.getCcountry());
			}
			
			bb = memberService.activationSendEmail(email, website,lang, bb);
			//激活成功发送邮件
			if (null != bb && bb.getRes() == 1) {
				res.setRet(CommonUtils.SUCCESS_RES);
				
			} else {
				res.setRet(CommonUtils.ERROR_RES);
				res.setErrCode("500");
				res.setErrMsg(bb.getMsg());
			}

		} catch (Exception e) {
			res.setRet(CommonUtils.ERROR_RES);
			res.setErrCode("500");
			res.setErrMsg("member activate faile" + e.getMessage());
			e.printStackTrace();
		}
		return res;
	}
	
	
	
	/**
	 * 用户订阅
	 * 
	 */
	@RequestMapping(value = "/v1/subscribe", method = RequestMethod.POST)
	public Result subscribe(
			@RequestParam(value = "website") Integer website,
			@RequestParam(value = "lang") Integer lang,
			@RequestParam(value = "email") String email,
			@RequestParam(value = "categoryArr") String[] categoryArr
			) {
		Result res = new Result();
		try {
			if (email != null && !"".equals(email) && categoryArr.length > 0) {
				List<String> subCategory = memberEdmService.addMemberEdm(categoryArr, email, website);
				StringBuilder html = new StringBuilder();
				if (subCategory.contains("newarrivals")) {
					html.append(packLinkHtml(website, "", "New Arrivals"));
					subCategory.remove("newarrivals");
				}
				if (subCategory.contains("hot")) {
					html.append(packLinkHtml(website, "", "Hot"));
					subCategory.remove("hot");
				}
				if (subCategory.contains("clearance")) {
					html.append(packLinkHtml(website, "", "Clearance"));
					subCategory.remove("clearance");
				}

				for (String c : subCategory) {
					html.append(packLinkHtml(website, c, c));
				}

				BaseBo bb = this.memberService.sendEmailForSubscribe(email, website,
						lang, html.toString());
				
				//对接思齐的接口有异常吃掉它，不要影响客户订阅
				try {
					memberEdmService.callWebservice(categoryArr, email, lang, website);
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				res.setRet(bb.getRes());
				res.setErrMsg(bb.getMsg());
				return res;
			}

		} catch (Exception e) {
			res.setRet(CommonUtils.ERROR_RES);
			res.setErrCode("500");
			res.setErrMsg("member subscribe faile" + e.getMessage());
			e.printStackTrace();
		}
		res.setRet(CommonUtils.SUCCESS_RES);
		return res;
	}
	
	
	private String packLinkHtml(Integer websiteId, String path, String name) {
		
		String website = setting.getUrlByKey(websiteId);
		
		StringBuilder html = new StringBuilder();
		html.append("<a href=\""+website+"/" + path + "\" ");
		html.append("style=\"color:#666; font-weight:bold; text-decoration:none;\" target=\"_blank\">");
		html.append(name + "</a><br />");
		return html.toString();
	}

}
