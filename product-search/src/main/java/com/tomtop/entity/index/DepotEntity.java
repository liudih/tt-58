package com.tomtop.entity.index;

import java.util.ArrayList;
import java.util.List;


/**
 * 仓库实体类
 * @author ztiny
 * @Date 2015-12-19
 */
public class DepotEntity {
	//仓库ID
	private Integer depotid;
	//仓库名称
	private String depotName;
	//物流模板Id
	private Integer lid = 1;
	//商品状态
	private Integer status = 1;
	//商品库存
	private Integer qty = 0;
	//商品价格
	private Double price=0.00;
	//促销价
	private List<SalePrice> salePrice = new ArrayList<SalePrice>();
	//是否免邮商品
	private boolean isFreeShipping = false;
	//是否为清仓
	private boolean isClearStocks = false;
	//仓库是否免邮--免邮专区
	private String freeShip;
	//仓库是否清仓--清仓专区
	private String clearance;
	
	public DepotEntity() {

	}

	public DepotEntity(Integer depotid, String depotName) {
		this.depotid = depotid;
		this.depotName = depotName;
	}
	
	public DepotEntity(Integer depotid, String depotName,Integer lid,Integer status,Integer qty,Double price,List<SalePrice> salePrice,
			Boolean isFreeShipping,Boolean isClearStocks,String freeShip,String clearance) {
		this.depotid = depotid;
		this.depotName = depotName;
		this.lid = lid;
		this.status = status;
		this.qty = qty;
		this.price = price;
		this.salePrice = salePrice;
		this.isFreeShipping = isFreeShipping;
		this.isClearStocks = isClearStocks;
		this.freeShip = freeShip;
		this.clearance = clearance;
	}
	
	public Integer getDepotid() {
		return depotid;
	}
	public void setDepotid(Integer depotid) {
		this.depotid = depotid;
	}
	public String getDepotName() {
		return depotName;
	}
	public void setDepotName(String depotName) {
		this.depotName = depotName;
	}

	public Integer getLid() {
		return lid;
	}

	public void setLid(Integer lid) {
		this.lid = lid;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getQty() {
		return qty;
	}

	public void setQty(Integer qty) {
		this.qty = qty;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public List<SalePrice> getSalePrice() {
		return salePrice;
	}

	public void setSalePrice(List<SalePrice> salePrice) {
		this.salePrice = salePrice;
	}

	public boolean isFreeShipping() {
		return isFreeShipping;
	}

	public void setFreeShipping(boolean isFreeShipping) {
		this.isFreeShipping = isFreeShipping;
	}

	public boolean isClearStocks() {
		return isClearStocks;
	}

	public void setClearStocks(boolean isClearStocks) {
		this.isClearStocks = isClearStocks;
	}

	public String getFreeShip() {
		return freeShip;
	}

	public void setFreeShip(String freeShip) {
		this.freeShip = freeShip;
	}

	public String getClearance() {
		return clearance;
	}

	public void setClearance(String clearance) {
		this.clearance = clearance;
	}

}
