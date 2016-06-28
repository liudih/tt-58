package com.tomtop.base.service;

import java.util.List;

import com.tomtop.base.models.bo.ClientBo;
import com.tomtop.base.models.dto.ClientBase;

public interface IClientBaseService {

	public List<ClientBo> getClientList();
	
	public List<ClientBo> getClientList(ClientBase cb);
	
	public ClientBo getClientById(Integer id);
}
