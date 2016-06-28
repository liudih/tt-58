package com.tomtop.services.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.tomtop.entity.BaseLayoutmoduleContenthProduct;
import com.tomtop.entity.Currency;
import com.tomtop.entity.LayoutModuleContent;
import com.tomtop.entity.index.IndexEntity;
import com.tomtop.entity.index.ProductTypeEntity;
import com.tomtop.entity.index.ReviewStartNum;
import com.tomtop.entity.rc.LayoutmoduleContentRc;
import com.tomtop.mappers.mysql.BaseLayoutModuleContentMapper;
import com.tomtop.services.ILayoutService;
import com.tomtop.services.ISearchService;
import com.tomtop.utils.CommonDefn;
import com.tomtop.utils.ProductComputeUtil;
import com.tomtop.utils.ProductPublicUtil;
/**
 * 布局配置商品信息
 * @author renyy
 *
 */
@Service
public class LayoutServiceImpl extends BaseService implements ILayoutService {
	
	private static final Logger logger = LoggerFactory
			.getLogger(LayoutServiceImpl.class);
	@Autowired
	BaseLayoutModuleContentMapper layoutMapper;
	@Autowired
	ISearchService searchService;
	@Autowired
	ProductComputeUtil productComputeUtil;
	@Autowired
	ProductPublicUtil productPublicUtil;
	
	/**
	 * 获取对应布局的商品信息
	 */
	@Cacheable(value="base_layout_module_content", keyGenerator = "customKeyGenerator")
	@Override
	public HashMap<String,List<BaseLayoutmoduleContenthProduct>> getBaseLayoutmoduleContenth(Integer lang,Integer client,Integer website,String layoutcode,String currency){
		HashMap<String,List<BaseLayoutmoduleContenthProduct>> map = new HashMap<String, List<BaseLayoutmoduleContenthProduct>>();
			List<LayoutModuleContent> lmc = layoutMapper.getListByLayoutModule(lang,client,layoutcode);
			if(lmc == null || lmc.size() == 0){
				lmc = layoutMapper.getListByLayoutModule(CommonDefn.ONE,client,layoutcode);
				if(lmc == null || lmc.size() == 0){
					return null;
				}
			}
			HashMap<String,BaseLayoutmoduleContenthProduct> hmap = new HashMap<String,BaseLayoutmoduleContenthProduct>(); 
			List<String> idList = new ArrayList<String>();
			lmc.forEach(ids ->{
				if(ids != null){
					if(StringUtils.isNotBlank(ids.getListingId()) && !"null".equals(ids.getLanguageId())){
						logger.info("listingId  ===================== " + ids.getListingId());
						idList.add(ids.getListingId());
					}
				}
			});
			
			List<IndexEntity> ieList = searchService.getSearchProductList(JSON.toJSONString(idList), lang, website);
			if(ieList != null && ieList.size() > 0){
				Currency cbo = this.getCurrencyBean(currency);
				for (int i = 0; i < ieList.size(); i++) {
					IndexEntity ie = ieList.get(i);
					if(ie != null){
						BaseLayoutmoduleContenthProduct bvo = new BaseLayoutmoduleContenthProduct();
						//通过公共的方法设置父类的属性
						productPublicUtil.transformProductBase(bvo, ie, cbo);
						ReviewStartNum recount = ie.getReview();
						if(recount != null ){
							bvo.setReviewCount(recount.getCount());
							bvo.setAvgScore(recount.getStart());
						}
						hmap.put(ie.getListingId(), bvo);

					}
				}
			}
			
			for (int j = 0; j < lmc.size(); j++) {
				LayoutModuleContent lmcdto = lmc.get(j);
				String lcode = lmcdto.getLayoutModuleCode();
				if(map.containsKey(lcode)){//如果layout_module_code存在
					List<BaseLayoutmoduleContenthProduct> bcpvoList = map.get(lcode);
					BaseLayoutmoduleContenthProduct bcvo = hmap.get(lmcdto.getListingId());
					if(bcvo != null){
						if(lmcdto.getSort() != null){
							bcvo.setSort(lmcdto.getSort());
						}
						bcpvoList.add(bcvo);
						map.put(lcode, bcpvoList);
					}
				}else{
					List<BaseLayoutmoduleContenthProduct> bcpvoList = new ArrayList<BaseLayoutmoduleContenthProduct>();
					BaseLayoutmoduleContenthProduct bcvo = hmap.get(lmcdto.getListingId());
					if(bcvo != null){
						if(lmcdto.getSort() != null){
							bcvo.setSort(lmcdto.getSort());
						}
						bcpvoList.add(bcvo);
						map.put(lcode, bcpvoList);
					}
				}
			}
			return map;
		}
	/**
	 * 获取对应布局的商品信息加上仓库条件
	 */
	@Cacheable(value="base_layout_module_content", keyGenerator = "customKeyGenerator")
	@Override
	public HashMap<String,List<BaseLayoutmoduleContenthProduct>> getBaseLayoutmoduleContenth(Integer lang,Integer client,Integer website,String layoutcode,String currency,String depotName){
		HashMap<String,List<BaseLayoutmoduleContenthProduct>> map = new HashMap<String, List<BaseLayoutmoduleContenthProduct>>();
			List<LayoutModuleContent> lmc = layoutMapper.getListByLayoutModule(lang,client,layoutcode);
			if(lmc == null || lmc.size() == 0){
				lmc = layoutMapper.getListByLayoutModule(CommonDefn.ONE,client,layoutcode);
				if(lmc == null || lmc.size() == 0){
					return null;
				}
			}
			HashMap<String,BaseLayoutmoduleContenthProduct> hmap = new HashMap<String,BaseLayoutmoduleContenthProduct>(); 
			List<String> idList = new ArrayList<String>();
			lmc.forEach(ids ->{
				if(ids != null){
					if(StringUtils.isNotBlank(ids.getListingId()) && !"null".equals(ids.getLanguageId())){
						logger.info("listingId  ===================== " + ids.getListingId());
						idList.add(ids.getListingId());
					}
				}
			});
			
			List<IndexEntity> ieList = searchService.getSearchProductList(JSON.toJSONString(idList), lang, website);
			if(ieList != null && ieList.size() > 0){
				Currency cbo = this.getCurrencyBean(currency);
				for (int i = 0; i < ieList.size(); i++) {
					IndexEntity ie = ieList.get(i);
					if(ie != null){
						BaseLayoutmoduleContenthProduct bvo = new BaseLayoutmoduleContenthProduct();
						//通过公共的方法设置父类的属性
						productPublicUtil.transformProductBase(bvo, ie, cbo,depotName);
						ReviewStartNum recount = ie.getReview();
						if(recount != null ){
							bvo.setReviewCount(recount.getCount());
							bvo.setAvgScore(recount.getStart());
						}
						bvo.setCollectNum(ie.getColltes());
						hmap.put(ie.getListingId(), bvo);

					}
				}
			}
			
			for (int j = 0; j < lmc.size(); j++) {
				LayoutModuleContent lmcdto = lmc.get(j);
				String lcode = lmcdto.getLayoutModuleCode();
				if(map.containsKey(lcode)){//如果layout_module_code存在
					List<BaseLayoutmoduleContenthProduct> bcpvoList = map.get(lcode);
					BaseLayoutmoduleContenthProduct bcvo = hmap.get(lmcdto.getListingId());
					if(bcvo != null){
						if(lmcdto.getSort() != null){
							bcvo.setSort(lmcdto.getSort());
						}
						bcpvoList.add(bcvo);
						map.put(lcode, bcpvoList);
					}
				}else{
					List<BaseLayoutmoduleContenthProduct> bcpvoList = new ArrayList<BaseLayoutmoduleContenthProduct>();
					BaseLayoutmoduleContenthProduct bcvo = hmap.get(lmcdto.getListingId());
					if(bcvo != null){
						if(lmcdto.getSort() != null){
							bcvo.setSort(lmcdto.getSort());
						}
						bcpvoList.add(bcvo);
						map.put(lcode, bcpvoList);
					}
				}
			}
			return map;
		}
	
	/**
	 * 获取对应布局的商品信息加上仓库条件
	 */
	//@Cacheable(value="base_layout_module_content", keyGenerator = "customKeyGenerator")
	@Override
	public HashMap<String,List<LayoutmoduleContentRc>> getBaseLayoutmoduleContenthRc(Integer lang,Integer client,Integer website,String layoutcode,String currency,String depotName){
		HashMap<String,List<LayoutmoduleContentRc>> map = new HashMap<String, List<LayoutmoduleContentRc>>();
			List<LayoutModuleContent> lmc = layoutMapper.getListByLayoutModule(lang,client,layoutcode);
			if(lmc == null || lmc.size() == 0){
				lmc = layoutMapper.getListByLayoutModule(CommonDefn.ONE,client,layoutcode);
				if(lmc == null || lmc.size() == 0){
					return null;
				}
			}
			HashMap<String,LayoutmoduleContentRc> hmap = new HashMap<String,LayoutmoduleContentRc>(); 
			List<String> idList = new ArrayList<String>();
			lmc.forEach(ids ->{
				if(ids != null){
					if(StringUtils.isNotBlank(ids.getListingId()) && !"null".equals(ids.getLanguageId())){
						logger.info("listingId  ===================== " + ids.getListingId());
						idList.add(ids.getListingId());
					}
				}
			});
			
			List<IndexEntity> ieList = searchService.getSearchProductList(JSON.toJSONString(idList), lang, website);
			if(ieList != null && ieList.size() > 0){
				Currency cbo = this.getCurrencyBean(currency);
				for (int i = 0; i < ieList.size(); i++) {
					IndexEntity ie = ieList.get(i);
					if(ie != null){
						LayoutmoduleContentRc bvo = new LayoutmoduleContentRc();
						//通过公共的方法设置父类的属性
						productPublicUtil.transformProductBase(bvo, ie, cbo,depotName);
						ReviewStartNum recount = ie.getReview();
						if(recount != null ){
							bvo.setReviewCount(recount.getCount());
							bvo.setAvgScore(recount.getStart());
						}
						bvo.setCollectNum(ie.getColltes());
						List<ProductTypeEntity> list = ie.getMutil().getProductTypes();
						String oneName = "";
						String onePath = "";
						for (ProductTypeEntity productTypeEntity : list) {
							if(productTypeEntity != null){
								if(productTypeEntity.getLevel() == 3){
									bvo.setCategoryName(productTypeEntity.getProductTypeName());
									bvo.setCpath(productTypeEntity.getCpath());
									break;//有3级则直接跳出
								}else if (productTypeEntity.getLevel() == 2){
									bvo.setCategoryName(productTypeEntity.getProductTypeName());
									bvo.setCpath(productTypeEntity.getCpath());
								}else if(productTypeEntity.getLevel() == 1){
									oneName = productTypeEntity.getProductTypeName();
									onePath = productTypeEntity.getCpath();
								}
							}
						}
						//没有2级3级类目才设置为1级类目
						if(bvo.getCategoryName() == null){
							bvo.setCategoryName(oneName);
							bvo.setCpath(onePath);
						}
						hmap.put(ie.getListingId(), bvo);
					}
				}
			}
			
			for (int j = 0; j < lmc.size(); j++) {
				LayoutModuleContent lmcdto = lmc.get(j);
				String lcode = lmcdto.getLayoutModuleCode();
				if(map.containsKey(lcode)){//如果layout_module_code存在
					List<LayoutmoduleContentRc> bcpvoList = map.get(lcode);
					LayoutmoduleContentRc bcvo = hmap.get(lmcdto.getListingId());
					if(bcvo != null){
						if(lmcdto.getSort() != null){
							bcvo.setSort(lmcdto.getSort());
						}
						bcpvoList.add(bcvo);
						map.put(lcode, bcpvoList);
					}
				}else{
					List<LayoutmoduleContentRc> bcpvoList = new ArrayList<LayoutmoduleContentRc>();
					LayoutmoduleContentRc bcvo = hmap.get(lmcdto.getListingId());
					if(bcvo != null){
						if(lmcdto.getSort() != null){
							bcvo.setSort(lmcdto.getSort());
						}
						bcpvoList.add(bcvo);
						map.put(lcode, bcpvoList);
					}
				}
			}
			return map;
		}
}
