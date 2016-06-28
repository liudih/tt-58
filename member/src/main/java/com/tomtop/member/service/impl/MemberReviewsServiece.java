package com.tomtop.member.service.impl;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;
import com.tomtop.member.mappers.interaction.InteractionCommentMapper;
import com.tomtop.member.mappers.interaction.InteractionProductMemberPhotosMapper;
import com.tomtop.member.mappers.interaction.InteractionProductMemberVideoMapper;
import com.tomtop.member.mappers.product.ProductImageMapper;
import com.tomtop.member.models.dto.InteractionComment;
import com.tomtop.member.models.dto.InteractionProductMemberVideo;
import com.tomtop.member.models.dto.ReviewsInMemberCenter;
import com.tomtop.member.models.dto.product.ProductUrl;
import com.tomtop.member.service.IMemberReviewsService;
import com.tomtop.member.service.IProductUrlService;

@Service
public class MemberReviewsServiece implements IMemberReviewsService {

	@Autowired
	InteractionCommentMapper memberReviewsMapper;

	@Autowired
	IProductUrlService productUrlService;

	@Autowired
	ProductImageMapper productImageMapper;

	@Autowired
	InteractionProductMemberPhotosMapper productMemberPhotosMapper;

	@Autowired
	InteractionProductMemberVideoMapper interactionproductmembervideomapper;
	
	@Value("${event_register_url}")
	private String event_register_url;
	
	@Value("${event_register_key}")
	private String event_register_key;

	public List<ReviewsInMemberCenter> getReviewsInMemberCenterList(
			List<InteractionComment> reviews) {
		if (null == reviews || reviews.size() < 1) {
			return Lists.newArrayList();
		}
		List<ReviewsInMemberCenter> reviewsInMemberCenterList = Lists
				.newArrayList();
		for (InteractionComment interactionComment : reviews) {
			reviewsInMemberCenterList
					.add(convertMemberCenter(interactionComment));
		}
		return reviewsInMemberCenterList;
	}

	public List<ReviewsInMemberCenter> getMyReviewsPageByEmail(String email,
			Integer pageIndex, Integer pageSize, Integer status,
			Integer dateType, Integer siteId) {
		List<InteractionComment> reviews = Lists.newArrayList();
		Date start = null;
		Date end = null;
		if (dateType != 0) {
			end = new Date();
			GregorianCalendar calendar = new GregorianCalendar();
			calendar.add(Calendar.MONTH, (dateType * (-1)));
			start = calendar.getTime();
		}
		reviews = memberReviewsMapper.getMyReviewsPageByEmail(email, pageIndex,
				pageSize, status, start, end, siteId);

		List<ReviewsInMemberCenter> reviewsInMemberCenters = getReviewsInMemberCenterList(reviews);
		return reviewsInMemberCenters;
	}

	@Override
	public Integer getTotalReviewsCountByMemberEmailAndSiteId(String email,
			Integer status, Integer dateType, Integer siteId) {

		Date start = null;
		Date end = null;
		if (dateType != 0) {
			end = new Date();
			GregorianCalendar calendar = new GregorianCalendar();
			calendar.add(Calendar.MONTH, (dateType * (-1)));
			start = calendar.getTime();
		}
		return memberReviewsMapper.getTotalReviewsCountByMemberEmailAndSiteId(
				email, status, start, end, siteId);
	}

	public Integer getPendingReviewsCountByMemberEmailAndSiteId(String email,
			Integer siteId) {
		return memberReviewsMapper
				.getPendingReviewsCountByMemberEmailAndSiteId(email, siteId);
	}

	public Integer getApprovedReviewsCountByMemberEmailAndSiteId(String email,
			Integer siteId) {
		return memberReviewsMapper
				.getApprovedReviewsCountByMemberEmailAndSiteId(email, siteId);
	}

	public Integer getFailedReviewsCountByMemberEmailAndSiteId(String email,
			Integer siteId) {
		return memberReviewsMapper.getFailedReviewsCountByMemberEmailAndSiteId(
				email, siteId);
	}

	@Override
	public InteractionComment getReviewById(Integer id,String email) {
		InteractionComment interactionComment = memberReviewsMapper
				.selectByPrimaryKey(id,email);
		return interactionComment;

	}
	
	@Override
	public ReviewsInMemberCenter getReviewsInMemberCenterById(Integer id,String email) {
		InteractionComment interactionComment = memberReviewsMapper
				.selectByPrimaryKey(id,email);
		return convertMemberCenter(interactionComment);
		
	}

	private ReviewsInMemberCenter convertMemberCenter(
			InteractionComment interactionComment) {
		String listingId = interactionComment.getClistingid();
		Integer commentId = interactionComment.getIid();
		String ccomment = interactionComment.getCcomment();
		Integer iprice = interactionComment.getIprice();
		Integer iquality = interactionComment.getIquality();
		Integer ishipping = interactionComment.getIshipping();
		Integer iusefulness = interactionComment.getIusefulness();
		Double foverallrating = interactionComment.getFoverallrating();
		Date dcreatedate = interactionComment.getDcreatedate();
		Integer istate = interactionComment.getIstate();
		String sku = interactionComment.getCsku();
		String productUrl = "";
		ProductUrl url = this.productUrlService.getProductUrlsByListingId(
				listingId, 1);
		if (null != url) {
			productUrl = url.getCurl();
		}
		String productSmallImgUrl = productImageMapper
				.getProductSmallImageForMemberViewsByListingId(listingId);
		List<String> commentPhotosUrl = productMemberPhotosMapper
				.getCommentImgageByCommentIdLimit5(commentId);
		InteractionProductMemberVideo video = interactionproductmembervideomapper
				.getCommentVideoByCommentIdLimit1(commentId);
		String videoUrl = "";
		String videoTitle = "";
		if (null != video) {
			videoUrl = video.getCvideourl();
			videoTitle = video.getClabel();
		}

		return new ReviewsInMemberCenter(commentId, ccomment, iprice, iquality,
				ishipping, iusefulness, foverallrating, dcreatedate, istate,
				productSmallImgUrl, commentPhotosUrl, videoUrl, productUrl,
				sku, videoTitle, listingId);
	}

	@Override
	public Boolean updateInteractionComment(InteractionComment review) {
		int result = memberReviewsMapper.updateByPrimaryKeySelective(review);
		return result > 0 ? true : false;
	}

	@Override
	public Boolean deleteInteractionComment(Integer iid,String email) {
		int result = memberReviewsMapper.deleteInteractionComment(iid,email);
		return result > 0 ? true : false;
	}

	@Override
	public int insertSelective(InteractionComment record) throws Exception {
		memberReviewsMapper.insertSelective(record);
		
		/*JSONObject jsonObj = new JSONObject();
		jsonObj.put("code", "EVENT_CODE_REVIEW");
		JSONObject param = new JSONObject();
		param.put("email", record.getCmemberemail());
		param.put("website", record.getIwebsiteid());
		jsonObj.put("param", param);
		//评论成功送积分或者优惠卷
		HttpSendRequest.sendPostByAsync(this.event_register_url, jsonObj.toJSONString(), this.event_register_key);
		*/
		return record.getIid();
	}

}
