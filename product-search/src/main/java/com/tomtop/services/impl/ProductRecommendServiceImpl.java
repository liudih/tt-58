package com.tomtop.services.impl;


import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tomtop.entity.Currency;
import com.tomtop.entity.ProductBaseRecommend;
import com.tomtop.entity.index.IndexEntity;
import com.tomtop.entity.index.PageBean;
import com.tomtop.entity.index.ReviewStartNum;
import com.tomtop.services.IProductRecommendService;
import com.tomtop.services.ISearchService;
import com.tomtop.utils.ProductComputeUtil;
import com.tomtop.utils.ProductPublicUtil;

/**
 * 获取推荐类数据
 * 
 * @author liulj
 *
 */
@Service
public class ProductRecommendServiceImpl extends BaseService implements IProductRecommendService {

	@Autowired
	ISearchService searchService;
	@Autowired
	ProductComputeUtil productComputeUtil;
	@Autowired
	ProductPublicUtil productPublicUtil;
	
	//private Logger logger = LoggerFactory.getLogger(this.getClass());
	/**
	 * 根据type获取对应推荐位商品详细
	 * 
	 * @param listingId
	 * @param website
	 * @param lang
	 * @param currency
	 * @param size 获取的大小
	 * @param type 推荐位置的类型
	 * 
	 * @author renyy
	 *
	 */
	@Override
	public List<ProductBaseRecommend> getRecommendProduct(String listingId,Integer website, Integer lang,
			String currency,Integer size,String type,String depotName) {
		List<ProductBaseRecommend> pbrList = new ArrayList<ProductBaseRecommend>();
		PageBean bean = searchService.getSearchPageBeanRecommendByType(listingId, lang, website, type, size);
		if(bean == null){
			return pbrList;
		}
		List<IndexEntity> ieList = bean.getIndexs();
		if(ieList != null && ieList.size() > 0){
			Currency cbo = this.getCurrencyBean(currency);
			for (int i = 0; i < ieList.size(); i++) {
				IndexEntity ie = ieList.get(i);
				if(ie != null){
					ProductBaseRecommend pbr = new ProductBaseRecommend();
					//通过公共的方法设置父类的属性
					if(depotName == null){
						productPublicUtil.transformProductBase(pbr, ie, cbo);
					}else{
						productPublicUtil.transformProductBase(pbr, ie, cbo,depotName);
					}
					pbr.setCollectNum(ie.getColltes());
					ReviewStartNum recount = ie.getReview();
					if(recount != null ){
						pbr.setReviewCount(recount.getCount());
						pbr.setAvgScore(recount.getStart());
					}
					if(pbr.getNowprice() == null){
						continue;
					}
					pbrList.add(pbr);
					
				}
			}
		}
	
		return pbrList;
	}
	

}
