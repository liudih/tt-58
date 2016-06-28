package com.tomtop.entry.po;

import java.io.Serializable;
import java.util.Arrays;

public class ShippingCalculateBase implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4814756843418571192L;
	//private ShippingMethod shippingMethod;//运输方式
	private String sku;					  //SKU
	private int templateId;			  //模板ID
	private boolean isSpecial;			  //对应模板是否为特殊模板
	private String[] chrd;				  //捆绑结构数据
	private int qty;	
	//private double price;				  //单价	
	private double weight;				  //重量	
	private double length;				  //长
	private double width;				  //高
	private double high;				  //高	
	private double volumeWeight;		  //体积重
		

	public String getSku() {
		return sku;
	}

	public void setSku(String sku) {
		this.sku = sku;
	}



	public int getTemplateId() {
		return templateId;
	}

	public void setTemplateId(int templateId) {
		this.templateId = templateId;
	}

	public boolean isSpecial() {
		return isSpecial;
	}

	public void setSpecial(boolean isSpecial) {
		this.isSpecial = isSpecial;
	}

	public String[] getChrd() {
		return chrd;
	}

	public void setChrd(String[] chrd) {
		this.chrd = chrd;
	}

	public int getQty() {
		return qty;
	}

	public void setQty(int qty) {
		this.qty = qty;
	}

	public double getWeight() {
		return weight;
	}

	public void setWeight(double weight) {
		this.weight = weight;
	}

	public double getLength() {
		return length;
	}

	public void setLength(double length) {
		this.length = length;
	}

	public double getWidth() {
		return width;
	}

	public void setWidth(double width) {
		this.width = width;
	}

	public double getHigh() {
		return high;
	}

	public void setHigh(double high) {
		this.high = high;
	}

	public double getVolumeWeight() {
		return volumeWeight;
	}

	public void setVolumeWeight(double volumeWeight) {
		this.volumeWeight = volumeWeight;
	}

	@Override
	public String toString() {
		return "ShippingCalculateBase [sku=" + sku + ", templateId="
				+ templateId + ", isSpecial=" + isSpecial + ", chrd="
				+ Arrays.toString(chrd) + ", qty=" + qty + ", weight=" + weight
				+ ", length=" + length + ", width=" + width + ", high=" + high
				+ ", volumeWeight=" + volumeWeight + "]";
	}


	
}
