package com.tomtop.services.impl.base;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tomtop.dto.base.Platform;
import com.tomtop.dto.base.Website;
import com.tomtop.mappers.base.PlatformMapper;
import com.tomtop.mappers.base.WebsiteMapper;
import com.tomtop.services.base.IWebsiteService;

@Service
public class WebsiteService implements IWebsiteService {

	private static final Logger Logger = LoggerFactory
			.getLogger(WebsiteService.class);

	@Autowired
	WebsiteMapper wsmapper;

	@Autowired
	PlatformMapper pfmapper;

	/*
	 * (non-Javadoc)
	 * 
	 * @see services.base.IWebsiteService#getAll()
	 */
	@Override
	public List<Website> getAll() {
		List<Website> list = wsmapper.getAll();
		return list;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see services.base.IWebsiteService#getWebsite(int)
	 */
	@Override
	public Website getWebsite(int websiteID) {
		return wsmapper.selectByPrimaryKey(websiteID);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see services.base.IWebsiteService#getWebsite(java.lang.String)
	 */
	@Override
	public Website getWebsite(String vhost) {
		return wsmapper.findByHostname(vhost);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see services.base.IWebsiteService#getDefaultWebsite()
	 */
	@Override
	public Website getDefaultWebsite() {
		return wsmapper.selectDefaultWebsite();
	}

	/**
	 * Search the nearest platform for the given hostname: e.g. www.tomtop.com,
	 * we search each level one by one (www.tomtop.com, tomtop.com, com) until
	 * an exact match.
	 *
	 * @param hostname
	 * @return
	 */
	Platform findNearestDomainPlatform(String hostname) {
		Platform platform = pfmapper.findByDomain(hostname);
		if (platform != null)
			return platform;
		int dotPos = hostname.indexOf('.');
		if (dotPos < 0) {
			return null;
		}
		String subhostname = hostname.substring(dotPos + 1, hostname.length());
		return findNearestDomainPlatform(subhostname);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see services.base.IWebsiteService#getWebsitesByWebsiteId(java.util.List)
	 */
	@Override
	public List<Website> getWebsitesByWebsiteId(List<Integer> websiteIds) {
		return wsmapper.getWebsitesByWebsiteIds(websiteIds);
	}

}
