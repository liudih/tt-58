package com.tomtop.services.payment.ocean;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import com.tomtop.dto.order.BillDetail;
import com.tomtop.dto.order.Order;
import com.tomtop.dto.order.OrderDetail;
import com.tomtop.mappers.order.PaymentAccountMapper;
import com.tomtop.services.impl.CurrencyService;
import com.tomtop.services.impl.base.SystemParameterService;
import com.tomtop.services.order.IBillDetailService;
import com.tomtop.services.order.IOrderService;
import com.tomtop.utils.Encryption;
import com.tomtop.utils.FoundationService;
import com.tomtop.valueobjects.payment.ocean.OceanPaymentResult;
import com.tomtop.valueobjects.payment.ocean.SummaryInformation;

@Service
public class OceanPaymentService {

	private static final Logger Logger = LoggerFactory
			.getLogger(OceanPaymentService.class);

	@Autowired
	IBillDetailService billDetailService;

	@Autowired
	IOrderService orderService;

	@Autowired
	Encryption encryption;

	@Autowired
	PaymentAccountMapper accountMapper;

	@Autowired
	CurrencyService currencyService;

	@Autowired
	SystemParameterService systemParameterService;

	@Autowired
	FoundationService foundationService;

	public List<SummaryInformation> getInformations(Integer orderId) {
		List<SummaryInformation> list = new ArrayList<SummaryInformation>();
		List<BillDetail> temp = billDetailService.getExtraBill(orderId);
		Map<String, BillDetail> map = Maps.uniqueIndex(temp, e -> e.getCtype());
		Collections.sort(list,
				(a, b) -> a.getDisplayOrder() - b.getDisplayOrder());
		return list;
	}

	/**
	 * 将order post到ocean所需的signvalue
	 *
	 * @param form
	 * @param secureCode
	 * @return
	 */
	public String getOceanPostSignValue(Map<String, String> form,
			String secureCode) {
		String text = form.get("account") + form.get("terminal")
				+ form.get("backUrl") + form.get("order_number")
				+ form.get("order_currency") + form.get("order_amount")
				+ form.get("billing_firstName") + form.get("billing_lastName")
				+ form.get("billing_email") + secureCode;
		return encryption.encodeSHA256(text);
	}

	/**
	 * 验证ocean返回数据所需的signvalue
	 *
	 * @param form
	 * @param secureCode
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public String getOceanValidSignValue(OceanPaymentResult result,
			String orderNum) {
		Order order = orderService.getOrderByOrderNumber(orderNum);
		if (order == null) {
			return null;
		}
		int site = foundationService.getSiteID();

		String ccy = order.getCcurrency();
		double usdgrandprice = currencyService.exchange(order.getFgrandtotal(),
				ccy, "USD");
		String paymentid = order.getCpaymentid(); // 支付方式
		String paramValue = systemParameterService.getSystemParameter(site,
				null, "credit_" + ccy);
		double grandprice = order.getFgrandtotal();
		if (paramValue == null) {
			grandprice = usdgrandprice;
			paramValue = systemParameterService.getSystemParameter(site, null,
					"credit_USD", "300");
		}
		double pricelimit = Double.parseDouble(paramValue);

		Logger.debug("pricelimit====={},{}", ccy, pricelimit);
		// 如果大于某金额就进入信用卡3D验证支付
		if (grandprice > pricelimit) {
			Logger.debug("goto  credit_3D_payment+++ completed");
			paymentid = OceanPaymentCreditPaymentProvider.credit_3D_payment;
		}
		String accountString = accountMapper.getAccount(1, usdgrandprice,
				paymentid);
		if (StringUtils.isEmpty(accountString)) {
			return null;
		}
		LinkedHashMap<String, String> account = JSONObject.parseObject(
				accountString, LinkedHashMap.class);
		String text = result.getAccount() + result.getTerminal()
				+ result.getOrder_number() + result.getOrder_currency()
				+ result.getOrder_amount() + result.getOrder_notes()
				+ result.getCard_number() + result.getPayment_id()
				+ result.getPayment_authType() + result.getPayment_status()
				+ result.getPayment_details() + result.getPayment_risk()
				+ account.get("secureCode");
		return encryption.encodeSHA256(text);
	}

	public Map<String, String> getOceanProductMap(List<OrderDetail> list) {
		Map<String, String> map = Maps.newHashMap();
		StringBuffer productSku = new StringBuffer();
		StringBuffer productName = new StringBuffer();
		StringBuffer productNum = new StringBuffer();
		for (int i = 0; i < list.size(); i++) {
			OrderDetail detail = list.get(i);
			if (i == list.size() - 1) {
				productSku.append(detail.getCsku());
				productName.append(detail.getCtitle());
				productNum.append(detail.getIqty());
			} else {
				productSku.append(detail.getCsku() + ";");
				productName.append(detail.getCtitle() + ";");
				productNum.append(detail.getIqty() + ";");
			}
		}
		map.put("productSku", productSku.toString());
		map.put("productName", productName.toString());
		map.put("productNum", productNum.toString());
		return map;
	}
}
