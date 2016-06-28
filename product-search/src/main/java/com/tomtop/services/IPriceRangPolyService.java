package com.tomtop.services;


import java.util.List;
import java.util.Map;

import com.tomtop.entity.PriceRangePoly;


public interface IPriceRangPolyService {

	public Map<Integer, List<PriceRangePoly>> getPriceRangeMap();
}
