package com.tomtop.member.service.impl;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Map;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Maps;
import com.tomtop.framework.core.utils.SecurityMD5;
import com.tomtop.member.configuration.global.GlobalConfigSettings;
import com.tomtop.member.mappers.user.ForgetPasswdBaseMapper;
import com.tomtop.member.mappers.user.MemberBaseMapper;
import com.tomtop.member.models.bo.BaseBo;
import com.tomtop.member.models.dto.ForgetPasswdBase;
import com.tomtop.member.models.dto.MemberBase;
import com.tomtop.member.models.dto.SendEmail;
import com.tomtop.member.models.dto.email.model.FindPasswordEmailModel;
import com.tomtop.member.service.IEmailTemplateService;
import com.tomtop.member.service.IForgotPasswordService;
import com.tomtop.member.utils.CommonUtils;
import com.tomtop.member.utils.CryptoUtils;
import com.tomtop.member.utils.HttpSendRequest;

/**
 * 用户忘记密码业务逻辑类
 * 
 * @author renyy
 */
@Service
public class ForgotPasswordServiceImpl implements IForgotPasswordService {
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Value("${sendEmailUrl}")
	private String sendEmailUrl;
	@Value("${userFindPwdUrl}")
	private String userFindPwdUrl;
	@Value("${sendEmailKey}")
	private String sendEmailKey;
	
	private final static String USERPASS = "UpdatePass";
	
	@Autowired
	ForgetPasswdBaseMapper forgetPassMapper;
	@Autowired
	MemberBaseMapper memberBaseMapper;
	@Autowired
	IEmailTemplateService emailTemplateService;
	@Autowired
	private GlobalConfigSettings globalSetting;
	
	private final int MAIL_VERIFY_TIME = 3;//先手动设置值.后续在parameter中获取
	
	/**
	 * 用户忘记密码发送验证邮件
	 * @param email
	 * 		                邮箱(必填)
	 * @param siteId
	 * 		               站点ID(必填)
	 * 
	 * @return BaseBo
	 * 
	 * @author renyy
	 */
	@Override
	public BaseBo forgotPassword(String email, Integer siteId,Integer lang) {
		BaseBo bb = new BaseBo();
		try {
			if(email == null){
				bb.setRes(-1201);
				bb.setMsg("email is null");
				return bb;
			}
			if(siteId == null){
				bb.setRes(-1202);
				bb.setMsg("siteId is null");
				return bb;
			}
			//判断是否发送超过限制
			Integer nums = forgetPassMapper.getCountByCmembermailAndDcreatedate(email, siteId);
			if (nums >= MAIL_VERIFY_TIME) {
				bb.setRes(-1203);
				bb.setMsg("More than a daily verification visits (3)");
				return bb;
			}
			String uuid = UUID.randomUUID().toString().replace("-", "");
			Timestamp now = new Timestamp(System.currentTimeMillis());
			long date = now.getTime();// 忽略毫秒数
			String sid = date + uuid;
			String cid = SecurityMD5.encode(sid);// 数字签名
			String code = CryptoUtils.getRandomString(6);
			cleanForgetPasswor(nums, email, siteId);
			ForgetPasswdBase fpb = new ForgetPasswdBase();
			fpb.setCid(cid);
			fpb.setCmemberemail(email);
			fpb.setCrandomcode(code);
			fpb.setBuse(true);
			fpb.setIwebsiteid(siteId);
			//添加记录到用户找回密码表
			Integer res = addForgetPasswordBass(fpb);
			if(res == 1){
				logger.info("add forget success {} ", email);
			}else{
				logger.error("add forget error {} ", email);
			}
			String web = globalSetting.getUrlByKey(siteId);
			String url = web + userFindPwdUrl + "?code=" + cid;
			//获取拼接url or 发送内容
			FindPasswordEmailModel model = new FindPasswordEmailModel(email,url,code,USERPASS,lang);
			logger.debug("model======>" + model.toString());
			String title = "";
			String content = "";
			Map<String, String> titleAndContentMap = Maps.newHashMap();
			try {
				titleAndContentMap = emailTemplateService.getEmailContent(model,
						siteId);
				if (null != titleAndContentMap && titleAndContentMap.size() > 0) {
					title = titleAndContentMap.get("title");
					content = titleAndContentMap.get("content");
				} else {
					logger.error("title and content is null ,can not send email");
					bb.setRes(-1333);
					bb.setMsg("title and content is null ,can not send email");
					return bb;
				}
			} catch (Exception e) {
				logger.error("can not deal with verify email content");
				e.printStackTrace();
				bb.setRes(-1334);
				bb.setMsg("can not deal with verify email content");
				return bb;
			}
			String fromName = globalSetting.getFromNameByKey(siteId);
			String fromEmail = globalSetting.getFromEmailByKey(siteId);
			//构建发送邮件对象转换json发送
			SendEmail se = new SendEmail(fromEmail,email,title,content,fromName);
			String json = new ObjectMapper().writeValueAsString(se); 
			HttpSendRequest.sendPostByAsync(sendEmailUrl, json, sendEmailKey);
			
			bb.setRes(CommonUtils.SUCCESS_RES);
			bb.setMsg(CommonUtils.SUCCESS_MSG_RES);
			
			return bb;
		
		} catch (Exception e) {
			e.printStackTrace();
			bb.setRes(-1336);
			bb.setMsg("forgot password exception ");
			return bb;
		}
	}
	
	/**
	 * 添加记录到 用户找回密码表 (私有)
	 * 
	 * @param ForgetPasswdBase
	 * 
	 * @return Integer
	 */
	private Integer addForgetPasswordBass(ForgetPasswdBase fpb){
		return forgetPassMapper.insert(fpb);
	}
	
	/**
	 * 用户再一次发送忘记密码时把上一次的token清除(私有)
	 * 
	 * @param ForgetPasswdBase
	 * 
	 */
	private void cleanForgetPasswor(Integer nums,String email,Integer siteId){
		if(nums > 0){
			ForgetPasswdBase updUse = new ForgetPasswdBase();
			updUse.setBuse(false);
			updUse.setCmemberemail(email);
			updUse.setIwebsiteid(siteId);
			forgetPassMapper.update(updUse);
		}
	}

	/**
	 * 忘记密码-修改
	 * @param cid
	 * 		               唯一id(必填)
	 * @param pwd
	 *  			密码(必填)
	 * @param siteId
	 * 		               站点ID(必填)
	 * 
	 * @return BaseBo
	 * 
	 * @author renyy
	 */
	@Override
	public BaseBo alertPassword(String cid, String pwd,
			Integer siteId) {
		BaseBo bb = new BaseBo();
		if(cid == null){
			bb.setRes(-1205);
			bb.setMsg("cid is null");
			return bb;
		}
		if(siteId == null){
			bb.setRes(-1206);
			bb.setMsg("siteId is null");
			return bb;
		}
		if(pwd == null){
			bb.setRes(-1207);
			bb.setMsg("password is null");
			return bb;
		}
		ForgetPasswdBase fpb = forgetPassMapper.selectByCid(cid);
		if(fpb == null){
			bb.setRes(-1208);
			bb.setMsg("this token is fail,ignore change password");
			return bb;
		}
		boolean b = fpb.isBuse();
		if(b){
			Timestamp ts = new Timestamp(System.currentTimeMillis());
			Timestamp createDate = fpb.getDcreatedate();
			Date nowDate = ts;
			Date validate = createDate;
			Calendar calendar = new GregorianCalendar();
			calendar.setTime(validate);
			calendar.add(Calendar.DATE, MAIL_VERIFY_TIME);
			validate = calendar.getTime();
			if (nowDate.getTime() > validate.getTime()) {
				logger.error("token has expired ");
				bb.setRes(-1209);
				bb.setMsg("token has expired");
				return bb;
			}
			String email = fpb.getCmemberemail();
			String cpassword = CryptoUtils.getHash(pwd, 2);
			int res = -1;
			MemberBase mb = new MemberBase();
			mb.setCemail(email);
			mb.setCpasswd(cpassword);
			mb.setIwebsiteid(siteId);
			res = memberBaseMapper.update(mb);
			if(res <= 0){
				logger.error("change password error ");
				bb.setRes(-1210);
				bb.setMsg("change password error");
				return bb;
			}
			ForgetPasswdBase updfpb = new ForgetPasswdBase();
			updfpb.setBuse(false);
			updfpb.setCid(cid);
			res = forgetPassMapper.update(updfpb);
			if(res <= 0){
				logger.error("cid update bus false error {}", cid);
			}
			bb.setRes(CommonUtils.SUCCESS_RES);
			bb.setMsg(CommonUtils.SUCCESS_MSG_RES);
		}else{
			bb.setRes(-1211);
			bb.setMsg("this token is fail,ignore change password");
		}
		return bb;
	}
	
	@Override
	public boolean updatePassword(String email, String pwd,
			Integer siteId) {
		
		String cpassword = CryptoUtils.getHash(pwd, 2);
		
		MemberBase mb = new MemberBase();
		mb.setCemail(email);
		mb.setCpasswd(cpassword);
		mb.setIwebsiteid(siteId);
		int res = memberBaseMapper.update(mb);
		
		return res > 0;
	}
}
