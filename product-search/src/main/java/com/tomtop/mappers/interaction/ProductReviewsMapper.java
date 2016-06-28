package com.tomtop.mappers.interaction;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.tomtop.entity.InteractionComment;
import com.tomtop.entity.ReviewPhotoVideo;
import com.tomtop.entity.rc.FiveReview;

public interface ProductReviewsMapper {

	/**
	 * 获取商品评论详情
	 * 
	 * @param listingId
	 * @return
	 */
	@Select({ 
		"select iid,clistingid,csku,cmemberemail,ccomment,iprice,iquality,ishipping,",
		 "iusefulness,foverallrating,dcreatedate,dauditdate,istate,iorderid,ccountry,",
		 "cplatform,iwebsiteid,ctitle from t_interaction_comment ",
		 "where clistingid=#{0} and istate=#{1} and iwebsiteid=#{2} and dcreatedate is not null ",
		 "ORDER BY dcreatedate desc LIMIT #{3}" })
	List<InteractionComment> getInteractionCommentDtoByListingId(String listingId,
			Integer state,Integer siteId,Integer limit);
	
	/**
	 * 获取商品评论详情(分页)
	 * 
	 * @param listingId
	 * @param state
	 * @param siteId
	 * @param page
	 * @param size
	 * 
	 * @return
	 */
	@Select({ 
		"select iid,clistingid,csku,cmemberemail,ccomment,iprice,iquality,ishipping,",
		 "iusefulness,foverallrating,dcreatedate,dauditdate,istate,iorderid,ccountry,",
		 "cplatform,iwebsiteid,ctitle from t_interaction_comment ",
		 "where clistingid=#{0} and istate=#{1} and iwebsiteid=#{2} ",
		 "ORDER BY dcreatedate desc limit #{4} offset (#{3}-1)*#{4}" })
	List<InteractionComment> getInteractionCommentDtoPage(String listingId,
			Integer state,Integer siteId,Integer page,Integer size);
	
	/**
	 * 获取商品评论数量
	 * 
	 * @param listingId
	 * @param state
	 * @param siteId
	 * @param page
	 * @param size
	 * 
	 * @return
	 */
	@Select({ 
		"select count(*) from t_interaction_comment ",
		 "where clistingid=#{0} and istate=#{1} and iwebsiteid=#{2} and dcreatedate is not null"})
	Integer getInteractionCommentDtoCount(String listingId,Integer state,Integer siteId);
	
	/**
	 * 获取商品评论图片视频
	 * 
	 * @param listingId
	 * @return
	 */
	@Select({ "<script>",
		"select icommentid commentid,'photo' code,clistingid listingid,cimageurl url from t_interaction_product_member_photos ",
		"where iauditorstatus=#{status} and iwebsiteid=#{siteid} and icommentid in ",
		"<foreach item='item' index='index' collection='list' open='(' separator=',' close=')'>#{item}</foreach> ", 
		" UNION ",
		"select icomment commentid,'video' code,clistingid listingid,cvideourl url from t_interaction_product_member_video ",
		"where iauditorstatus=#{status} and iwebsiteid=#{siteid} and icomment in ",
		"<foreach item='item' index='index' collection='list' open='(' separator=',' close=')'>#{item}</foreach>  ",
	    "ORDER BY commentid desc </script>"})
	List<ReviewPhotoVideo> getReviewPhotoVideoDtoByCommentId(@Param("status") Integer status,@Param("siteid") Integer siteid,@Param("list") List<Integer> commentId);
	
	/**
	 * 获取5条 5星的 内容大于50并且 审核通过 的评论
	 * 
	 * @param website
	 * @return
	 */
	@Select({"select DISTINCT(clistingid) listingId,ccomment as comment,foverallrating stars,dcreatedate createDate,",
			"ccountry countryName,ctitle title from t_interaction_comment ",
			"where foverallrating=5 and length(ccomment)>50 and istate=1 and iwebsiteid=#{0}",
			"order by dcreatedate desc limit 5"})
	List<FiveReview> getFoverallratingFiveReviewByWebsite(Integer website);

}
