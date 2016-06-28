package com.tomtop.member.service.impl;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Maps;
import com.tomtop.member.configuration.global.GlobalConfigSettings;
import com.tomtop.member.mappers.tracking.AffiliateInfoMapper;
import com.tomtop.member.mappers.user.MemberBaseMapper;
import com.tomtop.member.mappers.user.MemberEmailVerifyMapper;
import com.tomtop.member.models.bo.BaseBo;
import com.tomtop.member.models.bo.LoginMemberBo;
import com.tomtop.member.models.bo.MemberBaseBo;
import com.tomtop.member.models.bo.MemberData;
import com.tomtop.member.models.bo.MemberPhotoBo;
import com.tomtop.member.models.bo.MemberUidBo;
import com.tomtop.member.models.bo.RegisterMemberBo;
import com.tomtop.member.models.bo.TokenUUidBo;
import com.tomtop.member.models.dto.MemberBase;
import com.tomtop.member.models.dto.MemberEmailVerify;
import com.tomtop.member.models.dto.SendEmail;
import com.tomtop.member.models.dto.email.model.ActivateEmailModel;
import com.tomtop.member.models.dto.email.model.ActivateSuccessModel;
import com.tomtop.member.models.dto.email.model.SubscibeEmailModel;
import com.tomtop.member.service.ICollectService;
import com.tomtop.member.service.IEmailTemplateService;
import com.tomtop.member.service.IMemberPhotoService;
import com.tomtop.member.service.IMemberReviewsService;
import com.tomtop.member.service.IMemberService;
import com.tomtop.member.utils.CommonUtils;
import com.tomtop.member.utils.CryptoUtils;
import com.tomtop.member.utils.HttpClientUtil;
import com.tomtop.member.utils.HttpSendRequest;
import com.tomtop.member.utils.UUIDGenerator;

/**
 * 用户端业务逻辑类
 * 
 * @author renyy
 *
 */
@Service
public class MemberServiceImpl implements IMemberService {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	MemberBaseMapper memberBaseMapper;
	@Autowired
	MemberEmailVerifyMapper memberEmailVerifyMapper;
	@Autowired
	AffiliateInfoMapper affiliateInfoMapper;
	@Autowired
	IMemberReviewsService memberReviewsService;
	@Autowired
	ICollectService collectService;
	@Autowired
	IMemberPhotoService memberPhotoService;
	@Autowired
	IEmailTemplateService emailTemplateService;
	@Autowired
	private GlobalConfigSettings globalSetting;
	
	private final int MAIL_VERIFY_TIME = 3;
	@Value("${sendEmailUrl}")
	private String sendEmailUrl;

	@Value("${loginSecure}")
	private String LOGIN_SECURE;

	@Value("${sendEmailKey}")
	private String sendEmailKey;

	@Value("${userActivateUrl}")
	private String userActivateUrl;

	@Value("${base_api_url}")
	private String base_api_url;

	@Value("${event_register_url}")
	private String event_register_url;

	@Value("${event_register_key}")
	private String event_register_key;

	public static final String SECURE = "01d067";

	private static final SimpleDateFormat dateFormater = new SimpleDateFormat(
			"yyyy-MM-dd");

	@Value("${discountRedirectUrl}")
	private String discountRedirectUrl;

	/**
	 * 账号密码验证
	 * 
	 * @param email
	 *            邮箱(必填)
	 * @param pwd
	 *            密码(必填)
	 * @param siteId
	 *            站点ID(必填)
	 * @return LoginMemberBo
	 * 
	 * @author renyy
	 */
	@Override
	public LoginMemberBo memberLogin(String email, String pwd, Integer siteId) {
		LoginMemberBo lmbo = new LoginMemberBo();
		if (email == null || "".equals(email)) {
			lmbo.setRes(-1001);
			lmbo.setMsg("email is null");
			return lmbo;
		}else{
			email = email.toLowerCase();
		}
		if (siteId == null) {
			lmbo.setRes(-1002);
			lmbo.setMsg("siteId is null");
			return lmbo;
		}
		if(CommonUtils.checkEmail(email) == false){
			lmbo.setRes(-1111);
			lmbo.setMsg("email wrong format");
			return lmbo;
		}
		List<MemberBase> mbList = memberBaseMapper.getMemberBase(email, siteId,
				null, null, null);
		if (mbList != null && mbList.size() > 0) {
			MemberBase mb = mbList.get(0);
			String securityStr = mb.getCpasswd();
			boolean b = CryptoUtils.validateHash(pwd, securityStr);
			if (b) {
				lmbo.setRes(CommonUtils.SUCCESS_RES);
				lmbo.setId(mb.getIid());
				lmbo.setEmail(mb.getCemail());
				lmbo.setAccount(mb.getCaccount());
				lmbo.setCountry(mb.getCcountry());
				lmbo.setAid(affiliateInfoMapper.getAidByEmail(email, siteId));
			} else {
				lmbo.setRes(-1004);
				lmbo.setMsg("password is incorrect ");
			}
		} else {
			lmbo.setRes(-1005);
			lmbo.setMsg("not find member");
		}
		return lmbo;
	}

	/**
	 * 注册用户
	 * 
	 * @param email
	 *            邮箱(必填)
	 * @param pwd
	 *            密码(必填)
	 * @param country
	 *            国家(必填)
	 * @param siteId
	 *            站点ID(必填)
	 * @param vhost
	 *            来源(必填)
	 * @return RegisterMemberBo
	 * 
	 * @author renyy
	 * @throws Exception
	 */
	@Override
	public RegisterMemberBo memberRegister(String email, String pwd,
			String country, Integer siteId, String vhost) throws Exception {
		RegisterMemberBo rmb = new RegisterMemberBo();
		if (email == null || "".equals(email)) {
			rmb.setRes(-1006);
			rmb.setMsg("register email is null");
			return rmb;
		}else{
			email = email.toLowerCase();
		}
		if (siteId == null) {
			rmb.setRes(-1007);
			rmb.setMsg("register siteId is null");
			return rmb;
		}
		if (pwd == null || "".equals(pwd)) {
			rmb.setRes(-1008);
			rmb.setMsg("register password is null");
			return rmb;
		}
		if (country == null || "".equals(country)) {
			rmb.setRes(-1009);
			rmb.setMsg("register country is null");
			return rmb;
		}
		if (!CommonUtils.checkEmail(email)) {
			rmb.setRes(-1010);
			rmb.setMsg("register E-mail format is incorrect");
			return rmb;
		}
		List<MemberBase> mbList = memberBaseMapper.getMemberBase(email, siteId,
				null, null, null);
		if (mbList != null && mbList.size() > 0) {
			rmb.setRes(-1011);
			rmb.setMsg("email already exists.");
			return rmb;
		}
		MemberBase member = new MemberBase();
		member.setCemail(email);
		member.setCpasswd(CryptoUtils.getHash(pwd, 2));
		member.setIgroupid(1); // default 1
		member.setCcountry(country);
		member.setBnewsletter(false);
		member.setBactivated(false);
		member.setCvhost(vhost);
		member.setIwebsiteid(siteId);
		member.setCfirstname("");
		member.setClastname("");
		int ist = memberBaseMapper.insertMemberBase(member);
		if (ist <= 0) {
			rmb.setRes(-1012);
			rmb.setMsg("registration failed");
			return rmb;
		} else {
			// 根据国家找到默认语言，再根据默认语言找邮件模板，如果没有默认语言的模板，默认用英语
			Integer lang = 1;
			if (country != null && !"".equals(country)) {
				lang = this.getDefaultLangId(country);
			}
			BaseBo bb = activationSendEmail(email, siteId, lang, rmb);
			this.getOfferBySite("register", email, siteId);
			rmb.setMsg(bb.getMsg());
			rmb.setRes(bb.getRes());

			return rmb;
		}
	}

	/**
	 * 当用户登录时来验证账号密码,如果验证通过则会返回一个token和uuid,然后保存token和uuid到cookie来标示用户已经登录
	 * 
	 * @param email
	 *            邮箱(必填)
	 * @param pwd
	 *            密码(必填)
	 * @param siteId
	 *            站点ID(必填)
	 * 
	 * @return TokenUUidBo 验证通过
	 */
	@Override
	public TokenUUidBo getToken(String email, String password, Integer siteId) {
		TokenUUidBo tub = new TokenUUidBo();
		if (email == null || email.length() == 0) {
			tub.setRes(-1101);
			tub.setMsg("getToken email is null");
			return tub;
		}else{
			email = email.toLowerCase();
		}
		if (password == null || password.length() == 0) {
			tub.setRes(-1102);
			tub.setMsg("getToken password is null");
			return tub;
		}
		if (siteId == null) {
			siteId = 1;
		}

		MemberBase member = new MemberBase();
		member.setCemail(email);
		member.setIwebsiteid(siteId);

		member = memberBaseMapper.getMemberBaseWhere(member);
		boolean b = CryptoUtils.validateHash(password, member.getCpasswd());
		if (!b) {
			tub.setRes(-1103);
			tub.setMsg("getToken password is incorrect");
			return tub;
		}
		if (LOGIN_SECURE == null) {
			LOGIN_SECURE = SECURE;
		}

		// 取日期
		Date date = new Date();
		String dateStr = dateFormater.format(date);

		StringBuilder key = new StringBuilder();
		key.append(member.getCemail().toLowerCase());
		key.append(member.getCpasswd());
		key.append(LOGIN_SECURE);
		key.append(dateStr);
		String token = CryptoUtils.md5(key.toString());

		Integer id = member.getIid();
		String uuid = member.getCuuid();
		if (uuid == null) {
			uuid = UUIDGenerator.createAsString();
			MemberBase udmb = new MemberBase();
			udmb.setIid(id);
			udmb.setCuuid(uuid);
			memberBaseMapper.update(udmb);
		}
		tub.setToken(token);
		tub.setUuid(uuid);

		return tub;
	}

	/**
	 * 当第三方登陆的时候，生成token规则
	 * 
	 * @param email
	 *            邮箱(必填)
	 * @param pwd
	 *            密码(必填)
	 * @param siteId
	 *            站点ID(必填)
	 * 
	 * @return TokenUUidBo 验证通过
	 */
	@Override
	public TokenUUidBo getOtherToken(String email, Integer siteId) {
		TokenUUidBo tub = new TokenUUidBo();
		if (email == null || email.length() == 0) {
			tub.setRes(-1104);
			tub.setMsg("getToken email is null");
			return tub;
		}
		if (siteId == null) {
			siteId = 1;
		}

		MemberBase member = new MemberBase();
		member.setCemail(email);
		member.setIwebsiteid(siteId);
		;
		member = memberBaseMapper.getMemberBaseWhere(member);
		if (LOGIN_SECURE == null) {
			LOGIN_SECURE = SECURE;
		}
		// 取日期
		Date date = new Date();
		String dateStr = dateFormater.format(date);

		StringBuilder key = new StringBuilder();
		key.append(member.getCemail().toLowerCase());
		key.append(member.getCpasswd());
		key.append(LOGIN_SECURE);
		key.append(dateStr);
		String token = CryptoUtils.md5(key.toString());

		Integer id = member.getIid();
		String uuid = member.getCuuid();
		if (uuid == null) {
			uuid = UUIDGenerator.createAsString();
			MemberBase udmb = new MemberBase();
			udmb.setIid(id);
			udmb.setCuuid(uuid);
			memberBaseMapper.update(udmb);
		}
		tub.setToken(token);
		tub.setUuid(uuid);

		return tub;
	}

	/**
	 * 根据UUID获取邮箱
	 * 
	 * @param uuid
	 *            邮箱(必填)
	 * 
	 * @return String
	 */
	@Override
	public MemberUidBo getMemberEmailByUUid(String uuid, Integer siteId) {
		MemberUidBo mubo = new MemberUidBo();
		if (uuid == null || "".equals(uuid.trim())) {
			return null;
		}
		MemberBase mb = new MemberBase();

		mb.setCuuid(uuid);
		mb.setIwebsiteid(siteId);
		mb = memberBaseMapper.getMemberBaseWhere(mb);
		if (mb != null) {
			mubo.setRes(CommonUtils.SUCCESS_RES);
			mubo.setEmail(mb.getCemail());
			String account = mb.getCaccount();
			if (null == account) {
				account = "";
			}
			mubo.setAccount(account);
			mubo.setAid(affiliateInfoMapper.getAidByEmail(mb.getCemail(),
					siteId));
		} else {
			mubo.setRes(CommonUtils.ERROR_RES);
			mubo.setMsg("uuid not find");
		}
		return mubo;
	}

	/**
	 * 根据UUID获取用户邮箱
	 * 
	 * @param uuid
	 *            邮箱(必填)
	 * 
	 * @return String
	 */
	@Override
	public String getEmailByUUid(String uuid, Integer siteId) {
		if (uuid == null || "".equals(uuid.trim())) {
			return null;
		}
		MemberBase mb = new MemberBase(siteId,uuid);
		mb = memberBaseMapper.getMemberBaseWhere(mb);
		if (mb != null) {
			return mb.getCemail();
		} 
		return null;
	}
	
	@Override
	public MemberBaseBo getMemberBaseByEmailAndWebsiteId(String email,
			int siteid) {
		MemberBaseBo mbb = new MemberBaseBo();
		MemberBase mb = this.memberBaseMapper.getMemberBaseByEmailAndWebsiteId(
				email, siteid);
		if (mb != null) {
			try {

				mbb.setRes(CommonUtils.SUCCESS_RES);
				BeanUtils.copyProperties(mbb, mb);
				MemberPhotoBo photo = memberPhotoService.getMemberPhoto(email,
						siteid);

				mbb.setCpasswd(""); // 密码不返回
				if (null != photo) {
					mbb.setCcontenttype(photo.getCcontenttype());
					mbb.setBfile(photo.getBfile());
					mbb.setCimageurl(photo.getCimageurl());
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			mbb.setRes(CommonUtils.ERROR_RES);
			mbb.setMsg("email not found ");
		}
		return mbb;
	}

	@Override
	public boolean updateMemberBaseByEmailAndWebsiteId(MemberBase mb) {
		Integer i = this.memberBaseMapper.update(mb);

		return i > 0 ? true : false;
	}

	/**
	 * 对用户相关信息作各种操作之前先验证其是否有权限
	 * 
	 * @param json
	 *            包含uuid与email
	 * @return 是否有权限
	 */
	public boolean validate(Integer websiteId, String email, String uuid) {
		MemberUidBo member = this.getMemberEmailByUUid(uuid, websiteId);
		if (null != member && email.equals(member.getEmail())) {
			return true;
		} else {
			return false;
		}

	}

	public MemberBase getMemberBaseByUuid(String uuid) {
		return this.memberBaseMapper.getMemberBaseByUuid(uuid);
	}

	/**
	 * 当用户登录时来验证账号密码,如果验证通过则会返回一个token和uuid,然后保存token和uuid到cookie来标示用户已经登录
	 * 
	 * @param email
	 *            邮箱(必填)
	 * @param pwd
	 *            密码(必填)
	 * @param siteId
	 *            站点ID(必填)
	 * 
	 * @return TokenUUidBo 验证通过
	 */
	@Override
	public TokenUUidBo getTokenByEmailAndPwdAndSiteId(String email,
			String password, Integer siteId) {
		TokenUUidBo tub = new TokenUUidBo();
		if (email == null || email.length() == 0) {
			tub.setRes(-1101);
			tub.setMsg("getToken email is null");
			return tub;
		}
		if (password == null || password.length() == 0) {
			tub.setRes(-1102);
			tub.setMsg("getToken password is null");
			return tub;
		}
		if (siteId == null) {
			siteId = 1;
		}

		MemberBase member = new MemberBase();
		member.setCemail(email);
		member.setIwebsiteid(siteId);
		member = memberBaseMapper.getMemberBaseWhere(member);
		// boolean b = CryptoUtils.validateHash(password, member.getCpasswd());
		// if(!b){
		// tub.setRes(-1103);
		// tub.setMsg("getToken password is incorrect");
		// return tub;
		// }
		if (LOGIN_SECURE == null) {
			LOGIN_SECURE = SECURE;
		}

		// 取日期
		Date date = new Date();
		String dateStr = dateFormater.format(date);

		StringBuilder key = new StringBuilder();
		key.append(member.getCemail().toLowerCase());
		key.append(member.getCpasswd());
		key.append(LOGIN_SECURE);
		key.append(dateStr);
		String token = CryptoUtils.md5(key.toString());

		Integer id = member.getIid();
		String uuid = member.getCuuid();
		if (uuid == null) {
			uuid = UUIDGenerator.createAsString();
			MemberBase udmb = new MemberBase();
			udmb.setIid(id);
			udmb.setCuuid(uuid);
			memberBaseMapper.update(udmb);
		}
		tub.setToken(token);
		tub.setUuid(uuid);

		return tub;
	}

	/**
	 * 过期统计方法 （已失效）
	 */
	@Override
	public JSONObject getMemberStatisticsByEmailAndWebsiteId(String email,
			Integer websiteId, String uuid) {

		String couponCountUrl = this.discountRedirectUrl
				+ "/loyalty/v1/coupon/amount/" + uuid;
		String pointCountUrl = this.discountRedirectUrl
				+ "/integral/v1/qcount/" + websiteId + "?email=" + email;
		Integer pointCount = 0;
		String pointCountStr = HttpClientUtil.doGet(pointCountUrl);
		try {
			if (null == pointCountStr) {
				pointCount = 0;
			} else {
				pointCount = Integer.parseInt(pointCountStr);
			}
		} catch (Exception e) {
			pointCount = 0;
		}

		Integer couponCount = 0;
		try {
			String couponCountStr = HttpClientUtil.doGet(couponCountUrl);
			if (null == couponCountStr) {
				couponCount = 0;
			} else {
				couponCount = Integer.parseInt(couponCountStr);
			}

		} catch (Exception e) {
			couponCount = 0;
		}
		Integer collectCount = this.collectService.getCollectsCountByEmail(
				email, websiteId);
		Integer reviewsCount = this.memberReviewsService
				.getTotalReviewsCountByMemberEmailAndSiteId(email, -1, 0,
						websiteId);
		JSONObject json = new JSONObject();
		json.put("collectCount", collectCount);
		json.put("reviewsCount", reviewsCount);
		json.put("pointCount", pointCount);
		json.put("couponCount", couponCount);

		return json;
	}

	/**
	 * 用户邮箱激活
	 */
	public String activation(String activationcode, Integer site)
			throws Exception {
		MemberEmailVerify emailVeifi = memberEmailVerifyMapper
				.getActivationCode(activationcode);

		if (emailVeifi == null || StringUtils.isEmpty(emailVeifi.getCemail())) {
			return "code Invalid";
		}

		Date validate = emailVeifi.getDvaliddate();
		long currentTime = System.currentTimeMillis();

		if (currentTime > validate.getTime()) {
			return "time Invalid";
		}

		MemberBase member = memberBaseMapper.getMemberBaseByEmailAndWebsiteId(
				emailVeifi.getCemail(), site);
		//不存在用户
		if (null == member) {
			return "email Invalid";
		}
		//如果已经激活成功的直接返回
		if (member.isBactivated()) {
			return "email is activated";
		}
		
		member.setBactivated(true);
		int result = memberBaseMapper.update(member);

		if (result > 0) {

			Integer lang = getDefaultLangId(member.getCcountry());

			this.activationSuccess(emailVeifi.getCemail(), site, lang);
		}
		return result > 0 ? "success" : "faild";
	}

	public Integer getDefaultLangId(String ccountry) {
		// 根据国家找到默认语言，再根据默认语言找邮件模板，如果没有默认语言的模板，默认用英语
		Integer lang = 1;
		if (ccountry != null && !"".equals(ccountry)) {
			String url = base_api_url + "/base/country/v1/code/" + ccountry;
			try {
				String jsonStr = HttpClientUtil.doGet(url);
				ObjectMapper om = new ObjectMapper();
				JsonNode jsonNode = om.readTree(jsonStr);
				lang = jsonNode.get("data").get(0).get("officialLanguageId")
						.asInt();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return lang;
	}

	@Override
	public BaseBo activationSuccess(String email, Integer siteId, Integer lang)
			throws Exception {
		BaseBo rmb = new BaseBo();
		// 获取拼接url or 发送内容
		ActivateSuccessModel model = new ActivateSuccessModel(email,
				"ActivateSuccess", lang);
		logger.debug("model======>" + model.toString());
		String title = "";
		String content = "";
		Map<String, String> titleAndContentMap = Maps.newHashMap();
		try {
			titleAndContentMap = emailTemplateService.getEmailContent(model,
					siteId);
			if (null == titleAndContentMap) {
				model = new ActivateSuccessModel(email, "ActivateSuccess", 1);
				titleAndContentMap = emailTemplateService.getEmailContent(
						model, siteId);
			}
			if (null != titleAndContentMap && titleAndContentMap.size() > 0) {
				title = titleAndContentMap.get("title");
				content = titleAndContentMap.get("content");
			} else {
				rmb.setRes(-1);
				rmb.setMsg("title and content is null ,can not send email");
				return rmb;
			}
		} catch (Exception e) {
			logger.error("can not deal with activationSuccess email content");
			e.printStackTrace();
			rmb.setRes(-1);
			rmb.setMsg("can not deal with activationSuccess email content");
			return rmb;
		}
		String fromName = globalSetting.getFromNameByKey(siteId);
		String fromEmail = globalSetting.getFromEmailByKey(siteId);
		// 构建发送邮件对象转换json发送
		SendEmail se = new SendEmail(fromEmail, email, title, content,fromName);
		String json = new ObjectMapper().writeValueAsString(se);
		// String ret = HttpClientUtil.doPost(sendEmailUrl, json);

		HttpSendRequest.sendPostByAsync(sendEmailUrl, json, sendEmailKey);

		rmb.setRes(1);
		rmb.setMsg("email already send");
		//激活成功送积分优惠
		this.getOfferBySite("active", email, siteId);
		return rmb;
	}

	public BaseBo activationSendEmail(String email, Integer siteId,
			Integer lang, BaseBo rmb) throws Exception {
		MemberBase member = memberBaseMapper.getMemberBaseByEmailAndWebsiteId(
				email, siteId);
		if (member == null) {
			rmb.setRes(-1);
			rmb.setMsg("email Invalid");
			return rmb;
		}
		// 注册成功发送激活
		String code = UUIDGenerator.createAsString().replace("-", "");

		GregorianCalendar calendar = new GregorianCalendar();
		calendar.add(Calendar.DATE, 3);
		Date validdate = calendar.getTime();

		MemberEmailVerify mev = new MemberEmailVerify();
		mev.setCemail(email);
		mev.setIdaynumber(MAIL_VERIFY_TIME);
		mev.setDvaliddate(validdate);
		mev.setCactivationcode(code);

		int ist = memberEmailVerifyMapper.insert(mev);
		if (ist <= 0) {
			logger.error("MemberServiceImpl registration email verify error [-1013]");
		}
		String web = globalSetting.getUrlByKey(siteId);
		String url = web + userActivateUrl + "?code=" + code;
		// 获取拼接url or 发送内容
		ActivateEmailModel model = new ActivateEmailModel(email, url, code, "Activate", lang);
		logger.debug("model======>" + model.toString());
		String title = "";
		String content = "";
		Map<String, String> titleAndContentMap = Maps.newHashMap();
		try {
			titleAndContentMap = emailTemplateService.getEmailContent(model,
					siteId);
			if (null == titleAndContentMap) {
				model = new ActivateEmailModel(email, url, code, "Activate", 1);
				titleAndContentMap = emailTemplateService.getEmailContent(
						model, siteId);
			}
			if (null != titleAndContentMap && titleAndContentMap.size() > 0) {
				title = titleAndContentMap.get("title");
				content = titleAndContentMap.get("content");
			} else {
				logger.error("title and content is null ,can not send email");
				rmb.setRes(-1);
				rmb.setMsg("title and content is null ,can not send email");
				return rmb;
			}
		} catch (Exception e) {
			logger.error("can not deal with verify email content");
			e.printStackTrace();
			rmb.setRes(-1);
			rmb.setMsg("can not deal with verify email content");
			return rmb;
		}

		String fromName = globalSetting.getFromNameByKey(siteId);
		String fromEmail = globalSetting.getFromEmailByKey(siteId);
		// 构建发送邮件对象转换json发送
		SendEmail se = new SendEmail(fromEmail, email, title, content,
				fromName);
		String json = new ObjectMapper().writeValueAsString(se);

		HttpSendRequest.sendPostByAsync(sendEmailUrl, json, sendEmailKey);

		rmb.setRes(1);
		rmb.setMsg("email already send");
		return rmb;
	}

	public BaseBo sendEmailForSubscribe(String email, Integer websiteid,
			Integer language, String categoryLinks) throws Exception {
		//订阅不需要是会员
//		MemberBase member = memberBaseMapper.getMemberBaseByEmailAndWebsiteId(
//				email, websiteid);

		BaseBo rmb = new BaseBo();

//		if (member == null) {
//			rmb.setRes(-1);
//			rmb.setMsg("email Invalid");
//			return rmb;
//		}

		// 获取拼接url or 发送内容
		SubscibeEmailModel model = new SubscibeEmailModel(email, categoryLinks,
				"WelcomeSubscibe", language);
		logger.debug("model======>" + model.toString());
		String title = "";
		String content = "";
		Map<String, String> titleAndContentMap = Maps.newHashMap();
		try {
			titleAndContentMap = emailTemplateService.getEmailContent(model,
					websiteid);
			if (null == titleAndContentMap) {
				model = new SubscibeEmailModel(email, categoryLinks,
						"WelcomeSubscibe", 1);
				titleAndContentMap = emailTemplateService.getEmailContent(
						model, websiteid);
			}
			if (null != titleAndContentMap && titleAndContentMap.size() > 0) {
				title = titleAndContentMap.get("title");
				content = titleAndContentMap.get("content");
			} else {
				logger.error("title and content is null ,can not send email");
				rmb.setRes(-1);
				rmb.setMsg("title and content is null ,can not send email");
				return rmb;
			}
		} catch (Exception e) {
			logger.error("can not deal with verify email content");
			e.printStackTrace();
			rmb.setRes(-1);
			rmb.setMsg("can not deal with verify email content");
			return rmb;
		}
		
		content = content.replace("{0}", categoryLinks);
		
		String fromName = globalSetting.getFromNameByKey(websiteid);
		String fromEmail = globalSetting.getFromEmailByKey(websiteid);
		// 构建发送邮件对象转换json发送
		SendEmail se = new SendEmail(fromEmail, email, title, content,fromName);
		String json = new ObjectMapper().writeValueAsString(se);

		HttpSendRequest.sendPostByAsync(sendEmailUrl, json, sendEmailKey);

		rmb.setRes(1);
		rmb.setMsg("email already send");
		return rmb;
	}

	/**
	 * 更新会员资料 (add by tony 20160513)
	 * 
	 * @param md
	 *           更新对象(必填)
	 * 
	 * @return String
	 */
	@Override
	public int updateMemberData(MemberData md) {
		return memberBaseMapper.updateMemberData(md);
	}
	
	/**
	 * 根据站点判判断送优惠
	 */
	private void getOfferBySite(String optType, String email,Integer website){
		JSONObject jsonObj = new JSONObject();
		jsonObj.put("code", "EVENT_CODE_OFFER");
		JSONObject param = new JSONObject();
		param.put("email", email);
		param.put("website", website);
		
		//tomtop注册送积分
		if("register".equals(optType) && website == 1){
			param.put("ruleid", 0);
			param.put("amount", 0);
			param.put("point", 200);
			jsonObj.put("param", param);
			HttpSendRequest.sendPostByAsync(this.event_register_url,
					jsonObj.toJSONString(), this.event_register_key);
			return;
		}
		
		//chicuu注册并激活送积分加优惠卷
		if("active".equals(optType) && website == 10){
			param.put("ruleid", 796);//优惠卷规则Id
			param.put("amount", 1);//一张
			param.put("point", 100);//100积分
			jsonObj.put("param", param);
			// 激活成功送积分优惠卷
			HttpSendRequest.sendPostByAsync(this.event_register_url,
					jsonObj.toJSONString(), this.event_register_key);
			return;
		}
	} 
}
