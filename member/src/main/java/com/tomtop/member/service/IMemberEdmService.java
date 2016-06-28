package com.tomtop.member.service;

import java.util.List;

public interface IMemberEdmService {
	public List<String> addMemberEdm(String[] categoryArr,String email,int websiteId);
	public void callWebservice(String[] categoryArr , String email,int languageid,
			int websiteid);
}
