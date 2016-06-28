package com.tomtop.services.cart;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.tomtop.dto.product.ProductStorageMap;
import com.tomtop.mappers.product.ProductStorageMapMapper;
import com.tomtop.services.impl.product.EntityMapService;
import com.tomtop.services.impl.product.ProductEnquiryService;
import com.tomtop.services.impl.product.price.PriceService;
import com.tomtop.services.product.IProductService;
import com.tomtop.utils.DoubleCalculateUtils;
import com.tomtop.utils.FoundationService;
import com.tomtop.valueobjects.BundleCartItem;
import com.tomtop.valueobjects.CartItem;
import com.tomtop.valueobjects.SingleCartItem;
import com.tomtop.valueobjects.product.ProductLite;
import com.tomtop.valueobjects.product.price.Price;
import com.tomtop.valueobjects.product.spec.IProductSpec;
import com.tomtop.valueobjects.product.spec.ProductSpecBuilder;

@Service
public class CartService implements ICartService {

	Logger logger = Logger.getLogger(CartService.class);

	@Autowired
	FoundationService foundationService;
	@Autowired
	ProductEnquiryService productEnquiryService;
	@Autowired
	EntityMapService entityMapService;
	@Autowired
	PriceService priceService;
	@Autowired
	IProductService productService;
	@Autowired
	ProductStorageMapMapper productStorageMapMapper;

	@Override
	public List<CartItem> getCartItems(List<CartItem> items, int siteID,int languageID, String ccy,int storageId) {
		if(items==null || items.size()<1){
			return new ArrayList<CartItem>();
		}
		
		// 取得产品信息
//		List<ProductLite> products = productEnquiryService.getProductLiteForListingIDsAndStorageId(items, siteID, languageID,storageId);
		
		// 从接口获取产品信息(实现缓存机制)
		List<ProductLite> products = productService.getProductLiteListFromAPI(items, languageID, siteID, storageId);
		
		if(products==null || products.size()<1){
			return new ArrayList<CartItem>();
		}
		
		Map<String, ProductLite> productMap = new HashMap<String, ProductLite>();
		for (ProductLite productLite : products) {
			productMap.put(productLite.getListingId(), productLite);
		}
		//查询产品价格
		Map<String, Price> priceMap = priceService.getPriceByProvider(items, ccy, storageId);

		for (CartItem ci : items) {
			ci.setPrice(priceMap.get(ci.getClistingid()));
			ProductLite p = productMap.get(ci.getClistingid());
			if (p != null) {
				ci.setCtitle(p.getTitle());
				ci.setCurl(p.getUrl());
				ci.setCimageurl(p.getImageUrl());
				ci.setSku(p.getSku());
				ci.setIstatus(p.getIstatus());
				ci.setAttributeMap(p.getAttributeMap());
			}
		}
		return items;
	}
	
	@Override
	public List<CartItem> getCartItems(List<CartItem> items, int siteID,int languageID, String ccy) {
		
		if(items==null || items.size()<1){
			return new ArrayList<CartItem>();
		}
		
		List<String> listingids = Lists.newLinkedList();
		for (CartItem it : items) {
			listingids.add(it.getClistingid());
		}

		// 取得产品信息
		List<ProductLite> products = productEnquiryService.getProductLiteByListingIDs(listingids, siteID, languageID);
		Map<String, ProductLite> productMap = Maps.uniqueIndex(products,p -> p.getListingId());
		for (CartItem ci : items) {
			IProductSpec spec = ProductSpecBuilder.build(ci.getClistingid()).setQty(ci.getIqty()).get();
			
			ci.setPrice(priceService.getPrice(spec, ccy, ci.getStorageID()));
			ProductLite p = productMap.get(ci.getClistingid());
			if (p != null) {
				ci.setCtitle(p.getTitle());
				ci.setCurl(p.getUrl());
				ci.setCimageurl(p.getImageUrl());
				ci.setSku(p.getSku());
				ci.setIstatus(p.getIstatus());
				Map<String, String> attributeMap = entityMapService.getAttributeMap(ci.getClistingid(), languageID);
				ci.setAttributeMap(attributeMap);
			}
		}
		return items;
	}

	@Override
	public double getTotal(List<CartItem> items) {
		DoubleCalculateUtils duti = new DoubleCalculateUtils(0.0);
		for (CartItem ci : items) {
			if (ci.getPrice() != null) {
				duti = duti.add(ci.getPrice().getPrice());
			}
		}
		return duti.doubleValue();
	}

	@Override
	public boolean isEnoughQty(CartItem cartItem) {
		if (cartItem instanceof SingleCartItem) {
			if (productService.checkInventory(cartItem.getClistingid(),
					cartItem.getIqty()) && cartItem.getIqty() <= 999) {
				return true;
			}
		} else if (cartItem instanceof BundleCartItem) {
			List<SingleCartItem> blist = ((BundleCartItem) cartItem)
					.getChildList();
			for (int i = 0; i < blist.size(); i++) {
				if (!productService.checkInventory(blist.get(i)
						.getClistingid(), blist.get(i).getIqty())
						&& blist.get(i).getIqty() > 999) {
					return false;
				}
			}
			return true;
		}
		return false;
	}
	
	@Override
	public boolean checkCartItem(String listingids, Integer storageid){
		if(StringUtils.isBlank(listingids) || storageid==null){
			return false;
		}
		String[] lisarr = listingids.split(",");
		List<String> lislist = new ArrayList<String>();
		Map<String, Integer> lismap = new HashMap<String, Integer>();
		for(String item : lisarr){
			String[] itemarr = item.split(":");
			if(itemarr.length!=2){
				return false;
			}
			lislist.add(itemarr[0]);
			lismap.put(itemarr[0], Integer.parseInt(itemarr[1]));
		}
		List<ProductStorageMap> slist = productStorageMapMapper.getProductStorageMaps(lislist, storageid);
		
		boolean ispass = true;
		for(ProductStorageMap ps : slist){
			if(ps==null || ps.getIstatus()==null || ps.getIstatus()!=1
					|| ps.getIqty()<lismap.get(ps.getClistingid())){
				ispass = false;
				break;
			}
		}
		return ispass;
	}

}
