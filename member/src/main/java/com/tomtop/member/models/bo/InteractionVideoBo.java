package com.tomtop.member.models.bo;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.tomtop.member.models.dto.InteractionProductMemberVideo;

public class InteractionVideoBo implements Serializable {
	private static final long serialVersionUID = 1L;

	private Date dcreatedate;

	private Integer istate;

	private String productSmallImageUrl;

	private List<InteractionProductMemberVideo> videos;

	private String productUrl;
	 

	public Date getDcreatedate() {
		return dcreatedate;
	}

	public void setDcreatedate(Date dcreatedate) {
		this.dcreatedate = dcreatedate;
	}

	public String getIstate() {
		if (null != istate) {
			if (istate == 0) {
				return "Pending";
			} else if (istate == 1) {
				return "Approved";
			} else if (istate == 2) {
				return "Failed";
			}
		}
		return "Pending";
	}

	public void setIstate(Integer istate) {
		this.istate = istate;
	}

	public String getProductSmallImageUrl() {
		return productSmallImageUrl;
	}

	public void setProductSmallImageUrl(String productSmallImageUrl) {
		this.productSmallImageUrl = productSmallImageUrl;
	}
	 
	public List<InteractionProductMemberVideo> getVideos() {
		return videos;
	}

	public void setVideos(List<InteractionProductMemberVideo> videos) {
		this.videos = videos;
	}

	public String getProductUrl() {
		return productUrl;
	}

	public void setProductUrl(String productUrl) {
		this.productUrl = productUrl;
	}

	
}