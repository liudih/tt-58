package com.tomtop.services;

import java.util.List;

import com.tomtop.entity.BaseLayout;

public interface IBaseLayoutService {

	List<BaseLayout> getBaseLayoutByCode(String code, int client, int lang);
}
