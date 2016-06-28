package com.tomtop.services.impl.base;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tomtop.mappers.base.VhostMapper;

@Service
public class VhostService {
	@Autowired
	VhostMapper mapper;

	public String getCorderplaceholder(String vhost) {
		return mapper.getCorderplaceholder(vhost);
	}
}
