package com.tomtop.services.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tomtop.entity.HomeSearchKeyword;
import com.tomtop.mappers.mysql.HomeSearchAutocompleteMapper;
import com.tomtop.mappers.mysql.HomeSearchKeywordMapper;
import com.tomtop.services.IHomeSearchKeywordService;
import com.tomtop.utils.CommonDefn;

@Service
public class HomeSearchKeywordServiceImpl implements IHomeSearchKeywordService {

	@Autowired
	HomeSearchKeywordMapper searchKeywordMapper;
	@Autowired
	HomeSearchAutocompleteMapper searchAutocompleteMapper;
	
	@Override
	public List<HomeSearchKeyword> getKeywordList(int categoryId, int client,int lang) {
		List<HomeSearchKeyword> hskList = searchKeywordMapper.getKeywordList(categoryId, client, lang);
		if (hskList == null || hskList.isEmpty()) {
			if(lang > CommonDefn.ONE){
				hskList = searchKeywordMapper.getKeywordList(categoryId, client, lang);
			}
		}
		return hskList;
	}

	@Override
	public List<String> getKeywordAutoList(String keyword, int client, int lang) {
		List<String> slist = searchAutocompleteMapper.getKeywordList(keyword, client, lang);
		if (slist == null || slist.isEmpty()) {
			if(lang > CommonDefn.ONE){
				slist = searchAutocompleteMapper.getKeywordList(keyword, client, lang);
			}
		}
		return slist;
	}

}
