package com.tomtop.mappers.base;


import java.util.List;

import org.apache.ibatis.annotations.Select;

import com.tomtop.entity.StorageParent;


public interface StorageMapper {
	/**
	 * 查询所有父仓库
	 * @param listingID
	 * 
	 * @author renyy
	 */
	@Select("select sp.iid as id,sp.cstoragename as name,s.cstoragename shortName from t_storage_parent sp "
			+ "LEFT JOIN t_storage s on s.iparentstorage=sp.iid order by sp.iid")
	List<StorageParent> getStorageParentAll();
	/**
	 * 是否为海外仓
	 * @param listingID
	 * 
	 * @author renyy
	 */
	@Select("select iid from t_storage where ioverseas=0 limit 1")
	Integer getNotOverseasStorage();
	
	@Select("<script>select iid as id,cstoragename as name from t_storage where iid in "
			+ "<foreach item='item' index='index' collection='list' open='(' separator=',' close=')'>#{item}</foreach> "
			+ " order by iid </script>")
	List<StorageParent> getStorage(List<Integer> list);
}
