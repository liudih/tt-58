package com.tomtop.services.impl;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tomtop.entity.Currency;
import com.tomtop.entity.ProductCollect;
import com.tomtop.entity.UserCollectProduct;
import com.tomtop.entity.UserCollectProductBo;
import com.tomtop.entity.index.Filter;
import com.tomtop.entity.index.IndexEntity;
import com.tomtop.entity.index.OrderEntity;
import com.tomtop.entity.index.PageBean;
import com.tomtop.framework.core.utils.Page;
import com.tomtop.mappers.interaction.ProductCollectMapper;
import com.tomtop.services.ISearchIndex;
import com.tomtop.services.IUserCollectProductService;
import com.tomtop.utils.ProductComputeUtil;
import com.tomtop.utils.ProductPublicUtil;

@Service
public class UserCollectProductServiceImpl extends BaseService implements
		IUserCollectProductService {

	@Autowired
	ProductCollectMapper collectMapper;
	@Autowired
	ISearchIndex searchIndex;
	@Autowired
	ProductComputeUtil productComputeUtil;
	@Autowired
	ProductPublicUtil productPublicUtil;
	
	/**
	 * 获取用户收藏商品的详细信息
	 * 
	 * @param email 邮箱
	 * @param categoryId 品类Id
	 * @param sort 排序方式
	 * @param productKey 关键字
	 * @param lang 语言Id
	 * @param website 站点Id
	 * @param currency 币种
	 * @param page 页数
	 * @param size 大小
	 * 
	 */
	@Override
	public UserCollectProduct getUserCollectProductList(String email,
			Integer categoryId, String sort, String productKey,Integer lang,
			Integer website,String currency,Integer page,Integer size) {
		List<ProductCollect> pco = collectMapper.getCollectByEmail(email,website);
		Map<String,Date> dMap = new HashMap<String, Date>();
		List<String> listIds = new ArrayList<String>();
		PageBean bean = new PageBean();
		int endNum = page * size;
		int beginNum = endNum - size;
		bean.setBeginNum(beginNum);//开始记录数
		bean.setEndNum(size);//结束记录数
		if(pco != null && pco.size() > 0){
			for (ProductCollect productCollect : pco) {
				listIds.add(productCollect.getClistingid());
				dMap.put(productCollect.getClistingid(), productCollect.getDcreatedate());
			}
		}
		if(productKey != null && !"".equals(productKey.trim())){
			bean.setKeyword(productKey);
		}
		if(categoryId > 0){
			bean.getFilters().add(new Filter("mutil.productTypes.productTypeId",categoryId,"&&"));
		}
		if("priceAsc".equals(sort)){
			OrderEntity oe = new OrderEntity("yjPrice", 1, "asc");
			bean.getOrders().add(oe);
		}
		if("priceDesc".equals(sort)){
			OrderEntity oe = new OrderEntity("yjPrice", 1, "desc");
			bean.getOrders().add(oe);
		}
		bean.setWebSites(website.toString());
		bean.setLanguageName(this.getLangCode(lang));
		PageBean searchBean = searchIndex.queryByIdsAndFilter(bean,listIds);
		List<IndexEntity> indexs = searchBean.getIndexs();
		List<UserCollectProductBo> ucpboList = new ArrayList<UserCollectProductBo>();
		Currency cbo = this.getCurrencyBean(currency);
		if(indexs != null && indexs.size() > 0){
			for (IndexEntity ie : indexs) {
				UserCollectProductBo ucpbo = new UserCollectProductBo();
				//通过公共的方法设置父类的属性
				productPublicUtil.transformProductBase(ucpbo, ie, cbo);
				
				if(ie.getReview() != null){
					ucpbo.setStart(ie.getReview().getStart());
					ucpbo.setReviewNum(ie.getReview().getCount());
				}
				if(ie.getVideos() == null || ie.getVideos().size() == 0){
					ucpbo.setIsVideo(false);
				}else{
					ucpbo.setIsVideo(true);
				}
				if(ie.getSpu() == null){
					ucpbo.setIsMulti(false);
				}else{
					ucpbo.setIsMulti(true);
				}
				ucpbo.setIsFreeShipping(ie.getIsFreeShipping());
				ucpbo.setIsDayDelivery(true);//默认为true
				ucpbo.setCollectNum(ie.getColltes());
				ucpbo.setCollectDate(dMap.get(ie.getListingId()));
				ucpboList.add(ucpbo);
			}
			Collections.sort(ucpboList, this.GOODS_BY_INDATE);
		}
		
		UserCollectProduct ucpvo = new UserCollectProduct();
		ucpvo.setUcpList(ucpboList);
		Integer currentPage = page;
		Integer count = (int) searchBean.getTotalCount();
		Integer pageSize = size;
		Page pageObj = Page.getPage(currentPage, count, pageSize);
		ucpvo.setPage(pageObj);
		
		return ucpvo;
	}
	/**
	 * 根据收藏时间进行排序  Comparator
	 */
	public  final Comparator<UserCollectProductBo> GOODS_BY_INDATE = new Comparator<UserCollectProductBo>(){          
        public int compare(UserCollectProductBo o1, UserCollectProductBo o2) {  
            try{                                          
                //SimpleDateFormat df = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss",Locale.ENGLISH);
                Date d1 = o1.getCollectDate();                         
                Date d2 = o2.getCollectDate();           
                return d2.compareTo(d1);                         
            }catch(Exception e){
                e.printStackTrace();
            }         
            return -1;                        
        }  
    };
}
