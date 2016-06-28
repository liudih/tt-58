package com.tomtop.loyalty.models;

import java.io.Serializable;
import java.util.Date;

/**
 * 积分流水实体类
 * @author zhangxiangquan
 *
 */
public class IntegralModel implements Serializable{

	private static final long serialVersionUID = 1L;
	/**实体类的id*/
	private int id;
	/**站点id*/
	private int webSiteId;
	/**账号名称 */
	private String email;
	/**行为类型*/
	private String dotype;
	/**积分*/
	private int integralNum;
	/**创建时间**/
	private Date createDate;
	/**审核状态**/
	private int status;
	/**积分来源**/
	private String source;
	/**备注*/
	private String remark;
	
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getWebSiteId() {
		return webSiteId;
	}
	public void setWebSiteId(int webSiteId) {
		this.webSiteId = webSiteId;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getDotype() {
		return dotype;
	}
	public void setDotype(String dotype) {
		this.dotype = dotype;
	}
	public int getIntegralNum() {
		return integralNum;
	}
	public void setIntegralNum(int integralNum) {
		this.integralNum = integralNum;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	
	
}
