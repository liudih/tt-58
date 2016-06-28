package com.tomtop.base.models.vo;

import java.net.URL;

public class VisitLogVo {

	public URL url;

	public Integer client;

	public String ip;

	public VisitLogVo() {

	}

	public URL getUrl() {
		return url;
	}

	public void setUrl(URL url) {
		this.url = url;
	}

	public Integer getClient() {
		return client;
	}

	public void setClient(Integer client) {
		this.client = client;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}
}
