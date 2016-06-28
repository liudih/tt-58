package com.tomtop.services.impl.product.price;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Iterables;
import com.google.common.collect.ListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimaps;
import com.google.common.collect.Ordering;
import com.google.common.collect.Sets;
import com.tomtop.dto.Currency;
import com.tomtop.services.ICurrencyService;
import com.tomtop.services.product.IPriceProvider;
import com.tomtop.services.product.IPriceService;
import com.tomtop.services.product.IProductService;
import com.tomtop.valueobjects.CartItem;
import com.tomtop.valueobjects.product.price.Price;
import com.tomtop.valueobjects.product.price.PriceBuilder;
import com.tomtop.valueobjects.product.price.PriceCalculationContext;
import com.tomtop.valueobjects.product.price.PriceNew;
import com.tomtop.valueobjects.product.spec.IProductSpec;
import com.tomtop.valueobjects.product.spec.ProductSpecBuilder;

@Service
public class PriceService implements IPriceService {

	private static final Logger Logger = LoggerFactory
			.getLogger(PriceService.class);

	@Autowired
	Set<IPriceProvider> priceProviders;

	@Autowired
	Set<IDiscountProvider> discountProviders;

	@Autowired
	ICurrencyService currencyService;

	@Autowired
	PriceCalculationContextFactory ctxFactory;
	
	@Autowired
	IProductService productService;

	@Override
	public Price getPrice(String listingID, int storageId) {
		return getPrice(ProductSpecBuilder.build(listingID).get(),
				ctxFactory.create(storageId));
	}

	@Override
	public Price getPrice(String listingID, String ccy, int storageId) {
		return getPrice(ProductSpecBuilder.build(listingID).get(),
				ctxFactory.create(ccy, storageId));
	}

	@Override
	public Price getPrice(IProductSpec item, int storageId) {
		return getPrice(item, ctxFactory.create(storageId));
	}

	@Override
	public Price getPrice(IProductSpec item, String ccy, int storageId) {
		return getPrice(item, ctxFactory.create(ccy, storageId));
	}

	public Price getPrice(IProductSpec item, PriceCalculationContext ctx) {
		List<Price> prices = getPrice(Lists.newArrayList(item), ctx);
		if (prices != null && prices.size() == 1) {
			return prices.get(0);
		}
		return null;
	}

	@Override
	public List<Price> getPrice(List<IProductSpec> items, int storageId) {
		return getPrice(items, ctxFactory.create(storageId));
	}

	@Override
	public List<Price> getPrice(List<IProductSpec> items, Date priceAt,
			int storageId) {
		PriceCalculationContext ctx = ctxFactory.create(storageId);
		if (priceAt != null) {
			ctx.setPriceAt(priceAt);
		}
		return getPrice(items, ctx);
	}

	@Override
	public List<Price> getPrice(List<IProductSpec> items, Date priceAt,
			String currency, int storageId) {
		PriceCalculationContext ctx = ctxFactory.create(currency, storageId);
		if (priceAt != null) {
			ctx.setPriceAt(priceAt);
		}
		return getPrice(items, ctx);
	}

	@Override
	public List<Price> getPrice(List<IProductSpec> items,
			final PriceCalculationContext ctx) {
		Collection<List<Price>> values = getPriceByProvider(items, ctx)
				.values();
		List<Price> flatten = Lists.newLinkedList();
		for (List<Price> p : values) {
			flatten.addAll(p);
		}
		return flatten;
	}

	@Override
	public Map<IPriceProvider, List<Price>> getPriceByProvider(
			final List<IProductSpec> items, int storageId) {
		return getPriceByProvider(items, ctxFactory.create(storageId));
	}

	public Map<IPriceProvider, List<Price>> getPriceByProvider(
			final List<IProductSpec> items, final PriceCalculationContext ctx) {
		Map<IPriceProvider, List<Price>> priceMap = Maps.asMap(
				priceProviders,
				pp -> {
					List<IProductSpec> filtered = Lists.newArrayList(Iterables
							.filter(items, i -> pp.match(i)));
					List<Price> prices = pp.getPrice(filtered, ctx);
					return prices;
				});

		List<IDiscountProvider> dps = Lists.newArrayList(discountProviders);
		Collections.sort(dps, (Comparator<IDiscountProvider>) (a, b) -> (a
				.getPriority() - b.getPriority()));
		Map<IPriceProvider, List<Price>> discounted = Maps.transformValues(
				priceMap, (List<Price> lp) -> {
					List<Price> current = lp;
					for (IDiscountProvider dp : dps) {
						current = dp.decorate(current, ctx);
					}
					return current;
				});
		return discounted;
	}
	
	public Map<String, Price> getPriceByProvider(final List<CartItem> items, String ccy,Integer storageId) {
		
		Map<String, Price> reuslt = new HashMap<String, Price>();
		
		if(CollectionUtils.isEmpty(items)){
			return reuslt;
		}

		//获取所有listingid
		List<String> listingIds = Lists.newArrayList(Sets.newHashSet(Lists
				.transform(items, i -> i.getClistingid())));
		
		//购物车产品根据listingid分组
		Map<String, CartItem> cartMap = Maps.uniqueIndex(items,
				p -> p.getClistingid());
		//查询货币信息
		Currency currency = currencyService.getCurrencyByCode(ccy);
		//查询汇率信息
		double rate = currencyService.getRate(ccy);

		//查询价格
		List<PriceNew> priceList = productService.queryPrice(listingIds, storageId);

		//价格根据listingid分组
		ListMultimap<String, PriceNew> priceMap = Multimaps.index(priceList,
				p -> p.getListingId());
		
		Map<String, Collection<PriceNew>> priceListMap = priceMap.asMap();
		Iterator<String> iterator = priceListMap.keySet().iterator();
		while(iterator.hasNext()){
			String listingId = iterator.next();
			Collection<PriceNew> priceNewes = priceListMap.get(listingId);
			//取最近的促销价
			PriceNew priceNew = Ordering.natural()
					.onResultOf((PriceNew pp) -> pp.getSalePriceEnd()).min(priceNewes);
			
			CartItem cartItem = cartMap.get(listingId);
			IProductSpec spec = ProductSpecBuilder
					.build(cartItem.getClistingid()).setQty(cartItem.getIqty())
					.get();
			//设置产品基础价格
			Price price = PriceBuilder.build(spec, priceNew.getPrice(), priceNew.getCostPrice())
					.inCurrency(
							ccy,
							currency.getCsymbol(),
							rate)
					.get();
			//设置产品促销价格
			if (priceNew.getSalePrice() > 0 && priceNew.getSalePrice() < priceNew.getPrice()) {
				price = PriceBuilder
						.change(price)
						.withNewPrice(priceNew.getSalePrice(),
								priceNew.getSalePriceBegin(),
								priceNew.getSalePriceEnd()).get();
			}
			reuslt.put(listingId,price);
		}
		return reuslt;
	}
}
