package com.tomtop.services.impl;


import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.tomtop.entity.CollectCount;
import com.tomtop.entity.Currency;
import com.tomtop.entity.PrdouctDesc;
import com.tomtop.entity.ProductBase;
import com.tomtop.entity.ProductBaseDepotDtl;
import com.tomtop.entity.ProductBaseDtl;
import com.tomtop.entity.ProductCompute;
import com.tomtop.entity.ProductDepotDetails;
import com.tomtop.entity.ProductDetails;
import com.tomtop.entity.ProductPrice;
import com.tomtop.entity.ProductSeo;
import com.tomtop.entity.index.Filter;
import com.tomtop.entity.index.IndexEntity;
import com.tomtop.entity.index.PageBean;
import com.tomtop.entity.index.ReviewStartNum;
import com.tomtop.entity.index.StartNum;
import com.tomtop.framework.core.utils.Result;
import com.tomtop.services.IEsProductDetailService;
import com.tomtop.services.ISearchService;
import com.tomtop.utils.CommonDefn;
import com.tomtop.utils.ProductComputeUtil;
import com.tomtop.utils.ProductPublicUtil;

/**
 * 搜索引擎获取商品详情业务逻辑类
 * 
 * @author renyy
 *
 */
@Service
public class EsProductDetailServiceImpl extends BaseService implements IEsProductDetailService {

	private static final Logger logger = LoggerFactory
			.getLogger(EsProductDetailServiceImpl.class);
	
	@Autowired
	ISearchService searchService;
	@Autowired
	ProductComputeUtil productComputeUtil;
	@Autowired
	ProductPublicUtil productPublicUtil;
	
	
	/**
	 * 通过搜索引擎 获取商品详情资料 增加多仓库[new add 20160406]
	 * 
	 * @param key 
	 * 			url或者sku
	 * @param langId
	 * 
	 * @param siteId
	 * 
	 * @param currency
	 * 
	 * @return ProductBaseDepotDtl
	 * @author renyy
	 */
	@Override
	public ProductBaseDepotDtl getProductBaseDepotDtl(String key, int langId,
			int siteId, String currency) {
		ProductBaseDepotDtl pbdd = new ProductBaseDepotDtl();
		try{
			if (key == null) {
				pbdd.setRes(CommonDefn.DTL_NULL);
				pbdd.setMsg("key is null");
				return pbdd;
			}
			IndexEntity indexEntity = searchService.getSearchProduct(key, langId, siteId);
			if(indexEntity == null){
				if(langId > CommonDefn.ONE){
					langId = CommonDefn.ONE;
					indexEntity = searchService.getSearchProduct(key, langId, siteId);
					if(indexEntity == null){
						pbdd.setRes(CommonDefn.DTL_INDEX_NULL);
						pbdd.setMsg("indexEntity is null");
						return pbdd;
					}
				}else{
					pbdd.setRes(CommonDefn.DTL_INDEX_NULL);
					pbdd.setMsg("indexEntity is null");
					return pbdd;
				}
			}
			List<ProductDepotDetails> pddlist = getProductDepotDetailsIndexEntity(indexEntity, langId, siteId, currency, key);
			
			if(pddlist == null || pddlist.size() == 0){
				pbdd.setRes(CommonDefn.DTL_INDEX_SERACH_NULL);
				pbdd.setMsg("detail is not find");
				return pbdd;
			}
			//设置商品集合
			pbdd.setPdbList(pddlist);
			
			if(indexEntity.getMutil() != null){
				pbdd.setDesc(indexEntity.getMutil().getDesc());
				String metaDescription = indexEntity.getMutil().getMetaDescription();
				String meteKeyword = indexEntity.getMutil().getMetaKeyword();
				String meteTitle = indexEntity.getMutil().getMetaTitle();
				String title = indexEntity.getMutil().getTitle();
				if(metaDescription == null || "".equals(metaDescription)){
					metaDescription = title;
				}
				if(meteKeyword == null || "".equals(meteKeyword)){
					meteKeyword = title;
				}
				if(meteTitle == null || "".equals(meteTitle)){
					meteTitle = title;
				}
				pbdd.setMetaDescription(metaDescription);
				pbdd.setMetaKeyword(meteKeyword);
				pbdd.setMetaTitle(meteTitle);
			}
			//设置评论数
			ReviewStartNum rsnbo = indexEntity.getReview();
			Integer reviewCount = 0;
			if(rsnbo == null){
				rsnbo = new ReviewStartNum();
				pbdd.setCount(reviewCount);
				pbdd.setStart(0.00);
				pbdd.setStartNum(new ArrayList<StartNum>());
			}else{
				pbdd.setCount(rsnbo.getCount());
				pbdd.setStart(rsnbo.getStart());
				pbdd.setStartNum(rsnbo.getStartNum());
			}
			pbdd.setRes(Result.SUCCESS);
		}catch(Exception e){
			logger.error("getProductBaseDepotDtl exception [" + key + "]");
			e.printStackTrace();
		}
		return pbdd;
	}
	
	/**
	 * 通过搜索引擎 获取商品详情资料[new add 20160122]
	 * 
	 * @param sku
	 * 
	 * @param langId
	 * 
	 * @param siteId
	 * 
	 * @param currency
	 * 
	 * @return ProductBaseDtl
	 * @author renyy
	 */
	@Override
	public ProductBaseDtl getProductBaseDtlVo(String key, int langId,
			int siteId, String currency) {
		ProductBaseDtl pbdVo = new ProductBaseDtl();
		try{
			if (key == null) {
				pbdVo.setRes(CommonDefn.DTL_NULL);
				pbdVo.setMsg("key is null");
				return pbdVo;
			}
			IndexEntity indexEntity = searchService.getSearchProduct(key, langId, siteId);
			if(indexEntity == null){
				if(langId > CommonDefn.ONE){
					langId = CommonDefn.ONE;
					indexEntity = searchService.getSearchProduct(key, langId, siteId);
					if(indexEntity == null){
						pbdVo.setRes(CommonDefn.DTL_INDEX_NULL);
						pbdVo.setMsg("indexEntity is null");
						return pbdVo;
					}
				}else{
					pbdVo.setRes(CommonDefn.DTL_INDEX_NULL);
					pbdVo.setMsg("indexEntity is null");
					return pbdVo;
				}
			}
			//1
			List<ProductDetails> pdbList = getProductDetailIndexEntity(indexEntity, langId, siteId, currency,key);
			if(pdbList == null || pdbList.size() == 0){
				pbdVo.setRes(-44403);
				pbdVo.setMsg("detail is not find");
				return pbdVo;
			}
			pbdVo.setPdbList(pdbList);
			//2
			PrdouctDesc pdbo = new PrdouctDesc();
			pdbo.setDesc(indexEntity.getMutil().getDesc());
			pbdVo.setPdb(pdbo);
			//3
			ProductSeo psb = new ProductSeo();
			String metaDescription = indexEntity.getMutil().getMetaDescription();
			String meteKeyword = indexEntity.getMutil().getMetaKeyword();
			String meteTitle = indexEntity.getMutil().getMetaTitle();
			String title = indexEntity.getMutil().getTitle();
			if(metaDescription == null || "".equals(metaDescription)){
				metaDescription = title;
			}
			if(meteKeyword == null || "".equals(meteKeyword)){
				meteKeyword = title;
			}
			if(meteTitle == null || "".equals(meteTitle)){
				meteTitle = title;
			}
			psb.setMetaDescription(metaDescription);
			psb.setMetaKeyword(meteKeyword);
			psb.setMetaTitle(meteTitle);
			pbdVo.setPsb(psb);
			//4
			ReviewStartNum rsnbo = new ReviewStartNum();
			rsnbo = indexEntity.getReview();
			Integer reviewCount = 0;
			if(rsnbo == null){
				rsnbo = new ReviewStartNum();
				rsnbo.setCount(reviewCount);
				rsnbo.setStart(0.00);
				rsnbo.setStartNum(new ArrayList<StartNum>());
			}
			pbdVo.setRsnbo(rsnbo);
			//5
			CollectCount ccb = new CollectCount();
			ccb.setListingId(indexEntity.getListingId());
			ccb.setCollectCount(indexEntity.getColltes());
			pbdVo.setCcb(ccb);
			pbdVo.setRes(Result.SUCCESS);
		}catch(Exception e){
			e.printStackTrace();
		}
		return pbdVo;
	}
	
	/**
	 * 通过搜索引擎 获取商品详情基本信息
	 * 
	 * @param sku
	 * 
	 * @param langId
	 * 
	 * @param siteId
	 * 
	 * @param currency
	 * 
	 * @return List<ProductDetailsBo>
	 * @author renyy
	 */
	@Override
	public List<ProductDetails> getProductDetailsBoList(String sku,
			Integer langId, Integer siteId, String currency) {
		List<ProductDetails> pdbList = null;
		if (sku == null) {
			return new ArrayList<ProductDetails>();
		}
		IndexEntity indexEntity = searchService.getSearchProduct(sku, langId, siteId);
		if(indexEntity == null){
			return new ArrayList<ProductDetails>();
		}
		pdbList = getProductDetailIndexEntity(indexEntity, langId, siteId, currency,sku);
		return pdbList;
	}
	/**
	 * 通过搜索引擎 获取商品详情的Description
	 * 
	 * @param listingId
	 * 
	 * @param langId
	 * 
	 * @return PrdouctDescBo
	 * @author renyy
	 * 
	 */
	@Override
	public PrdouctDesc getPrdouctDescBo(String sku, Integer langId,
			Integer siteId) {
		PrdouctDesc pdbo = new PrdouctDesc();
		if (sku == null) {
			pdbo.setRes(-31001);
			pdbo.setMsg("sku is null");
			return pdbo;
		}
		IndexEntity indexEntity = searchService.getSearchProduct(sku, langId, siteId);
		if(indexEntity == null){
			pdbo.setRes(-31002);
			pdbo.setMsg("getPrdouctDescBo indexEntity  is null");
			return pdbo;
		}
		String desc = indexEntity.getMutil().getDesc();
		if (desc == null || "".equals(desc)) {
			pdbo.setRes(-31003);
			pdbo.setMsg("desc not find");
			return pdbo;
		}
		pdbo.setRes(1);
		pdbo.setDesc(desc);
		
		return pdbo;
	}
	
	/**
	 * 通过搜索引擎 获取商品详情的SEO
	 * 
	 * @param listingId
	 * 
	 * @param langId
	 * 
	 * @return ProductSeoBo
	 * @author renyy
	 */
	@Override
	public ProductSeo getProductSeoBo(String sku, Integer langId,
			Integer siteId) {
		ProductSeo psb = new ProductSeo();
		if (sku == null) {
			psb.setRes(-31004);
			psb.setMsg("sku is null");
			return psb;
		}
		IndexEntity indexEntity = searchService.getSearchProduct(sku, langId, siteId);
		if(indexEntity == null){
			psb.setRes(-31005);
			psb.setMsg("getProductSeoBo indexEntity is null");
			return psb;
		}
		String metaDescription = indexEntity.getMutil().getMetaDescription();
		String meteKeyword = indexEntity.getMutil().getMetaKeyword();
		String meteTitle = indexEntity.getMutil().getMetaTitle();
		String title = indexEntity.getMutil().getTitle();
		if(metaDescription == null || "".equals(metaDescription)){
			metaDescription = title;
		}
		if(meteKeyword == null || "".equals(meteKeyword)){
			meteKeyword = title;
		}
		if(meteTitle == null || "".equals(meteTitle)){
			meteTitle = title;
		}
		psb.setMetaDescription(metaDescription);
		psb.setMetaKeyword(meteKeyword);
		psb.setMetaTitle(meteTitle);
		psb.setRes(1);
		return psb;
	}
	/**
	 * 获取商品单品价格
	 * 
	 * @param listingId
	 * 
	 * @param langId
	 * 
	 * @param siteId
	 * 
	 * @return ProductBasePriceInfoBo
	 * @author renyy
	 */
	public ProductPrice getProductBasePriceBo(String listingId,
			Integer langId, Integer siteId, String currency) {
		ProductPrice bo = new ProductPrice();
		if (listingId == null) {
			bo.setRes(-31014);
			bo.setMsg("sku is null");
			return bo;
		}
		IndexEntity indexEntity = searchService.getSearchProduct(listingId, langId, siteId);
		if(indexEntity == null){
			bo.setRes(-31015);
			bo.setMsg("getProductBasePriceBo indexEntity is null");
			return bo;
		}
		Currency cbo = this.getCurrencyBean(currency);
		ProductCompute pprbo = productComputeUtil.getPrice(indexEntity.getCostPrice(), indexEntity.getYjPrice(), indexEntity.getPromotionPrice(), cbo);
		bo.setOrigprice(pprbo.getOriginalPrice());//原价
		bo.setNowprice(pprbo.getPrice());//折扣价
		bo.setSymbol(cbo.getSymbolCode());
		return bo;
	}
	/**
	 * 通过搜索引擎 获取商品评论星级和数量
	 * 
	 * @param listingId
	 * 
	 * @return ReviewStartNumBo
	 * @author renyy
	 */
	@Override
	public ReviewStartNum getReviewStartNumBoById(String listingId, Integer langId,
			Integer siteId) {
		ReviewStartNum rsnbo = new ReviewStartNum();
		if(listingId == null || "".equals(listingId.trim())){
			return null;
		}
		IndexEntity indexEntity = searchService.getSearchProduct(listingId, langId, siteId);
		if(indexEntity == null){
			return null;
		}
		rsnbo = indexEntity.getReview();
		Integer reviewCount = 0;
		if(rsnbo == null){
			rsnbo = new ReviewStartNum();
			rsnbo.setCount(reviewCount);
			rsnbo.setStart(0.00);
			rsnbo.setStartNum(new ArrayList<StartNum>());
		}
		return rsnbo;
	}
	
	/**
	 * 通过搜索引擎 获取商品收藏数量
	 * 
	 * @param listingId
	 * 
	 * @return CollectCountBo
	 * @author renyy
	 */
	@Override
	public CollectCount getCollectCountByListingId(String listingId, Integer langId,
			Integer siteId) {
		CollectCount ccb = new CollectCount();
		IndexEntity indexEntity = searchService.getSearchProduct(listingId, langId, siteId);
		if(indexEntity == null){
			ccb.setRes(-50003);
			ccb.setMsg("getCollectCountByListingId  indexEntity is null");
			return ccb;
		}
		ccb.setListingId(indexEntity.getListingId());
		ccb.setCollectCount(indexEntity.getColltes());
		ccb.setRes(1);
		return ccb;
	}
	
	/**
	 * 通过搜索引擎 获取热门商品
	 * 
	 * @param siteId
	 * 			站点Id
	 * @param type 
	 * 			hot
	 * @param page 页数 
	 * 
	 * @param pageSize 获取多少条
	 * 
	 * @return List<String>
	 * @author renyy
	 */
	@Cacheable(value = "product_hot", keyGenerator = "customKeyGenerator", cacheManager="dayCacheManager")
	@Override
	public List<ProductBase> getProductHotBoList(Integer langId, Integer siteId,String currency,Integer size) {
		List<ProductBase> photboList = new ArrayList<ProductBase>();
		List<IndexEntity> ieList = searchService.getSearchProductHotList(langId, siteId,size);
		if(ieList == null || ieList.size() == 0){
			return photboList;
		}
		Currency cbo = this.getCurrencyBean(currency);
		if(ieList != null && ieList.size() > 0){
			for (int i = 0; i < ieList.size(); i++) {
				IndexEntity ie = ieList.get(i);
				ProductBase phot = new ProductBase();
				productPublicUtil.transformProductBase(phot, ie, cbo);
				photboList.add(phot);
			}
		}
		return photboList;
	}
	
	/**
	 * 通过搜索引擎 获取热门商品
	 * 
	 * @param siteId
	 * 			站点Id
	 * @param type 
	 * 			hot
	 * @param page 页数 
	 * 
	 * @param pageSize 获取多少条
	 * 
	 * @return List<String>
	 * @author renyy
	 */
	@Cacheable(value = "product_hot", keyGenerator = "customKeyGenerator", cacheManager="dayCacheManager")
	@Override
	public List<ProductBase> getProductHotBoList(Integer langId, Integer siteId,String currency,Integer size,String depotName) {
		List<ProductBase> photboList = new ArrayList<ProductBase>();
		List<IndexEntity> ieList = searchService.getSearchProductHotList(langId, siteId,size);
		if(ieList == null || ieList.size() == 0){
			return photboList;
		}
		Currency cbo = this.getCurrencyBean(currency);
		if(ieList != null && ieList.size() > 0){
			for (int i = 0; i < ieList.size(); i++) {
				IndexEntity ie = ieList.get(i);
				ProductBase phot = new ProductBase();
				productPublicUtil.transformProductBase(phot, ie, cbo,depotName);
				photboList.add(phot);
			}
		}
		return photboList;
	}
	/**
	 * 获取商品详情
	 */
	private List<ProductDetails> getProductDetailIndexEntity(IndexEntity indexEntity,Integer langId, Integer siteId, String currency,String key){
		List<ProductDetails> pdbList = new ArrayList<ProductDetails>();
		Currency cbo = this.getCurrencyBean(currency);
		String spu = indexEntity.getSpu();
		if(spu != null){
		//有多属性商品
			PageBean bean = new PageBean();
			bean.setWebSites(siteId.toString());
			List<Filter> filterList = new ArrayList<Filter>();
			Filter filter = new Filter();
			filter.setExpress("&&");
			filter.setPropertyValue(spu);
			filter.setPropetyName("spu");
			filterList.add(filter);
			bean.setFilters(filterList);
			bean.setEndNum(100);
			List<IndexEntity> ieList = searchService.getSearchProductList(bean, langId);
			if(ieList != null && ieList.size() > 0){
				for (int i = 0; i < ieList.size(); i++) {
					IndexEntity ie = ieList.get(i);
					ProductDetails pdbo = new ProductDetails();
					productPublicUtil.transformProductDtl(pdbo, ie, key, langId, cbo);
					pdbList.add(pdbo);
				}
			}
		}else{
		//没有多属性商品时	
			ProductDetails pdbo = new ProductDetails();
			productPublicUtil.transformProductDtl(pdbo, indexEntity, key, langId, cbo);
			pdbList.add(pdbo);
		}
		return pdbList;
	}
	
	/**
	 * 获取商品详情
	 */
	private List<ProductDepotDetails> getProductDepotDetailsIndexEntity(IndexEntity indexEntity,Integer langId, Integer siteId, String currency,String key){
		List<ProductDepotDetails> pddList = new ArrayList<ProductDepotDetails>();
		Currency cbo = this.getCurrencyBean(currency);
		String spu = indexEntity.getSpu();
		if(spu != null){
		//有多属性商品
			PageBean bean = new PageBean();
			bean.setWebSites(siteId.toString());
			List<Filter> filterList = new ArrayList<Filter>();
			Filter filter = new Filter("spu",spu,"&&");
			filterList.add(filter);
			filter = new Filter("depots.status","-10","!=",false);//设置status过滤条件不聚合
			filterList.add(filter);
			bean.setFilters(filterList);
			bean.setEndNum(100);
			List<IndexEntity> ieList = searchService.getSearchProductList(bean, langId);
			if(ieList != null && ieList.size() > 0){
				for (int i = 0; i < ieList.size(); i++) {
					IndexEntity ie = ieList.get(i);
					ProductDepotDetails pdbo = new ProductDepotDetails();
					productPublicUtil.transformProductDepotDetails(pdbo, ie, key, langId, cbo);
					pddList.add(pdbo);
				}
			}
		}else{
		//没有多属性商品时	
			ProductDepotDetails pdbo = new ProductDepotDetails();
			productPublicUtil.transformProductDepotDetails(pdbo, indexEntity, key, langId, cbo);
			pddList.add(pdbo);
		}
		return pddList;
	}
}
