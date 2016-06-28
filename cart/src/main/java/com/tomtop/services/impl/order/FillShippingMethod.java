package com.tomtop.services.impl.order;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.Lists;
import com.tomtop.dto.order.ShippingMethod;
import com.tomtop.services.order.IFillShippingMethod;
import com.tomtop.services.order.IShippingParameterService;

@Service
public class FillShippingMethod implements IFillShippingMethod {

	@Autowired
	private IShippingParameterService service;

	private List<String> freeShippingCode;
	private List<String> pCode;
	private List<String> notSpecialCode;
	private List<String> specailCode;
	private Map<String, Integer> storageIDMap;
	private Double noTrackingEndPrice;
	private Double freeShippingEndPrice;

	/*
	 * (non-Javadoc)
	 * 
	 * @see services.shipping.IFillShippingMethod#fill(java.util.List)
	 */
	@Override
	public List<ShippingMethod> fill(List<ShippingMethod> methods) {
		if (!init()) {
			return Lists.newArrayList();
		}
		return FluentIterable.from(methods).transform(m -> fillMethod(m))
				.filter(m -> m != null).toList();
	}

	private ShippingMethod fillMethod(ShippingMethod method) {
		if (method == null) {
			return null;
		}
		String ccode = method.getCcode();
		if (StringUtils.isEmpty(ccode)) {
			return null;
		}
		method.setBexistfree(freeShippingCode.contains(ccode) ? true : false);
		method.setFfreebeginprice(freeShippingCode.contains(ccode) ? 0.0 : null);
		method.setFfreeendprice(freeShippingCode.contains(ccode) ? freeShippingEndPrice
				: null);
		method.setFbeginprice(0.0);
		method.setFendprice(pCode.contains(ccode) ? noTrackingEndPrice
				: 100000.0);
		if (storageIDMap.get(ccode) == null) {
			method.setBenabled(false);
		} else {
			method.setIstorageid(storageIDMap.get(ccode));
		}
		if (notSpecialCode.contains(ccode)) {
			method.setBisspecial(false);
		} else if (specailCode.contains(ccode)) {
			method.setBisspecial(true);
		}
		if (StringUtils.isEmpty(method.getCrule())) {
			return null;
		} else if (StringUtils.isEmpty(method.getCcountrys())) {
			return null;
		}
		if (StringUtils.isEmpty(method.getCsuperrule())) {
			method.setCsuperrule("$p");
		}
		method.setCsuperrule("(" + method.getCsuperrule() + ")*1.03");
		return method;
	}

	@SuppressWarnings("unchecked")
	private boolean init() {
		Map<String, String> map = service.getAll();
		String freeshipping_code = map.get("freeshipping_code");
		if (StringUtils.isNotEmpty(freeshipping_code)) {
			freeShippingCode = JSONObject.parseArray(freeshipping_code,
					String.class);
			;
		} else {
			freeShippingCode = Lists.newArrayList();
		}

		String p_code = map.get("p_code");
		if (StringUtils.isNotEmpty(p_code)) {
			pCode = JSONObject.parseArray(p_code, String.class);
		} else {
			pCode = Lists.newArrayList();
		}

		String notspecial_code = map.get("notspecial_code");
		if (StringUtils.isNotEmpty(notspecial_code)) {
			notSpecialCode = JSONObject.parseArray(notspecial_code,
					String.class);
		} else {
			notSpecialCode = Lists.newArrayList();
		}

		String specail_code = map.get("specail_code");
		if (StringUtils.isNotEmpty(specail_code)) {
			specailCode = JSONObject.parseArray(specail_code, String.class);
		} else {
			specailCode = Lists.newArrayList();
		}

		String storageid_map = map.get("storageid_map");
		if (StringUtils.isNotEmpty(storageid_map)) {
			storageIDMap = JSONObject.parseObject(storageid_map, Map.class);
		} else {
			return false;
		}

		String notracking_endprice = map.get("notracking_endprice");
		if (StringUtils.isNotEmpty(notracking_endprice)) {
			noTrackingEndPrice = JSONObject.parseObject(notracking_endprice,
					Double.class);
		} else {
			return false;
		}

		String freeshipping_endprice = map.get("freeshipping_endprice");
		if (StringUtils.isNotEmpty(freeshipping_endprice)) {
			freeShippingEndPrice = JSONObject.parseObject(
					freeshipping_endprice, Double.class);
		} else {
			return false;
		}

		return true;
	}
}
