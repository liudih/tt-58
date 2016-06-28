package com.tomtop.services;

import java.util.List;

import com.tomtop.entity.HomeFeaturedCategory;
import com.tomtop.entity.HomeFeaturedCategoryContent;

public interface IHomeFeaturedCategoryService {

	public List<HomeFeaturedCategory> getHomeFeaturedCategory(int client,int lang,int website);
	public HomeFeaturedCategoryContent getHomeFeaturedCategoryContent(int categoryId,int client,int lang,int website,String currency);
	public HomeFeaturedCategoryContent getHomeFeaturedCategoryContent(int categoryId,int client,int lang,int website,String currency,String depotName);
}
