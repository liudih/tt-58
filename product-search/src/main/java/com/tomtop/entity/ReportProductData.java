package com.tomtop.entity;

import java.io.Serializable;
import java.util.List;

import com.tomtop.framework.core.utils.Page;

/**
 * 导出数据实体类
 */
public class ReportProductData implements Serializable {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2705753302255308503L;
	private List<ReportProductDataBo> rpdbos;
	private Page page;
	
	public List<ReportProductDataBo> getRpdbos() {
		return rpdbos;
	}
	public void setRpdbos(List<ReportProductDataBo> rpdbos) {
		this.rpdbos = rpdbos;
	}
	public Page getPage() {
		return page;
	}
	public void setPage(Page page) {
		this.page = page;
	}
	
	
}
