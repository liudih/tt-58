package com.tomtop.mappers.product;


import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.tomtop.entity.TopicPage;

public interface TopicPageMapper {

	List<TopicPage> getTopicPage(@Param("type") String type,
			@Param("languageId") Integer languageId,
			@Param("startDate") Date startDate, @Param("endDate") Date endDate,
			@Param("siteId") Integer siteId, @Param("size") Integer size);
}
