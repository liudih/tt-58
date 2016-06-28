package com.tomtop.member.mappers.order;

import java.util.Map;

import org.apache.ibatis.annotations.Select;

import com.tomtop.member.models.dto.OrderDetailComment;

public interface OrderDetailMapper {

	@Select("select commentid cid,clistingid listingId,csku sku from t_order_detail where iorderid=#{0} and clistingid=#{1}")
	OrderDetailComment getOrderDetailCommentId(Integer oid,String listingId);
	
	Integer updateOrderDetailCommentId(Map<String,Object> map);
	
	@Select("select istatus from t_order where iid=#{0}")
	Integer getOrderIdStatus(Integer oid);
	
}
