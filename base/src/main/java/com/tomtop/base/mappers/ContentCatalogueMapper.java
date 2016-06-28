package com.tomtop.base.mappers;

import java.util.List;

import org.apache.ibatis.annotations.Select;

import com.tomtop.base.models.dto.ContentCatalogue;
import com.tomtop.base.models.dto.ContentDeails;

public interface ContentCatalogueMapper {

	@Select({"select cc.id,cdc.title,cdc.url,cc.level,cc.parent_id parentId,cc.sort from content_catalogue cc ",
			 "LEFT JOIN content_details cd on cc.id=cd.catalogue_id and cd.is_deleted=0 ",
			 "LEFT JOIN content_details_context cdc on cd.id=cdc.details_id and cdc.is_enabled=1 and cdc.language_id=#{2} ",
			 "where cc.parent_id=(select id from content_catalogue where name=#{0} and client_id=#{1} and is_deleted=0 limit 1) ",
			 "and cc.client_id=#{1} and cc.is_deleted=0 order by sort"})
	public List<ContentCatalogue> getContentCatalogueDto(String name,Integer client,Integer lang); 
	
	@Select({"select cc.id,cdc.title,cdc.url,cc.level,cc.parent_id parentId,cc.sort from content_catalogue cc ",
			"LEFT JOIN content_details cd on cc.id=cd.catalogue_id and cd.is_deleted=0 ",
			"LEFT JOIN content_details_context cdc on cd.id=cdc.details_id and cdc.is_enabled=1 and cdc.language_id=#{2} ",
			"where cc.parent_id=#{0} and cc.client_id=#{1} and cc.is_deleted=0 order by sort"})
	public List<ContentCatalogue> getContentCatalogueDtoByParentId(Integer parentId,Integer client,Integer lang); 
	
	@Select({"select cdc.title,cdc.url,cdc.content,cdc.meta_title metaTitle,"
			+ "cdc.meta_keyword metaKeyword,cdc.meta_description metaDescription"
			+ " from content_details cd, content_details_context cdc where cd.id=cdc.details_id and "
			+ "cd.catalogue_id=#{0} and cd.is_deleted=0 and cdc.language_id=#{1} and cdc.is_enabled=1"})
	public List<ContentDeails> getContentCatalogueDetails(Integer catalogueId,Integer lang); 
	
	@Select({"select cdc.title,cdc.url,cdc.content,cdc.meta_title metaTitle,",
			"cdc.meta_keyword metaKeyword,cdc.meta_description metaDescription ",
			"from content_details cd, content_details_context cdc,content_catalogue cc ",
			"where cd.id=cdc.details_id and cd.catalogue_id=cc.id and ",
			"cdc.url=#{0} and cd.is_deleted=0 and cdc.language_id=#{1} and cc.client_id=#{2} and cdc.is_enabled=1"})
	public List<ContentDeails> getContentCatalogueDetailsByUrl(String url,Integer lang,Integer client); 
}
