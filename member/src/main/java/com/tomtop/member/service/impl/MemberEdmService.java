package com.tomtop.member.service.impl;


import java.rmi.RemoteException;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.tomtop.member.mappers.loyalty.MemberEdmMapper;
import com.tomtop.member.models.dto.loyalty.MemberEdm;
import com.tomtop.member.service.IMemberEdmService;
import com.tomtop.member.services.webservice.rspread.DoubleOptIn;
import com.tomtop.member.services.webservice.rspread.ServiceSoapProxy;
import com.tomtop.member.utils.HttpSendRequest;


/** 
* @ClassName: MemberEdmService 
* @Description: 会员订阅操作类
* xcf
*/
@Service
public class MemberEdmService implements IMemberEdmService{

	@Autowired
	MemberEdmMapper memberEdmMapper;
	
	@Value("${rspread.url}")
	private String url;
	
	@Value("${rspread.account}")
	private String account;
	
	@Value("${rspread.password}")
	private String password;
	
	@Value("${event_register_url}")
	private String event_register_url;

	@Value("${event_register_key}")
	private String event_register_key;
	
	/**
	 * 邮件订阅
	 */
	public List<String> addMemberEdm(String[] categoryArr,String email,int websiteId){
		List<MemberEdm> existList = memberEdmMapper.getEdmByEmail(email, websiteId);
		boolean flag = false;
		if(existList == null || existList.size() == 0){
			flag = true;
		}
		List<String> existCategory = Lists.transform(existList, list->list.getCcategory());
		Set<String> arrset = Sets.newHashSet(categoryArr);
		if(existCategory.size()>0){
			arrset = Sets.filter(arrset, cate -> !existCategory.contains(cate));
		}
		for(String cate : arrset){
			MemberEdm m = new MemberEdm();
			m.setCcategory(cate);
			m.setCemail(email);
			m.setDcreatedate(new Date());
			m.setBenabled(true);
			m.setIwebsiteid(websiteId);
			memberEdmMapper.insertSelective(m);
		}
		if(flag){
			JSONObject jsonObj = new JSONObject();
			jsonObj.put("code", "EVENT_CODE_OFFER");
			JSONObject param = new JSONObject();
			param.put("email", email);
			param.put("website", websiteId);
			param.put("ruleid", 6);
			param.put("amount", 0);
			param.put("point", 20);
			jsonObj.put("param", param);
			// 邮件订阅成功送积分20
			HttpSendRequest.sendPostByAsync(this.event_register_url,
					jsonObj.toJSONString(), this.event_register_key);
		}
		List<String> allCategory = Lists.newArrayList();
		allCategory.addAll(existCategory);
		allCategory.addAll(arrset);
		return allCategory;
	}
	
	/**
	  * @param arr
	  * @param email void
	  * @Title: callWebservice
	  * @Description: TODO(同步到思齐第三方服务器)
	  * @author liudi
	  */
	public void callWebservice(String[] categoryArr , String email,int languageid,
			int websiteid){
		
		ServiceSoapProxy ws = new ServiceSoapProxy();
		ws.setEndpoint(url);
		DoubleOptIn addOption = DoubleOptIn.Off;
		
		for(String cn : categoryArr){
			try {
				ws.createSubscription(account,password,
						cn,cn);
				ws.addSubscriberByEmail(account,password,
						email,cn, addOption);
			}catch (RemoteException e) {
				e.printStackTrace();
				if(e.toString().indexOf("\""+cn+"\" already exist")>0){
					try {
						ws.addSubscriberByEmail(account,password,
							email,cn, addOption);
					}catch (RemoteException e1) {
						e1.printStackTrace();
					}
				}
			}
		}
	}
}
