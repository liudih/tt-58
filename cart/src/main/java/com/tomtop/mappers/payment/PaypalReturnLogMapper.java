package com.tomtop.mappers.payment;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;

import com.tomtop.dto.paypal.PaypalLog;

public interface PaypalReturnLogMapper {
	@Insert("insert into t_payment_paypal_return_log(iwebsiteid,corderid,ccontent,ctransactionid) values (#{log.iwebsiteid},#{log.corderid},#{log.ccontent},#{log.ctransactionid})")
	int insert(@Param("log") PaypalLog log);

}
