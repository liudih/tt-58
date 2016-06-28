package com.tomtop.member.models.dto;


import java.io.Serializable;
import java.util.Date;
import java.util.List;


public class ReviewsInMemberCenter implements Serializable  {
	private static final long serialVersionUID = 1L;

	private Integer commentid;

	private String ccomment;

	private Integer iprice;

	private Integer iquality;

	private Integer ishipping;

	private Integer iusefulness;

	private Double foverallrating;

	private Date dcreatedate;

	private Integer istate;

	private String productSmallImageUrl;

	private List<String> commentPhotosUrl;

	private String commentVideoUrl;

	private String productUrl;
	
	private String videoTitle;
	private String sku;
	private String listingId;

	
	public Date getDcreatedate() {
		return dcreatedate;
	}

	public void setDcreatedate(Date dcreatedate) {
		this.dcreatedate = dcreatedate;
	}

	public ReviewsInMemberCenter(Integer commentid, String ccomment,
			Integer iprice, Integer iquality, Integer ishipping,
			Integer iusefulness, Double foverallrating, Date dcreatedate,
			Integer istate, String productSmallImageUrl,
			List<String> commentPhotosUrl, String commentVideoUrl,
			String productUrl) {
		super();
		this.commentid = commentid;
		this.ccomment = ccomment;
		this.iprice = iprice;
		this.iquality = iquality;
		this.ishipping = ishipping;
		this.iusefulness = iusefulness;
		this.foverallrating = foverallrating;
		this.dcreatedate = dcreatedate;
		this.istate = istate;
		this.productSmallImageUrl = productSmallImageUrl;
		this.commentPhotosUrl = commentPhotosUrl;
		this.commentVideoUrl = commentVideoUrl;
		this.productUrl = productUrl;
	}
	public ReviewsInMemberCenter(Integer commentid, String ccomment,
			Integer iprice, Integer iquality, Integer ishipping,
			Integer iusefulness, Double foverallrating, Date dcreatedate,
			Integer istate, String productSmallImageUrl,
			List<String> commentPhotosUrl, String commentVideoUrl,
			String productUrl, String sku, String videoTitle, String listingId) {
		super();
		this.commentid = commentid;
		this.ccomment = ccomment;
		this.iprice = iprice;
		this.iquality = iquality;
		this.ishipping = ishipping;
		this.iusefulness = iusefulness;
		this.foverallrating = foverallrating;
		this.dcreatedate = dcreatedate;
		this.istate = istate;
		this.productSmallImageUrl = productSmallImageUrl;
		this.commentPhotosUrl = commentPhotosUrl;
		this.commentVideoUrl = commentVideoUrl;
		this.productUrl = productUrl;
		this.sku = sku;
		this.videoTitle = videoTitle;
		this.listingId = listingId;
	}

	public Integer getCommentid() {
		return commentid;
	}

	public String getCcomment() {
		return ccomment;
	}

	public List<String> getCommentPhotosUrl() {
		return commentPhotosUrl;
	}

	public String getCommentVideoUrl() {
		return commentVideoUrl;
	}

	public String getProductSmallImageUrl() {
		return productSmallImageUrl;
	}

	public Integer getIpriceStarWidth() {
		if (null != iprice) {
			return iprice;
		}
		return 5;
	}

	public double getFoverallratingStarWidth() {
		if (null != foverallrating) {
			return foverallrating;
		}
		return 5;
	}

	public Integer getIqualityStarWidth() {
		if (null != iquality) {
			return iquality;
		}
		return 5;
	}

	public Integer getIshippingStarWidth() {
		if (null != ishipping) {
			return ishipping;
		}
		return 5;
	}

	public Integer getIusefulness() {
		if (null != iusefulness) {
			return iusefulness;
		}
		return 5;
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
	public String getProductUrl() {
		return productUrl;
	}

	public String getVideoTitle() {
		return videoTitle;
	}

	public void setVideoTitle(String videoTitle) {
		this.videoTitle = videoTitle;
	}

	public String getSku() {
		return sku;
	}

	public void setSku(String sku) {
		this.sku = sku;
	}

	public String getListingId() {
		return listingId;
	}

	public void setListingId(String listingId) {
		this.listingId = listingId;
	}
	
	
}