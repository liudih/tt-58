package com.tomtop.services;

import java.util.HashMap;
import java.util.List;

import com.tomtop.entity.BaseLayoutmoduleContenthProduct;
import com.tomtop.entity.rc.LayoutmoduleContentRc;

public interface ILayoutService {
	
	public HashMap<String,List<BaseLayoutmoduleContenthProduct>> getBaseLayoutmoduleContenth(Integer lang,Integer client,Integer website,String layoutcode,String currency);
	public HashMap<String,List<BaseLayoutmoduleContenthProduct>> getBaseLayoutmoduleContenth(Integer lang,Integer client,Integer website,String layoutcode,String currency,String depotName);
	public HashMap<String,List<LayoutmoduleContentRc>> getBaseLayoutmoduleContenthRc(Integer lang,Integer client,Integer website,String layoutcode,String currency,String depotName);
}
