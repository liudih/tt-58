package com.tomtop.entity.rc;

import java.util.Date;

import com.tomtop.entity.ProductBase;
/**
 * 五星置顶最新评论对象
 * @author renyy
 *
 */
public class FiveReview extends ProductBase {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8116343388200040982L;
	private String comment;//评论内容
	private Integer stars;//星星
	private Date createDate;//创建时间
	private String countryName;//国家名称
	private String title;//标题
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public Integer getStars() {
		return stars;
	}
	public void setStars(Integer stars) {
		this.stars = stars;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public String getCountryName() {
		return countryName;
	}
	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	
}
