package com.tomtop.entity;

/**
 * 多仓库版本的对象
 */
public class SearchDepotProduct extends ProductBaseDepot {

	/**
	 * 
	 */
	private static final long serialVersionUID = -77060152458714013L;
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
