package com.tomtop.entity;


public class SearchProduct extends ProductBase {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3044070625874299427L;
	/**
	 * 是否免邮
	 */
	private boolean isFreeShipping;
	/**
	 * 收藏数
	 */
	private Integer collectNum;
	/**
	 * 商品评论条数
	 */
	private Integer reviewCount;

	/**
	 * 商品评分等级
	 */
	private Double avgScore;
	
	public boolean isFreeShipping() {
		return isFreeShipping;
	}
	public void setFreeShipping(boolean isFreeShipping) {
		this.isFreeShipping = isFreeShipping;
	}
	public Integer getCollectNum() {
		return collectNum;
	}
	public void setCollectNum(Integer collectNum) {
		this.collectNum = collectNum;
	}
	public Integer getReviewCount() {
		return reviewCount;
	}
	public void setReviewCount(Integer reviewCount) {
		this.reviewCount = reviewCount;
	}
	public Double getAvgScore() {
		return avgScore;
	}
	public void setAvgScore(Double avgScore) {
		this.avgScore = avgScore;
	}
}
