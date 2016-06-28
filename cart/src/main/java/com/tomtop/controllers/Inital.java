package com.tomtop.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.tomtop.dto.StorageParent;
import com.tomtop.services.impl.base.StorageService;

@Component
public class Inital {
	
	@Autowired
	StorageService storageService;
	//获取所有仓库，
	static Map<String,StorageParent> storageCachemap = new HashMap<String,StorageParent>();
	
	//查询所有仓库转换成Map对象放入内存
	@PostConstruct 
	private Map<String,StorageParent> getStorageMap(){
		try{
			List<StorageParent> list = storageService.getAllStorageParentList();
			if(list!=null && list.size()<1){
				list.forEach(o ->{
					storageCachemap.put(String.valueOf(o.getIid()), o);
				});
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return storageCachemap;
	}
	
	//根据仓库id获取仓库名称
	public String getStorageName(int storageId){
		if(storageCachemap!=null){
			StorageParent obj= storageCachemap.get(String.valueOf(storageId));
			if(obj!=null){
				return obj.getCstoragename() ;
			}else{
				obj = storageService.findById(storageId);
				if(obj!=null){
					return obj.getCstoragename();
				}
			}
		}
		return null ;
	}

	public StorageService getStorageService() {
		return storageService;
	}

	public void setStorageService(StorageService storageService) {
		this.storageService = storageService;
	}
	
	
}
