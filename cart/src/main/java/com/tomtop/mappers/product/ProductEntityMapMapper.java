package com.tomtop.mappers.product;

import java.util.List;

import org.apache.ibatis.annotations.Select;

import com.tomtop.dto.product.ProductEntityMap;

public interface ProductEntityMapMapper {

	@Select("SELECT tentity.iid,tentity.clistingid,tentity.csku,tentity.bshow, "
		+ "max(case when tkey.ckeyname is null then key_de.ckeyname else tkey.ckeyname end) ckeyname, "
		+ "max(case when tvalue.cvaluename is null then value_de.cvaluename else tvalue.cvaluename end) cvaluename "
		+ "FROM  t_product_entity_mapv2 tentity "
		+ "left join t_attribute_key_namev2 tkey on tentity.ikey=tkey.ikeyid and tkey.ilanguageid=#{1} "
		+ "left join t_attribute_value_namev2 tvalue on tentity.ivalue= tvalue.ivalueid and tvalue.ilanguageid=#{1} "
		+ "left join t_attribute_key_namev2 key_de on tentity.ikey=key_de.ikeyid and key_de.ilanguageid=1 "
		+ "left join t_attribute_value_namev2 value_de on tentity.ivalue= value_de.ivalueid and value_de.ilanguageid=1 "
		+ "WHERE tentity.multiattribute=true and tentity.clistingid = #{0} "
		+ "group by tentity.iid,tentity.clistingid,tentity.csku,tentity.bshow "
		)
	List<ProductEntityMap> getProductEntityMapByListingId(String listingID,
			Integer langid);
}