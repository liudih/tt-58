package com.tomtop.mappers.payment;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;

import com.tomtop.entity.payment.paypal.PaymentLogEvent;

public interface PaypaiReturnLogMapper {
	@Insert("insert into t_payment_paypal_return_log(iwebsiteid,corderid,ccontent,ctransactionid) values(#{0},#{1},#{2},#{3})")
	void Insert(Integer iwebsiteid, String corderid, String content,
			String transactionId);

	@Select("SELECT * FROM t_payment_paypal_return_log WHERE corderid = #{0} limit 1")
	PaymentLogEvent getPaypaiReturnLogByOrderId(String corderid);
	
	@Select("SELECT * FROM t_payment_paypal_return_log WHERE corderid = #{0}")
	List<PaymentLogEvent> getPaypaiReturnLogByOrderIds(String corderid);
}
