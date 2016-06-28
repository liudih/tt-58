package com.tomtop.mappers.product;

import java.util.List;

import org.apache.ibatis.annotations.Select;

import com.tomtop.entity.Price;
import com.tomtop.entity.Product;


public interface ProductDtlMapper {

	/**
	 * 查询商品详情的基本
	 * @param listingID
	 * @param langId
	 * 
	 * @author renyy
	 */
	@Select("select tbase.clistingid,iwebsiteid,ilanguageid,tbase.csku,istatus,dnewformdate,dnewtodate,bspecial "
			+ ",cvideoaddress,iqty,fprice,fcostprice,ctitle,cdescription,cshortdescription,ckeyword "
			+ ",cmetatitle,cmetakeyword,cmetadescription "
			+ ",bmultiattribute,cparentsku,bvisible,bpulish,bmain FROM t_product_base tbase inner join t_product_translate "
			+ "translate on tbase.clistingid=translate.clistingid where tbase.clistingid=#{0} and ilanguageid=#{1} and tbase.iwebsiteid=#{2} ")
	Product getProductBaseByListingId(String listingID, Integer langId,Integer siteId);
	
	/**
	 * 查询商品详情其他属性产品
	 * @param parentSku
	 * @param listingID
	 * @param langId
	 * 
	 * @author renyy
	 */
	@Select({"select tbase.clistingid,iwebsiteid,ilanguageid,tbase.csku,istatus,dnewformdate,dnewtodate,bspecial,",
			"cvideoaddress,iqty,fprice,fcostprice,ctitle,cdescription,cshortdescription,ckeyword,",
			"cmetatitle,cmetakeyword,cmetadescription,bmultiattribute,cparentsku,bvisible,bpulish,bmain",
			"FROM t_product_base tbase inner join t_product_translate ptra on tbase.clistingid=ptra.clistingid ",
			"where tbase.clistingid!=#{0} and ilanguageid=#{1} and cparentsku=#{2} and tbase.iwebsiteid=#{3}"})
	List<Product> getProductBaseByParentSku(String listingID,Integer langId,String parentSku,Integer siteId);

	/**
	 * 查询商品的库存
	 * @param listingIds
	 * 
	 * @author renyy
	 */
	@Select("select iqty from t_product_base where clistingid=#{0} and iwebsiteid=#{1}")
		Integer getProductQtyByListingId(String listingID,Integer siteId);
	
	@Select("select fprice as price ,fcostprice as costPrice  from t_product_base "
			+ "where clistingid=#{0} and iwebsiteid=#{1} limit 1")
	Price getProductPriceByListingId(String listingId,Integer siteId);
}
