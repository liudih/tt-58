package com.tomtop.services.base;

import java.util.List;

import com.tomtop.dto.StorageParent;
import com.tomtop.dto.base.Storage;

public interface IStorageService {

	public abstract List<Storage> getAllStorages();

	public abstract int getCountStorages(int parentStorageId);

	public abstract Storage getStorageForStorageId(int iid);

	public abstract Storage getNotOverseasStorage();

	public abstract Integer getStorageIdByName(String storageName);

	public abstract List<Storage> getStorageByStorageIds(
			List<Integer> storageIds);
	
	
	public StorageParent findById(int iid);

}