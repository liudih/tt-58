package com.tomtop.services.impl.order;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.tomtop.dto.Country;
import com.tomtop.dto.Currency;
import com.tomtop.dto.base.Storage;
import com.tomtop.dto.order.BillDetail;
import com.tomtop.dto.order.DropShipping;
import com.tomtop.dto.order.DropShippingOrder;
import com.tomtop.dto.order.DropShippingOrderDetail;
import com.tomtop.dto.order.Order;
import com.tomtop.dto.order.OrderDetail;
import com.tomtop.events.EventBroker;
import com.tomtop.events.order.OrderConfirmationEvent;
import com.tomtop.exceptions.CreateOrderException;
import com.tomtop.exceptions.ExType;
import com.tomtop.exceptions.OrderException;
import com.tomtop.mappers.order.OrderMapper;
import com.tomtop.services.base.ICountryService;
import com.tomtop.services.common.UUIDGenerator;
import com.tomtop.services.impl.CurrencyService;
import com.tomtop.services.impl.member.AddressService;
import com.tomtop.services.impl.product.ProductEnquiryService;
import com.tomtop.services.order.IBillDetailService;
import com.tomtop.services.order.IFreightService;
import com.tomtop.services.order.IOrderService;
import com.tomtop.services.order.IOrderStatusService;
import com.tomtop.services.order.IShippingMethodService;
import com.tomtop.services.product.IShippingService;
import com.tomtop.utils.DoubleCalculateUtils;
import com.tomtop.valueobjects.CartItem;
import com.tomtop.valueobjects.order.ConfirmedOrder;
import com.tomtop.valueobjects.order.PaymentContext;
import com.tomtop.valueobjects.order.ShippingMethod;
import com.tomtop.valueobjects.product.Weight;

@Service
public class DropShippoingToOrderService {

	private static final Logger Logger = LoggerFactory
			.getLogger(DropShippoingToOrderService.class);

	@Autowired
	private DropShippingOrderEnquiryService dsOrderEnquiry;
	@Autowired
	private IOrderService orderService;
	@Autowired
	private DropShippingOrderDetailEnquiryService dsDetailEnquiry;
	@Autowired
	private ICountryService countryEnquiry;
	@Autowired
	private IFreightService freightService;
	@Autowired
	private IShippingMethodService shippingMethodService;
	@Autowired
	private IShippingService shippingServices;
	@Autowired
	private IBillDetailService billDetailService;
	@Autowired
	private IOrderStatusService statusService;
	@Autowired
	private EventBroker eventBroker;
	@Autowired
	private DropShippingMapUpdateService dropShippingMapUpdate;
	@Autowired
	private DropShippingMapEnquiryService dropShippingMapEnquiry;
	@Autowired
	private CurrencyService currencyService;
	@Autowired
	private DropShippingUpdateService dropShippingUpdate;
	@Autowired
	private ProductEnquiryService productEnquiry;
	@Autowired
	private OrderTaggingService taggingService;
	@Autowired
	private AddressService addressService;

	private String errorLog;

	@Autowired
	OrderMapper orderMapper;

	@SuppressWarnings({ "rawtypes", "static-access" })
	public List<Order> converToOrderAndInsert(Map<Integer, Integer> idMap,
			String orgin, String ip, String vhost, int memberID, int lang) {
		if (idMap != null) {
			throw new NullPointerException("this code must recode");
		}
		if(idMap==null || idMap.size()<1){
			errorLog = "the idMap is null";
			return null;
		}
		@SuppressWarnings("unused")
		List<Integer> idList = Lists.newArrayList(idMap.keySet());
		List<DropShippingOrder> dsOrders = dsOrderEnquiry.getListByIDs(idList);
		for (DropShippingOrder dsOrder : dsOrders) {
			if (dsOrder.getCerrorlog() != null) {
				errorLog = "There is a wrong order: "
						+ dsOrder.getCuserorderid() + ", error log: "
						+ dsOrder.getCerrorlog();
				return null;
			}
		}
		if (dsOrders.isEmpty()) {
			errorLog = "haven't found any dropshipping order with id: "
					+ JSON.toJSON(idList);
			return null;
		}
		List<Order> orderList = Lists.newArrayList();
		DoubleCalculateUtils dcu = new DoubleCalculateUtils(0);
		String dropShippingID = dsOrders.get(0).getCdropshippingid();
		for (DropShippingOrder dsOrder : dsOrders) {
			Order order = new Order();
			saveFieldToOrder(order, dsOrder, orgin, ip, vhost);
			order.setCordernumber(orderService.createGeneralOrderNumberV2(null));
			List<DropShippingOrderDetail> details = dsDetailEnquiry
					.getByDropShippingOrderID(dsOrder.getIid());
			List<String> listingIDs = Lists.transform(details,
					d -> d.getClistingid());
			// recode
			checkShippingMethod(order, dsOrder, listingIDs, null, lang, details);
			Integer orderID = null;
			if (orderService.insertOrder(order)) {
				orderID = order.getIid();
			} else {
				throw new OrderException(ExType.Unknown);
			}
			taggingService.tag(orderID, Lists.newArrayList("dropshipping"));
			Map<String, List> map = createDetail(order, orderID, details);
			insertOrderInfo(map, order, orderID);
			orderList.add(order);
			dropShippingMapUpdate.updateOrderNumber(order.getCordernumber(),
					dsOrder.getIid());
			dcu = dcu.add(order.getFgrandtotal());
			try {
				Thread.currentThread().sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		updateDropShippingTotal(dcu.doubleValue(), dropShippingID);
		return orderList;
	}

	private void updateDropShippingTotal(double doubleValue,
			String dropShippingID) {
		DropShipping ds = new DropShipping();
		ds.setCdropshippingid(dropShippingID);
		ds.setFtotalprice(doubleValue);
		dropShippingUpdate.updateByDropShippingID(ds);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void insertOrderInfo(Map<String, List> map, Order order,
			Integer orderID) {
		List<OrderDetail> details = map.get("detailList");
		List<BillDetail> bills = map.get("billList");
		if (details.size() != 0 && orderService.insertDetail(details)) {
			if (billDetailService.batchInsert(bills)) {
				statusService.changeOrdeStatus(orderID,
						IOrderStatusService.PAYMENT_PENDING);
				Logger.debug(
						"Start sending ------>OrderConfirmationEvent<------events,orderNum={},class==DropShippoingToOrderService,method=insertOrderInfo",
						order.getCordernumber());
				eventBroker.post(new OrderConfirmationEvent(order.getCemail(),
						order.getCordernumber(), order.getCcurrency()));
			} else {
				throw new OrderException(ExType.BillDetailFailed);
			}
		} else {
			throw new OrderException(ExType.OrderDetailFailed);
		}
	}

	@SuppressWarnings("rawtypes")
	private Map<String, List> createDetail(Order order, Integer orderID,
			List<DropShippingOrderDetail> details) {
		Map<String, List> map = Maps.newHashMap();
		List<String> listingIDs = Lists.transform(details,
				d -> d.getClistingid());
		List<OrderDetail> detailList = Lists.newArrayList();
		List<BillDetail> billList = Lists.newArrayList();
		List<Weight> weights = productEnquiry.getWeightList(listingIDs);
		Map<String, Weight> weightMap = Maps.uniqueIndex(weights,
				w -> w.getListingId());
		for (DropShippingOrderDetail dsDetail : details) {
			OrderDetail detail = new OrderDetail();
			detail.setCid(UUIDGenerator.createAsString());
			detail.setClistingid(dsDetail.getClistingid());
			detail.setCsku(dsDetail.getCsku());
			detail.setCtitle(dsDetail.getCtitle());
			detail.setForiginalprice(dsDetail.getForiginalprice());
			detail.setFprice(dsDetail.getFprice());
			detail.setFtotalprices(dsDetail.getFtotalprice());
			detail.setIorderid(orderID);
			detail.setIqty(dsDetail.getIqty());
			detail.setFweight(weightMap.get(dsDetail.getClistingid())
					.getWeight());
			orderService.parseBill(billList, detail);
			detailList.add(detail);
		}
		BillDetail billDetail = orderService.getShippingBill(order);
		billList.add(billDetail);
		map.put("detailList", detailList);
		map.put("billList", billList);
		return map;
	}

	private void checkShippingMethod(Order order, DropShippingOrder dsOrder,
			List<String> listingIDs, String shipCode, int lang,
			List<DropShippingOrderDetail> details) {
		Country country = countryEnquiry.getCountryByShortCountryName(dsOrder
				.getCcountrysn());

		List<CartItem> items = FluentIterable.from(details).transform(d -> {
			CartItem item = new CartItem();
			item.setClistingid(d.getClistingid());
			item.setIqty(d.getIqty());
			return item;
		}).toList();

		Storage shippingStorage = shippingServices.getShippingStorage(
				dsOrder.getIwebsiteid(), country, listingIDs);

		ShippingMethod hit = orderService.checkShippingMethodCorrect(
				shippingStorage.getIid(), country.getCshortname(), shipCode,
				dsOrder.getFtotal(), items, dsOrder.getCcurrency(), 1);

		if (hit == null) {
			throw new CreateOrderException("invalid shipping method");
		}
		Double freight = hit.getPrice();

		double grandTotal = new DoubleCalculateUtils(freight).add(
				order.getFordersubtotal()).doubleValue();
		order.setFshippingprice(freight);
		order.setCshippingcode(shipCode);
		order.setIstorageid(shippingStorage.getIid());
		order.setFgrandtotal(grandTotal);
	}

	private void saveFieldToOrder(Order order, DropShippingOrder dsOrder,
			String orgin, String ip, String vhost) {
		order.setCcity(dsOrder.getCcity());
		order.setCcountry(dsOrder.getCcountry());
		order.setCcountrysn(dsOrder.getCcountrysn());
		order.setCcurrency(dsOrder.getCcurrency());
		order.setCemail(dsOrder.getCuseremail());
		order.setCfirstname(dsOrder.getCfirstname());
		order.setCmemberemail(dsOrder.getCuseremail());
		order.setCmessage(dsOrder.getCcnote());
		order.setCpostalcode(dsOrder.getCpostalcode());
		order.setCprovince(dsOrder.getCprovince());
		order.setCstreetaddress(dsOrder.getCstreetaddress());
		order.setCtelephone(dsOrder.getCtelephone());
		order.setIwebsiteid(dsOrder.getIwebsiteid());
		order.setFordersubtotal(dsOrder.getFtotal());
		order.setFextra(0.0);
		order.setCip(ip);
		order.setCorigin(orgin);
		order.setCvhost(vhost);
	}

	public PaymentContext getPaymentContext(String dropShippingID, String email) {
		List<String> orderNumbers = dropShippingMapEnquiry
				.getOrderNumbersByID(dropShippingID);
		List<OrderDetail> details = Lists.newArrayList();
		Order order = null;
		DoubleCalculateUtils total = new DoubleCalculateUtils(0.0);// 总价
		DoubleCalculateUtils subtotal = new DoubleCalculateUtils(0.0);// 产品总价
		DoubleCalculateUtils shippingTotal = new DoubleCalculateUtils(0.0);// 邮费总价
		//MemberAddress address = addressService.getDefaultOrderAddress(email);
		for (int i = 0; i < orderNumbers.size(); i++) {
			String orderNumber = orderNumbers.get(i);
			Order o = orderService.getOrderByOrderNumber(orderNumber);
			if (o != null) {
				List<OrderDetail> des = orderMapper.getOrderDetailByOrderId(o
						.getIid());
				for (OrderDetail od : des) {
					od.setCtitle("Drop Shipping Order Item " + (i + 1));
				}
				// OrderDetail detail = new OrderDetail();
				// detail.setIorderid(o.getIid());
				// detail.setCid(o.getCordernumber());
				// detail.setCtitle("Drop Shipping Order Item " + (i + 1));
				// detail.setCsku(o.getCordernumber());
				// detail.setIqty(1);
				// detail.setFtotalprices(o.getFgrandtotal());
				// detail.setFprice(o.getFgrandtotal());
				if (des.size() > 0) {
					details.addAll(des);
				}
				total = total.add(o.getFgrandtotal());
				subtotal = subtotal.add(o.getFordersubtotal());
				shippingTotal = shippingTotal.add(o.getFshippingprice());
			}
			if (i == 0 && o != null) {
				order = new Order();
				order.setCordernumber(dropShippingID);
				order.setCpaymentid(o.getCpaymentid());
				order.setCfirstname(o.getCfirstname());
				order.setCcurrency(o.getCcurrency());
				order.setIwebsiteid(o.getIwebsiteid());
				// if (address != null && address.getIcountry() != null) {
				// Country country = countryEnquiry
				// .getCountryByCountryId(address.getIcountry());
				// order.setCcountry(country.getCname());
				// order.setCcountrysn(country.getCshortname());
				// } else {
				// order.setCcountry(o.getCcountry());
				// order.setCcountrysn(o.getCcountrysn());
				// }
			}
		}
		Currency currency = null;
		if (order != null) {
			currency = currencyService.getCurrencyByCode(order.getCcurrency());
			order.setFgrandtotal(total.doubleValue());
			order.setCemail(email);
			// add by lijun
			order.setFordersubtotal(subtotal.doubleValue());
			order.setFshippingprice(shippingTotal.doubleValue());
		}
		ConfirmedOrder co = new ConfirmedOrder(order, details);
		PaymentContext context = new PaymentContext(co, null, currency);
		return context;
	}

	public String getErrorLog() {
		return errorLog;
	}

	public void setErrorLog(String errorLog) {
		this.errorLog = errorLog;
	}

}
