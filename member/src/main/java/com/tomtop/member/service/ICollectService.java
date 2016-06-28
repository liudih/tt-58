package com.tomtop.member.service;

import com.tomtop.member.models.dto.ProductCollect;

public interface ICollectService {
	public abstract boolean addCollect(String lid, String email, Integer websiteId);

	public abstract boolean delCollect(String lid, String email, Integer websiteId);
	
	public abstract ProductCollect getProductCollectByIid(Integer iid);
	
	public abstract boolean delCollectByIid(Integer iid);
	
	public abstract ProductCollect getProductCollectByListingIdAndEmail(String listingId, String email, Integer websiteId);
	
	public abstract int getCollectsCountByEmail(String email, Integer websiteId);
}
