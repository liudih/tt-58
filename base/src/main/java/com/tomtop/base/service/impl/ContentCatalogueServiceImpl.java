package com.tomtop.base.service.impl;

import java.util.LinkedHashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tomtop.base.mappers.ContentCatalogueMapper;
import com.tomtop.base.models.dto.ContentCatalogue;
import com.tomtop.base.models.dto.ContentDeails;
import com.tomtop.base.service.IContentCatalogueService;
import com.tomtop.base.utils.CommonUtils;

@Service
public class ContentCatalogueServiceImpl implements IContentCatalogueService {

	Logger logger = LoggerFactory.getLogger(ContentCatalogueServiceImpl.class);
	
	@Autowired
	ContentCatalogueMapper ccmapper;
	
	@Override
	public List<ContentCatalogue> getContentCatalogue(
			String name, Integer client,Integer lang) {
		List<ContentCatalogue> ccdlist = ccmapper.getContentCatalogueDto(name, client,lang);
		if(ccdlist == null || ccdlist.size() == 0){
			return null;
		}
		for (int i = 0; i < ccdlist.size(); i++) {
			ContentCatalogue ccd = ccdlist.get(i);
			if(ccd != null){
				List<ContentCatalogue> list = ccmapper.getContentCatalogueDtoByParentId(ccd.getId(), client,lang);
				for (ContentCatalogue contentCatalogue : list) {
					ccdlist.add(contentCatalogue);
				}
			}
		}
		return ccdlist;
	}

	@Override
	public List<ContentDeails> getContentDeails(Integer catalogueId, Integer lang) {
		List<ContentDeails> cdList = ccmapper.getContentCatalogueDetails(catalogueId, lang);
		if(cdList == null || cdList.size() == 0){
			if(lang > CommonUtils.ONE){
				cdList = ccmapper.getContentCatalogueDetails(catalogueId, CommonUtils.ONE);
			}
		}
		return cdList;
	}

	@Override
	public LinkedHashMap<String, List<ContentCatalogue>> getContentCatalogueByTitle(
			String name, Integer client, Integer lang) {
		//logger.error("info getContent linkedHashMap");
		List<ContentCatalogue> ccdlist = ccmapper.getContentCatalogueDto(name, client,lang);
		if(ccdlist == null || ccdlist.size() == 0){
			return null;
		}
		LinkedHashMap<String, List<ContentCatalogue>> lmap = new LinkedHashMap<String, List<ContentCatalogue>>();
		for (int i = 0; i < ccdlist.size(); i++) {
			ContentCatalogue ccd = ccdlist.get(i);
			if(ccd != null && ccd.getTitle() != null){
				List<ContentCatalogue> list = ccmapper.getContentCatalogueDtoByParentId(ccd.getId(), client,lang);
				if(list != null && list.size() > 0){
					lmap.put(ccd.getTitle(), list);
				}
			}
		}
		return lmap;
	}

	@Override
	public List<ContentDeails> getContentDeailsByPath(String url,
			Integer lang, Integer client) {
		List<ContentDeails> cdList = ccmapper.getContentCatalogueDetailsByUrl(url, lang,client);
		if(cdList == null || cdList.size() == 0){
			if(lang > CommonUtils.ONE){
				cdList = ccmapper.getContentCatalogueDetailsByUrl(url, CommonUtils.ONE,client);
			}
		}
		return cdList;
	}

}
