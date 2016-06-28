package com.tomtop.entity;

import java.io.Serializable;
import java.util.List;

/**
 * 首面特别类目下的内容
 * 
 * @author liulj
 *
 */
public class HomeFeaturedCategoryContent implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7512605452347462681L;

	private List<HomeFeaturedCategorySku> skus;

	private List<HomeFeaturedCategoryKey> keys;

	private List<HomeFeaturedCategoryBanner> banners;

	public List<HomeFeaturedCategorySku> getSkus() {
		return skus;
	}

	public void setSkus(List<HomeFeaturedCategorySku> skus) {
		this.skus = skus;
	}

	public List<HomeFeaturedCategoryKey> getKeys() {
		return keys;
	}

	public void setKeys(List<HomeFeaturedCategoryKey> keys) {
		this.keys = keys;
	}

	public List<HomeFeaturedCategoryBanner> getBanners() {
		return banners;
	}

	public void setBanners(List<HomeFeaturedCategoryBanner> banners) {
		this.banners = banners;
	}

}