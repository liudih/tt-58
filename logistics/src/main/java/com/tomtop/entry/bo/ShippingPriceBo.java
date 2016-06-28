package com.tomtop.entry.bo;

import java.io.Serializable;
import java.util.List;
import java.util.Map;


public class ShippingPriceBo implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -362938783939643666L;
	private String surfaceMapWeight; //体积重决定平邮是否计算  N:不决定   Y：决定
	private boolean surfaceShow;//平邮是否显示
	private boolean isSurfaceStrack;//平邮是否显示
	private Double surfacePrice;	//平邮
	private String surfaceCode;	//特快码
	private String surfaceMapRegist; //平邮决定挂号是否计算  N:不决定   Y：决定
	private String surfaceTitle;
	private String surfaceDescription;
	private int surfaceCodeId;
	private int surfaceOrder;
	private Map<String,String> surfaceCodeMap;
	
	private boolean registShow; //挂号是否显示
	private boolean isRegistStrack;//挂号是否可追踪
	private Double registPrice;	//挂号
	private String registCode;	//特快码
	private String registTitle;
	private String registDescription;
	private int registOrder;
	private int registCodeId;
	private Map<String,String> registCodeMap;
	
	private boolean expressShow;//快递是否显示
	private boolean isExpressStrack;//快递是否可追踪
	private Double expressPrice;	//快递
	private String expressCode;	//特快码
	private String expressTitle;
	private String expressDescription;
	private int expressOrder;
	private int expressCodeId;
	private Map<String,String> expressCodeMap;
	
	private boolean specialShow;//特快是否显示
	private boolean isSpecialStrack;//特快是否可追踪
	private Double specialPrice;	//特快
	private String specialCode;	//特快码
	private String specialTitle;
	private String specialDescription;
	private int specialOrder;
	private int specialCodeId;
	private Map<String,String> specialCodeMap;
	
	public Map<String, String> getSurfaceCodeMap() {
		return surfaceCodeMap;
	}
	public void setSurfaceCodeMap(Map<String, String> surfaceCodeMap) {
		this.surfaceCodeMap = surfaceCodeMap;
	}
	public Map<String, String> getRegistCodeMap() {
		return registCodeMap;
	}
	public void setRegistCodeMap(Map<String, String> registCodeMap) {
		this.registCodeMap = registCodeMap;
	}
	public Map<String, String> getExpressCodeMap() {
		return expressCodeMap;
	}
	public void setExpressCodeMap(Map<String, String> expressCodeMap) {
		this.expressCodeMap = expressCodeMap;
	}
	public Map<String, String> getSpecialCodeMap() {
		return specialCodeMap;
	}
	public void setSpecialCodeMap(Map<String, String> specialCodeMap) {
		this.specialCodeMap = specialCodeMap;
	}
	public int getSurfaceCodeId() {
		return surfaceCodeId;
	}
	public void setSurfaceCodeId(int surfaceCodeId) {
		this.surfaceCodeId = surfaceCodeId;
	}
	public int getSurfaceOrder() {
		return surfaceOrder;
	}
	public void setSurfaceOrder(int surfaceOrder) {
		this.surfaceOrder = surfaceOrder;
	}
	public int getRegistOrder() {
		return registOrder;
	}
	public void setRegistOrder(int registOrder) {
		this.registOrder = registOrder;
	}
	public int getRegistCodeId() {
		return registCodeId;
	}
	public void setRegistCodeId(int registCodeId) {
		this.registCodeId = registCodeId;
	}
	public int getExpressOrder() {
		return expressOrder;
	}
	public void setExpressOrder(int expressOrder) {
		this.expressOrder = expressOrder;
	}
	public int getExpressCodeId() {
		return expressCodeId;
	}
	public void setExpressCodeId(int expressCodeId) {
		this.expressCodeId = expressCodeId;
	}
	public int getSpecialOrder() {
		return specialOrder;
	}
	public void setSpecialOrder(int specialOrder) {
		this.specialOrder = specialOrder;
	}
	public int getSpecialCodeId() {
		return specialCodeId;
	}
	public void setSpecialCodeId(int specialCodeId) {
		this.specialCodeId = specialCodeId;
	}
	public boolean isSurfaceStrack() {
		return isSurfaceStrack;
	}
	public void setSurfaceStrack(boolean isSurfaceStrack) {
		this.isSurfaceStrack = isSurfaceStrack;
	}
	public boolean isRegistStrack() {
		return isRegistStrack;
	}
	public void setRegistStrack(boolean isRegistStrack) {
		this.isRegistStrack = isRegistStrack;
	}
	public boolean isExpressStrack() {
		return isExpressStrack;
	}
	public void setExpressStrack(boolean isExpressStrack) {
		this.isExpressStrack = isExpressStrack;
	}
	public boolean isSpecialStrack() {
		return isSpecialStrack;
	}
	public void setSpecialStrack(boolean isSpecialStrack) {
		this.isSpecialStrack = isSpecialStrack;
	}
	public String getSurfaceTitle() {
		return surfaceTitle;
	}
	public void setSurfaceTitle(String surfaceTitle) {
		this.surfaceTitle = surfaceTitle;
	}
	public String getSurfaceDescription() {
		return surfaceDescription;
	}
	public void setSurfaceDescription(String surfaceDescription) {
		this.surfaceDescription = surfaceDescription;
	}
	public String getRegistTitle() {
		return registTitle;
	}
	public void setRegistTitle(String registTitle) {
		this.registTitle = registTitle;
	}
	public String getRegistDescription() {
		return registDescription;
	}
	public void setRegistDescription(String registDescription) {
		this.registDescription = registDescription;
	}
	public String getExpressTitle() {
		return expressTitle;
	}
	public void setExpressTitle(String expressTitle) {
		this.expressTitle = expressTitle;
	}
	public String getExpressDescription() {
		return expressDescription;
	}
	public void setExpressDescription(String expressDescription) {
		this.expressDescription = expressDescription;
	}
	public String getSpecialTitle() {
		return specialTitle;
	}
	public void setSpecialTitle(String specialTitle) {
		this.specialTitle = specialTitle;
	}
	public String getSpecialDescription() {
		return specialDescription;
	}
	public void setSpecialDescription(String specialDescription) {
		this.specialDescription = specialDescription;
	}
	public String getSurfaceMapWeight() {
		return surfaceMapWeight;
	}
	public void setSurfaceMapWeight(String surfaceMapWeight) {
		this.surfaceMapWeight = surfaceMapWeight;
	}
	public String getSurfaceMapRegist() {
		return surfaceMapRegist;
	}
	public void setSurfaceMapRegist(String surfaceMapRegist) {
		this.surfaceMapRegist = surfaceMapRegist;
	}
	public String getSurfaceCode() {
		return surfaceCode;
	}
	public void setSurfaceCode(String surfaceCode) {
		this.surfaceCode = surfaceCode;
	}
	public String getRegistCode() {
		return registCode;
	}
	public void setRegistCode(String registCode) {
		this.registCode = registCode;
	}
	public String getExpressCode() {
		return expressCode;
	}
	public void setExpressCode(String expressCode) {
		this.expressCode = expressCode;
	}
	public String getSpecialCode() {
		return specialCode;
	}
	public void setSpecialCode(String specialCode) {
		this.specialCode = specialCode;
	}
	public boolean isSurfaceShow() {
		return surfaceShow;
	}
	public void setSurfaceShow(boolean surfaceShow) {
		this.surfaceShow = surfaceShow;
	}
	public boolean isRegistShow() {
		return registShow;
	}
	public void setRegistShow(boolean registShow) {
		this.registShow = registShow;
	}
	public boolean isExpressShow() {
		return expressShow;
	}
	public void setExpressShow(boolean expressShow) {
		this.expressShow = expressShow;
	}
	public boolean isSpecialShow() {
		return specialShow;
	}
	public void setSpecialShow(boolean specialShow) {
		this.specialShow = specialShow;
	}
	public Double getSurfacePrice() {
		return surfacePrice;
	}
	public void setSurfacePrice(Double surfacePrice) {
		this.surfacePrice = surfacePrice;
	}
	public Double getRegistPrice() {
		return registPrice;
	}
	public void setRegistPrice(Double registPrice) {
		this.registPrice = registPrice;
	}
	public Double getExpressPrice() {
		return expressPrice;
	}
	public void setExpressPrice(Double expressPrice) {
		this.expressPrice = expressPrice;
	}
	public Double getSpecialPrice() {
		return specialPrice;
	}
	public void setSpecialPrice(Double specialPrice) {
		this.specialPrice = specialPrice;
	}
	@Override
	public String toString() {
		return "ShippingPriceBo [surfaceMapWeight=" + surfaceMapWeight
				+ ", surfaceShow=" + surfaceShow + ", surfacePrice="
				+ surfacePrice + ", surfaceCode=" + surfaceCode
				+ ", surfaceMapRegist=" + surfaceMapRegist + ", surfaceTitle="
				+ surfaceTitle + ", surfaceDescription=" + surfaceDescription
				+ ", registShow=" + registShow + ", registPrice=" + registPrice
				+ ", registCode=" + registCode + ", registTitle=" + registTitle
				+ ", registDescription=" + registDescription + ", expressShow="
				+ expressShow + ", expressPrice=" + expressPrice
				+ ", expressCode=" + expressCode + ", expressTitle="
				+ expressTitle + ", expressDescription=" + expressDescription
				+ ", specialShow=" + specialShow + ", specialPrice="
				+ specialPrice + ", specialCode=" + specialCode
				+ ", specialTitle=" + specialTitle + ", specialDescription="
				+ specialDescription + "]";
	}
	
	
}
