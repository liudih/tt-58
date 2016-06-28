package com.tomtop.member.service.impl;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tomtop.member.mappers.interaction.ProductCollectMapper;
import com.tomtop.member.models.dto.ProductCollect;
import com.tomtop.member.service.ICollectService;

@Service
public class CollectServiceImpl implements ICollectService {

	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	ProductCollectMapper productCollectMapper;

	@Override
	public boolean addCollect(String lid, String email, Integer websiteId) {
		ProductCollect p = new ProductCollect();
		p.setClistingid(lid);
		p.setCemail(email);
		p.setDcreatedate(new Date());
		p.setIwebsiteid(websiteId);
		int flag = productCollectMapper.insertSelective(p);
		return flag > 0;
	}

	@Override
	public boolean delCollect(String lid, String email, Integer websiteId) {
		int flag = productCollectMapper.delCollect(lid, email, websiteId);
		if(flag > 0){
			return true;
		}
		logger.error("CollectServiceImpl delCollect erroe");
		return false;
	}

	@Override
	public ProductCollect getProductCollectByIid(Integer iid) {
		return this.productCollectMapper.getProductCollectByIid(iid);
	}

	@Override
	public boolean delCollectByIid(Integer iid) {
		int flag = productCollectMapper.deleteByIid(iid);
		return flag > 0;
	}

	@Override
	public ProductCollect getProductCollectByListingIdAndEmail(
			String listingId, String email, Integer websiteId) {
		return this.productCollectMapper.getProductCollectByListingIdAndEmail(
				listingId, email, websiteId);
	}

	@Override
	public int getCollectsCountByEmail(String email, Integer websiteId) {
		return this.productCollectMapper.getCollectsCountByEmail(email, websiteId);
	}

}
