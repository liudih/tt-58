package com.tomtop.entity;


import java.util.List;

import com.tomtop.entity.index.ReviewStartNum;

public class ProductBaseDtl extends BaseBean{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private List<ProductDetails> pdbList;//详情基本信息
	private PrdouctDesc pdb;//desc
	private ProductSeo psb;//seo
	private ReviewStartNum rsnbo;//评论星级
	private CollectCount ccb;//收藏数
	
	public List<ProductDetails> getPdbList() {
		return pdbList;
	}
	public void setPdbList(List<ProductDetails> pdbList) {
		this.pdbList = pdbList;
	}
	public PrdouctDesc getPdb() {
		return pdb;
	}
	public void setPdb(PrdouctDesc pdb) {
		this.pdb = pdb;
	}
	public ProductSeo getPsb() {
		return psb;
	}
	public void setPsb(ProductSeo psb) {
		this.psb = psb;
	}
	public ReviewStartNum getRsnbo() {
		return rsnbo;
	}
	public void setRsnbo(ReviewStartNum rsnbo) {
		this.rsnbo = rsnbo;
	}
	public CollectCount getCcb() {
		return ccb;
	}
	public void setCcb(CollectCount ccb) {
		this.ccb = ccb;
	}
	
}
