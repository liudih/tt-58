package com.tomtop.services.impl.product;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.chain.web.WebContext;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.tomtop.CommonConstant;
import com.tomtop.dto.product.ProductBase;
import com.tomtop.mappers.product.ProductBaseMapper;
import com.tomtop.services.product.IProductService;
import com.tomtop.utils.HttpClientUtil;
import com.tomtop.valueobjects.CartItem;
import com.tomtop.valueobjects.product.Product4API;
import com.tomtop.valueobjects.product.ProductLite;
import com.tomtop.valueobjects.product.Weight;
import com.tomtop.valueobjects.product.price.PriceNew;

@Service
public class ProductService implements IProductService {

	@Autowired
	ProductBaseMapper productBaseMapper;

	@Autowired
	private ApplicationContext context;

	private static final Logger Logger = LoggerFactory
			.getLogger(ProductService.class);

	@Override
	public ProductBase getBaseByListingIdAndLanguage(String listingId,
			Integer languageId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ProductBase getBaseByListingId(String listingId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getListingIdByParentSkuAndWebsiteIdAndStatusAndIsMain(
			String parentsku, Integer isstatus, Integer websiteId,
			boolean ismain) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ProductBase> getProductsWithSameParentSkuMatchingAttributes(
			String listingID, WebContext context) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<String> selectListingidBySearchNameAndSort(WebContext context,
			String searchname, String sort, Integer categoryId,
			List<String> pcListingIds) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ProductBase getProductByListingIdAndLanguageWithdoutDesc(
			String listingId, Integer languageId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getProductDescByListingIdAndLanguagePart(String listingId,
			Integer languageId, int begin, int len) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ProductBase> getProductBasesByListingIds(List<String> listingids) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ProductLite> getProductLiteByListingIDs(
			List<String> listingIDs, int websiteID, int languageID) {
		if (listingIDs != null && listingIDs.size() > 0) {
			List<ProductLite> plist = Lists.newArrayList();
			try {
				plist = productBaseMapper.getProductByListingIDs(listingIDs,
						websiteID, languageID);
			} catch (Exception ex) {
				Logger.error(ex.toString());
			}
			return plist;
		}
		return Lists.newArrayList();
	}

	@Override
	public List<ProductBase> getProductBaseBySkus(List<String> skus,
			Integer siteid) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getCountBundleProduct(String main, String bundle) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public List<Weight> getWeightList(List<String> listingIds) {
		// modify by lijun
		if (listingIds == null || listingIds.size() == 0) {
			throw new NullPointerException("listingIds is null");
		}
		return productBaseMapper.getWeightByListingIDs(listingIds);
	}

	/**
	 * 检查库存是否足够
	 * 
	 * @param listingID
	 * @param qty
	 * @return
	 */
	public boolean checkInventory(String listingID, Integer qty) {
		int inv = productBaseMapper.getQtyByListingID(listingID);
		boolean res = false;
		if (null == qty && inv > 0) {
			res = true;
		} else if (null != qty && inv >= qty) {
			res = true;
		}
		return res;
	}

	@Override
	public List<Product4API> getProductListFromAPI(String listingIdsStr,
			int lang, int website, int storage) {
		List<Product4API> list = new ArrayList<Product4API>();
		// 请求参数
		String param = "?listingIds=" + listingIdsStr + "&lang=" + lang
				+ "&website=" + website + "&depotId=" + storage;
		String result = HttpClientUtil.doGet(CommonConstant.PRODUCT_URL
				+ CommonConstant.PRODUCT_DETAIL_URL + param);
		if (result != null && !"".equals(result)) {
			JSONObject object = JSON.parseObject(result);
			Integer ret = object.getInteger("ret");
			if (ret != null && ret == 1) {
				JSONArray date = object.getJSONArray("data");
				list = JSON.parseArray(date.toJSONString(), Product4API.class);
			}
		}
		
		return list;
	}

	@Override
	public List<Product4API> getProductListFromAPI(List<CartItem> items,
			int lang, int website, int storage) {
		// 此处需要走代理对象，不然不走缓存
		IProductService bean = (IProductService) context
				.getBean("productService");
		List<String> noCacheIds = new ArrayList<String>();
		List<Product4API> list = new ArrayList<Product4API>();
		for (CartItem item : items) {
			Product4API p = null;
            try{
                p = bean.getProductFromCache(item.getClistingid(), lang, website, storage);
            }catch(Exception e){
                Logger.error("查询产品缓存出错 ,listingid={}",item.getClistingid(),e);
            }
			if (p != null) {
				list.add(p);
			}else{
				noCacheIds.add(item.getClistingid());
			}
		}
		String listingIdsStr = StringUtils.join(noCacheIds, ",");
		
		List<Product4API> productList = getProductListFromAPI(listingIdsStr, lang,website, storage);
		for (Product4API product4api : productList) {
			try{
                bean.putProductCache(product4api, lang, website, storage);
            }catch(Exception e){
                Logger.error("保存产品缓存出错 ,listingid={}",product4api.getListingId(),e);
            }
		}

		list.addAll(productList);
		return list;
	}
	
	@Override
	public List<ProductLite> getProductLiteListFromAPI(List<CartItem> items,
			int lang, int website, int storage) {
		List<ProductLite> liteList = new ArrayList<ProductLite>();
		List<Product4API> list = getProductListFromAPI(items, lang,
				website, storage);

		for (Product4API p : list) {
			ProductLite lite = new ProductLite();
			lite.setListingId(p.getListingId());
			lite.setTitle(p.getTitle());
			lite.setUrl(p.getUrl());
			lite.setImageUrl(p.getImageUrl());
			lite.setSku(p.getSku());
			lite.setIstatus(p.getStatus());
			lite.setAttributeMap(p.getAttributeMap());

			liteList.add(lite);
		}
		return liteList;
	}
	
	@Override
	@Cacheable(value = "api_search_product", key = "#listingId+'/'+#lang+'/'+#website+'/'+#storage", cacheManager = "dayCacheManager")
	public Product4API getProductFromCache(String listingId, int lang,
			int website, int storage) {
		return null;
	}
	
	@CachePut(value = "api_search_product", key = "#product4api.listingId+'/'+#lang+'/'+#website+'/'+#storage", cacheManager = "dayCacheManager")
	public Product4API putProductCache(Product4API product4api,int lang,
			int website, int storage) {
		return product4api;
	}

	@Override
	public List<PriceNew> queryPrice(List<String> listingList, Integer storageId) {
		if (listingList == null || listingList.isEmpty()) {
			return null;
		}
		if (storageId == null) {
			storageId = 1;
		}
		return productBaseMapper.queryPrice(listingList, storageId);
	}

	@Override
	public PriceNew queryPrice(String listingId, Integer storageId) {
		List<PriceNew> list = this.queryPrice(Lists.newArrayList(listingId),
				storageId);
		if (list == null || list.isEmpty()) {
			return null;
		}
		return list.get(0);
	}

}
