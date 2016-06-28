package com.tomtop.member.models.bo;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class InteractionPhotoBo implements Serializable {
	private static final long serialVersionUID = 1L;

	private Date dcreatedate;

	private Integer istate;

	private String productSmallImageUrl;

	private List<String> photosUrl;

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

	public List<String> getPhotosUrl() {
		return photosUrl;
	}

	public void setPhotosUrl(List<String> photosUrl) {
		this.photosUrl = photosUrl;
	}

	public String getProductUrl() {
		return productUrl;
	}

	public void setProductUrl(String productUrl) {
		this.productUrl = productUrl;
	}

	
}