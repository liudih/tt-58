package com.tomtop.base.mappers;

import java.util.List;

import com.tomtop.base.models.dto.ClientBase;

public interface ClientBaseMapper {

	List<ClientBase> getAllClientBase();
	
	List<ClientBase> getClientBase(ClientBase cb);
}
