package com.tomtop.entry.bo;

import java.io.Serializable;

/**
 * 
 * @author Administrator
 *
 */
public class VolumeWeightBo implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7966439493140879712L;
	private double length;				  //长
	private double width;				  //高
	private double high;				  //高
	private double weight;				  //重量	
	private double volumeWeight;		  //体积重
	
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

	public double getWeight() {
		return weight;
	}

	public void setWeight(double weight) {
		this.weight = weight;
	}

	public double getVolumeWeight() {
		return volumeWeight;
	}

	public void setVolumeWeight(double volumeWeight) {
		this.volumeWeight = volumeWeight;
	}

	@Override
	public String toString() {
		return "VolumeWeightBo [length=" + length + ", width=" + width
				+ ", high=" + high + ", volumeWeight=" + volumeWeight + "]";
	}
	
	
}
