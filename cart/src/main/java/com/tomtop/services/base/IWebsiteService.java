package com.tomtop.services.base;

import java.util.List;

import com.tomtop.dto.base.Website;

public interface IWebsiteService {

	public abstract List<Website> getAll();

	public abstract Website getWebsite(int websiteID);

	public abstract Website getWebsite(String vhost);

	public abstract Website getDefaultWebsite();

	public abstract List<Website> getWebsitesByWebsiteId(
			List<Integer> websiteIds);

}