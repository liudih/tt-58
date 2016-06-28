package com.tomtop.member.mappers.interaction;


import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.tomtop.member.models.dto.InteractionProductMemberPhotos;
import com.tomtop.member.models.dto.Purl;


public interface InteractionProductMemberPhotosMapper {

	@Select("select cimageurl from t_interaction_product_member_photos where icommentid = #{0} order by dcreatedate desc limit 5")
	List<String> getCommentImgageByCommentIdLimit5(Integer commentId);
	
	@Select("select cimageurl from t_interaction_product_member_photos where icommentid = #{0} order by dcreatedate desc")
	List<String> getCommentImgageByCommentId(Integer commentId);
	
	int insertSelective(InteractionProductMemberPhotos record);
	
	
	@Select("<script>"
			+ "select clistingid,csku,cmemberemail,cimageurl,clabel,dcreatedate,iauditorstatus,iwebsiteid from "
			+ "t_interaction_product_member_photos where icommentid is NULL and  clistingid in(SELECT clistingid FROM t_interaction_product_member_photos WHERE 1=1  "
			+ " and  icommentid is NULL and cmemberemail = #{0} and iwebsiteid=#{6} "
			+ "<if test=\"status ==0 \"> and istate=0 </if>"
			+ "<if test=\"status ==1 \"> and istate=1 </if>"
			+ "<if test=\"status ==2 \"> and istate=2 </if> "
			+ "<if test=\"start !=null and end !=null \">  and dcreatedate between #{start} and #{end} </if> "
			+ " GROUP BY clistingid order by MAX(dcreatedate) desc limit #{2} offset (#{1}-1)*#{2}"
			+ ") "
			+ "</script>")
	List<InteractionProductMemberPhotos> getMyReviewsPhotosPage(String email,
			Integer pageIndex, Integer pageSize,
			@Param("status") Integer status, @Param("start") Date start,
			@Param("end") Date end, Integer siteId);
	
	
	@Select("SELECT count(iid) FROM t_interaction_product_member_photos WHERE  icommentid is NULL and cmemberemail = #{0} and iwebsiteid=#{1}")
	Integer getTotalPhotosCountByMemberEmailAndSiteId(String email,
			Integer websiteId);
	
	@Delete("delete from t_interaction_product_member_photos WHERE  icommentid is NULL and cmemberemail = #{0} and iwebsiteid=#{1} and clistingid=#{2}")
	int deletePhotoByListingIdAndEmail(String email,
			Integer websiteId, String listingId);

	
	@Delete("delete from t_interaction_product_member_photos WHERE  cmemberemail = #{0} and iwebsiteid=#{1} and clistingid=#{2} and cimageurl=#{3}")
	int deletePhotoByListingIdAndEmailAndUrl(String email, Integer websiteId,
			String listingId, String imageUrl);
	
	@Select("select 'photo' as type,cimageurl url,'' title from t_interaction_product_member_photos "
			+ "where icommentid=#{0} UNION "
			+ "select 'video' as type,cvideourl url,clabel title from t_interaction_product_member_video "
			+ "where icomment=#{0}")
	List<Purl> getUrlByListing(Integer commentId);
}