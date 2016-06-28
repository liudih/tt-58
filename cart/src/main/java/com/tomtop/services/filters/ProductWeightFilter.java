package com.tomtop.services.filters;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.FluentIterable;
import com.google.common.collect.Lists;
import com.tomtop.entity.order.ShippingMethodRequst;
import com.tomtop.services.product.IProductService;
import com.tomtop.valueobjects.order.ShippingMethodInformation;
import com.tomtop.valueobjects.product.Weight;

@Service
public class ProductWeightFilter implements IFreightFilter {
	
	@Autowired
	private IProductService productService;

	@Override
	public List<ShippingMethodInformation> processing(
			List<ShippingMethodInformation> list, ShippingMethodRequst requst) {
		if (requst.getListingIds() == null || requst.getListingIds().isEmpty()) {
			return Lists.newArrayList();
		}
		List<Weight> weights = productService.getWeightList(requst
				.getListingIds());
		if (!FluentIterable.from(weights).filter(w -> w.getWeight() > 2000)
				.isEmpty()) {
			return Lists.newArrayList(FluentIterable.from(list).filter(
					s -> !s.getCode().contains("P")));
		}
		return list;
	}

	@Override
	public int order() {
		return 20;
	}

}
