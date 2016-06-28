package com.tomtop.services;


import java.util.List;

import com.tomtop.entity.TopicPageBo;


public interface ITopicPageService {

	public List<TopicPageBo> filterTopicPage(String type, Integer siteID,
			Integer languageId, Integer year, Integer month, Integer size);
}
