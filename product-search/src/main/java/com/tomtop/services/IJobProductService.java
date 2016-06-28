package com.tomtop.services;

import java.util.List;

import com.tomtop.entity.DealsCategory;
import com.tomtop.entity.TopSellersProduct;

public interface IJobProductService {

	public List<TopSellersProduct> getTopSellersProduct(Integer website,Integer lang);
	public List<DealsCategory> getDealsCategory(Integer website,Integer lang);
}
