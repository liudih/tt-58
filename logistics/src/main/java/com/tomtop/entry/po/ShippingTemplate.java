package com.tomtop.entry.po;

import java.io.Serializable;
import java.security.Timestamp;
//模板类型
public class ShippingTemplate implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1498606109580663689L;
	private int id;
	//查询模板限制条件
	private String shippingTemplateId;	//模板iD
	private String country;				//国家
	private String countrySwitch;		//国家     0:包含    1:排除  2:全部
	private double startAmount;
	private double startWeight;
	private double amountLimit;			 //金额限制
	private double weightLimit;			 //重量限制
	
	
	private String templateName;		//模板名称
	private int shippingTypeId;
	private String typeName;			//模板类型
	private int warehouse;				//仓库
	private String filter;				//筛选规则   
	private int filterId;				//过滤条件规则
	private String priorityShippingCode;//优先发货代码
	private boolean whetherSpecial;		//是否为特殊品
	private boolean whetherFreeshipping; //是否免邮
	private Double extraCharge;			 //附加费用 以美元为单位	
	private String extraChargeNote;		 //附加说明
	private boolean  isCalculateWeight;
	
	private String status;				 //状态  0：不可用  1：可用
	private String updateBy;			 //更新人	
	private Timestamp updateDate;		 //更新时间
	private String createBy;			 //创建人
	private Timestamp createDate;		 //创建时间
	private int templateOrder;

	public int getTemplateOrder() {
		return templateOrder;
	}


	public void setTemplateOrder(int templateOrder) {
		this.templateOrder = templateOrder;
	}


	public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
	}


	public String getShippingTemplateId() {
		return shippingTemplateId;
	}


	public void setShippingTemplateId(String shippingTemplateId) {
		this.shippingTemplateId = shippingTemplateId;
	}


	public String getCountry() {
		return country;
	}


	public void setCountry(String country) {
		this.country = country;
	}


	public String getCountrySwitch() {
		return countrySwitch;
	}


	public void setCountrySwitch(String countrySwitch) {
		this.countrySwitch = countrySwitch;
	}


	public double getStartAmount() {
		return startAmount;
	}


	public void setStartAmount(double startAmount) {
		this.startAmount = startAmount;
	}


	public double getStartWeight() {
		return startWeight;
	}


	public void setStartWeight(double startWeight) {
		this.startWeight = startWeight;
	}


	public double getAmountLimit() {
		return amountLimit;
	}


	public void setAmountLimit(double amountLimit) {
		this.amountLimit = amountLimit;
	}


	public double getWeightLimit() {
		return weightLimit;
	}


	public void setWeightLimit(double weightLimit) {
		this.weightLimit = weightLimit;
	}


	public String getTemplateName() {
		return templateName;
	}


	public void setTemplateName(String templateName) {
		this.templateName = templateName;
	}


	public int getShippingTypeId() {
		return shippingTypeId;
	}


	public void setShippingTypeId(int shippingTypeId) {
		this.shippingTypeId = shippingTypeId;
	}


	public String getTypeName() {
		return typeName;
	}


	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}


	public int getWarehouse() {
		return warehouse;
	}


	public void setWarehouse(int warehouse) {
		this.warehouse = warehouse;
	}


	public String getFilter() {
		return filter;
	}


	public void setFilter(String filter) {
		this.filter = filter;
	}


	public int getFilterId() {
		return filterId;
	}


	public void setFilterId(int filterId) {
		this.filterId = filterId;
	}


	public String getPriorityShippingCode() {
		return priorityShippingCode;
	}


	public void setPriorityShippingCode(String priorityShippingCode) {
		this.priorityShippingCode = priorityShippingCode;
	}


	public boolean isWhetherSpecial() {
		return whetherSpecial;
	}


	public void setWhetherSpecial(boolean whetherSpecial) {
		this.whetherSpecial = whetherSpecial;
	}


	public boolean isWhetherFreeshipping() {
		return whetherFreeshipping;
	}


	public void setWhetherFreeshipping(boolean whetherFreeshipping) {
		this.whetherFreeshipping = whetherFreeshipping;
	}


	public Double getExtraCharge() {
		return extraCharge;
	}


	public void setExtraCharge(Double extraCharge) {
		this.extraCharge = extraCharge;
	}


	public String getExtraChargeNote() {
		return extraChargeNote;
	}


	public void setExtraChargeNote(String extraChargeNote) {
		this.extraChargeNote = extraChargeNote;
	}


	public boolean isCalculateWeight() {
		return isCalculateWeight;
	}


	public void setCalculateWeight(boolean isCalculateWeight) {
		this.isCalculateWeight = isCalculateWeight;
	}


	public String getStatus() {
		return status;
	}


	public void setStatus(String status) {
		this.status = status;
	}


	public String getUpdateBy() {
		return updateBy;
	}


	public void setUpdateBy(String updateBy) {
		this.updateBy = updateBy;
	}


	public Timestamp getUpdateDate() {
		return updateDate;
	}


	public void setUpdateDate(Timestamp updateDate) {
		this.updateDate = updateDate;
	}


	public String getCreateBy() {
		return createBy;
	}


	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}


	public Timestamp getCreateDate() {
		return createDate;
	}


	public void setCreateDate(Timestamp createDate) {
		this.createDate = createDate;
	}


	@Override
	public String toString() {
		return "ShippingTemplate [id=" + id + ", shippingTemplateId="
				+ shippingTemplateId + ", country=" + country
				+ ", countrySwitch=" + countrySwitch + ", startAmount="
				+ startAmount + ", startWeight=" + startWeight
				+ ", amountLimit=" + amountLimit + ", weightLimit="
				+ weightLimit + ", templateName=" + templateName
				+ ", shippingTypeId=" + shippingTypeId + ", typeName="
				+ typeName + ", warehouse=" + warehouse + ", filter=" + filter
				+ ", filterId=" + filterId + ", priorityShippingCode="
				+ priorityShippingCode + ", whetherSpecial=" + whetherSpecial
				+ ", whetherFreeshipping=" + whetherFreeshipping
				+ ", extraCharge=" + extraCharge + ", extraChargeNote="
				+ extraChargeNote + ", isCalculateWeight=" + isCalculateWeight
				+ ", status=" + status + ", updateBy=" + updateBy
				+ ", updateDate=" + updateDate + ", createBy=" + createBy
				+ ", createDate=" + createDate + ", templateOrder="
				+ templateOrder + "]";
	}


	
}
