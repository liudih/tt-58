package com.tomtop.services.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.tomtop.entity.InteractionComment;
import com.tomtop.entity.ProductReview;
import com.tomtop.entity.ReviewPhotoVideo;
import com.tomtop.entity.index.IndexEntity;
import com.tomtop.entity.index.PageBean;
import com.tomtop.entity.rc.FiveReview;
import com.tomtop.mappers.interaction.ProductReviewsMapper;
import com.tomtop.services.IInteractionCommentService;
import com.tomtop.services.ISearchIndex;

@Service
public class InteractionCommentServiceImpl extends BaseService implements
		IInteractionCommentService {

	@Autowired
	ProductReviewsMapper productReviewsMapper;
	@Autowired
	ISearchIndex searchIndex;
	
	private final static int state = 1;
	private final static int limit = 5;
	
	
	/**
	 * 获取商品评论详情
	 * 
	 * @param listingId
	 * 
	 * @param siteId
	 * 
	 * @return List<ProductReviewBo>
	 * @author renyy
	 */
	@Cacheable(value = "product_review", keyGenerator = "customKeyGenerator")
	@Override
	public List<ProductReview> getProductReviewBoList(String listingId,
			Integer siteId) {
		List<ProductReview> prboList = new ArrayList<ProductReview>();
		List<InteractionComment> icList = productReviewsMapper.getInteractionCommentDtoByListingId(listingId, state, siteId, limit);
		Map<Integer, ProductReview> prMap = new LinkedHashMap<Integer, ProductReview>();
		if(icList != null && icList.size() > 0){
			List<Integer> commentIds = new ArrayList<Integer>();
			for (int i = 0; i < icList.size(); i++) {
				InteractionComment icd = icList.get(i);
				Integer id = icd.getIid();
				ProductReview prbo = new ProductReview();
				prbo.setEmail(icd.getCmemberemail());
				prbo.setOverall(icd.getFoverallrating());
				prbo.setUsefulness(icd.getIusefulness());
				prbo.setShipping(icd.getIshipping());
				prbo.setPrice(icd.getIprice());
				prbo.setQuality(icd.getIquality());
				prbo.setCommentDate(DateFormatUtils.format(icd.getDcreatedate(), "yyyy-MM-dd HH:mm:ss"));
				prbo.setComment(icd.getCcomment());
				prMap.put(id, prbo);
				commentIds.add(id);
			}
			List<ReviewPhotoVideo> rpvdtoList = productReviewsMapper.getReviewPhotoVideoDtoByCommentId(state, siteId, commentIds);
			if(rpvdtoList != null  && rpvdtoList.size() > 0){
				for (int i = 0; i < rpvdtoList.size(); i++) {
					ReviewPhotoVideo rpvdto = rpvdtoList.get(i);
					String type = rpvdto.getCode();
					String url = rpvdto.getUrl();
					ProductReview prbo = prMap.get(rpvdto.getCommentid());
					if("photo".equals(type)){
						List<String> imgUrls = prbo.getImgUrls();
						if(imgUrls == null){
							imgUrls = new ArrayList<String>();
						}
						imgUrls.add(url);
						prbo.setImgUrls(imgUrls);
						prMap.put(rpvdto.getCommentid(), prbo);
					}
					if("video".equals(type)){
						List<String> videos = prbo.getVideos();
						if(videos == null){
							videos = new ArrayList<String>();
						}
						videos.add(url);
						prbo.setVideos(videos);
						prMap.put(rpvdto.getCommentid(), prbo);
					}
				}
			}
		}
		if(prMap.size() > 0){
			ProductReview prb = null;
			Iterator<Map.Entry<Integer,ProductReview>> iter = prMap.entrySet().iterator();
			while (iter.hasNext()) {
				Map.Entry<Integer, ProductReview> entry = iter.next(); 
				prb = (ProductReview) entry.getValue();
				prboList.add(prb);
			}
		}
		return prboList;
	}

    /**
     * 查询商品评论详情列表
     * @param listingId
     * @param siteId
     * @param page
     * @param size
     */
	@Override
	public List<ProductReview> getProductReviewPage(String listingId,
			Integer siteId, Integer page, Integer size) {
		List<ProductReview> prboList = new ArrayList<ProductReview>();
		List<InteractionComment> icList = productReviewsMapper.getInteractionCommentDtoPage(listingId, state, siteId, page, size);
		Map<Integer, ProductReview> prMap = new LinkedHashMap<Integer, ProductReview>();
		if(icList != null && icList.size() > 0){
			List<Integer> commentIds = new ArrayList<Integer>();
			for (int i = 0; i < icList.size(); i++) {
				InteractionComment icd = icList.get(i);
				Integer id = icd.getIid();
				ProductReview prbo = new ProductReview();
				prbo.setEmail(icd.getCmemberemail());
				prbo.setOverall(icd.getFoverallrating());
				prbo.setUsefulness(icd.getIusefulness());
				prbo.setShipping(icd.getIshipping());
				prbo.setPrice(icd.getIprice());
				prbo.setQuality(icd.getIquality());
				prbo.setCommentDate(DateFormatUtils.format(icd.getDcreatedate(), "yyyy-MM-dd HH:mm:ss"));
				prbo.setComment(icd.getCcomment());
				prMap.put(id, prbo);
				commentIds.add(id);
			}
			List<ReviewPhotoVideo> rpvdtoList = productReviewsMapper.getReviewPhotoVideoDtoByCommentId(state, siteId, commentIds);
			if(rpvdtoList != null  && rpvdtoList.size() > 0){
				for (int i = 0; i < rpvdtoList.size(); i++) {
					ReviewPhotoVideo rpvdto = rpvdtoList.get(i);
					String type = rpvdto.getCode();
					String url = rpvdto.getUrl();
					ProductReview prbo = prMap.get(rpvdto.getCommentid());
					if("photo".equals(type)){
						List<String> imgUrls = prbo.getImgUrls();
						if(imgUrls == null){
							imgUrls = new ArrayList<String>();
						}
						imgUrls.add(url);
						prbo.setImgUrls(imgUrls);
						prMap.put(rpvdto.getCommentid(), prbo);
					}
					if("video".equals(type)){
						List<String> videos = prbo.getVideos();
						if(videos == null){
							videos = new ArrayList<String>();
						}
						videos.add(url);
						prbo.setVideos(videos);
						prMap.put(rpvdto.getCommentid(), prbo);
					}
				}
			}
		}
		if(prMap.size() > 0){
			ProductReview prb = null;
			Iterator<Map.Entry<Integer,ProductReview>> iter = prMap.entrySet().iterator();
			while (iter.hasNext()) {
				Map.Entry<Integer, ProductReview> entry = iter.next(); 
				prb = (ProductReview) entry.getValue();
				prboList.add(prb);
			}
		}
		return prboList;
	}


	@Override
	public Integer getProductReviewCount(String listingId, Integer siteId) {
		return productReviewsMapper.getInteractionCommentDtoCount(listingId, state, siteId);
	}
	/**
	 * 获取RC首页评论
	 * @param website
	 * @param lang
	 * @return
	 */
	public List<FiveReview> getRcHomeFiveReview(Integer website,Integer lang){
		List<FiveReview> frList = productReviewsMapper.getFoverallratingFiveReviewByWebsite(website);
		if(frList != null && frList.size() > 0){
			List<String> lists = new ArrayList<String>();
			for (FiveReview fr : frList) {
				if(fr.getListingId() != null){
					lists.add(fr.getListingId());
				}
			}
			//通过lists获取商品信息 并赋值到frList中返回
			PageBean bean = new PageBean();
			bean.setLanguageName(getLangCode(lang));//设置语言名字
			bean.setEndNum(limit);
			bean = searchIndex.queryByIdsAndFilter(bean, lists);
			if(bean == null){
				return null;
			}
			List<IndexEntity> ieList = bean.getIndexs();
			List<FiveReview> frList2 = new ArrayList<FiveReview>();
			if(ieList != null && ieList.size() > 0){
				for (int i = 0; i < ieList.size(); i++) {
					IndexEntity ie = ieList.get(i);
					for (FiveReview fr : frList) {
						if(fr.getListingId().equals(ie.getListingId()) && ie.getMutil() != null){
							fr.setTitle(ie.getMutil().getTitle());
							if(ie.getMutil().getUrl() != null && ie.getMutil().getUrl().size() > 0){
								fr.setUrl(ie.getMutil().getUrl().get(0));
							}
							fr.setImageUrl(ie.getDefaultImgUrl());
							frList2.add(fr);
						}
					}
				}
			}
			return frList2;
		}
		return frList;
	}

}
