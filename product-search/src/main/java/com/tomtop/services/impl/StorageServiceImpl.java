package com.tomtop.services.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tomtop.entity.StorageParent;
import com.tomtop.mappers.base.StorageMapper;
import com.tomtop.services.IStorageService;

@Service
public class StorageServiceImpl implements IStorageService {

	@Autowired
	StorageMapper storageMapper;
	
	@Override
	public List<StorageParent> getAllStorageParent() {
		return storageMapper.getStorageParentAll();
	}

}
