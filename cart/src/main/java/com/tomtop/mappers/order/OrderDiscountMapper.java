package com.tomtop.mappers.order;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.tomtop.dto.order.OrderDiscount;
import com.tomtop.valueobjects.Discount;

/**
 * 
 * @author lijun
 *
 */
public interface OrderDiscountMapper {

	@Insert("<script>insert into t_order_discount(order_number,website,code,discount,currency,type) values "
			+ "<foreach collection=\"list\" item=\"item\" index=\"index\" separator=\",\"> "
			+ "( '${orderNum}',${website},'${item.code}',${item.discount},'${item.currency}','${item.type}')</foreach></script>")
	public int insert(@Param("orderNum") String orderNum,
			@Param("website") int website,
			@Param("list") List<Discount> discounts);
	
	@Select("select * from t_order_discount where order_number=#{0} and website=#{1}")
	public List<OrderDiscount> getOrderDiscountList(String orderNumber, Integer siteid);
}
