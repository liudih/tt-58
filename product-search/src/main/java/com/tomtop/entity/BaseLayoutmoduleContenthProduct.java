package com.tomtop.entity;

/**
 * 模块内容实体内
 * 
 * @author liulj
 *
 */
public class BaseLayoutmoduleContenthProduct extends ProductBase{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7352116836167339041L;
	/**
	 * 排序号
	 */
	private Integer sort;
	/**
	 * 商品评论条数
	 */
	private Integer reviewCount;

	/**
	 * 商品评分等级
	 */
	private Double avgScore;
	/**
	 * 收藏数
	 */
	private Integer collectNum;
	
	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
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

	public Integer getCollectNum() {
		return collectNum;
	}

	public void setCollectNum(Integer collectNum) {
		this.collectNum = collectNum;
	}
	
}