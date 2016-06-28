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
import com.tomtop.member.mappers.interaction.InteractionProductMemberPhotosMapper;
import com.tomtop.member.mappers.product.ProductImageMapper;
import com.tomtop.member.models.bo.InteractionPhotoBo;
import com.tomtop.member.models.dto.InteractionProductMemberPhotos;
import com.tomtop.member.models.dto.product.ProductUrl;
import com.tomtop.member.service.IInteractionProductPhotosService;
import com.tomtop.member.service.IProductUrlService;

@Service
public class InteractionProductPhotosServiceImpl implements
		IInteractionProductPhotosService {

	@Autowired
	InteractionProductMemberPhotosMapper mapper;

	@Autowired
	IProductUrlService productUrlService;

	@Autowired
	ProductImageMapper productImageMapper;

	@Autowired
	InteractionProductMemberPhotosMapper productMemberPhotosMapper;

	@Override
	public List<String> getCommentImgageByCommentIdLimit5(Integer commentId) {
		return this.mapper.getCommentImgageByCommentIdLimit5(commentId);
	}

	@Override
	public int insertSelective(InteractionProductMemberPhotos record) {
		return this.mapper.insertSelective(record);
	}

	@Override
	public List<InteractionPhotoBo> getMyReviewsPhotos(String email,
			Integer total, Integer pageIndex, Integer pageSize, Integer status,
			Integer dateType, Integer websiteId) {
		List<InteractionProductMemberPhotos> reviews = Lists.newArrayList();
		Date start = null;
		Date end = null;
		if (dateType != 0) {
			end = new Date();
			GregorianCalendar calendar = new GregorianCalendar();
			calendar.add(Calendar.MONTH, (dateType * (-1)));
			start = calendar.getTime();
		}
		reviews = mapper.getMyReviewsPhotosPage(email, pageIndex, pageSize,
				status, start, end, websiteId);

		List<InteractionPhotoBo> reviewsInMemberCenters = getReviewsInMemberCenterList(reviews);

		return reviewsInMemberCenters;
	}

	public List<InteractionPhotoBo> getReviewsInMemberCenterList(
			List<InteractionProductMemberPhotos> reviews) {
		if (null == reviews || reviews.size() < 1) {
			return Lists.newArrayList();
		}
		List<InteractionPhotoBo> reviewsInMemberCenterList = Lists
				.newArrayList();

		Map<String, List<InteractionProductMemberPhotos>> catMap = new HashMap<String, List<InteractionProductMemberPhotos>>();

		if (null != reviews && reviews.size() > 0) {
			Iterator<InteractionProductMemberPhotos> e = reviews.iterator();
			while (e.hasNext()) {
				InteractionProductMemberPhotos cm = e.next();
				List<InteractionProductMemberPhotos> tempList = null;
				if (catMap.containsKey(cm.getClistingid())) {
					tempList = catMap.get(cm.getClistingid());
					tempList.add(cm);

				} else {
					tempList = new ArrayList<InteractionProductMemberPhotos>();
					tempList.add(cm);
				}

				catMap.put(cm.getClistingid(), tempList);
			}
		}

		for (Map.Entry<String, List<InteractionProductMemberPhotos>> interactionComment : catMap
				.entrySet()) {

			String listingId = interactionComment.getKey();
			List<InteractionProductMemberPhotos> photoList = interactionComment
					.getValue();
			Date dcreatedate = photoList.get(0).getDcreatedate();
			Integer istate = photoList.get(0).getIauditorstatus();

			String productUrl = "";
			ProductUrl url = this.productUrlService.getProductUrlsByListingId(
					listingId, 1);
			if (null != url) {
				productUrl = url.getCurl();
			}
			String productSmallImgUrl = productImageMapper
					.getProductSmallImageForMemberViewsByListingId(listingId);
			List<String> photosUrl = Lists.newArrayList();
			for (InteractionProductMemberPhotos i : photoList) {
				photosUrl.add(i.getCimageurl());
			}

			InteractionPhotoBo ipb = new InteractionPhotoBo();

			ipb.setDcreatedate(dcreatedate);
			ipb.setIstate(istate);
			ipb.setPhotosUrl(photosUrl);
			ipb.setProductSmallImageUrl(productSmallImgUrl);
			ipb.setProductUrl(productUrl);
			reviewsInMemberCenterList.add(ipb);
		}
		return reviewsInMemberCenterList;
	}

	@Override
	public Integer getTotalPhotoCountByMemberEmailAndSiteId(String email,
			Integer websiteId) {
		return this.productMemberPhotosMapper
				.getTotalPhotosCountByMemberEmailAndSiteId(email, websiteId);
	}

	@Override
	public boolean deletePhotosByListingIdAndEmail(String email,
			Integer websiteId, String listingId, String imageUrl) {
		int result = this.productMemberPhotosMapper
				.deletePhotoByListingIdAndEmailAndUrl(email, websiteId, listingId, imageUrl);
		return result > 0 ? true : false;
	}
	
	@Override
	public boolean deletePhotosByListingIdAndEmail(String email,
			Integer websiteId, String listingId) {
		int result = this.productMemberPhotosMapper
				.deletePhotoByListingIdAndEmail(email, websiteId, listingId);
		return result > 0 ? true : false;
	}
}
