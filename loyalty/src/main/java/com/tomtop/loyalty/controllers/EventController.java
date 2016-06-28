package com.tomtop.loyalty.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.tomtop.framework.core.utils.Result;
import com.tomtop.loyalty.configuration.mybatis.BaseMybatisConfig;
import com.tomtop.loyalty.models.Coupon;
import com.tomtop.loyalty.models.MemberEvent;
import com.tomtop.loyalty.models.OrderPoint;
import com.tomtop.loyalty.models.Point;
import com.tomtop.loyalty.service.ICouponService;
import com.tomtop.loyalty.service.IPointService;
import com.tomtop.loyalty.service.IPreferRuleService;
import com.tomtop.loyalty.service.impl.ThirdService;
import com.tomtop.loyalty.utils.CommonUtils;

@RestController
@RequestMapping(value = "/loyalty")
public class EventController {
	@Autowired
	ThirdService thirdService;
	@Autowired
	IPointService pointService;
	@Value("${upload-photo}")
	private Integer uploadphoto;
	@Value("${eventReview}")
	private Integer eventReview;
	@Value("${eventActivate}")
	private Integer eventActivate;
	@Value("${eventSubscribe}")
	private Integer eventSubscribe;
	
	@Autowired
	private ICouponService couponService ;
	@Autowired
	private IPreferRuleService preferRuleService ;

	//chiuu激活送优惠券名
	private static final String activateSentCouponsName = "CHICUU35%OFF";
	//tomtop激活送优惠券名
	private static final String activateSentCouponsName_tt = "CHICUU35%OFF";
	
	//chiuu邮件订阅送优惠券名
	private static final String subscribeSentCoupons = "CHICUU35%OFF";
	//tomtop邮件订阅送优惠券名
	private static final String subscribeSentCoupons_tt = "CHICUU35%OFF";

	private static Logger log = LoggerFactory
			.getLogger(EventController.class);
	/**
	 * 激活
	 * 
	 * @return
	 */
	@ResponseBody
	@RequestMapping(method = RequestMethod.POST, value = "/v1/event/activate")
	public Result activate(@RequestBody MemberEvent activeEvent) {
		Result res = new Result();
		String remark = "signup-points";
		String type = "signup";
		String source = "register";
		Integer point = eventActivate;
		Boolean isSend = pointService.givingPoint(activeEvent, point, type,
				remark, source);
		if (isSend) {
			res.setRet(CommonUtils.SUCCESS_RES);
			return res;
		} else {
			res.setRet(CommonUtils.ERROR_RES);
			return res;
		}

	}
	
	/**
	 * 
	  * @Description: 注册激活送优惠券
	  * @param @param activeEvent
	  * @param @return  
	  * @return Result  
	  * @author liuyufeng 
	  * @date 2016年4月15日 下午4:54:41
	 */
	@ResponseBody
	@RequestMapping(method = RequestMethod.POST, value = "/v1/event/activateSentCoupons")
	public Result activateSentCoupons(@RequestBody MemberEvent activeEvent) {
		Result res = new Result();
		String couponsName = (10 ==activeEvent.getWebsite())?activateSentCouponsName:activateSentCouponsName_tt;
		Integer ruleId = preferRuleService.getRuleIidByName(couponsName);//规则ID
		Coupon coupon = couponService.createCouponByRuleid(activeEvent.getEmail(), ruleId,
				activeEvent.getWebsite());
		if (coupon!=null) {
			res.setRet(CommonUtils.SUCCESS_RES);
			return res;
		} else {
			res.setRet(CommonUtils.ERROR_RES);
			return res;
		}
		
	}
	
	
	/**
	 * 
	 * @Description: 邮件订阅送优惠券
	 * @param @param activeEvent
	 * @param @return  
	 * @return Result  
	 * @author liuyufeng 
	 * @date 2016年4月15日 下午4:54:41
	 */
	@ResponseBody
	@RequestMapping(method = RequestMethod.POST, value = "/v1/event/subscribeSentCoupons")
	public Result subscribeSentCoupons(@RequestBody MemberEvent activeEvent) {
		Result res = new Result();
		String couponsName = (10 ==activeEvent.getWebsite())?subscribeSentCoupons:subscribeSentCoupons_tt;
		Integer ruleId = preferRuleService.getRuleIidByName(couponsName);//规则ID
		Coupon coupon = couponService.createCouponByRuleid(activeEvent.getEmail(), ruleId,
				activeEvent.getWebsite());
		if (coupon!=null) {
			res.setRet(CommonUtils.SUCCESS_RES);
			return res;
		} else {
			res.setRet(CommonUtils.ERROR_RES);
			return res;
		}
		
	}

	
	
	
	
	@ResponseBody
	@RequestMapping(method = RequestMethod.POST, value = "/v1/event/order")
	public Result order(@RequestBody OrderPoint orderPoint) {
		Result res = new Result();
		log.info("-----orderPoint.getMoney()="+orderPoint.getMoney());
		Integer point = this.convertMoneyToPoint(orderPoint.getMoney(),
				orderPoint.getCurrency());
		log.info("-----point="+point);
		MemberEvent member = new MemberEvent();
		member.setEmail(orderPoint.getEmail());
		member.setWebsite(orderPoint.getWebsite());
		String remark = "order-payment-points.No."
				+ orderPoint.getOrderNumber();
		String source = "order-payment";
		String type = "order-payment";
		int count = pointService.getByRemark(remark);
		if(count>0){
			res.setRet(CommonUtils.ERROR_RES);
			res.setErrMsg("this order already send points");
			return res;
		}
		Boolean isSend = pointService.givingPoint(member, point, type, remark,
				source);
		if (isSend) {
			res.setRet(CommonUtils.SUCCESS_RES);
			return res;
		} else {
			res.setRet(CommonUtils.ERROR_RES);
			return res;
		}

	}
	
	/**
	 * @function 根据传入的金额计算赠送积分
	 * @param orderPoint
	 * @return
	 */
	@ResponseBody
	@RequestMapping(method = RequestMethod.POST, value = "/v1/event/convertMoneyToPoint")
	public String convertMoneyToPoint(@RequestBody OrderPoint orderPoint) {
		log.info("-----convertMoneyToPoint.getMoney()="+orderPoint.getMoney());
		JSONObject o = new JSONObject();
		int point = 0 ;
		try{
			 point = this.convertMoneyToPoint(orderPoint.getMoney(),
					orderPoint.getCurrency());
			 o.put("ret", CommonUtils.SUCCESS_RES);
			 o.put("point", point);
			log.info("-----point="+point);
		}catch(Exception e){
			o.put("ret", CommonUtils.ERROR_RES);
			o.put("point", point);
		}
		return o.toJSONString();
	}

	
	@ResponseBody
	@RequestMapping(method = RequestMethod.POST, value = "/v1/event/review")
	public Result order(@RequestBody MemberEvent member) {
		Result res = new Result();
		String remark = "review-points";
		String type = "review";
		String source = "review";
		Integer point = eventReview;
		Boolean isSend = pointService.givingPoint(member, point, type, remark,
				source);
		if (isSend) {
			res.setRet(CommonUtils.SUCCESS_RES);
			return res;
		} else {
			res.setRet(CommonUtils.ERROR_RES);
			return res;
		}
	}

	@ResponseBody
	@RequestMapping(method = RequestMethod.POST, value = "/v1/event/uploadphoto")
	public Result uploadphoto(@RequestBody MemberEvent member) {
		Result res = new Result();
		String remark = "uploadphoto-points";
		String type = "uploadphoto";
		String source = "uploadphoto";
		Integer point = uploadphoto;
		Boolean isSend = pointService.givingPoint(member, point, type, remark,
				source);
		if (isSend) {
			res.setRet(CommonUtils.SUCCESS_RES);
			return res;
		} else {
			res.setRet(CommonUtils.ERROR_RES);
			return res;
		}
	}
	
	@ResponseBody
	@RequestMapping(method = RequestMethod.POST, value = "/v1/event/subscribe")
	public Result subscribe(@RequestBody  MemberEvent member) {
		Result res = new Result();
		String remark = "subscribe";
		String type = "subscribe";
		String source = "subscribe";
		Integer point = eventSubscribe;
		Point pastPoint=new Point();
		pastPoint.setDotype("subscribe");
		pastPoint.setEmail(member.getEmail());
		pastPoint.setWebsite(member.getWebsite());
		Integer sendPastId=pointService.getPointByType(pastPoint);
		if(null!=sendPastId){
			res.setRet(CommonUtils.ERROR_RES);
			res.setErrMsg("the email has subscribed");
			return res;
		}
		Boolean isSend = pointService.givingPoint(member, point, type, remark,
				source);
		if (isSend) {
			res.setRet(CommonUtils.SUCCESS_RES);
			return res;
		} else {
			res.setRet(CommonUtils.ERROR_RES);
			return res;
		}
	}


	private Integer convertMoneyToPoint(Double money, String currency) {
		Double targetMoney = Double.valueOf(thirdService.exchangeCurrency(
				money, currency, "USD"));
		Integer point = (int) (targetMoney * 1);
		return point;
	}
}
