package com.tomtop.controllers.order;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.tomtop.dto.Currency;
import com.tomtop.dto.order.Order;
import com.tomtop.dto.order.OrderPack;
import com.tomtop.dto.order.OrderStatus;
import com.tomtop.dto.order.OrderStatusHistory;
import com.tomtop.services.ICurrencyService;
import com.tomtop.services.impl.order.OrderEnquiryService;
import com.tomtop.services.impl.product.price.PriceService;
import com.tomtop.services.order.IOrderPackService;
import com.tomtop.services.order.IOrderService;
import com.tomtop.services.order.IOrderStatusService;
import com.tomtop.services.product.IShippingService;
import com.tomtop.utils.FoundationService;
import com.tomtop.utils.HttpClientUtil;
import com.tomtop.utils.Page;
import com.tomtop.utils.Result;
import com.tomtop.valueobjects.order.MemberOrderForm;
import com.tomtop.valueobjects.order.OrderCount;
import com.tomtop.valueobjects.order.OrderItem;
import com.tomtop.valueobjects.order.OrderList;
import com.tomtop.valueobjects.order.PaymentContext;
import com.tomtop.valueobjects.order.ShippingMethodMini;
import com.tomtop.valueobjects.product.price.Price;
import com.tomtop.valueobjects.product.spec.IProductSpec;
import com.tomtop.valueobjects.product.spec.ProductSpecBuilder;

/**
 * 订单流程
 * 
 * @author lijun
 *
 */
@Controller
@RequestMapping(value = "/order")
public class OrderApiController {
	@Autowired
	IOrderService orderService;

	@Autowired
	ICurrencyService currencyService;

	@Autowired
	FoundationService foundationService;
	
	@Autowired
	OrderEnquiryService orderEnquiryService;
	
	@Autowired
	IOrderStatusService orderStatusService;
	
	@Autowired
	IOrderPackService orderPackService;
	
	@Autowired
	PriceService priceService;
	
	@Autowired
	IShippingService shippingService;
	
	@Value("${loyalty.trans_point}")
	String transPoint;

	/**
	 * 获取订单全部状态
	 * @param email
	 * @param site
	 * @return
	 */
	@RequestMapping(value = "/status", method = RequestMethod.GET)
	@ResponseBody
	public Object getStatus(@RequestParam("email") String email,
			@RequestParam("site") Integer site){
		Result res = new Result();
		if(email==null || "".equals(email)){
			res.setRet(Result.FAIL);
			res.setErrMsg("email is null");
			return res;
		}
		OrderCount count = orderEnquiryService.getCountByEmail(email, site, 1, true);
		res.setRet(Result.SUCCESS);
		res.setData(count);
		return res;
	}
	
	/**
	 * 获取订单列表
	 * @param form
	 * @return
	 */
	@RequestMapping(value = "/orderlist", method = RequestMethod.GET)
	@ResponseBody
	public Object getOrderList(MemberOrderForm form){
		Result res = new Result();
		if(form.getEmail()==null || "".equals(form.getEmail())){
			res.setRet(Result.FAIL);
			res.setErrMsg("email is null");
			return res;
		}
		List<Order> orders = orderEnquiryService.searchOrders(form, true);
		int ordersCount = orderEnquiryService.searchOrderCount(form, true);
		Page p = Page.getPage(form.getPageNum(), ordersCount, form.getPageSize());
		res.setPage(p);
		
		List<OrderList> allItems = Lists.newArrayList();
		Map<String, Currency> currencyIndex = Maps.newHashMap();
		if(orders.size()>0){
			List<String> currencys = Lists.transform(orders,
					list -> list.getCcurrency());
			List<Currency> curList = currencyService.getCurrencyByCodes(new HashSet<String>(currencys));
			currencyIndex = Maps.uniqueIndex(curList, i -> i.getCcode());
		}
		
		Map<Integer, OrderStatus> idMap = orderStatusService.getIdMap();
		for (Order ord : orders) {
			OrderList o = new OrderList();
			o.setOrder(ord);
			List<OrderItem> items = orderService.getOrderDetailByOrder(ord, form.getLang());
			o.setCurrency(currencyIndex.get(ord.getCcurrency()));
			o.setOrderItems(items);
			if(idMap.get(ord.getIstatus())!=null){
				o.setOrderStatus(idMap.get(ord.getIstatus()).getCname());
			}
			OrderPack op = orderPackService.getOneByOrderId(ord.getIid());
			if(op!=null){
				o.setTrackingNumber(op.getCtrackingnumber());
			}
			allItems.add(o);
		}
		res.setRet(Result.SUCCESS);
		res.setData(allItems);
		return res;
	}
	
	
	/**
	 * 获取订单详情
	 * @param form
	 * @return
	 */
	@RequestMapping(value = "/orderdetail", method = RequestMethod.GET)
	@ResponseBody
	public Object getOrderDetail(MemberOrderForm form){
		Result res = new Result();
		if(form.getEmail()==null || "".equals(form.getEmail())){
			res.setRet(Result.FAIL);
			res.setErrMsg("email is empty");
			return res;
		}
		
		PaymentContext context = orderService.getPaymentContext(form.getOrderNumber(),
				form.getLang());
		Order order = context.getOrder().getOrder();
		if(order==null){
			res.setRet(Result.FAIL);
			res.setErrMsg("order is empty");
			return res;
		}
		Map<String, Object> mjson = new HashMap<String, Object>();
		List<OrderItem> orderList = orderService.getOrderDetailByOrder(order, form.getLang());
		mjson.put("order", order);
		mjson.put("orderItems", orderList);
		Currency currency = currencyService.getCurrencyByCode(order.getCcurrency());
		if(currency!=null){
			mjson.put("symbol", currency.getCsymbol());
		}
		//订单状态
		Map<Integer, OrderStatus> idMap = orderStatusService.getIdMap();
		OrderStatus os = idMap.get(order.getIstatus());
		if(os!=null){
			mjson.put("orderStatus", os.getCname());
		}
		//订单状态历史
		Map<String, OrderStatusHistory> orderStatusHistoryMap = orderStatusService
				.getOrderHistoryMap(order.getIid());
		mjson.put("orderStatusHistoryMap", orderStatusHistoryMap);
		
		//追踪号
		OrderPack op = orderPackService.getOneByOrderId(order.getIid());
		String trackingNumber = "";
		if(op!=null){
			trackingNumber = op.getCtrackingnumber();
		}
		mjson.put("trackingNumber", trackingNumber);
		//获取物流信息
		if(order.getCshippingcode()!=null && !"".equals(order.getCshippingcode())){
			ShippingMethodMini sm = shippingService.getShipMethodInfo(order.getCshippingcode(), form.getLang());
			mjson.put("shippingMethodInfo", sm);
		}else{
			mjson.put("shippingMethodInfo", "");
		}
		
		//获取订单总价应该得的积分
		Map<String,String> priceMap = new HashMap<String, String>();
		priceMap.put("currency", order.getCcurrency());
		priceMap.put("money", order.getFgrandtotal()+"");
		try {
			String result = HttpClientUtil.doPost(transPoint,JSON.toJSONString(priceMap));
			JSONObject jb = JSON.parseObject(result);
			if(jb!=null){
				Integer ret = jb.getInteger("ret");
				if(ret!=null && ret==1){
					Integer point = jb.getInteger("point");
					mjson.put("point", point);
				}else{
					mjson.put("point", 0);
				}
			}else{
				mjson.put("point", 0);
			}
		} catch (Exception e) {
			mjson.put("point", 0);
		}
		res.setRet(Result.SUCCESS);
		res.setData(mjson);
		return res;
	}
	
	/**
	 * 逻辑删除订单
	 * @param body
	 * @return
	 */
	@RequestMapping(value = "/delorders", method = RequestMethod.PUT)
	@ResponseBody
	public Object delOrders(@RequestBody String body){
		Result res = new Result();
		JSONObject jo = JSON.parseObject(body);
		if(jo!=null){
			String email = jo.getString("email");
			Integer siteid = jo.getInteger("siteId");
			Integer type = jo.getInteger("type");	//删除类型：1普通删除，2回收站删除，3回收站恢复
			JSONArray ja = jo.getJSONArray("orderNumbers");
			List<String> olist = JSON.parseArray(ja.toJSONString(), String.class);
			Integer showType = OrderEnquiryService.SHOW_TYPE_RECYCLE;
			if(type!=null && type==1){
				showType = OrderEnquiryService.SHOW_TYPE_RECYCLE;
			}
			if(type!=null && type==2){
				showType = OrderEnquiryService.SHOW_TYPE_FALSE;
			}
			if(type!=null && type==3){
				showType = OrderEnquiryService.SHOW_TYPE_TURE;
			}
			
			boolean flag = orderEnquiryService.updateOrderShow(email, siteid,  olist, 
					showType);
			if(flag){
				res.setRet(Result.SUCCESS);
				res.setData("success");
			}else{
				res.setRet(Result.FAIL);
				res.setData("cannot delete");
			}
		}else{
			res.setRet(Result.FAIL);
			res.setData("data is null");
		}
		return res;
	}
	
	/**
	 * 获取单个产品的价格
	 * @param listingid
	 * @param currency
	 * @param qty
	 * @return
	 */
	@RequestMapping(value = "/price", method = RequestMethod.GET)
	@ResponseBody
	public Object getPrice(@RequestParam("listingid") String listingid,
			@RequestParam("currency") String currency,
			@RequestParam("qty") Integer qty,
			@RequestParam(value = "storageid", required = false, defaultValue = "1") Integer storageid){
		Result res = new Result();
		IProductSpec spec = ProductSpecBuilder
				.build(listingid).setQty(qty).get();
		Price price = priceService.getPrice(spec, currency, storageid);
		if(price!=null){
			res.setRet(Result.SUCCESS);
			res.setData(price);
		}else{
			res.setRet(Result.FAIL);
		}
		return res;
	}
	
	/**
	 * 根据产品ID与邮箱查询未评论的订单ID
	 * @param listingid
	 * @param email
	 * @return
	 */
	@RequestMapping(value = "/withoutcomment", method = RequestMethod.GET)
	@ResponseBody
	public Object getorderIdWithoutComment(String email,String listingid){
		Result res = new Result();
		if(StringUtils.isBlank(email)){
			res.setRet(Result.FAIL);
			res.setErrMsg("email is empty");
			return res;
		}
		
		if(StringUtils.isBlank(listingid)){
			res.setRet(Result.FAIL);
			res.setErrMsg("listingid is empty");
			return res;
		}
		String orderId = orderEnquiryService.getorderIdWithoutComment(email, listingid);
		if(StringUtils.isBlank(orderId)){
			res.setRet(Result.FAIL);
			res.setErrMsg("can not find orderid");
			return res;
		}
		res.setRet(Result.SUCCESS);
		res.setData(orderId);
		return res;
	}

}
