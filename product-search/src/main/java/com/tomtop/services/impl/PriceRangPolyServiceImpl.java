package com.tomtop.services.impl;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import javax.annotation.PostConstruct;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.tomtop.entity.PriceRangePoly;
import com.tomtop.mappers.mysql.PriceRangePolyMapper;
import com.tomtop.services.IPriceRangPolyService;


@Service
public class PriceRangPolyServiceImpl implements IPriceRangPolyService {
	
	@Autowired
	PriceRangePolyMapper priceRangeMapper;
	
	@Value("${cleanBaseTime}")
	private Long cleanBaseTime;
	
	public static Map<Integer, List<PriceRangePoly>> priceRangeMap;
	
	@PostConstruct
	public void init() {
		priceRangeMap = getPriceRangePolyMap();
		new Timer().schedule(new TimerTask() {
			@Override
			public void run() {
				priceRangeMap = getPriceRangePolyMap();
				LoggerFactory.getLogger(this.getClass()).info(
						"clean price range");
			}
		}, cleanBaseTime * 1000, cleanBaseTime * 1000);
	}
	
	public Map<Integer, List<PriceRangePoly>> getPriceRangePolyMap() {
		Map<Integer, List<PriceRangePoly>> pmap = new HashMap<Integer, List<PriceRangePoly>>();
		List<PriceRangePoly> prList = priceRangeMapper.getPriceRangePolyList();
		if(prList != null && prList.size() > 0){
			for (PriceRangePoly priceRangePoly : prList) {
				if(pmap.containsKey(priceRangePoly.getClient())){
					List<PriceRangePoly> list = pmap.get(priceRangePoly.getClient());
					list.add(priceRangePoly);
					pmap.put(priceRangePoly.getClient(), list);
				}else{
					List<PriceRangePoly> newlist = new ArrayList<PriceRangePoly>();
					newlist.add(priceRangePoly);
					pmap.put(priceRangePoly.getClient(), newlist);
				}
			}
		}
		return pmap;
	}
	@Override
	public Map<Integer, List<PriceRangePoly>> getPriceRangeMap() {
		if (priceRangeMap == null || priceRangeMap.isEmpty()) {
			priceRangeMap = getPriceRangePolyMap();
			if (priceRangeMap != null && priceRangeMap.size() > 0) {
				return priceRangeMap;
			}
		} else {
			return priceRangeMap;
		}
		return null;
	}
}
