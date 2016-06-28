package com.tomtop.services.impl.order;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.tomtop.dao.order.IOrderDetailDao;
import com.tomtop.dto.Country;
import com.tomtop.dto.Currency;
import com.tomtop.dto.base.Storage;
import com.tomtop.dto.member.MemberAddress;
import com.tomtop.dto.order.BillDetail;
import com.tomtop.dto.order.Order;
import com.tomtop.dto.order.OrderDetail;
import com.tomtop.dto.order.OrderDiscount;
import com.tomtop.dto.order.ShippingMethodDetail;
import com.tomtop.entity.order.CreateOrderRequest;
import com.tomtop.events.EventBroker;
import com.tomtop.events.order.OrderConfirmationEvent;
import com.tomtop.events.order.OrderOpreationEvent;
import com.tomtop.events.order.OrderOpreationEvent.OpreationResult;
import com.tomtop.exceptions.CreateOrderException;
import com.tomtop.exceptions.DiscountException;
import com.tomtop.exceptions.ExType;
import com.tomtop.exceptions.InventoryShortageException;
import com.tomtop.exceptions.OrderException;
import com.tomtop.mappers.order.DetailMapper;
import com.tomtop.mappers.order.OrderDiscountMapper;
import com.tomtop.mappers.order.OrderMapper;
import com.tomtop.mappers.product.ProductStorageMapMapper;
import com.tomtop.services.ICurrencyService;
import com.tomtop.services.base.ICountryService;
import com.tomtop.services.base.IStorageService;
import com.tomtop.services.common.UUIDGenerator;
import com.tomtop.services.impl.LoyaltyService;
import com.tomtop.services.impl.base.VhostService;
import com.tomtop.services.member.IAddressService;
import com.tomtop.services.order.IBillDetailService;
import com.tomtop.services.order.ICheckoutService;
import com.tomtop.services.order.IFreightService;
import com.tomtop.services.order.IOrderService;
import com.tomtop.services.order.IOrderStatusService;
import com.tomtop.services.product.IEntityMapService;
import com.tomtop.services.product.IProductLabelService;
import com.tomtop.services.product.IProductService;
import com.tomtop.services.product.IShippingService;
import com.tomtop.utils.DoubleCalculateUtils;
import com.tomtop.utils.FoundationService;
import com.tomtop.utils.Utils;
import com.tomtop.valueobjects.BundleCartItem;
import com.tomtop.valueobjects.CartItem;
import com.tomtop.valueobjects.Constants;
import com.tomtop.valueobjects.Discount;
import com.tomtop.valueobjects.SingleCartItem;
import com.tomtop.valueobjects.base.LoginContext;
import com.tomtop.valueobjects.order.ConfirmedOrder;
import com.tomtop.valueobjects.order.OrderItem;
import com.tomtop.valueobjects.order.PaymentContext;
import com.tomtop.valueobjects.order.ShippingMethod;
import com.tomtop.valueobjects.payment.CheckoutDetails;
import com.tomtop.valueobjects.product.ProductLite;
import com.tomtop.valueobjects.product.Weight;
import com.tomtop.valueobjects.product.price.Price;

/**
 * 订单相关服务
 * 
 * @author lijun
 */
@Service
public class OrderService implements IOrderService {

	private static final Logger Logger = LoggerFactory
			.getLogger(OrderService.class);

	private static final String[] SYMBOLS = { "0", "1", "2", "3", "4", "5",
			"6", "7", "8", "9", "A", "B", "C", "D", "E", "F", "G", "H", "I",
			"J", "K", "L", "M", "N", "P", "Q", "R", "S", "T", "U", "V", "W",
			"X", "Y", "Z" };
	
	@Autowired
	ICurrencyService currencyService;

	@Autowired
	OrderMapper orderMapper;

	@Autowired
	IProductLabelService productLabelService;

	@Autowired
	IFreightService freightService;

	@Autowired
	IAddressService addressService;

	@Autowired
	ICheckoutService checkoutService;

	@Autowired
	ICountryService countryService;

	@Autowired
	IShippingService shippingService;

	@Autowired
	IStorageService storageService;

	@Autowired
	FoundationService foundation;

	@Autowired
	IBillDetailService billDetailService;

	@Autowired
	IOrderStatusService statusService;

	@Autowired
	IProductService productService;

	@Autowired
	EventBroker eventBroker;

	@Autowired
	DetailMapper detailMapper;

	@Autowired
	IOrderDetailDao orderDetailDao;

	@Autowired
	IEntityMapService entityMapService;

	@Autowired
	LoyaltyService loyaltyService;

	@Autowired
	OrderDiscountMapper orderDiscountMapper;

	@Autowired
	VhostService vhostService;
	
	@Autowired
	ProductStorageMapMapper  productStorageMapMapper;
	
	/**
	 * 订单已完成状态对应的状态id
	 */
	private Integer completedStatusid;

	@Override
	public ShippingMethod checkShippingMethodCorrect(int storageId,
			String country, String shipCode, Double subTotal,
			List<CartItem> items, String cunrrency, int langID) {
		Assert.hasLength(shipCode, "ship code is null");
		Assert.hasLength(country, "country code is null");
		Assert.hasLength(cunrrency, "cunrrency code is null");
		Assert.notEmpty(items, "items is null");
		List<ShippingMethod> shipMethods = shippingService.getShipMethod(
				country, storageId, langID, items, cunrrency, subTotal);

		if (shipMethods != null) {
			ImmutableList<ShippingMethod> hits = FluentIterable
					.from(shipMethods)
					.filter(m -> {
						if (m != null && m.getCode() != null
								&& m.getCode().equals(shipCode)) {
							return true;
						} else {
							return false;
						}
					}).toList();
			if (hits.size() > 0) {
				return hits.get(0);
			}
		}
		return null;
	}

	/**
	 * @author lijun
	 * @param request
	 * @return
	 */
	public Order createOrderInstance(CreateOrderRequest request) {
		LoginContext loginCtx = this.foundation.getLoginContext();
		String email = loginCtx.getEmail();
		if (email != null) {
			email = email.toLowerCase();
		}
		Order order = new Order();
		List<CartItem> items = request.getItems();

		List<String> listingIds = Lists.newLinkedList();

		double subTotal = checkoutService.subToatl(items);

		for (CartItem item : items) {
			if (item instanceof BundleCartItem) {
				listingIds.addAll(((BundleCartItem) item).getAllListingId());
			} else {
				listingIds.add(item.getClistingid());
			}
		}

		CheckoutDetails checkout = null;
		Double freight = 0d;
		// 非EC支付
		if (!Constants.PAYPAL_EC.equals(request.getCpaymenttype())) {
			if (request.getAddressId() == null) {
				throw new CreateOrderException("address id is null");
			}
			MemberAddress adderess = addressService
					.getMemberAddressById(request.getAddressId());
			if (adderess == null) {
				throw new CreateOrderException("system not exist this address "
						+ request.getAddressId());
			}

			Country country = countryService.getCountryByCountryId(adderess
					.getIcountry());
			if (country == null) {
				throw new CreateOrderException("system not exist this country "
						+ adderess.getIcountry());
			}

			if (request.getShipCode() == null) {
				throw new CreateOrderException("shipping method code is null");
			}

			//使用优惠后的商品价格
			checkout = checkoutService.sum(items, 0,
					request.getCurrency());
			// 验证shipcode 是否正确
			ShippingMethod hit = this.checkShippingMethodCorrect(
					request.getStorage(), country.getCshortname(),
					request.getShipCode(), checkout.getTotal(), items,
					request.getCurrency(), request.getLangID());
			if (hit == null) {
				throw new CreateOrderException(
						"shipping method code is not correct");
			}

			freight = hit.getPrice();

			String provinceName = adderess.getCprovince();
			Storage shippingStorage;
			if (request.getStorage() == null) {
				shippingStorage = shippingService.getShippingStorage(
						request.getSiteId(), country, listingIds);
			} else {
				shippingStorage = this.storageService
						.getStorageForStorageId(request.getStorage());
			}

			// finalTotal = checkoutService.sum(items, freight,
			// request.getCurrency());
			

			order.setCcountrysn(country.getCshortname());
			order.setCcountry(country.getCname());
			order.setCcity(adderess.getCcity());
			order.setCpostalcode(adderess.getCpostalcode());
			order.setCprovince(provinceName);
			order.setCfirstname(adderess.getCfirstname());
			order.setCmiddlename(adderess.getCmiddlename());
			order.setClastname(adderess.getClastname());
			order.setCstreetaddress(adderess.getCstreetaddress());
			order.setCtelephone(adderess.getCtelephone());
			order.setFshippingprice(freight);
			order.setIshippingmethodid(hit.getId());
			order.setIstorageid(shippingStorage.getIid());
			order.setCmemberemail(adderess.getCmemberemail());
			order.setCshippingcode(request.getShipCode());
		} else {
			checkout = checkoutService.sum(items, 0, request.getCurrency());
		}

		Double finalTotal = checkout.getTotal();
		//加上运费
		DoubleCalculateUtils dcu = new DoubleCalculateUtils(finalTotal);
		dcu = dcu.add(freight);
		finalTotal = dcu.doubleValue();
		

		Double extraTotal = checkout.getDiscount();
		// 优惠券相关代码

		String finalTotalStr = Utils.money(finalTotal, request.getCurrency());
		finalTotalStr = finalTotalStr.replaceAll(",", "");

		finalTotal = Double.parseDouble(finalTotalStr);

		order.setCcurrency(request.getCurrency());
		order.setCemail(email);
		order.setCmemberemail(email);

		order.setFextra(extraTotal);
		order.setFgrandtotal(finalTotal);
		order.setFordersubtotal(subTotal);

		order.setIwebsiteid(request.getSiteId());
		order.setCorigin(request.getOrigin());

		order.setCmessage(request.getMessage());
		order.setCip(request.getIp());

		order.setCvhost(request.getVhost());
		String orderNum = this.createGeneralOrderNumberV2(null);
		order.setCordernumber(orderNum);
		order.setCpaymenttype(request.getCpaymenttype()); // 支付类型

		order.setIstorageid(request.getStorage());

		// 锁定优惠券
		List<Discount> discounts = checkout.getUsedDiscount();

		if (discounts.size() > 0) {
			boolean locked = this.loyaltyService.lock(discounts, orderNum,
					items, email, request.getCurrency());

			if (!locked) {
				throw new DiscountException(null, null,
						"coupon or promo or point lock failed");
			}
			try {
				// 记录日志
				orderDiscountMapper.insert(orderNum, request.getSiteId(),
						discounts);
			} catch (Exception e) {
				Logger.error("log discount error", e);
			}
		}
		
		// insert order first with NULL status
		if (insertOrder(order)) {

			Integer orderId = order.getIid();
			List<BillDetail> bills = new ArrayList<BillDetail>();
			bills.add(getShippingBill(order));
			// add coupon promo point bill
			if (discounts != null && discounts.size() > 0) {
				discounts.forEach(d -> {
					BillDetail bill = new BillDetail();
					bill.setIorderid(orderId);
					bill.setCmsg(d.getCode());
					bill.setFtotalprice(d.getDiscount());
					bill.setForiginalprice(d.getDiscount());
					bill.setFpresentprice(d.getDiscount());
					bill.setCtype(d.getType().name());
					bills.add(bill);
				});
			}

			List<OrderDetail> details = createDetails(orderId, items, bills);
			if (details.size() != 0 && insertDetail(details)) {
				// recode 要不要保存优化信息，再考虑
				if (billDetailService.batchInsert(bills)) {
					// everything ok, update order status
					statusService.changeOrdeStatus(orderId,
							IOrderStatusService.PAYMENT_PENDING);
					Logger.debug(
							"Start sending ------>OrderConfirmationEvent<------events,orderNum={},class==OrderService,method=createOrderInstance",
							order.getCordernumber());
					eventBroker.post(new OrderConfirmationEvent(email,
							orderNum, request.getCurrency()));
					return order;
				} else {
					throw new OrderException(ExType.BillDetailFailed);
				}

			} else {
				throw new OrderException(ExType.OrderDetailFailed);
			}
		} else {
			throw new OrderException(ExType.Unknown);
		}

	}

	private OrderDetail createDetail(Integer orderId, CartItem item,
			Map<String, Weight> weightMap) {

		Price price = item.getPrice();
		Weight weight = weightMap.get(item.getClistingid());

		if (null == price || weight == null) {
			Logger.debug("OrderService Save Order Details createDetails price is null!");
			return null;
		}

		OrderDetail detail = new OrderDetail();
		detail.setCid(UUIDGenerator.createAsString());
		detail.setCtitle(item.getCtitle());
		detail.setClistingid(item.getClistingid());
		detail.setIqty(item.getIqty());
		detail.setIorderid(orderId);
		detail.setCsku(item.getSku());

		detail.setFprice(price.getUnitPrice());
		detail.setFtotalprices(price.getPrice());
		detail.setForiginalprice(price.getUnitBasePrice());

		detail.setFweight(weight.getWeight());

		return detail;
	}

	private List<OrderDetail> createDetails(Integer orderId,
			List<CartItem> items, List<BillDetail> bills) {

		List<String> listingIds = Lists.newLinkedList();

		for (CartItem item : items) {
			if (item instanceof SingleCartItem) {
				listingIds.add(item.getClistingid());
			} else if (item instanceof BundleCartItem) {
				listingIds.addAll(((BundleCartItem) item).getAllListingId());
			} else {
				listingIds.add(item.getClistingid());
			}
		}

		List<OrderDetail> listTotal = Lists.newArrayList();
		List<Weight> weights = productService.getWeightList(listingIds);
		Map<String, Weight> weightMap = Maps.uniqueIndex(weights,
				w -> w.getListingId());
		FluentIterable
				.from(items)
				.forEach(
						e -> {
							if (e == null) {
								return;
							}
							OrderDetail mainDetail = createDetail(orderId, e,
									weightMap);
							if (mainDetail == null) {
								return;
							}

							parseBill(bills, mainDetail);
							listTotal.add(mainDetail);

							if (e instanceof BundleCartItem) {
								List<SingleCartItem> childitems = ((BundleCartItem) e)
										.getChildList();
								if (childitems == null
										|| childitems.size() == 0) {
									throw new NullPointerException(
											"childitems is null");
								}

								FluentIterable
										.from(childitems)
										.forEach(
												i -> {
													if (null == i) {
														Logger.debug("OrderService Save Order Details createDetails item is null!");
														return;
													}

													OrderDetail detail = createDetail(
															orderId, i,
															weightMap);

													if (detail == null) {
														return;
													}

													detail.setCparentid(mainDetail
															.getCid());

													parseBill(bills, detail);
													listTotal.add(detail);
												});
							}
						});

		return listTotal;
	}

	@Override
	public boolean insertDetail(List<OrderDetail> details) {
		if (details == null || details.isEmpty()) {
			return true;
		}
		int i = 0;
		i = detailMapper.batchInsert(details);
		if (details.size() == i) {
			Logger.debug("OrderService Save Order Details insertDetail true!");
			return true;
		}
		Logger.debug("OrderService Save Order Details insertDetail false!");
		return false;
	}

	@Override
	public BillDetail getShippingBill(Order order) {
		BillDetail billDetail = new BillDetail();
		billDetail.setCmsg(order.getIshippingmethodid() == null ? null : order
				.getIshippingmethodid().toString());
		billDetail.setCtype(IBillDetailService.TYPE_SHIPPING_METHOD);
		billDetail.setForiginalprice(order.getFshippingprice());
		billDetail.setFpresentprice(order.getFshippingprice());
		billDetail.setFtotalprice(order.getFshippingprice());
		billDetail.setIorderid(order.getIid());
		billDetail.setIqty(1);
		return billDetail;
	}

	@Override
	public boolean insertOrder(Order order) {
		Logger.info("order===",order.toString());
		try{
			int i = orderMapper.insert(order);
			OrderOpreationEvent event = new OrderOpreationEvent(
					order.getIwebsiteid(), order.getCemail(),
					order.getCordernumber(),
					OrderOpreationEvent.OpreationType.CREATE_ORDER,
					JSON.toJSONString(order));
			if (1 == i) {
				Logger.debug("OrderService Save Order insertOrder true!");
				eventBroker.post(event);
				return true;
			}
			event.setCresult(OrderOpreationEvent.OpreationResult.FAILURE);
			eventBroker.post(event);
			Logger.debug("OrderService Save Order insertOrder false!");
		}catch(RuntimeException ex){
			OrderOpreationEvent event = new OrderOpreationEvent(
					order.getIwebsiteid(), order.getCemail(),
					order.getCordernumber(),
					OrderOpreationEvent.OpreationType.CREATE_ORDER,
					JSON.toJSONString(order));
			event.setCresult(OpreationResult.FAILURE);
			eventBroker.post(event);
			throw new CreateOrderException("create order has error");
		}
		return false;
	}

	private void setShippingAddress(Order order) {
		if(order==null){
			return ;
		}
		MemberAddress address = addressService.getDefaultOrderAddress(order
				.getCmemberemail(), order.getIwebsiteid());
		if (address != null) {
			Country country = countryService.getCountryByCountryId(address
					.getIcountry());
			if (country != null) {
				order.setCcountry(country.getCname());
				order.setCcountrysn(country.getCshortname());
			}
			order.setCcity(address.getCcity());
			order.setCfirstname(address.getCfirstname());
			order.setCmiddlename(address.getCmiddlename());
			order.setClastname(address.getClastname());
			order.setCpostalcode(address.getCpostalcode());
			order.setCprovince(address.getCprovince());
			order.setCtelephone(address.getCtelephone());
			order.setCstreetaddress(address.getCstreetaddress());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see services.order.IOrderService#parseBill(java.util.List,
	 * dto.order.OrderDetail)
	 */
	@Override
	public BillDetail parseBill(List<BillDetail> bills, OrderDetail orderDetail) {
		BillDetail detail = new BillDetail();
		detail.setCmsg(orderDetail.getCtitle());
		detail.setIqty(orderDetail.getIqty());
		detail.setCtype(IBillDetailService.TYPE_PRODUCT);
		detail.setForiginalprice(orderDetail.getForiginalprice());
		detail.setFpresentprice(orderDetail.getFprice());
		detail.setFtotalprice(orderDetail.getFtotalprices());
		detail.setIorderid(orderDetail.getIorderid());
		bills.add(detail);
		return detail;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see services.order.IOrderService#getOrderDetailByOrder(dto.order.Order)
	 */
	@Override
	public List<OrderItem> getOrderDetailByOrder(Order order) {
		int langID = foundation.getLanguage();
		return getOrderDetailByOrder(order, langID);
	}

	@Override
	public List<OrderItem> getOrderDetailByOrder(Order order, int langID) {
		// 初始化订单已完成状态对应的状态id
		if(completedStatusid == null){
			completedStatusid = statusService.getIdByName(IOrderStatusService.COMPLETED);
		}
		boolean isCompleted = order.getIstatus() == completedStatusid ? true : false;

		List<OrderDetail> dlist = detailMapper.getOrderDetailByOrderId(order
				.getIid());
		// products
		List<String> ids = Lists.transform(dlist, d -> d.getClistingid());
		List<ProductLite> products = productService.getProductLiteByListingIDs(
				ids, order.getIwebsiteid(), langID);

		Map<String, ProductLite> productMap = Maps.uniqueIndex(products,
				p -> p.getListingId());
		ProductLite product = new ProductLite();
		List<OrderItem> resultItems = Lists.newArrayList();
		for (int i = 0; i < dlist.size(); i++) {
			product = productMap.get(dlist.get(i).getClistingid());
			OrderItem ci = new OrderItem();
			ci.setCid(dlist.get(i).getCid());
			ci.setOrderid(dlist.get(i).getIorderid());
			ci.setClistingid(dlist.get(i).getClistingid());
			ci.setDcreatedate(dlist.get(i).getDcreatedate());
			ci.setIqty(dlist.get(i).getIqty());
			ci.setSku(dlist.get(i).getCsku());
			ci.setWeight(dlist.get(i).getFweight());
			ci.setReview((dlist.get(i).getCommentid() == null && isCompleted));
			Map<String, String> attributeMap = entityMapService
					.getAttributeMap(dlist.get(i).getClistingid(), langID);
			ci.setAttributeMap(attributeMap);
			if (product != null) {
				ci.setCtitle(product.getTitle());
				ci.setCurl(product.getUrl());
				ci.setCimageurl(product.getImageUrl());
			} else {
				ci.setCtitle(dlist.get(i).getCtitle());
			}
			ci.setBismain(dlist.get(i).getCparentid() == null ? true : false);
			ci.setUnitPrice(dlist.get(i).getFprice());
			ci.setTotalPrice(dlist.get(i).getFtotalprices());
			ci.setCparentId(dlist.get(i).getCparentid());
			ci.setOriginalPrice(dlist.get(i).getForiginalprice());
			ci.setChildList(Lists.newArrayList());
			resultItems.add(ci);
		}
		Map<String, OrderItem> cimaps = Maps.uniqueIndex(resultItems,
				p -> p.getCid());
		for (Map.Entry<String, OrderItem> entry : cimaps.entrySet()) {
			if (entry.getValue().getCparentId() != null
					&& cimaps.get(entry.getValue().getCparentId()) != null) {
				cimaps.get(entry.getValue().getCparentId()).getChildList()
						.add(entry.getValue());
				cimaps.get(entry.getValue().getCparentId()).setTotalPrice(
						cimaps.get(entry.getValue().getCparentId())
								.getTotalPrice()
								+ entry.getValue().getTotalPrice());
			}
		}
		List<OrderItem> resultItems2 = Lists.newArrayList();
		for (Map.Entry<String, OrderItem> entry : cimaps.entrySet()) {
			if (entry.getValue().getCparentId() == null) {
				resultItems2.add(entry.getValue());
			}
		}
		return resultItems2;
	}

	@Override
	public boolean isAlreadyPaid(String orderNum) {
		Order order = orderMapper.getOrderByOrderNumber(orderNum);
		if (order != null) {
			if (order.getIstatus() != null) {
				Integer status = order.getIstatus();
				String statusNam = statusService.getOrderStatusNameById(status);
				if (IOrderStatusService.PAYMENT_CONFIRMED.equals(statusNam)
						|| IOrderStatusService.PAYMENT_PROCESSING
								.equals(statusNam)) {
					return true;
				}
			}

			if (order.getIpaymentstatus() != null) {
				if (order.getIpaymentstatus() == 1
						|| order.getIpaymentstatus() == 2) {
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public boolean isAlreadyPaid(Order order) {
		if (order != null) {
			if (order.getIstatus() != null) {
				Integer status = order.getIstatus();
				String statusNam = statusService.getOrderStatusNameById(status);
				if (IOrderStatusService.PAYMENT_CONFIRMED.equals(statusNam)
						|| IOrderStatusService.PAYMENT_PROCESSING
								.equals(statusNam)) {
					return true;
				}
			}

			if (order.getIpaymentstatus() != null) {
				if (order.getIpaymentstatus() == 1
						|| order.getIpaymentstatus() == 2) {
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public List<CartItem> deserializeOrder(String orderNum) {

		return null;
	}

	@Override
	public boolean updateShipAddressAndShipPrice(Order order) {
		boolean flag = true;
		try{
			Map<String, Object> paras = Maps.newHashMap();
			paras.put("email", order.getCemail());
			paras.put("memberEmail", order.getCmemberemail());
			paras.put("countryCode", order.getCcountrysn());
			paras.put("country", order.getCcountry());
			paras.put("street", order.getCstreetaddress());
			paras.put("city", order.getCcity());
			paras.put("province", order.getCprovince());
			paras.put("zipCode", order.getCpostalcode());
			paras.put("firstName", order.getCfirstname());
			paras.put("shippingPrice", order.getFshippingprice());
			paras.put("telephone", order.getCtelephone());
			paras.put("orderNum", order.getCordernumber());
			paras.put("message", order.getCmessage());
			paras.put("grandtotal", order.getFgrandtotal());
			paras.put("lastName", order.getClastname());
			paras.put("cshippingcode", order.getCshippingcode());
			paras.put("shippingMethodId", order.getIshippingmethodid());
			
			int affectColumns = this.orderMapper.update(paras);
			flag =  affectColumns > 0 ? true : false;
		}catch(Exception ex){
			throw new CreateOrderException("sorry ,create order error! error code:UPDATESHIPADDRESSANDSHIPPRICE ");
		}
		
		return flag;
	}

	@Override
	public double getFreight(String orderNum, String shipCode,
			String shipToCountryCode) {
		if (orderNum == null || orderNum.length() == 0) {
			throw new NullPointerException("orderNum is null");
		}
		if (shipCode == null || shipCode.length() == 0) {
			throw new NullPointerException("ship code is null");
		}
		if (shipToCountryCode == null || shipToCountryCode.length() == 0) {
			throw new NullPointerException("shipToCountryCode is null");
		}

		Order order = orderMapper.getOrderByOrderNumber(orderNum);
		// 订单基本总价
		double baseTotal = order.getFordersubtotal();

		List<CartItem> items = orderDetailDao
				.selectCartItemsByOrderNum(orderNum);
		Logger.debug("order items size:{}", items.size());
		ShippingMethod hit = this.checkShippingMethodCorrect(
				order.getIstorageid(), shipToCountryCode, shipCode, baseTotal,
				items, order.getCcurrency(), 1);
		if (hit == null) {
			throw new NullPointerException("ship code not correct");
		}
		return hit.getPrice();
	}

	/**
	 * 2位国家代码+1位订单来源+1位订单类型+5位日期YYMDD+5位时分秒标识+6位随机码
	 * 
	 * @author lijun
	 * @param shipToCountryCode
	 * @param type
	 * @return
	 */
	private String createOrderNumberV2(String shipToCountryCode, String type) {
		String vhost = this.foundation.getVhost();
		// // 订单来源
		String placeholder = vhostService.getCorderplaceholder(vhost);
		if (placeholder == null || placeholder.length() == 0) {
			placeholder = "A";
		}

		StringBuilder number = new StringBuilder();
		if (shipToCountryCode != null && shipToCountryCode.length() > 0) {
			number.append(shipToCountryCode);
			number.append("-");
		}

		number.append(placeholder);
		number.append(type);
		// 系统时间
		SimpleDateFormat formater = new SimpleDateFormat("yy-M-dd-HH-mm-ss");
		Date date = new Date();
		String dateStr = formater.format(date);
		String[] dates = dateStr.split("-");
		// 年
		number.append(dates[0]);
		// 月
		/*
		 * M: I 规则>1到12个月对应 {A,B,C,D,E,F,G,H,I,J,K,L}
		 */
		int month = Integer.parseInt(dates[1]) + 64;
		number.append((char) month);
		// 日
		number.append(dates[2]);
		// hh 时(一位)：0(即24点)点对应X, 24小时制对应字母
		int hhInt = Integer.parseInt(dates[3]);
		int hh = (hhInt == 0 ? 88 : hhInt + 64);
		number.append((char) hh);
		// 分秒
		number.append(dates[4]);
		number.append(dates[5]);
		number.append("-");
		// 6位随机码
		String randomString = this.randomString(6);
		number.append(randomString);
		return number.toString();
	}

	private String randomString(int len) {
		StringBuilder sb = new StringBuilder(len);
		Random random = new Random();
		for (int i = 0; i < len; i++) {
			sb.append(SYMBOLS[random.nextInt(SYMBOLS.length)]);
		}
		return sb.toString();
	}

	@Override
	public String createGeneralOrderNumberV2(String shipToCountryCode) {
		return this.createOrderNumberV2(shipToCountryCode, "N");
	}

	@Override
	public String createGuestOrderNumberV2(String shipToCountryCode) {
		return this.createOrderNumberV2(shipToCountryCode, "G");
	}

	@Override
	public String createAgentOrderNumberV2(String shipToCountryCode) {
		return this.createOrderNumberV2(shipToCountryCode, "D");
	}

	/**
	 * 生成订单号
	 */
	@Override
	public String generateOrderNum() {
		return this.createGeneralOrderNumberV2(null);
	}

	@Override
	public Order getOrderByOrderNumber(String orderNumber) {
		return orderMapper.getOrderByOrderNumber(orderNumber);
	}

	@Override
	public Order getOrderById(int orderId) {
		return orderMapper.getOrderByOrderId(orderId);
	}

	@Override
	public List<OrderDetail> getOrderDetails(Integer orderId) {
		return detailMapper.getOrderDetailByOrderId(orderId);
	}

	@Override
	public List<OrderDetail> getOrderDetails(String orderId) {
		Integer iid = orderMapper.getOrderIdByOrderNumber(orderId);
		if (iid == null) {
			return Lists.newArrayList();
		}
		return detailMapper.getOrderDetailByOrderId(iid);
	}

	@Override
	public PaymentContext getPaymentContext(String orderNum, int langID) {

		Order order = orderMapper.getOrderByOrderNumber(orderNum);
		if (null == order) {
			return null;
		}
		List<OrderDetail> details = detailMapper.getOrderDetailByOrderId(order
				.getIid());
		// recode
		ShippingMethodDetail shippingMethod = null;

		if (StringUtils.isEmpty(order.getCcountrysn())) {
			setShippingAddress(order);
		}
		ConfirmedOrder confirmedOrder = new ConfirmedOrder(order, details);
		Currency currency = currencyService.getCurrencyByCode(order
				.getCcurrency());
		return new PaymentContext(confirmedOrder, shippingMethod, currency);
	}

	public boolean updateOrderPaymentId(Integer iid, String paymentId) {
		int flag = orderMapper.updateOrderPaymentId(iid, paymentId);
		return flag > 0;
	}

	public List<OrderDiscount> getOrderDiscountList(String orderNumber,
			Integer siteid) {
		return orderDiscountMapper.getOrderDiscountList(orderNumber, siteid);
	}

	@Override
	public List<OrderDetail> getOrderDetailsByOrderNum(String orderNum) {
		if (StringUtils.isEmpty(orderNum)) {
			return null;
		}
		return this.detailMapper.getOrderDetailsByOrderNum(orderNum);
	}

	@Override
	public boolean validateInventory(Integer storageid,List<OrderDetail> details) {
		if(CollectionUtils.isEmpty(details)){
			throw new InventoryShortageException("order details is empty");
		}
		for (OrderDetail orderDetail : details) {
			//查询库存，如果购买数量大于库存数量，则失败并提示
			Integer qty = productStorageMapMapper.getQty(orderDetail.getClistingid(), storageid);
			if(qty == null || orderDetail.getIqty() > qty){
				throw new InventoryShortageException(orderDetail.getCsku() + " is not enough");
			}
		}
		return true;
	}

}
