package com.tomtop.services.impl.base;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tomtop.dto.StorageParent;
import com.tomtop.dto.base.Storage;
import com.tomtop.mappers.base.StorageMapper;
import com.tomtop.mappers.base.StorageParentMapper;
import com.tomtop.services.base.IStorageService;

@Service
public class StorageService implements IStorageService {

	public static final int DEL_STATE = -1;

	@Autowired
	StorageMapper storageMapper;
	@Autowired
	StorageParentMapper storageParentMapper;

	/*
	 * (non-Javadoc)
	 * 
	 * @see services.IStorageService#getAllStorages()
	 */
	@Override
	public List<Storage> getAllStorages() {

		return storageMapper.getAllStorageList();

	}

	@Override
	public int getCountStorages(int parentStorageId) {
		return storageMapper.getCountStorage(parentStorageId);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see services.IStorageService#getStorageForStorageId(int)
	 */
	@Override
	public Storage getStorageForStorageId(int iid) {
		return storageMapper.getStorageForStorageId(iid);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see services.IStorageService#getNotOverseasStorage()
	 */
	@Override
	public Storage getNotOverseasStorage() {
		return storageMapper.getNotOverseasStorage();
	}

	/*
	 * 根据仓库名称获取仓库ID
	 * 
	 * @see services.IStorageService#getStorageIdByName(java.lang.String)
	 */
	@Override
	public Integer getStorageIdByName(String storageName) {
		return storageMapper.getStorageIdByStorageName(storageName);
	}

	/*
	 * (non-Javadoc) <p>Title: getStorageByStorageIds</p> <p>Description:
	 * 通过仓库ID查询仓库</p>
	 * 
	 * @param storageIds
	 * 
	 * @return
	 * 
	 * @see services.IStorageService#getStorageByStorageIds(java.util.List)
	 */
	public List<Storage> getStorageByStorageIds(List<Integer> storageIds) {
		return storageMapper.getStorageByStorageIds(storageIds);
	}
	
	public List<StorageParent> getAllStorageParentList(){
		return storageParentMapper.getAllStorageParentList();
	}

	@Override
	public StorageParent findById(int iid) {
		return storageParentMapper.getStorageParentByStorageId(iid);
	}
	
	
	
}
