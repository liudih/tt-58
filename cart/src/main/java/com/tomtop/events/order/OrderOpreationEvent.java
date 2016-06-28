package com.tomtop.events.order;

import java.util.Date;

/**
 * 订单操作日志时间
 * 
 * @author liuchengqiang
 * @Date 2016-06-01
 */
public class OrderOpreationEvent {

	// 站点
	private int iwebsiteid;
	// 操作人
	private String cemail;
	//订单号
	private String cordernumber;
	// 操作类型
	private OpreationType copreation;
	// 操作时间
	private Date ccreatetime;
	// 操作明细
	private String ccontent;
	//email for paypal
	private String cmemberemail;
	// 操作结果
	private OpreationResult cresult = OpreationResult.SUCCESS;
	//域名
	private String vhost;
	
	public OrderOpreationEvent(OpreationType copreation,
			OpreationResult result, String content) {
		this.copreation = copreation;
		this.cresult = result;
		this.ccontent = content;
	}
	
	public OrderOpreationEvent(OpreationType copreation,
			OpreationResult result, String content,String ordernumber) {
		this.copreation = copreation;
		this.cresult = result;
		this.ccontent = content;
		this.cordernumber = ordernumber;
	}
	
	public OrderOpreationEvent(int iwebsiteid, String cemail,String cordernumber,
			OpreationType copreation) {
		this.iwebsiteid = iwebsiteid;
		this.cemail = cemail;
		this.cordernumber = cordernumber;
		this.copreation = copreation;
	}

	public OrderOpreationEvent(int iwebsiteid, String cemail,String cordernumber,
			OpreationType copreation, String ccontent) {
		this.iwebsiteid = iwebsiteid;
		this.cemail = cemail;
		this.cordernumber = cordernumber;
		this.copreation = copreation;
		this.ccontent = ccontent;
	}

	
	public OrderOpreationEvent(int iwebsiteid, String cemail,String cordernumber,
			OpreationType copreation, String ccontent,String cmemberemail) {
		this.iwebsiteid = iwebsiteid;
		this.cemail = cemail;
		this.cordernumber = cordernumber;
		this.copreation = copreation;
		this.ccontent = ccontent;
		this.cmemberemail = cmemberemail;
	}
	
	public int getIwebsiteid() {
		return iwebsiteid;
	}

	public void setIwebsiteid(int iwebsiteid) {
		this.iwebsiteid = iwebsiteid;
	}

	public String getCemail() {
		return cemail;
	}

	public void setCemail(String cemail) {
		this.cemail = cemail;
	}

	public String getCordernumber() {
		return cordernumber;
	}

	public void setCordernumber(String cordernumber) {
		this.cordernumber = cordernumber;
	}

	public OpreationType getCopreation() {
		return copreation;
	}

	public void setCopreation(OpreationType copreation) {
		this.copreation = copreation;
	}

	public Date getCcreatetime() {
		return ccreatetime;
	}

	public void setCcreatetime(Date ccreatetime) {
		this.ccreatetime = ccreatetime;
	}

	public String getCcontent() {
		return ccontent;
	}

	public void setCcontent(String ccontent) {
		this.ccontent = ccontent;
	}

	public OpreationResult getCresult() {
		return cresult;
	}

	public void setCresult(OpreationResult cresult) {
		this.cresult = cresult;
	}

	
	public String getCmemberemail() {
		return cmemberemail;
	}

	public void setCmemberemail(String cmemberemail) {
		this.cmemberemail = cmemberemail;
	}

	public String getVhost() {
		return vhost;
	}

	public void setVhost(String vhost) {
		this.vhost = vhost;
	}
	
	@Override
	public String toString() {
		return "OrderOpreationEvent [iwebsiteid=" + iwebsiteid + ", cemail="
				+ cemail + ", cordernumber=" + cordernumber + ", copreation="
				+ copreation + ", ccreatetime=" + ccreatetime + ", ccontent="
				+ ccontent + ", cmemberemail=" + cmemberemail + ", cresult="
				+ cresult + ", vhost=" + vhost + "]";
	}

	public enum OpreationType {
		EC_PAY_REQUEST("1","EC支付请求"),
		EC_ORDER_CONFIRM("2","EC支付订单确认"),
		EC_PAY_CONFIRM("3","EC支付支付确认"),
		PAYPAL_REQUEST("4","Paypal支付请求"),
		PAYPAL_DOPAYMENT("5","Paypal支付确认"),
		OCEAN_REQUEST("6","钱海支付请求"),
		OCEAN_RESPONSE("7","钱海支付确认"),
		CREATE_ORDER("8", "创建订单"),
		LOAD_SHIPMETHOD("9", "获取物流方式"),
		GET_PAYPAL_DETAIL("10", "获取Paypal信息"),
		GET_COUPON("11", "获取可用优惠券"),
		GET_POINT("12", "获取可用积分"),
		APPLY_COUPON("13", "使用优惠券"),
		APPLY_PROMO("14", "使用促销码"),
		APPLY_POINT("15", "使用积分"),
		LOCK_DISCOUNT("16", "锁定优惠"),
		GLEEPAY_REQUEST("17", "鼎付支付请求"),
		GLEEPAY_RESPONSE("18", "鼎付支付确认");

		private String code;
		private String cname;

		private OpreationType(String code, String cname) {
			this.code = code;
			this.cname = cname;
		}

		public String getCode() {
			return code;
		}

		public void setCode(String code) {
			this.code = code;
		}

		public String getCname() {
			return cname;
		}

		public void setCname(String cname) {
			this.cname = cname;
		}
	}
	
	public enum OpreationResult {
		SUCCESS("1", "操作成功"), FAILURE("0", "操作失败"),NONE("2", "无结果"),INVALID("3", "无效的");

		private String code;
		private String cname;

		private OpreationResult(String code, String cname) {
			this.code = code;
			this.cname = cname;
		}

		public String getCode() {
			return code;
		}

		public void setCode(String code) {
			this.code = code;
		}

		public String getCname() {
			return cname;
		}

		public void setCname(String cname) {
			this.cname = cname;
		}
	}
}
