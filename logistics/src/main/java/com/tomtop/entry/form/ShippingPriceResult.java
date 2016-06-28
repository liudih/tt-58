package com.tomtop.entry.form;

import java.io.Serializable;

public class ShippingPriceResult  implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 5707898954629302909L;
	/**
	 * 
	 */
	
	private ShippingPriceResultItem surfaceResult;
	private ShippingPriceResultItem registResult;
	private ShippingPriceResultItem expressResult;
	private ShippingPriceResultItem specialResult;
	public ShippingPriceResultItem getSurfaceResult() {
		return surfaceResult;
	}
	public void setSurfaceResult(ShippingPriceResultItem surfaceResult) {
		this.surfaceResult = surfaceResult;
	}
	public ShippingPriceResultItem getRegistResult() {
		return registResult;
	}
	public void setRegistResult(ShippingPriceResultItem registResult) {
		this.registResult = registResult;
	}
	public ShippingPriceResultItem getExpressResult() {
		return expressResult;
	}
	public void setExpressResult(ShippingPriceResultItem expressResult) {
		this.expressResult = expressResult;
	}
	public ShippingPriceResultItem getSpecialResult() {
		return specialResult;
	}
	public void setSpecialResult(ShippingPriceResultItem specialResult) {
		this.specialResult = specialResult;
	}
	
	
}
