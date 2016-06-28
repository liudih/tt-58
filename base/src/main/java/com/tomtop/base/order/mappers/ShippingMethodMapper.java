package com.tomtop.base.order.mappers;

import java.util.List;

import org.apache.ibatis.annotations.Select;

import com.tomtop.base.models.dto.ShippingMethodDto;

public interface ShippingMethodMapper {

	@Select({
			"select Shipping.istorageid as storageid,Detail.ilanguageid as languageid,Detail.ctitle as title,",
			"Detail.ccontent as content,Detail.ccode as code,Detail.cimageurl as imageurl,Detail.igroupid as groupid",
			"from t_shipping_method_detail Detail",
			"INNER JOIN (select istorageid,ccode from t_shipping_method where benabled=TRUE ",
			"GROUP BY istorageid,ccode ORDER BY istorageid,ccode) Shipping on Shipping.ccode=Detail.ccode where Shipping.istorageid=#{0} and  Detail.ilanguageid=#{1}" })
	public List<ShippingMethodDto> getListByStorageId(Integer storageId,
			Integer lang);

	@Select({
			"select Shipping.istorageid as storageid,Detail.ilanguageid as languageid,Detail.ctitle as title,",
			"Detail.ccontent as content,Detail.ccode as code,Detail.cimageurl as imageurl,Detail.igroupid as groupid",
			"from t_shipping_method_detail Detail",
			"INNER JOIN (select istorageid,ccode from t_shipping_method where benabled=TRUE ",
			"GROUP BY istorageid,ccode ORDER BY istorageid,ccode) Shipping on Shipping.ccode=Detail.ccode where Detail.ilanguageid=#{0}" })
	public List<ShippingMethodDto> getListByLang(Integer lang);
}
