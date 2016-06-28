package com.tomtop.entity;

public class ProductBaseRecommend extends ProductBase {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5356365818646921209L;
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
