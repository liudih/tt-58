package com.tomtop.entity;

import java.io.Serializable;

public class NewArrivalsAgg implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5888655047197794625L;
	private String dateStr;
	private String dateName;
	private Integer num;
	
	public String getDateStr() {
		dateStr = dateStr.replace(" 00:00:00", "");
		if(dateStr.indexOf("MOT") == -1){
			dateStr = "EQU" + dateStr;
		}
		return dateStr;
	}
	public void setDateStr(String dateStr) {
		this.dateStr = dateStr;
	}
	public String getDateName() {
		dateName = dateName.replace(" 00:00:00", "");
		return dateName;
	}
	public void setDateName(String dateName) {
		this.dateName = dateName;
	}
	public Integer getNum() {
		return num;
	}
	public void setNum(Integer num) {
		this.num = num;
	}
}
