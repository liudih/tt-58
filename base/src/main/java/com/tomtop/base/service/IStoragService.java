package com.tomtop.base.service;

import java.util.List;

import com.tomtop.base.models.bo.StorageBo;

public interface IStoragService {

	StorageBo getById(Integer id);

	StorageBo getByName(String name);

	List<StorageBo> getAll();
}
