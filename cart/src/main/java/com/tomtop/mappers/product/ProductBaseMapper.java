package com.tomtop.mappers.product;

import java.util.Date;
import java.util.List;
import java.util.Set;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.tomtop.dto.product.ProductBase;
import com.tomtop.valueobjects.product.ProductLite;
import com.tomtop.valueobjects.product.Weight;
import com.tomtop.valueobjects.product.price.PriceNew;

public interface ProductBaseMapper {

	@Select("select count(iid) from t_product_base where csku=#{csku}")
	int validate(String csku);

	@Select("select tbase.clistingid,iwebsiteid,ilanguageid,tbase.csku,istatus,dnewformdate,dnewtodate,bspecial "
			+ ",cvideoaddress,iqty,fprice,fcostprice,ctitle,cdescription,cshortdescription,ckeyword "
			+ ",cmetatitle,cmetakeyword,cmetadescription "
			+ ",bmultiattribute,cparentsku,bvisible,bpulish,bmain FROM t_product_base tbase inner join t_product_translate "
			+ "translate on tbase.clistingid=translate.clistingid ")
	List<ProductBase> getProductBase();

	@Select("select csku,clistingid from t_product_base where bpulish=true and istatus=1")
	List<ProductBase> getSimpleProductBaseByStatus();

	@Select("select tbase.clistingid,tbase.iwebsiteid,translate.ilanguageid,tbase.csku,istatus,dnewformdate,dnewtodate,bspecial "
			+ ",cvideoaddress,iqty,fprice,fcostprice,translate.ctitle,'$'||'{attributes}'||translate.cdescription||'$'||'{product_images}' as cdescription ,translate.cshortdescription,translate.ckeyword "
			+ ",translate.cmetatitle,translate.cmetakeyword,translate.cmetadescription "
			+ ",translate_default.ctitle ctitle_default,'$'||'{attributes}'||translate_default.cdescription||'$'||'{product_images}' cdescription_default,translate_default.cshortdescription cshortdescription "
			+ ",translate_default.ckeyword ckeyword_default,translate_default.cmetatitle cmetatitle_default,translate_default.cmetakeyword cmetakeyword_default "
			+ ",translate_default.cmetadescription cmetadescription_default "
			+ ",bmultiattribute,cparentsku,bvisible,bpulish FROM t_product_base tbase "
			+ " left join t_product_translate translate on tbase.clistingid=translate.clistingid and ilanguageid =#{1} "
			+ " left join t_product_translate translate_default on tbase.clistingid=translate_default.clistingid and  1!=#{1} and  translate_default.ilanguageid=1 "
			+ " where tbase.clistingid =#{0} ")
	ProductBase getProductBaseByListingIdAndLanguage(String listingID, int lang);

	@Select({
			"<script>",
			"select tbase.* from t_product_base tbase ",
			"where tbase.clistingid in ",
			"<foreach item='item' index='index' collection='list' open='(' separator=',' close=')' >#{item}</foreach> ",
			"</script>" })
	List<ProductBase> getProductByListingIds(
			@Param("list") List<String> listingIds);

	@Select("select count(distinct p.clistingid) from t_product_base p "
			+ "inner join t_product_category_mapper c "
			+ "on p.ilanguageid = #{1} and c.clistingid = p.clistingid "
			+ "inner join t_product_url u " + "on u.clistingid = p.clistingid "
			+ "where c.icategory = "
			+ "(select iid from t_category_base where cpath = #{0} limit 1)")
	Integer getCategoryProductCount(String cpath, Integer language);

	
	
	
	List<ProductLite> getProductByListingIDs(@Param("list") List<String> listingIDs, int websiteID,int languageID);
	
	List<ProductLite> getProductForListingIdsAndStorageIds(@Param("list") List<String> listingids,int websiteId,int language,@Param("istorageId") int istorageId);

	@Select("select  * from t_product_base tbase where bpulish=true and csku =#{0} and iwebsiteid=#{1} and bactivity=false ")
	List<ProductBase> getProductBaseBySkuAndWebsiteId(String csku, int siteID);

	@Select("select  * from t_product_base tbase where bpulish=true and clistingid =#{0} limit 1")
	ProductBase getProductBaseByListingId(String listingId);

	@Select("select tb.* "
			+ "from t_product_base tb "
			+ "where tb.bpulish=true and tb.csku in ("
			+ "  select ts.csku from t_product_base tb2 "
			+ "  inner join t_product_multiattribute_sku ts ON ts.cparentsku = tb2.cparentsku "
			+ "  where tb2.clistingid = #{0} and tb2.iwebsiteid = #{1} " + ")")
	List<ProductBase> getProductsWithSameParentSku(String listingID, int siteID);

	@Select("select b.* "
			+ "from t_product_base b "
			+ "where b.bpulish=true and b.clistingid in ("
			+ "select distinct m.clistingid "
			+ "from t_product_entity_map m "
			+ "where m.iwebsiteid = #{1} and (m.ikey, m.ivalue) in ("
			+ "		select m2.ikey, m2.ivalue "
			+ "		from t_product_entity_map m2 "
			+ "		where  m2.iwebsiteid=#{1}"
			+ "  and m.csku in ("
			+ "		select ts.csku from t_product_base tb2 "
			+ "		inner join t_product_multiattribute_sku ts on ts.cparentsku = tb2.cparentsku "
			+ "		where tb2.clistingid=#{0} and tb2.iwebsiteid=#{1} " + ")))")
	List<ProductBase> getProductsWithSameParentSkuMatchingAttributes(
			String listingID, int siteID);

	@Select({
			"<script>",
			"SELECT clistingid,fprice,fcostprice ",
			"FROM t_product_base WHERE clistingid IN ",
			"<foreach item=\"item\" index=\"index\" collection=\"list\" open=\"(\" separator=\",\" close=\")\">#{item}</foreach>",
			"</script>" })
	List<ProductBase> getBasePrice(@Param("list") List<String> listingIdList);

	/**
	 * 获取多仓库价格
	 * 
	 * @author lijun
	 * @param listingIdList
	 * @param storageId
	 * @return
	 */
	@Select({
		"<script>",
		"select a.clistingid,a.fcostprice,case when (b.fprice is null or b.fprice=0) then a.fprice else b.fprice end",
		"from t_product_base a ",
		"left join t_product_storage_map b ",
		"on a.clistingid=b.clistingid and b.istorageid=${storageId} ",
		"where a.clistingid in ",
		"<foreach item=\"item\" index=\"index\" collection=\"list\" open=\"(\" separator=\",\" close=\")\">#{item}</foreach>",
		"</script>" })
	List<ProductBase> getBasePriceForMultiStorage(
			@Param("list") List<String> listingIdList,
			@Param("storageId") int storageId);

	@Select("select b.* "
			+ "from t_product_base b "
			+ "where b.clistingid in ("
			+ "select distinct m.clistingid "
			+ "from t_product_entity_map m "
			+ "where m.csku in ("
			+ "		select ts.csku FROM t_product_base tb2 "
			+ "		inner join t_product_multiattribute_sku ts on ts.cparentsku = tb2.cparentsku "
			+ "		where tb2.clistingid=#{0} and tb2.iwebsiteid=#{1} " //
			+ ") and m.iwebsiteid=#{1})")
	List<ProductBase> getProductsWithSameParentSkuByListingId(String listingID,
			int siteID);

	List<Weight> getWeightByListingIDs(List<String> ids);

	@Select({
			"<script>",
			"select b.clistingid from t_product_base b ",
			"<if test='searchname!=null'>",
			"inner join t_product_translate translate on b.clistingid=translate.clistingid ",
			"and translate.ilanguageid = #{0} ",
			"and lower(translate.ctitle) like CONCAT('%',lower(#{searchname}),'%') ",
			"</if>",
			"<if test='categoryid!=null and categoryid!=-1'>",
			"inner join t_product_category_mapper m on m.clistingid=b.clistingid and m.icategory=#{categoryid}",
			"</if>",
			"<if test=\"listingIds!=null and listingIds.size>0\">",
			" where b.clistingid in ",
			"<foreach item='item' index='index' collection='listingIds' open='(' separator=',' close=')'>#{item}</foreach> ",
			"</if>", "<if test=\"sort!=null and sort=='low2high'\">",
			" order by b.fprice asc", "</if>",
			"<if test=\"sort!=null and sort=='high2low'\">",
			" order by b.fprice desc", "</if>", "</script>" })
	List<String> selectListingidBySearchNameAndSort(Integer language,
			@Param("searchname") String searchname, @Param("sort") String sort,
			@Param("categoryid") Integer categoryId,
			@Param("listingIds") List<String> pcListingIds);

	@Select("select tb.* "
			+ "from t_product_base tb where tb.cparentsku = #{0} ")
	List<ProductBase> getProductsWithSameParentSkuByParentSku(String cparentsku);

	@Select({ "<script> select * from t_product_base where iwebsiteid=#{websiteid} and csku in"
			+ " <foreach item=\"item\" index=\"index\" collection=\"list\" open=\"(\" "
			+ " separator=\",\" close=\")\">#{item}</foreach></script> " })
	List<ProductBase> getProducts(@Param("websiteid") Integer websiteId,
			@Param("list") List<String> skus);

	@Update("update t_product_base set fcostprice=#{2} where csku=#{0} and iwebsiteid=#{1}")
	int updateCostPrice(String sku, int websiteId, double costprice);

	@Update("update t_product_base set fprice=#{2},ffreight=#{3},fcostprice=#{4} where csku=#{0} and iwebsiteid=#{1}")
	int updatePrice(String sku, int websiteId, double price, double freight,
			double costprice);

	@Update("update t_product_base set cparentsku=#{2} where csku=#{0} and iwebsiteid=#{1}")
	int updateSpuBySkuAndSite(String sku, int websiteId, String spu);

	@Update("update t_product_base set istatus=#{2} where csku=#{0} and iwebsiteid=#{1}")
	int updateStatusBySkuAndSite(String sku, int websiteId, Integer status);

	@Select("select count(iid) from t_product_base where dcreatedate between #{0} and #{1}")
	int selectTodayNewProductCount(Date start, Date end);

	@Select(" select iid, clistingid, iwebsiteid, csku, istatus, dnewformdate, dnewtodate, bspecial, cvideoaddress, "
			+ " iqty, fprice, fcostprice, fweight, bmultiattribute, cparentsku,ffreight, "
			+ " bvisible, bpulish, ccreateuser, dcreatedate from t_product_base where iwebsiteid=#{0} and dcreatedate between #{1} and #{2} ")
	List<ProductBase> getProductsUsingTime(Integer websiteId, Date start,
			Date end);

	@Select("select clistingid from t_product_base where csku=#{0} and iwebsiteid = #{1} and istatus=1 and bactivity='false' limit 1")
	String getListingsBySku(String sku, Integer siteId);

	@Select("select clistingid from t_product_base where cparentsku=#{0} and iwebsiteid=#{1} and bmain=true")
	List<String> getMultiProductMainListing(String parentsku, int websiteid);

	@Select("select clistingid from t_product_base where istatus=#{0}")
	List<String> getLisingsByStatus(Integer status);

	@Select("select istatus from t_product_base where clistingid=#{0}")
	Integer getProductStatusByListingId(String listingId);

	@Select("SELECT * from t_product_base where iwebsiteid=#{0} and clistingid=#{1}")
	ProductBase getProductByWebsiteAndListingid(int websiteid, String clistingid);

	@Select("select iqty from t_product_base where clistingid = #{0}")
	Integer getQtyByListingID(String listingID);

	@Select("SELECT clistingid from t_product_base where cparentsku=#{0} and istatus=#{1} and iwebsiteid=#{2} and bmain=#{3} limit 1")
	String getListingIdByParentSkuAndWebsiteIdAndStatusAndIsMain(
			String parentsku, Integer isstatus, Integer websiteId,
			boolean ismain);

	@Select("select clistingid from t_product_base where cparentsku=#{0} and istatus=1")
	List<String> getChildrenListingIdByParentSku(String parentSku);

	@Select("select csku from t_product_base where cparentsku = (select cparentsku from t_product_base where clistingid=#{clistingid})")
	List<ProductBase> getRelatedSkuByClistingid(String clistingid);

	@Select({ "<scprit>select csku ,clistingid from t_product_base where cparentsku in "
			+ "(select cparentsku from t_product_base where clistingid in "
			+ "<foreach item=\"item\" index=\"index\" collection=\"list\" open=\"(\" separator=\",\" close=\")\">#{item}</foreach>"
			+ ")</script>" })
	List<ProductBase> getRelatedSkuByListingids(
			@Param("list") List<String> listingids);

	@Select("select clistingid from t_product_base where cparentsku = #{spu}")
	List<ProductBase> getClistingidBySpu(String spu);

	@Select("select  * from t_product_base tbase where bpulish=true and csku =#{0} and iwebsiteid=#{1} ")
	ProductBase getProductBaseByParams(String csku, int siteID);

	@Select("select csku from t_product_base where clistingid = #{0} limit 1")
	String getProductSkuByListingId(String listingId);

	@Select("select clistingid,csku,fprice,fcostprice from t_product_base where csku=#{0} and iwebsiteid =#{1} and istatus =#{2} "
			+ " and bvisible =#{3} and bactivity = #{4} limit 1")
	ProductBase getProductListingId(String sku, Integer siteId, Integer status,
			Boolean bvisible, Boolean bactivity);

	@Select("<script> select clistingid from t_product_base m where "
			+ " dcreatedate between #{start} and #{end} and bvisible=true and istatus =1 and "
			+ " exists(select a.clistingid from t_product_category_mapper a "
			+ " where a.clistingid=m.clistingid and a.icategory IN "
			+ "<foreach item='item' index='index' collection='list' open='(' separator=',' close=')'>#{item}</foreach>) "
			+ " </script>")
	List<String> getLisingIdsByDate(@Param("list") List<Integer> listingids,
			@Param("start") Date start, @Param("end") Date end);

	@Select("select clistingid from t_product_base where istatus=1 and bvisible=true and iwebsiteid=#{0}")
	Set<String> getListingsByCanShow(Integer siteId);

	@Select({ "<script> select iid,clistingid,iwebsiteid,csku,istatus,fprice,fweight from t_product_base "
			+ " where iwebsiteid=#{websiteid} and bactivity=false and csku in"
			+ " <foreach item=\"item\" index=\"index\" collection=\"list\" open=\"(\" "
			+ " separator=\",\" close=\")\">#{item}</foreach></script> " })
	List<ProductBase> getProductBaseBySkus(@Param("list") List<String> skus,
			@Param("websiteid") Integer siteid);

	@Select("select csku from t_product_base where cparentsku = #{0} and istatus=1 and bactivity = 'false'")
	List<String> getSkuBySpu(String spu);

	@Select("select csku from t_product_base where clistingid = #{0} limit 1")
	String getSkuByListingid(String listingid);

	@Select("select clistingid from t_product_base where csku = #{0} and bvisible = false limit 1")
	String getListingidBySku(String sku);

	@Select("select * from t_product_base where  csku=#{0} and iwebsiteid = #{1} and istatus=#{2} and bactivity = 'false' limit 1 ")
	ProductBase getProductBySku(String sku, Integer siteId, Integer state);

	@Select("select * from t_product_base where csku = #{0} and bactivity = 'false' and iwebsiteid = #{1}")
	ProductBase getProductByCskuAndIsActivity(String csku, Integer siteId);

	@Select("select * from t_product_base where csku = #{0} limit 1")
	ProductBase getBasePriceBySku(String mainSku);

	@Select({ "<script> select clistingid from t_product_base where bactivity = false and iwebsiteid = #{1} and cparentsku in"
			+ " <foreach item=\"item\" index=\"index\" collection=\"list\" open=\"(\" "
			+ " separator=\",\" close=\")\">#{item}</foreach></script> " })
	List<String> getlistingIdsBySpus(@Param("list") List<String> spus,
			Integer websiteId);

	@Select("select clistingid "
			+ "from t_product_base where csku = #{0} and iwebsiteid= #{1} limit 1")
	String getListingIdBySkuAndWebsiteId(String sku, Integer websiteId);

	@Select({
			"<script>",
			"select * from t_product_base ",
			" where iwebsiteid = #{1} and csku in ",
			" <foreach item='item' index='index' collection='list' open='(' separator=',' close=')' >#{item}</foreach> ",
			"</script>" })
	List<ProductBase> checkSkuListingExits(@Param("list") List<String> skus,
			Integer websiteId);

	@Select("select * from t_product_base where clistingid = #{0} and iwebsiteid = #{1}")
	ProductBase getStatusByListingIdAndsiteId(String listingid, Integer siteId);

	@Select("select csku from t_product_base where lower(csku) like CONCAT(lower(#{sku}),'%') ORDER BY csku  limit 10")
	List<String> getSkuLike(@Param("sku") String sku);

	@Select("SELECT t.csku from t_product_base t  where t.istatus=1 and t.iwebsiteid=1  ORDER BY t.iid  limit #{1} offset (#{0}-1)*#{1}")
	List<String> getSkuLimit(int page, int pagesize);

	@Select("SELECT count(1) from t_product_base t  where t.istatus=#{0} and t.iwebsiteid=#{1} ")
	int getProductBaseCount(int status, int site);

	@Select("select  * from t_product_base tbase where bpulish=true and csku =#{0} and iwebsiteid=#{1} and bactivity='false' limit 1")
	ProductBase getProductBaseBySkuAndSite(String csku, int siteID);
	
	/**
	 * 根据产品标识和仓库id查询价格集合
	 * 
	 * @param listingList 产品标识集合
	 * @param storageId 仓库id
	 * @return 价格集合
	 * @author shuliangxing
	 * @date 2016年6月8日 下午5:39:15
	 */
	List<PriceNew> queryPrice(@Param("listingList") List<String> listingList,
			@Param("storageId") Integer storageId);
}
