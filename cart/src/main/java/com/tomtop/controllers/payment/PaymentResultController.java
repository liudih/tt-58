package com.tomtop.controllers.payment;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.tomtop.dto.order.Order;
import com.tomtop.dto.order.OrderDetail;
import com.tomtop.dto.order.OrderDiscount;
import com.tomtop.entity.order.PaymentError;
import com.tomtop.exceptions.BadRequestException;
import com.tomtop.mappers.order.DetailMapper;
import com.tomtop.services.ICurrencyService;
import com.tomtop.services.order.IOrderService;
import com.tomtop.utils.CookieUtils;
import com.tomtop.utils.FoundationService;
import com.tomtop.utils.HttpClientUtil;
import com.tomtop.valueobjects.product.ProductVoV2;

@Controller
@RequestMapping("/payment-result")
public class PaymentResultController {

	@Autowired
	FoundationService foundation;

	@Autowired
	IOrderService orderService;

	@Autowired
	ICurrencyService currencyService;

	@Autowired
	DetailMapper detailMapper;
	
	private static final Logger logger = LoggerFactory
			.getLogger(PaymentResultController.class);

	@RequestMapping("/succeed/{type}/{orderNum}")
	public String succeed(@PathVariable("orderNum") String orderNum,
			HttpServletRequest request, HttpServletResponse response,
			Map<String, Object> model) {

		Order order = orderService.getOrderByOrderNumber(orderNum);
		if (order == null) {
			throw new BadRequestException("can not found order with orderNum:"
					+ orderNum);
		}

		// 保存订单总金额
		if (order != null) {
			List<OrderDetail> dlist = detailMapper
					.getOrderDetailByOrderId(order.getIid());
			if ("USD".equals(order.getCcurrency())) {
				model.put("order", order);
				model.put("orderDetails", dlist);
			} else {
				// 美元总价
				double usdGrandprice = currencyService.exchange(
						order.getFgrandtotal(), order.getCcurrency(), "USD");
				double shipPrice = currencyService.exchange(
						order.getFshippingprice(), order.getCcurrency(), "USD");
				double subtotal = currencyService.exchange(
						order.getFordersubtotal(), order.getCcurrency(), "USD");
				double extra = currencyService.exchange(order.getFextra(),
						order.getCcurrency(), "USD");
				order.setFgrandtotal(usdGrandprice);
				order.setFshippingprice(shipPrice);
				order.setFordersubtotal(subtotal);
				order.setFextra(extra);
				model.put("order", order);
				for (OrderDetail od : dlist) {
					double odprice = currencyService.exchange(od.getFprice(),
							order.getCcurrency(), "USD");
					double odtotalprice = currencyService.exchange(
							od.getFtotalprices(), order.getCcurrency(), "USD");
					od.setFtotalprices(odtotalprice);
					od.setFprice(odprice);
				}
				model.put("orderDetails", dlist);
			}
			
			//保存优惠信息
			List<OrderDiscount> odlist = orderService.getOrderDiscountList(order.getCordernumber(),order.getIwebsiteid());
			for(OrderDiscount od : odlist){
				if(od.getCurrency()!=null && !"USD".equals(od.getCurrency())){
					double odprice = currencyService.exchange(
							od.getDiscount(), od.getCurrency(), "USD");
					od.setDiscount(odprice);
				}
			}
			model.put("discounts", odlist);
		}

		model.put("ordernumber", orderNum);
		
		
		String firstdomain = foundation.getSubdomains();
		if("m".equals(firstdomain)){
			
			//收藏列表
			String collectCookies = CookieUtils.getCookie(FoundationService.WEB_HISTORY);
			if (!StringUtils.isEmpty(collectCookies)) {
				try {
					collectCookies = URLDecoder.decode(collectCookies, "UTF-8");
				} catch (UnsupportedEncodingException e) {
					logger.error(e.toString());
				}
				String[] collectarr = collectCookies.split(",");
				if(collectarr.length>6){
					collectCookies = "";
					for(int i=0;i<6;i++){
						collectCookies += collectarr[i]+",";
					}
				}
				String colurl = FoundationService.PRODUCT_URL+"/ic/v2/products?listingIds="+collectCookies;
				colurl += "&currency="+foundation.getCurrency();
				String result = HttpClientUtil.doGet(colurl);
				if (result != null && !"".equals(result)) {
					JSONObject object = JSON.parseObject(result);
					Integer ret = object.getInteger("ret");
					if(ret!=null && ret==1){
						JSONArray ja = object.getJSONArray("data");
						List<ProductVoV2> clist = Lists.newArrayList();
						if(ret==1 && ja!=null){
							ja.forEach(c -> {
								ProductVoV2 co = JSON.parseObject(c.toString(), ProductVoV2.class);
								clist.add(co);
							});
						}
						model.put("collectlist",clist);
						model.put("currencyBo", foundation.getCurrencyBo());
//						model.put("mainurl", this.foundation.getMainDomain());
					}
				}
			}
			
			return "/mobile/order/pay_success2";
		}else{
			return "/order/pay_success2";
		}
	}
	
	@RequestMapping("/error")
	public String errorshow(Map<String, Object> model,
			@RequestParam(value = "errorCode", required = false, defaultValue = "") String errorCode,
			@RequestParam(value = "orderNum", required = false, defaultValue = "") String orderNum,
			@RequestParam(value = "error", required = false, defaultValue = "") String error,
			@RequestParam(value = "returnWhere", required = false, defaultValue = "no-ec") String returnWhere,
			@RequestParam(value = "storageid", required = false, defaultValue = "1") Integer storageid,
			@RequestParam(value = "retryUrl", required = false, defaultValue = "") String retryUrl
			){
		PaymentError paymentError = new PaymentError(errorCode, orderNum,
				error);
		if(retryUrl!=null && !"".equals(retryUrl)){
			paymentError.setRetryUrl(retryUrl);
		}
		model.put("error", paymentError);
		model.put("returnWhere", returnWhere);
		model.put("storageid", storageid);
//		model.put("mainurl", this.foundation.getMainDomain());
		
		String firstdomain = foundation.getSubdomains();
		if("m".equals(firstdomain)){
			return "/mobile/order/pay_fail";
		}else{
			return "/order/pay_fail";
		}
	}
}
