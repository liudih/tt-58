package com.tomtop.service.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tomtop.common.utils.DoubleCalculateUtils;
import com.tomtop.service.ICurrencyService;

/**
 *
 * @ClassName: FreightService
 * @Description: 有关运费计算的服务 如果exchangeRate == null，则默认从foundationService
 *               currencyService 中取得当前货币汇率 重量单位为：克
 * @author luojiaheng
 * @date 2015年1月20日 上午10:03:58
 *
 */
@Service
public class FreightService{
	private static final Logger log=Logger.getLogger(FreightService.class.getName());
	
	private static final ScriptEngine engine = new ScriptEngineManager(
			ClassLoader.getSystemClassLoader()).getEngineByName("nashorn");
	@Autowired
	ICurrencyService currencyService;
	
	public Double getFreight(HashMap<String, String> subs,String rule,String superRule,
			Double weight, String currency,Double extra) {
		
		try{
			
			
			Double p = getFirstFreight(rule, weight);
			if (null == p) {
				return null;
			}
			subs.put("\\$p", p.toString());
			
			Double temp = runJS(subs, superRule);
			if(temp==null){
				return 0.0;
			}
			//这个地方都是以人民币来核算的
			if(extra!=null && extra>0){//调整附加费用
				temp=temp+extra;
			}
			
			Double freight = currencyService.exchange(temp, "CNY", currency);
			if ("JPY".equals(currency)) {
				BigDecimal b1 = new BigDecimal(freight);
				b1.setScale(0, RoundingMode.HALF_UP);
				return new Double(b1.intValue());
			}
			return new DoubleCalculateUtils(freight).doubleValue();
		}catch(Exception e){
			log.error("getFreight rule:"+rule+  "  superRule:"+superRule+"  weight:"+weight+"   currency:"+currency+"  extra:"+extra,e);
			return 0.0;
		}
	}
	public Double getFirstFreight(String rule,
			Double weight) {
		String value = rule; // 要运算的公式,包含变量
		//String value = shippingMethod.getCrule(); // 要运算的公式,包含变量
		// 哈希, key为要代替公式中变量的正则，value为为要代替公式中变量的实际值
		if (StringUtils.isEmpty(value)) {
			return null;
		}
		HashMap<String, String> subs = new HashMap<String, String>();
		subs.put("\\$w", weight.toString());
		return runJS(subs, value);
	}
	private Double runJS(HashMap<String, String> subs, String ruleValue) {
		try {
			subs.put("ceil", "Math.ceil");
			subs.put("floor", "Math.floor");
			for (String subKey : subs.keySet()) {
				ruleValue = ruleValue.replaceAll(subKey, subs.get(subKey));
			}
			Double cost = new Double(String.valueOf(engine.eval(ruleValue)));
			DoubleCalculateUtils duti = new DoubleCalculateUtils(cost);
			return duti.doubleValue();
		} catch (Exception e) {
			log.error("****************************************************************");
			log.error("* Run Js Error !");
			log.error("* Rule: " + ruleValue);
			log.error("****************************************************************");
			log.error("runJS Exception Details", e);
			return null;
		}
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

}
