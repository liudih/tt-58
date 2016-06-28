package com.tomtop.member.models.bo;

import com.tomtop.member.models.base.FilterBaseBean;


public class ReviewsBo extends FilterBaseBean {

	/**
	 * 
	 */
	private static final long serialVersionUID = 9219241013697771336L;
	private Integer commentId;
	private String listingId;
	private String sku;
	private String comment;//评论内容
	private Integer ps;//价格评分
	private Integer qs;//质量评分
	private Integer ss;//物流评分
	private Integer us;//有用评级
	private Integer fs;//综合评级
	private String videoUrl;//视频url
	private String videoTitle;//视频标题
	private String email;
	private String countryName;//国家
	private String pform;//来源: 如web app
	private Integer oid;//订单的iid
	
	public Integer getCommentId() {
		return commentId;
	}
	public void setCommentId(Integer commentId) {
		this.commentId = commentId;
	}
	public String getListingId() {
		return listingId;
	}
	public void setListingId(String listingId) {
		this.listingId = listingId;
	}
	public String getSku() {
		return sku;
	}
	public void setSku(String sku) {
		this.sku = sku;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public Integer getPs() {
		return ps;
	}
	public void setPs(Integer ps) {
		this.ps = ps;
	}
	public Integer getQs() {
		return qs;
	}
	public void setQs(Integer qs) {
		this.qs = qs;
	}
	public Integer getSs() {
		return ss;
	}
	public void setSs(Integer ss) {
		this.ss = ss;
	}
	public Integer getUs() {
		return us;
	}
	public void setUs(Integer us) {
		this.us = us;
	}
	public Integer getFs() {
		return fs;
	}
	public void setFs(Integer fs) {
		this.fs = fs;
	}
	public String getVideoUrl() {
		return videoUrl;
	}
	public void setVideoUrl(String videoUrl) {
		this.videoUrl = videoUrl;
	}
	public String getVideoTitle() {
		return videoTitle;
	}
	public void setVideoTitle(String videoTitle) {
		this.videoTitle = videoTitle;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPform() {
		return pform;
	}
	public void setPform(String pform) {
		this.pform = pform;
	}
	public String getCountryName() {
		return countryName;
	}
	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}
	public Integer getOid() {
		return oid;
	}
	public void setOid(Integer oid) {
		this.oid = oid;
	}
	
}
