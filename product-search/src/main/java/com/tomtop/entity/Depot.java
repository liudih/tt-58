package com.tomtop.entity;

/**
 * 多仓库的对象
 * @author renyy
 *
 */
public class Depot extends BaseBean {

	 /**
	 * 
	 */
	private static final long serialVersionUID = -8028030337172178338L;
	
	private Integer depotId;//仓库Id
    private String depotName;//仓库名字
    private Integer lid;//物流Id
    private Integer status;//当前所在的仓库对应的状态
    private Integer qty;//当前所在的仓库对应的数量
    private String symbol = "$";
    private String nowprice;//现价格
    private String origprice;//原价
	private String saleEndDate = "";//促销结束时间 如果为空则没有在促销
	private Boolean freeShipping;//是否免邮
	
	public Depot(){
		
	}
	public Depot(Integer depotId,String depotName,Integer lid,Integer status,Integer qty,String symbol,String nowprice,String origprice,String saleEndDate,Boolean freeShipping){
		this.depotId = depotId;
		this.depotName = depotName;
		this.lid = lid;
		this.status = status;
		this.qty = qty;
		this.symbol = symbol;
		this.nowprice = nowprice;
		this.origprice = origprice;
		this.saleEndDate = saleEndDate;
		this.freeShipping = freeShipping;
	}
	
	public Integer getDepotId() {
		return depotId;
	}
	public void setDepotId(Integer depotId) {
		this.depotId = depotId;
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
	public String getSymbol() {
		return symbol;
	}
	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}
	public String getNowprice() {
		return nowprice;
	}
	public void setNowprice(String nowprice) {
		this.nowprice = nowprice;
	}
	public String getOrigprice() {
		return origprice;
	}
	public void setOrigprice(String origprice) {
		this.origprice = origprice;
	}
	public String getSaleEndDate() {
		return saleEndDate;
	}
	public void setSaleEndDate(String saleEndDate) {
		this.saleEndDate = saleEndDate;
	}
	public Boolean getFreeShipping() {
		return freeShipping;
	}
	public void setFreeShipping(Boolean freeShipping) {
		this.freeShipping = freeShipping;
	}
    
}
