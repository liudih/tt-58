package com.tomtop.mappers.order;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.tomtop.dto.order.ShippingMethod;
import com.tomtop.dto.order.ShippingMethodDetail;

public interface ShippingMethodMapper {

	List<ShippingMethodDetail> getShippingMethods(
			@Param("storageId") Integer storageId,
			@Param("country") String country, @Param("weight") Double weight,
			@Param("lang") Integer lang, @Param("price") Double price,
			@Param("isSpecial") Boolean isSpecial);

	@Select("select * from t_shipping_method where iid = #{id}")
	ShippingMethod getShippingMethodById(Integer id);

	@Select("select m.*,d.cname, d.ctitle, d.ccontent, d.cfreecontent, d.ilanguageid , d.cimageurl, d.igroupid from t_shipping_method m "
			+ "inner join t_shipping_method_detail d on d.ccode = m.ccode where m.iid = #{0} and d.ilanguageid = #{1} limit 1")
	ShippingMethodDetail getShippingMethodDetail(Integer id, Integer lang);

	@Select("select * from t_shipping_method_detail where ccode = #{0} and ilanguageid = #{1} limit 1")
	ShippingMethodDetail getShippingMethodDetailByCode(String code, Integer lang);

	@Select("select ctitle from t_shipping_method_detail where ccode = #{0} and ilanguageid = #{1}")
	String getTitleById(String code, Integer lang);

	@Select("select * from t_shipping_method_detail where ilanguageid = #{0}")
	List<ShippingMethodDetail> getShippingMethodDetailByLanguageId(
			Integer ilanguageid);

	@Select("select count(ccode) from t_shipping_method where ccode=#{0} and istorageid=#{1} and ccountrys=#{2} and crule=#{3} and csuperrule=#{4} and istartweight=#{5} and iendweight=#{6} ")
	Integer getShippingMethodCount(String code, Integer storageid,
			String countrys, String rule, String superrule,
			Integer startweight, Integer endweight);

	@Select("<script>"
			+ "select * from t_shipping_method where iid in "
			+ "<foreach item='item' index='index' collection='list' open='(' separator=',' close=')' >#{item}</foreach>"
			+ "</script>")
	List<ShippingMethod> getShippingMethodsByIds(
			@Param("list") List<Integer> smIds);
}
