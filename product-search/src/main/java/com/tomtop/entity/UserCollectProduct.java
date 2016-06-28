package com.tomtop.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.tomtop.framework.core.utils.Page;

/**
 * 用户收藏商品详情查询包括分页
 */
public class UserCollectProduct implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8600700349584213266L;
	private List<UserCollectProductBo> ucpList = new ArrayList<UserCollectProductBo>();
	private Page page;
	
	public List<UserCollectProductBo> getUcpList() {
		return ucpList;
	}
	public void setUcpList(List<UserCollectProductBo> ucpList) {
		this.ucpList = ucpList;
	}
	public Page getPage() {
		return page;
	}
	public void setPage(Page page) {
		this.page = page;
	}

}
