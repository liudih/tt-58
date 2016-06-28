package com.tomtop.services.impl.order;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
import com.tomtop.dao.product.IProductLabelDao;
import com.tomtop.dto.order.ShippingMethod;
import com.tomtop.dto.order.ShippingMethodDetail;
import com.tomtop.dto.order.ShippingParameter;
import com.tomtop.dto.product.ProductLabel;
import com.tomtop.entity.order.ShippingMethodRequst;
import com.tomtop.enums.ProductLabelType;
import com.tomtop.mappers.order.ShippingMethodMapper;
import com.tomtop.services.ICurrencyService;
import com.tomtop.services.base.ILanguageService;
import com.tomtop.services.base.IStorageService;
import com.tomtop.services.filters.IFreightFilter;
import com.tomtop.services.order.IFillShippingMethod;
import com.tomtop.services.order.IFreightService;
import com.tomtop.services.order.IShippingMethodService;
import com.tomtop.services.order.IShippingParameterService;
import com.tomtop.services.product.IProductLabelService;
import com.tomtop.valueobjects.order.ShippingMethodInformation;
import com.tomtop.valueobjects.order.ShippingMethodInformations;

@Service
public class ShippingMethodService implements IShippingMethodService {

	private static final Logger Logger = LoggerFactory
			.getLogger(ShippingMethodService.class);

	@Autowired
	ShippingMethodMapper methodMapper;

	@Autowired
	ILanguageService languageService;

	@Autowired
	IProductLabelService productLabelService;

	@Autowired
	IFreightService freightService;

	@Autowired
	ICurrencyService currencyService;

	@Autowired
	IStorageService storageEnquiryService;

	@Autowired
	Set<IFreightFilter> plugins;

	@Autowired
	IFillShippingMethod fillShippingMethod;

	@Autowired
	IProductLabelDao productLabelEnquiryDao;

	@Autowired
	IShippingParameterService shippingParameterService;

	/*
	 * (non-Javadoc)
	 * 
	 * @see services.shipping.IShippingMethodService#isSpecial(java.util.List)
	 */
	@Override
	public boolean isSpecial(List<String> listingIds) {
		return productLabelService.getListByListingIdsAndType(listingIds,
				ProductLabelType.Special.toString()).size() > 0 ? true : false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * services.shipping.IShippingMethodService#getShippingMethods(java.lang
	 * .Integer, java.lang.String, java.lang.Double, java.lang.Integer,
	 * java.lang.Double, java.lang.Boolean)
	 */
	@Override
	public List<ShippingMethodDetail> getShippingMethods(Integer storageId,
			String country, Double weight, Integer lang, Double subTotal,
			Boolean isSpecial) {
		List<ShippingMethodDetail> list = methodMapper.getShippingMethods(
				storageId, country, weight, lang, subTotal, isSpecial);
		if (list.isEmpty()) {
			list = methodMapper.getShippingMethods(storageId, country, weight,
					languageService.getDefaultLanguage().getIid(), subTotal,
					isSpecial);
		}
		storageId = storageEnquiryService.getNotOverseasStorage().getIid();
		if (list.isEmpty()) {
			return Lists.newArrayList();
		}
		Logger.debug("getShippingMethods result: {}",
				Lists.transform(list, m -> m.getIid()).toString());
		return list;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * services.shipping.IShippingMethodService#getShippingMethodById(java.lang
	 * .Integer)
	 */
	@Override
	public ShippingMethod getShippingMethodById(Integer id) {
		return methodMapper.getShippingMethodById(id);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * services.shipping.IShippingMethodService#getShippingMethodDetail(java
	 * .lang.Integer, java.lang.Integer)
	 */
	@Override
	public ShippingMethodDetail getShippingMethodDetail(Integer id, Integer lang) {
		// modify by lijun
		if (lang == null) {
			lang = languageService.getDefaultLanguage().getIid();
		}
		ShippingMethodDetail detail = methodMapper.getShippingMethodDetail(id,
				lang);
		if (null == detail) {
			detail = methodMapper.getShippingMethodDetail(id, languageService
					.getDefaultLanguage().getIid());
		}
		return detail;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * services.shipping.IShippingMethodService#getShippingMethodDetailByLanguageId
	 * (java.lang.Integer)
	 */
	@Override
	public List<ShippingMethodDetail> getShippingMethodDetailByLanguageId(
			Integer languageId) {
		return methodMapper.getShippingMethodDetailByLanguageId(languageId);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * services.shipping.IShippingMethodService#getShippingMethodDetailByCode
	 * (java.lang.String, java.lang.Integer)
	 */
	@Override
	public ShippingMethodDetail getShippingMethodDetailByCode(String code,
			Integer lang) {
		return methodMapper.getShippingMethodDetailByCode(code, lang);
	}

	public List<ShippingMethod> getShippingMethodsByIds(List<Integer> smIds) {
		return methodMapper.getShippingMethodsByIds(smIds);
	}

	/**
	 * 取所有可以免邮的邮寄地址
	 * 
	 * @return
	 */
	public List<String> getPcodeList() {
		ShippingParameter freecode = shippingParameterService
				.getByKey("p_code");
		List<String> freeShippingCode = Lists.newArrayList();
		if (freecode != null) {
			freeShippingCode = JSONObject.parseArray(freecode.getCjsonvalue(),
					String.class);

		}
		return freeShippingCode;
	}

	/**
	 * 产品是否都是免邮的
	 * 
	 * @param listingids
	 * @return
	 */
	public boolean checkIsAllfree(List<String> listingids) {
		// 设置免邮产品的运费为0
		List<ProductLabel> labellist = this.productLabelEnquiryDao
				.getBatchProductLabel(listingids);
		Multimap<String, ProductLabel> pmap = Multimaps.index(labellist,
				l -> l.getClistingid());
		boolean isAllfree = true;
		for (String lis : pmap.keySet()) {
			List<String> typeList = Lists.transform(
					Lists.newArrayList(pmap.get(lis)), p -> p.getCtype());
			// Logger.debug("typeList==={}==={}",typeList,typeList.contains(ProductLabelType.FreeShipping.toString()));
			if (!typeList.contains(ProductLabelType.FreeShipping.toString())
					&& !typeList.contains(ProductLabelType.AllFreeShipping
							.toString())) {
				isAllfree = false;
				break;
			}
		}
		return isAllfree;
	}

	@Override
	public List<ShippingMethodInformation> processingInPlugin(
			List<ShippingMethodInformation> list, ShippingMethodRequst requst) {
		List<IFreightFilter> pluginList = Lists.newArrayList(plugins);
		Collections.sort(pluginList, (a, b) -> b.order() - a.order());
		for (IFreightFilter plugin : plugins) {
			list = plugin.processing(list, requst);
		}
		return list;
	}
	
	
	@Override
	public ShippingMethodInformations getShippingMethodInformations(
			ShippingMethodRequst requst) {
		Logger.debug("getShippingMethodInformations requst{}", requst);
		Double usdTotal = currencyService.exchange(requst.getGrandTotal(),
				requst.getCurrency(), "USD");
		List<ShippingMethodDetail> shippingMethods = getShippingMethods(
				requst.getStorageId(), requst.getCountry(), requst.getWeight(),
				requst.getLang(), usdTotal, requst.getIsSpecial());
		//邮寄地址为空
		if(shippingMethods.size()==0){
			return new ShippingMethodInformations(Lists.newArrayList());
		}
				
		List<ShippingMethodInformation> temp = Lists.transform(
				shippingMethods,
				e -> {
					Double freight = freightService.getFinalFreight(e,
							requst.getWeight(), requst.getShippingWeight(),
							requst.getCurrency(), requst.getGrandTotal(),
							requst.isHasAllFreeshipping());
					if (null == freight) {
						return null;
					}
					
					String shippingContext = e.getCcontent();
					return new ShippingMethodInformation(e, shippingContext,
							freight);
				});
		Collection<ShippingMethodInformation> informations = Collections2
				.filter(temp, e -> {
					return e != null;
				});
		List<ShippingMethodInformation> list = filterShippingMethod(
				informations, requst.getIsSpecial());
		list = processingInPlugin(list, requst);
		
		Collections.sort(list,
				(a, b) -> (int) ((a.getFreight() - b.getFreight()) * 100));
		
		return new ShippingMethodInformations(list);
	}
	
	/**
	 * <ul>
	 * 过滤
	 * <li>同一code只留价格最高的一个</li>
	 * <li>同一groupid如果存在特殊和非特殊的方式，则去掉特殊的，取非特殊中最高的价格</li>
	 * <li>同一groupid若不存在特殊和给特殊的方式，则留价格最高的一个</li>
	 * </ul>
	 * 
	 * @param list
	 * @param isSpecial
	 * @return
	 */
	private List<ShippingMethodInformation> filterShippingMethod(
			Collection<ShippingMethodInformation> list, Boolean isSpecial) {
		Map<String, ShippingMethodInformation> map = Maps.newHashMap();
		// 按code进行过滤
		for (ShippingMethodInformation e : list) {
			if (null == map.get(e.getCode())) {
				map.put(e.getCode(), e);
			} else {
				if (e.getFreight() > map.get(e.getCode()).getFreight()) {
					map.put(e.getCode(), e);
				}
			}
		}
		Collection<ShippingMethodInformation> tempCollection = map.values();
		Map<Integer, ShippingMethodInformation> groupMap = Maps.newHashMap();
		// 按groupid尽心过滤
		for (ShippingMethodInformation e : tempCollection) {
			ShippingMethodInformation smi = groupMap.get(e.getGroupId());
			if (null == smi) {
				groupMap.put(e.getGroupId(), e);
			} else {
				if (!isSpecial) {// 订单为非特殊的过滤
					if (!smi.isSpecial() && !e.isSpecial()
							&& e.getFreight() > smi.getFreight()) {
						// 二者都是非特殊，比较费用，费用多的代替费用少的
						groupMap.put(e.getGroupId(), e);
					} else if (smi.isSpecial() && !e.isSpecial()) {
						// 当前为特殊，遍历到非特殊，用非特殊代替特殊
						// 当前为非特殊，遍历到特殊，不做处理
						groupMap.put(e.getGroupId(), e);
					} else if (smi.isSpecial() && e.isSpecial()
							&& e.getFreight() > smi.getFreight()) {
						// 二者都是特殊，比较费用，费用多的代替费用少的
						groupMap.put(e.getGroupId(), e);
					}
				} else {// 订单为特殊的过滤
					if (e.getFreight() > smi.getFreight()) {
						groupMap.put(e.getGroupId(), e);
					}
				}
			}
		}
		return Lists.newArrayList(groupMap.values());
	}
}
