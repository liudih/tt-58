package com.tomtop.base.productbase.mappers;

import java.util.List;

import org.apache.ibatis.annotations.Select;

import com.tomtop.base.models.dto.StorageDto;

public interface StorageMapper {

	@Select("select * from t_storage where iid=#{0} ")
	StorageDto getById(Integer id);

	@Select("select * from t_storage where cstoragename=#{0} limit 1")
	StorageDto getByName(String name);

	@Select("select * from t_storage")
	List<StorageDto> getAll();
}
