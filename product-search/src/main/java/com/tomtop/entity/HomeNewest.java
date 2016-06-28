package com.tomtop.entity;

import java.io.Serializable;
import java.util.List;

public class HomeNewest implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1039530041470737874L;
	private List<HomeNewestImage> image;
	private List<HomeNewestReview> review;
	private List<HomeNewestVideo> video;
	
	public HomeNewest(List<HomeNewestImage> image,List<HomeNewestReview> review,List<HomeNewestVideo> video){
		this.image = image;
		this.review = review;
		this.video = video;
	}
	public HomeNewest(){
		
	}
	public List<HomeNewestImage> getImage() {
		return image;
	}
	public void setImage(List<HomeNewestImage> image) {
		this.image = image;
	}
	public List<HomeNewestReview> getReview() {
		return review;
	}
	public void setReview(List<HomeNewestReview> review) {
		this.review = review;
	}
	public List<HomeNewestVideo> getVideo() {
		return video;
	}
	public void setVideo(List<HomeNewestVideo> video) {
		this.video = video;
	}
	
}
