package com.tomtop.services.impl;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.tomtop.entity.HomeNewest;
import com.tomtop.entity.HomeNewestBase;
import com.tomtop.entity.HomeNewestDto;
import com.tomtop.entity.HomeNewestImage;
import com.tomtop.entity.HomeNewestReview;
import com.tomtop.entity.HomeNewestVideo;
import com.tomtop.entity.index.IndexEntity;
import com.tomtop.mappers.mysql.HomeNewestMapper;
import com.tomtop.services.IHomeNewestService;
import com.tomtop.services.ISearchService;

/**
 * 首页获取评论、视频、图片配置
 * 
 * @author liulj
 *
 */
@Service
public class HomeNewestServiceImpl implements IHomeNewestService {

	private static final Logger logger = LoggerFactory
			.getLogger(HomeNewestServiceImpl.class);
	
	@Autowired
	HomeNewestMapper homeNewestMapper;
	@Autowired
    ISearchService searchService;
	
	private final static String IMAGE_TYPE = "image";
	private final static String REVIEW_TYPE = "review";
	private final static String VIDEO_TYPE = "video";
	
	/**
	 * 获取首页 Customers voices
	 * @return HomeNewest
	 */
	@Cacheable(value="customers_voices", keyGenerator = "customKeyGenerator")
	@Override
	public HomeNewest getCustomersVoices(Integer client, Integer lang , Integer website) {
		try{
			List<HomeNewestDto> hndto = homeNewestMapper.getHomeNewest(client, lang);
			if(hndto == null || hndto.size() == 0){
				logger.info("Home Newest is null");
				return null;
			}
			Map<String,HomeNewestBase> hnbMap = new HashMap<String, HomeNewestBase>();
			List<String> idList = new ArrayList<String>();
			hndto.forEach(ids ->{
				idList.add(ids.getListingId());
			});
			List<IndexEntity> ieList = searchService.getSearchProductList(JSON.toJSONString(idList), lang, website);
			logger.info("find index entity soru");
			if(ieList != null && ieList.size() > 0){
				for (int i = 0; i < ieList.size(); i++) {
					IndexEntity ie = ieList.get(i);
					if(ie == null){
						logger.error("index entity is null");
					}else{
						String listingId = ie.getListingId();
						HomeNewestBase hnb = new HomeNewestBase();
						if (ie.getMutil() != null) {
							if(ie.getMutil().getUrl() != null){
								hnb.setSkuUrl(ie.getMutil().getUrl().get(0));
							}
							hnb.setSkuTitle(ie.getMutil().getTitle());
						}
						if(ie.getDefaultImgUrl() == null){
							if(ie.getImgs() != null && ie.getImgs().size()>0){
								for (int j = 0; j < ie.getImgs().size(); j++) {
									if(ie.getImgs().get(j).getIsBase()){
										hnb.setSkuImageUrl(ie.getImgs().get(j).getUrl());
									}
								}
							}
						}else{
							hnb.setSkuImageUrl(ie.getDefaultImgUrl());
						}
						hnbMap.put(listingId, hnb);
					}
				}

				List<HomeNewestImage> image = new ArrayList<HomeNewestImage>();
				List<HomeNewestReview> review = new ArrayList<HomeNewestReview>();
				List<HomeNewestVideo> video = new ArrayList<HomeNewestVideo>();
				for (int i = 0; i < hndto.size(); i++) {
					HomeNewestDto hn = hndto.get(i);
					if(hn == null){
						logger.info("Home Newest Dto is null");
					}else{
						String listingId = hn.getListingId();
						HomeNewestBase hnb = hnbMap.get(listingId);
						if(hn.getType().equals(IMAGE_TYPE)){
							HomeNewestImage hni = new HomeNewestImage();
							hni.setImgUrl(hn.getContent());
							hni.setTitle(hn.getTitle());
							hni.setListingId(listingId);
							hni.setSku(hn.getSku());
							hni.setImgBy(hn.getUserBy());
							hni.setCountry(hn.getCountry());
							hni.setSkuTitle(hnb.getSkuTitle());
							hni.setSkuUrl(hnb.getSkuUrl());
							hni.setSkuImageUrl(hnb.getSkuImageUrl());
							image.add(hni);
						}else if(hn.getType().equals(REVIEW_TYPE)){
							HomeNewestReview hnv = new HomeNewestReview();
							hnv.setReviewContent(hn.getContent());
							hnv.setTitle(hn.getTitle());
							hnv.setListingId(listingId);
							hnv.setSku(hn.getSku());
							hnv.setReviewBy(hn.getUserBy());
							hnv.setCountry(hn.getCountry());
							hnv.setSkuTitle(hnb.getSkuTitle());
							hnv.setSkuUrl(hnb.getSkuUrl());
							hnv.setSkuImageUrl(hnb.getSkuImageUrl());
							review.add(hnv);
						}else if(hn.getType().equals(VIDEO_TYPE)){
							HomeNewestVideo hnv = new HomeNewestVideo();
							hnv.setVideoUrl(hn.getContent());
							hnv.setTitle(hn.getTitle());
							hnv.setListingId(listingId);
							hnv.setSku(hn.getSku());
							hnv.setVideoBy(hn.getUserBy());
							hnv.setCountry(hn.getCountry());
							hnv.setSkuTitle(hnb.getSkuTitle());
							hnv.setSkuUrl(hnb.getSkuUrl());
							hnv.setSkuImageUrl(hnb.getSkuImageUrl());
							video.add(hnv);
						}
					}
				}
				HomeNewest hn = new HomeNewest(image,review,video);
				return hn;
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}

}
