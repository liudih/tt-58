package com.tomtop.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.tomtop.entity.index.AggregationEntity;
import com.tomtop.framework.core.utils.Page;

public class ProductBaseSearchKeyword implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8337930272085130991L;
	private List<SearchProduct> pblist = new ArrayList<SearchProduct>();
	/**聚合结果集**/
	private Map<String,List<AggregationEntity>> aggsMap = new HashMap<String,List<AggregationEntity>>();
	private Page page;
	
	public List<SearchProduct> getPblist() {
		return pblist;
	}
	public void setPblist(List<SearchProduct> pblist) {
		this.pblist = pblist;
	}
	public Page getPage() {
		return page;
	}
	public void setPage(Page page) {
		this.page = page;
	}
	public Map<String, List<AggregationEntity>> getAggsMap() {
		return aggsMap;
	}
	public void setAggsMap(Map<String, List<AggregationEntity>> aggsMap) {
		this.aggsMap = aggsMap;
	}
}
