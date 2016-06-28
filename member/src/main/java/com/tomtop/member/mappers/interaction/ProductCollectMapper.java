package com.tomtop.member.mappers.interaction;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.tomtop.member.models.dto.ProductCollect;

public interface ProductCollectMapper {
	int deleteByIid(Integer iid);

	int insert(ProductCollect record);

	int insertSelective(ProductCollect record);

	@Delete("delete from t_product_collect where clistingid=#{0} and cemail=#{1} and iwebsiteid=#{2}")
	int delCollect(String listingId, String email, Integer websiteId);

	@Delete({
			"<script>",
			"delete from t_product_collect ",
			"where cemail=#{1} and iwebsiteid=#{2} and clistingid in ",
			"<foreach item='item' index='index' collection='list' open='(' separator=',' close=')'>#{item}</foreach> ",
			"</script>" })
	int delCollectByListingids(@Param("list") List<String> listingIds,
			String email, Integer websiteId);

	@Select("select * from t_product_collect c where c.iid=#{1}")
	ProductCollect getProductCollectByIid(Integer iid);

	@Select("select * from t_product_collect  where clistingid=#{0} and cemail=#{1} and iwebsiteid=#{2} limit 1")
	ProductCollect getProductCollectByListingIdAndEmail(String listingId,
			String email, Integer websiteId);
	
	
    @Select("select count(*) from t_product_collect where cemail=#{0} and iwebsiteid=#{1}")
    int getCollectsCountByEmail(String email, Integer websiteId);
}