package com.tomtop.loyalty.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Maps;
import com.tomtop.loyalty.models.Product;
import com.tomtop.loyalty.models.filter.ProductFilter;
import com.tomtop.loyalty.models.third.ExchangeCurrency;
import com.tomtop.loyalty.models.third.ProductThird;
import com.tomtop.loyalty.utils.ClientUtil;
import com.tomtop.loyalty.utils.CommonUtils;

@Service
public class ThirdService {

	@Value("${thirdProductUrl}")
	private String thirdProductUrl;

	@Value("${thirdBaseUrl}")
	private String thirdBaseUrl;

	/**
	 * vo转bo对象
	 * 
	 * @param productVo
	 * @return
	 */
	public List<Product> convert(List<ProductFilter> productVo,Integer website,Integer depotId) {

		List<Product> result = new ArrayList<Product>();
		Map<String, ProductFilter> productVoMap = Maps.uniqueIndex(productVo,
				p -> p.getListingId());
		String listingId = "";
		if (CollectionUtils.isNotEmpty(productVo)) {
			for (int i = 0; i < productVo.size(); i++) {
				listingId += productVo.get(i).getListingId() + ",";
			}
		}
		listingId = listingId.substring(0, listingId.length() - 1);
		ProductThird thirdResult = new ProductThird();
		if(1==website){//tomotp
			thirdResult = ClientUtil.getRequestEntity(thirdProductUrl
					+ "/ic/v2/call/product/lable/" + listingId+"?website="+website+"&depotId="+depotId, ProductThird.class);
		}else{//chicuu
			 thirdResult = ClientUtil.getRequestEntity(thirdProductUrl
					+ "/ic/v1/call/product/lable/" + listingId+"?website="+website, ProductThird.class);
		}

		if (thirdResult.getRet() == CommonUtils.SUCCESS_RES) {
			Map<String, Product> map = thirdResult.getData();
			map.keySet().forEach(p -> {
				if (productVoMap.containsKey(p)) {
					Product product = map.get(p);
					product.setPrice(productVoMap.get(p).getPrice());
					product.setQty(productVoMap.get(p).getQty());
					product.setListingId(p);
					result.add(product);
				}
			});
		}

		return result;
	}

	public static void main(String[] args) {
	    float f = 5.5f;  
	    int intBits = Float.floatToIntBits(f);  
	    System.out.println(Integer.toBinaryString(intBits));  
	}
	/**
	 * 币种转换
	 * 
	 * @param amount
	 * @param originCurrencyId
	 * @param targetCurrency
	 * @return
	 */
	public String exchangeCurrency(Double amount, String originCurrency,
			String targetCurrency) {
		String result = ClientUtil.getRequest(thirdBaseUrl
				+ "/base/currency/v1/exchange?money=" + amount + "&oldCy="
				+ originCurrency + "&newCy=" + targetCurrency);
		ExchangeCurrency amountChange = JSON.parseObject(result,
				ExchangeCurrency.class);
		if (null == amountChange
				|| amountChange.getRet() == CommonUtils.ERROR_RES) {
			return null;
		}
		// ExchangeCurrency amountChange = ClientUtil.getRequestEntity(
		// thirdBaseUrl + "/base/currency/v1/exchange?money=" + amount
		// + "&oldCy=" + originCurrency + "&newCy="
		// + targetCurrency, ExchangeCurrency.class);
		String amountString = amountChange.getData();
		return amountString;
	}

}
