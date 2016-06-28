package com.tomtop.member.models.bo;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class MemberReviewsBo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8664707107482768679L;

	private Integer rid;//评论Id
	private String comment;//评论内容
	private Integer ps;//价格评分
	private Integer qs;//质量评分
	private Integer ss;//物流评分
	private Integer us;//有用评级
	private Double fs;//综合评级
	private String videoUrl;//视频url
	private String videoTitle;//视频标题
	private Integer status;//状态
	private Date createDate;
	private String listingId;
	private String sku;
	private String url;//商品的url
	private String imgUrl;//商品图片Url
	private List<String> photosUrl;//评论图片url
	
	public MemberReviewsBo(){
		
	}
	public MemberReviewsBo(Integer rid,String comment,Integer ps,Integer qs,Integer ss,Integer us,Double fs,
			String videoUrl,String videoTitle,Integer status,Date createDate,String listingId,String sku,
			String url,String imgUrl,List<String> photosUrl){
		this.rid = rid;
		this.comment = comment;
		this.ps = ps;
		this.qs = qs;
		this.ss = ss;
		this.us = us;
		this.fs = fs;
		this.videoUrl = videoUrl;
		this.videoTitle = videoTitle;
		this.status = status;
		this.createDate = createDate;
		this.listingId = listingId;
		this.sku = sku;
		this.url = url;
		this.imgUrl = imgUrl;
		this.photosUrl = photosUrl;
	}
	public Integer getRid() {
		return rid;
	}
	public void setRid(Integer rid) {
		this.rid = rid;
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
	public Double getFs() {
		return fs;
	}
	public void setFs(Double fs) {
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
	public String getStatus() {
		if (null != status) {
			if (status == 0) {
				return "Pending";
			} else if (status == 1) {
				return "Approved";
			} else if (status == 2) {
				return "Failed";
			}
		}
		return "Pending";
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
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
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getImgUrl() {
		return imgUrl;
	}
	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}
	public List<String> getPhotosUrl() {
		return photosUrl;
	}
	public void setPhotosUrl(List<String> photosUrl) {
		this.photosUrl = photosUrl;
	}
	
}
