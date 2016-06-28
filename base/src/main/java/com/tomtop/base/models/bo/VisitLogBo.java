package com.tomtop.base.models.bo;

import java.io.Serializable;

import com.tomtop.base.utils.CommonUtils;

/**
 * 访问记录业务实体类
 * 
 * @author shuliangxing
 *
 * @date 2016年5月6日 下午3:34:45
 */
public class VisitLogBo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4191320530111517572L;

	/**
	 * aid
	 */
	public String aid;

	/**
	 * 站点标识
	 */
	public Integer website;

	/**
	 * 用户ip
	 */
	public String uip;

	/**
	 * 访问路径
	 */
	public String path;

	/**
	 * 来源
	 */
	public String source;

	public VisitLogBo() {

	}

	public VisitLogBo(String aid, Integer website, String uip, String path,
			String source) {
		this.aid = aid;
		this.website = website;
		this.uip = uip;
		this.path = path;
		this.source = source;
	}

	public String getAid() {
		return CommonUtils.filterAndSubstring(aid, 200);
	}

	public void setAid(String aid) {
		this.aid = aid;
	}

	public Integer getWebsite() {
		// 站点标识 默认为1
		if (website == null) {
			return 1;
		}
		return website;
	}

	public void setWebsite(Integer website) {
		this.website = website;
	}

	public String getUip() {
		return CommonUtils.filterAndSubstring(uip, 100);
	}

	public void setUip(String uip) {
		this.uip = uip;
	}

	public String getPath() {
		return CommonUtils.filterAndSubstring(path, 2000);
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getSource() {
		return CommonUtils.filterAndSubstring(source, 200);
	}

	public void setSource(String source) {
		this.source = source;
	}

	@Override
	public String toString() {
		return "VisitLogBo [aid=" + aid + ", website=" + website + ", uip="
				+ uip + ", path=" + path + ", source=" + source + "]";
	}

}
