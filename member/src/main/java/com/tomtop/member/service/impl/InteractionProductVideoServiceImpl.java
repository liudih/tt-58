package com.tomtop.member.service.impl;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;
import com.tomtop.member.mappers.interaction.InteractionProductMemberVideoMapper;
import com.tomtop.member.mappers.product.ProductImageMapper;
import com.tomtop.member.models.bo.InteractionVideoBo;
import com.tomtop.member.models.dto.InteractionProductMemberVideo;
import com.tomtop.member.models.dto.product.ProductUrl;
import com.tomtop.member.service.IInteractionProductVideoService;
import com.tomtop.member.service.IProductUrlService;
@Service
public class InteractionProductVideoServiceImpl implements
		IInteractionProductVideoService {

	@Autowired
	InteractionProductMemberVideoMapper videoMapper;

	@Autowired
	IProductUrlService productUrlService;
	
	@Autowired
	ProductImageMapper  productImageMapper;
	
	@Override
	public InteractionProductMemberVideo getCommentVideoByCommentIdLimit1(Integer commentId) {
		return this.videoMapper.getCommentVideoByCommentIdLimit1(commentId);
	}

	@Override
	public int insertSelective(InteractionProductMemberVideo record) {
		return this.videoMapper.insertSelective(record);
	}

	@Override
	public Integer getTotalVideoCountByMemberEmailAndSiteId(String email,
			Integer websiteId) {
		return videoMapper.getTotalVideosCountByMemberEmailAndSiteId(email, websiteId);
	}

	@Override
	public List<InteractionVideoBo> getMyReviewsVideos(String email,
			Integer total, Integer pageIndex, Integer pageSize, Integer status,
			Integer dateType, Integer websiteId) {
		List<InteractionProductMemberVideo> reviews = Lists.newArrayList();
		Date start = null;
		Date end = null;
		if (dateType != 0) {
			end = new Date();
			GregorianCalendar calendar = new GregorianCalendar();
			calendar.add(Calendar.MONTH, (dateType * (-1)));
			start = calendar.getTime();
		}
		reviews = videoMapper.getMyReviewsVideosPage(email, pageIndex,
				pageSize, status, start, end, websiteId);

		List<InteractionVideoBo> reviewsInMemberCenters = getReviewsInMemberCenterList(reviews);
		
		return reviewsInMemberCenters;
	}
	
	public List<InteractionVideoBo> getReviewsInMemberCenterList(
			List<InteractionProductMemberVideo> reviews) {
		if (null == reviews || reviews.size() < 1) {
			return Lists.newArrayList();
		}
		List<InteractionVideoBo> reviewsInMemberCenterList = Lists
				.newArrayList();
		
		Map<String, List<InteractionProductMemberVideo>> catMap = new HashMap<String, List<InteractionProductMemberVideo>>();

		if (null != reviews && reviews.size() > 0) {
			Iterator<InteractionProductMemberVideo> e = reviews.iterator();
			while (e.hasNext()) {
				InteractionProductMemberVideo cm = e.next();
				List<InteractionProductMemberVideo> tempList = null;
				if (catMap.containsKey(cm.getClistingid())) {
					tempList = catMap.get(cm.getClistingid());
					tempList.add(cm);

				} else {
					tempList = new ArrayList<InteractionProductMemberVideo>();
					tempList.add(cm);
				}

				catMap.put(cm.getClistingid(), tempList);
			}
		}
		
		
		for (Map.Entry<String, List<InteractionProductMemberVideo>> interactionComment : catMap.entrySet()) {
			
			String listingId = interactionComment.getKey();
			List<InteractionProductMemberVideo> videoList = interactionComment.getValue();
			Date dcreatedate = videoList.get(0).getDcreatedate();
			Integer istate = videoList.get(0).getIauditorstatus();
			
			String productUrl = "";
			ProductUrl url = this.productUrlService
					.getProductUrlsByListingId(listingId, 1);
			if (null != url) {
				productUrl = url.getCurl();
			}
			String productSmallImgUrl = productImageMapper
					.getProductSmallImageForMemberViewsByListingId(listingId);
			 
			
			InteractionVideoBo ipb = new InteractionVideoBo();
			
			ipb.setDcreatedate(dcreatedate);
			ipb.setIstate(istate);
			ipb.setVideos(videoList);
			ipb.setProductSmallImageUrl(productSmallImgUrl);
			ipb.setProductUrl(productUrl);
			reviewsInMemberCenterList.add(ipb);
		}
		return reviewsInMemberCenterList;
	}

	@Override
	public boolean deleteVideosByListingIdAndEmail(String email, Integer websiteId,
			String listingId) {
		int result = this.videoMapper.deleteVideosByListingIdAndEmail(email, websiteId, listingId);
		return result>0?true:false;
	}

	@Override
	public InteractionProductMemberVideo getVideoByCommentIdAndEmailAndSiteId(
			Integer commentId, String email, Integer websiteId) {
		return this.videoMapper.getVideoByCommentIdAndEmailAndSiteId(commentId, email, websiteId);
	}

	@Override
	public int updateByPrimaryKeySelective(InteractionProductMemberVideo record) {
		return this.videoMapper.updateByPrimaryKeySelective(record);
	}
	

}
