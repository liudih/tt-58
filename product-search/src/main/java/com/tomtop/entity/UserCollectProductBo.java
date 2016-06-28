package com.tomtop.entity;

import java.util.Date;

/**
 * 用户收藏商品详细
 */
public class UserCollectProductBo extends ProductBase {

	
	private static final long serialVersionUID = 1L;
	private Double start;//评论星级
	private Integer reviewNum;//评论数
	private Boolean isVideo;//商品是否有视频
	private Boolean isMulti;//是否为多属性商品
	private Boolean isFreeShipping;//是否为免邮商品
	private Boolean isDayDelivery;//是否24小时内发货
	private Integer collectNum;//收藏数
	private Date collectDate;//收藏日期
	
	public Double getStart() {
		return start;
	}
	public void setStart(Double start) {
		this.start = start;
	}
	public Integer getReviewNum() {
		return reviewNum;
	}
	public void setReviewNum(Integer reviewNum) {
		this.reviewNum = reviewNum;
	}

	public Boolean getIsVideo() {
		return isVideo;
	}
	public void setIsVideo(Boolean isVideo) {
		this.isVideo = isVideo;
	}
	public Boolean getIsMulti() {
		return isMulti;
	}
	public void setIsMulti(Boolean isMulti) {
		this.isMulti = isMulti;
	}
	public Boolean getIsFreeShipping() {
		return isFreeShipping;
	}
	public void setIsFreeShipping(Boolean isFreeShipping) {
		this.isFreeShipping = isFreeShipping;
	}
	public Boolean getIsDayDelivery() {
		return isDayDelivery;
	}
	public void setIsDayDelivery(Boolean isDayDelivery) {
		this.isDayDelivery = isDayDelivery;
	}
	public Integer getCollectNum() {
		return collectNum;
	}
	public void setCollectNum(Integer collectNum) {
		this.collectNum = collectNum;
	}
	public Date getCollectDate() {
		return collectDate;
	}
	public void setCollectDate(Date collectDate) {
		this.collectDate = collectDate;
	}
	
}
