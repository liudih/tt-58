package com.tomtop.services.impl;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tomtop.entity.BaseBean;
import com.tomtop.entity.ReportError;
import com.tomtop.entity.ReportErrorVo;
import com.tomtop.entity.WholesaleInquiry;
import com.tomtop.entity.WholesaleInquiryVo;
import com.tomtop.mappers.interaction.ReportErrorMapper;
import com.tomtop.mappers.interaction.WholesaleInquiryMapper;
import com.tomtop.services.IFeedbackService;
import com.tomtop.utils.CommonsUtil;


/**
 * 详情用户反馈业务
 * 
 * @author renyy
 *
 */
@Service
public class FeedbackServiceImpl implements IFeedbackService {

	@Autowired
	WholesaleInquiryMapper wholesaleInquiryMapper;
	
	@Autowired
	ReportErrorMapper reportErrorMapper;
	
	/**
	 * 添加 Wholesale Inquiry
	 * 
	 * @param WholesaleInquiryVo
	 * 
	 * 
	 * @return BaseBean
	 * @author renyy
	 */
	@Override
	public BaseBean addWholesaleInquiry(WholesaleInquiryVo wivo) {
		BaseBean bb = new BaseBean();
	    String listingId = wivo.getListingId();
	    if(listingId == null || "".equals(listingId)){
	    	bb.setRes(-80001);
	    	bb.setMsg("listingId is null");
	    	return bb;
	    }
	    String sku = wivo.getSku();
	    if(sku == null || "".equals(sku)){
	    	bb.setRes(-80002);
	    	bb.setMsg("sku is null");
	    	return bb;
	    }
	    String name = wivo.getName();
	    if(name == null || "".equals(name)){
	    	bb.setRes(-80003);
	    	bb.setMsg("name is null");
	    	return bb;
	    }
	    if(CommonsUtil.checkLength(name, 40) == false){
	    	bb.setRes(-80013);
	    	bb.setMsg("name length over");
	    	return bb;
	    }
	    String mobilePhone = wivo.getMobilePhone();
	    if(mobilePhone == null || "".equals(mobilePhone)){
	    	bb.setRes(-80004);
	    	bb.setMsg("mobilePhone is null");
	    	return bb;
	    }
	    if(CommonsUtil.checkLength(mobilePhone, 50) == false){
	    	bb.setRes(-80014);
	    	bb.setMsg("mobilePhone length over");
	    	return bb;
	    }
	    String emailAddress = wivo.getEmailAddress();
	    if(emailAddress == null || "".equals(emailAddress)){
	    	bb.setRes(-80005);
	    	bb.setMsg("emailAddress is null");
	    	return bb;
	    }
	    if(CommonsUtil.checkLength(emailAddress, 80) == false){
	    	bb.setRes(-80015);
	    	bb.setMsg("emailAddress length over");
	    	return bb;
	    }
	    Double targetPrice = wivo.getTargetPrice();
	    if(targetPrice == null ){
	    	bb.setRes(-80006);
	    	bb.setMsg("targetPrice is null");
	    	return bb;
	    }
	    Integer orderQuantity = wivo.getOrderQuantity();
	    if(orderQuantity == null ){
	    	bb.setRes(-80007);
	    	bb.setMsg("orderQuantity is null");
	    	return bb;
	    }
	    String countryState = wivo.getCountryState();
	    if(countryState == null || "".equals(countryState)){
	    	bb.setRes(-80008);
	    	bb.setMsg("countryState is null");
	    	return bb;
	    }
	    if(CommonsUtil.checkLength(countryState, 80) == false){
	    	bb.setRes(-80018);
	    	bb.setMsg("countryState length over");
	    	return bb;
	    }
	    String companyName = wivo.getCompanyName();
	    if(companyName == null || "".equals(companyName)){
	    	bb.setRes(-80009);
	    	bb.setMsg("companyName is null");
	    	return bb;
	    }
	    if(CommonsUtil.checkLength(companyName, 50) == false){
	    	bb.setRes(-80019);
	    	bb.setMsg("companyName length over");
	    	return bb;
	    }
	    String writeInquiry = wivo.getWriteInquiry();
	    if(writeInquiry == null || "".equals(writeInquiry)){
	    	bb.setRes(-80010);
	    	bb.setMsg("writeInquiry is null");
	    	return bb;
	    }
	    if(CommonsUtil.checkLength(writeInquiry, 1800) == false){
	    	bb.setRes(-80020);
	    	bb.setMsg("writeInquiry length over");
	    	return bb;
	    }
	    WholesaleInquiry widto = new WholesaleInquiry();
	    widto.setClistingid(listingId);
	    widto.setCsku(sku);
	    widto.setCname(name);
	    widto.setCphone(mobilePhone);
	    widto.setCemail(emailAddress);
	    widto.setFtargetprice(targetPrice);
	    widto.setIquantity(orderQuantity);
	    widto.setCcountrystate(countryState);
	    widto.setCcompany(companyName);
	    widto.setCinquiry(writeInquiry);
	    int res =  wholesaleInquiryMapper.insertSelective(widto);
	    if(res > 0 ){
	    	bb.setRes(1);
	    }else{
	    	bb.setRes(-80011);
	    	bb.setMsg("wholesale inquiry add failure");
	    }
		return bb;
	}
	/**
	 * 添加 Report Error
	 * 
	 * @param ReportErrorVo
	 * 
	 * 
	 * @return BaseBean
	 * @author renyy
	 */
	@Override
	public BaseBean addReportError(ReportErrorVo revo) {
		BaseBean bb = new BaseBean();
		String listingId = revo.getListingId();
		if(listingId == null || "".equals(listingId)){
		    	bb.setRes(-80012);
		    	bb.setMsg("listingId is null");
		    	return bb;
		}
	    String sku = revo.getSku();
	    if(sku == null || "".equals(sku)){
	    	bb.setRes(-80013);
	    	bb.setMsg("sku is null");
	    	return bb;
	    }
	    String errorType = revo.getErrorType();
	    if(errorType == null || "".equals(errorType)){
	    	bb.setRes(-80014);
	    	bb.setMsg("errorType is null");
	    	return bb;
	    }
	    String email = revo.getEmail();
	    if(email == null || "".equals(email)){
	    	bb.setRes(-80015);
	    	bb.setMsg("email is null");
	    	return bb;
	    }
	    String writeInquiry = revo.getWriteInquiry();
	    if(writeInquiry == null || "".equals(writeInquiry)){
	    	bb.setRes(-80016);
	    	bb.setMsg("writeInquiry is null");
	    	return bb;
	    }
	    ReportError redto = new ReportError();
	    redto.setClistingid(listingId);
	    redto.setCsku(sku);
	    redto.setCerrortype(errorType);
	    redto.setCemail(email);
	    redto.setCinquiry(writeInquiry);
	    
	    int res =  reportErrorMapper.insertSelective(redto);
	    if(res > 0 ){
	    	bb.setRes(1);
	    }else{
	    	bb.setRes(-80017);
	    	bb.setMsg("report error add failure");
	    }
		return bb;
	}

}
