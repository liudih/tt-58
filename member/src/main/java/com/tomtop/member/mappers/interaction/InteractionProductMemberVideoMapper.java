package com.tomtop.member.mappers.interaction;


import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.tomtop.member.models.dto.InteractionProductMemberVideo;

public interface InteractionProductMemberVideoMapper {
 
    @Select("select * from t_interaction_product_member_video where icomment = #{0} order by dcreatedate desc limit 1")
    InteractionProductMemberVideo getCommentVideoByCommentIdLimit1(Integer commentId);
    
    int insertSelective(InteractionProductMemberVideo record);
    
    @Select("select * from t_interaction_product_member_video where icomment = #{0} and cmemberemail = #{1} and iwebsiteid=#{2}  limit 1")
    InteractionProductMemberVideo getVideoByCommentIdAndEmailAndSiteId(Integer commentId, String email, Integer websiteId);
    
    int updateByPrimaryKeySelective(InteractionProductMemberVideo record);
    
	@Select("<script>"
			+ "select * from "
			+ "t_interaction_product_member_video where icomment is NULL and clistingid in(SELECT clistingid FROM t_interaction_product_member_video WHERE 1=1  "
			+ " and  icomment is NULL and cmemberemail = #{0} and iwebsiteid=#{6} "
			+ "<if test=\"status ==0 \"> and istate=0 </if>"
			+ "<if test=\"status ==1 \"> and istate=1 </if>"
			+ "<if test=\"status ==2 \"> and istate=2 </if> "
			+ "<if test=\"start !=null and end !=null \">  and dcreatedate between #{start} and #{end} </if> "
			+ " GROUP BY clistingid order by MAX(dcreatedate) desc limit #{2} offset (#{1}-1)*#{2}"
			+ ") "
			+ "</script>")
	List<InteractionProductMemberVideo> getMyReviewsVideosPage(String email,
			Integer pageIndex, Integer pageSize,
			@Param("status") Integer status, @Param("start") Date start,
			@Param("end") Date end, Integer siteId);
	
	
	@Select("SELECT count(iid) FROM t_interaction_product_member_video WHERE  icomment is NULL and cmemberemail = #{0} and iwebsiteid=#{1}")
	Integer getTotalVideosCountByMemberEmailAndSiteId(String email,
			Integer websiteId);
	
	@Delete("delete from t_interaction_product_member_video WHERE  icomment is NULL and cmemberemail = #{0} and iwebsiteid=#{1} and clistingid=#{2}")
	int deleteVideosByListingIdAndEmail(String email,
			Integer websiteId, String listingId);
	
} 