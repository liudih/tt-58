package com.tomtop.mapper.shipping;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.tomtop.entry.po.ShippingDisplayNamePo;
import com.tomtop.entry.po.ShippingTemplate;
import com.tomtop.entry.po.ShippingTypePo;
import com.tomtop.entry.vo.CacheParamsVo;
import com.tomtop.entry.vo.ShippingTitleDescribeParamsVo;


public interface ShippingTemplateMapper {

	@Select("<script>"
			+ "SELECT "
			+ "m.shipping_type_id shippingTypeId, "
			+ "m.shipping_template_id templateId, " 
			+ "	m.warehouse_id warehouse, "
			+ "	m.country, "
			+ "	m.filter_id filterId, "
			+ "	m.priority_shipping_code priorityShippingCode, "
			+ "	m.is_freeshipping whetherFreeshipping, "
			+ "	m.is_especial whetherSpecial, "
			+ "	m.amount_limit amountLimit, " 
			+ "	m.weight_limit weightLimit, "
			+ "	m.extra_charge extraCharge "
			+ "	from sku_map_shippingtemplate t INNER JOIN  "
			+ "	shipping_template_config m  "
			+ "	on t.template_id=m.shipping_template_id "
			+ "	where t.status='1' "
			+ "	and m.is_deleted=0 "
			+ "	and m.is_enabled=1  "
			+ "and t.id in "
			+ "<foreach item='item' index='index' collection='list' open='(' separator=',' close=')' >#{item}</foreach>"
			+ "</script>")
	List<ShippingTemplate> getTemplateInfoByIds(@Param("list")List<String> smIds);

	
	List<ShippingTemplate> getTemplateConfigInfo(@Param("templateId") Integer templateId,@Param("storageId") Integer storageId);
	
	
	@Select("SELECT CONCAT('t_t_',id) ckey ,value cvalue FROM base_parameter "
			+ " where id=#{id}")
	CacheParamsVo getFilterNameById(@Param("id")Integer id);
	
	@Select("SELECT CONCAT('t_t_',id) ckey ,value cvalue FROM base_parameter")
	List<CacheParamsVo> getAllFilterName();
	
	
	@Select("SELECT shipping_sequence typeOrder,t.type_name typeName from shipping_type t"
			+ " where  t.id=#{id} "
			+ " and t.is_enabled=1  "
			+ " and t.is_deleted=0")
	ShippingTypePo getTemplateTypeNameById(@Param("id")Integer id);
	
	
	@Select("SELECT CONCAT('t_f_',t.id) ckey,t.type_name cvalue from shipping_type t"
			+ " where  t.is_enabled=1 "
			+ " and t.is_deleted=0 ")
	List<CacheParamsVo> getAllTemplateTypeName(@Param("list")List<Integer> ids);

	
	@Select("SELECT t.shipping_code from shipping_type t where t.id=#{id} ")
	String getAllShippingCodeTypeById(@Param("id")Integer id);
	
	@Select("SELECT d.display_name title,d.description description from shipping_type_displayname_description d "
			+" where d.language_id=#{language}"
			+" and d.shipping_type_id=#{templateTyleId} ")
	ShippingTitleDescribeParamsVo getShippingTitleDescription(@Param("templateTyleId")Integer templateId,@Param("language")Integer language);
	
	
	@Select("SELECT d.display_name title,d.description description from shipping_type_displayname_description d ")
	ShippingTitleDescribeParamsVo getAllShippingTitleDescription(@Param("templateTyleId")Integer templateId,@Param("language")Integer language);
	
	@Select("SELECT d.shipping_type_id shippingTypeId,d.language_id languageId,d.display_name title,d.description "+ 
			" from shipping_type_displayname_description d")
	List<ShippingDisplayNamePo> getAllshippingCodeName();
	
	@Select("SELECT id,s.type_name typeName,s.shipping_code  shippingCode ,s.shipping_sequence typeOrder from shipping_type s"
			+ " where  s.is_enabled=1  "
			+ " and s.is_deleted=0")
	List<ShippingTypePo> getAllTemplateType();
}
