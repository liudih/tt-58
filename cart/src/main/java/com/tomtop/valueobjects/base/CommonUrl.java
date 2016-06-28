package com.tomtop.valueobjects.base;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.tomtop.utils.FoundationService;

@Service
public class CommonUrl {
	@Autowired
	FoundationService foundationService;

	@Value("${chicuu.url.forget_password}")
	private String chicuuForgetPassword;
	@Value("${chicuu.url.reg_rul}")
	private String chicuuRegUrl;
	
	@Value("${tomtop.url.forget_password}")
	private String tomtopForgetPassword;
	@Value("${tomtop.url.reg_rul}")
	private String tomtopRegUrl;
	
	@Value("${rcmoment.url.forget_password}")
	private String rcmomentForgetPassword;
	@Value("${rcmoment.url.reg_rul}")
	private String rcmomentRegUrl;
	
	public String getForgetPassword() {
		String hostname = foundationService.getHostName();
		if(hostname==null || "".equals(hostname)){
			return tomtopForgetPassword;
		}else if("chicuu".equals(hostname)){
			return chicuuForgetPassword;
		}else if("rcmoment".equals(hostname)){
			return rcmomentForgetPassword;
		}else{
			return tomtopForgetPassword;
		}
	}
	public String getRegUrl() {
		String hostname = foundationService.getHostName();
		if(hostname==null || "".equals(hostname)){
			return tomtopRegUrl;
		}else if("chicuu".equals(hostname)){
			return chicuuRegUrl;
		}else if("rcmoment".equals(hostname)){
			return rcmomentRegUrl;
		}else{
			return tomtopRegUrl;
		}
	}
}
