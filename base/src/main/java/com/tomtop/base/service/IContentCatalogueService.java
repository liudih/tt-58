package com.tomtop.base.service;

import java.util.LinkedHashMap;
import java.util.List;

import com.tomtop.base.models.dto.ContentCatalogue;
import com.tomtop.base.models.dto.ContentDeails;

public interface IContentCatalogueService {

	public List<ContentCatalogue> getContentCatalogue(String name,Integer client,Integer lang);
	public LinkedHashMap<String,List<ContentCatalogue>> getContentCatalogueByTitle(String name,Integer client,Integer lang);
	public List<ContentDeails> getContentDeails(Integer catalogueId,Integer lang);
	public List<ContentDeails> getContentDeailsByPath(String url,Integer lang,Integer client);
}
