package com.tomtop.base.service;

import java.util.HashMap;
import java.util.List;

public interface IProductAttributeService {

	public HashMap<String, String> getProductAttributeMap(Integer lang);
	public HashMap<String, List<String>> getProductAttributeKeyList(Integer lang);
}
