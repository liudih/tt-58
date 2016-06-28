package com.tomtop.mappers.interaction;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.tomtop.entity.CollectCount;
import com.tomtop.entity.ProductCollect;

public interface ProductCollectMapper {

	int insertSelective(ProductCollect record);
	
	@Select({
		"<script>",
		"select c.clistingid listingId,count(iid) collectCount ",
		"from t_product_collect c where c.iwebsiteid=#{website} and c.clistingid in ",
		"<foreach item='item' index='index' collection='list' open='(' separator=',' close=')'>#{item}</foreach> ",
		"group by clistingid", "</script>" })
	public List<CollectCount> getCollectCountByListingIds(
			@Param("list") List<String> listingIds,@Param("website") Integer website);
	
	@Select({"select pc.clistingid listingId,count(pc.clistingid) collectCount ",
	"from t_product_collect pc where pc.clistingid=#{listingId} and pc.iwebsiteid=#{website} group by pc.clistingid" })
	public CollectCount getCollectCountByListingId(
		@Param("listingId") String listingIds,@Param("website") Integer website);
	
	@Select("select iid,cemail,clistingid,dcreatedate from t_product_collect where clistingid=#{0} and cemail=#{1} and iwebsiteid=#{2} LIMIT 1")
	ProductCollect getCollectByMember(String listingID, String email,Integer website);

	@Select("select c.clistingid from t_product_collect c where c.cemail=#{0} and c.iwebsiteid=#{1} order by c.dcreatedate desc")
	List<String> getCollectListingIDByEmail(String email,Integer website);
	
	@Select({"select distinct clistingid,dcreatedate from t_product_collect ",
	"where cemail=#{0} and iwebsiteid=#{1} order by dcreatedate desc" })
	public List<ProductCollect> getCollectByEmail(String email,Integer website);
}