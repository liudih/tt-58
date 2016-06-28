package com.tomtop.mappers.product;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;

import com.tomtop.dto.product.ProductCategoryMapper;

public interface ProductCategoryMapperMapper {

	@Select("select m.* from t_product_category_mapper m "
			+ " left join t_category_base a on m.icategory=a.iid "
			+ " where clistingid=#{0} order by a.ilevel asc ")
	@ResultMap("BaseResultMap")
	List<ProductCategoryMapper> selectByListingId(String listingid);

	@Select({
			"<script>",
			"select * from t_product_category_mapper ",
			"where clistingid IN ",
			"<foreach item='item' index='index' collection='list' open='(' separator=',' close=')'>#{item}</foreach> ",
			"</script>" })
	@ResultMap("BaseResultMap")
	List<ProductCategoryMapper> selectByListingIds(
			@Param("list") List<String> listingIds);

	@Select("select m.icategory from t_product_category_mapper m inner join t_category_base b "
			+ "on b.iid = m.icategory where clistingid=#{ilistingid} order by b.ilevel desc limit 1")
	Integer getLastCategoryIdtByListingId(String ilistingid);

	@Select("select * from t_product_category_mapper where clistingid = #{0} and icategory = #{1} limit 1")
	ProductCategoryMapper getProductCategoryMapperByListingIdAndCategoryId(
			String clistingId, Integer categoryId);

	@Select("select icategory from t_product_category_mapper where clistingid=#{0} order by icategory")
	List<Integer> getCategoryIdByListingId(String listingid);

	@Select("select tm.icategory from t_product_category_mapper tm "
			+ "inner join t_category_website tw on tm.icategory=tw.icategoryid  "
			+ "where tw.ilevel=1 and tw.iparentid is null and tm.clistingid=#{0} and tm.ilanguageid=#{1} limit 1 ")
	Integer getProductRootCategoryIdByListingId(String listingId,
			Integer languageId);

	@Select("select clistingid from t_product_category_mapper where icategory = #{0}")
	List<String> getAllListingIdsByRootId(Integer rootCategoryId);

	@Select({
			"<script> select clistingid from t_product_category_mapper ",
			"where icategory IN ",
			"<foreach item='item' index='index' collection='list' open='(' separator=',' close=')'>#{item}</foreach> ",
			"</script>" })
	List<String> getAllListingIdsByRootIds(List<Integer> rootCategoryIds);

	@Select("select clistingid from t_product_category_mapper where icategory = #{0} order by iid limit #{1} offset (#{1} * (#{2} - 1))")
	List<String> getListingIdsByRootId(Integer rootCategoryId,
			Integer pageSize, Integer pageNum);

	@Select("<script> select clistingid from t_product_category_mapper where icategory in "
			+ "<foreach item='item' index='index' collection='list' open='(' separator=',' close=')'>#{item}</foreach> "
			+ "limit #{1} offset (#{1} * (#{2} - 1)) </script>")
	List<String> getListingIdsByCategoryId(
			@Param("list") List<Integer> rootCategoryId, Integer pageSize,
			Integer pageNum);

}