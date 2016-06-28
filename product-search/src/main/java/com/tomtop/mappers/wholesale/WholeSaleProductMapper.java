package com.tomtop.mappers.wholesale;


import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.tomtop.entity.WholeSaleProduct;

public interface WholeSaleProductMapper {

	@Select("select iid,iwebsiteid,cemail,csku,iqty,dcreatedate from t_wholesale_product "
			+ "where cemail = #{0} and iwebsiteid = #{1} and csku=#{2} limit 1")
	WholeSaleProduct getWholeSaleProductsByEmailAndSkuAndWebsite(String email,
			Integer websiteId, String sku);
	
	@Insert("insert into t_wholesale_product(iwebsiteid, csku, cemail, iqty) values(#{iwebsiteid}, #{csku}, #{cemail}, #{iqty})")
	int addWholeSaleProduct(WholeSaleProduct record);
	
	@Update("update t_wholesale_product set iqty=#{iqty} where iid=#{iid}")
	int updateQtyByIid(WholeSaleProduct record);
}
