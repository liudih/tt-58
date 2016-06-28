package com.tomtop.services.impl;


import java.util.List;






import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tomtop.entity.BaseBean;
import com.tomtop.entity.CollectCount;
import com.tomtop.entity.ProductCollect;
import com.tomtop.mappers.interaction.ProductCollectMapper;
import com.tomtop.services.IProductCollectService;
import com.tomtop.utils.CommonDefn;

/**
 * 产品收藏
 * 
 * @author renyy
 *
 */
@Service
public class ProductCollectServiceImpl implements IProductCollectService {

	@Autowired
	ProductCollectMapper productCollectMapper;

	/**
	 * 获取收藏数
	 * 
	 * @param listingIds
	 * @return
	 */
	@Override
	public List<CollectCount> getCollectCountByListingIds(List<String> listingIds,Integer website) {
		return productCollectMapper.getCollectCountByListingIds(listingIds,website);
	}

	/**
	 * 获取单个商品收藏数
	 * 
	 * @param listingIds
	 * @return
	 */
	@Override
	public CollectCount getCollectCountByListingId(String listingIds,Integer website) {
		CollectCount cc = productCollectMapper.getCollectCountByListingId(listingIds,website);
		if(cc == null){
			cc = new CollectCount();
			cc.setCollectCount(CommonDefn.ZERO);
			cc.setListingId(listingIds);
		}
		return cc;
	}

	/**
	 * 添加收藏
	 * 
	 * @param listingIds
	 * @param email
	 * @param website
	 * @return
	 */
	@Override
	public BaseBean addCollectCount(String listingId, String email,Integer website) {
		BaseBean bb = new BaseBean();
		if(listingId == null || "".equals(listingId)){
		  	bb.setRes(CommonDefn.LISTINGID_NULL);
		   	bb.setMsg("listingId is null");
		    return bb;
	    }
	    if(email == null || "".equals(email)){
		    bb.setRes(CommonDefn.EMAIL_NULL);
		    bb.setMsg("email is null");
		    return bb;
		}
	    
		ProductCollect pcdto = productCollectMapper.getCollectByMember(listingId, email,website);
		if(pcdto == null){
			pcdto = new ProductCollect();
		}else{
			bb.setRes(CommonDefn.FAVORITES_TOO);
			bb.setMsg("You have favorites too");
			return bb;
		}
		pcdto.setClistingid(listingId);
		pcdto.setCemail(email);
		pcdto.setIwebsiteid(website);
		int res = productCollectMapper.insertSelective(pcdto);
		if(res > 0 ){
		   	bb.setRes(CommonDefn.ONE);
	    }else{
		   	bb.setRes(CommonDefn.ADD_FAVORITES_ERROR);
		  	bb.setMsg("product collect add failure");
		}
		return bb;
	}
	
	/**
	 * 根据邮箱获取收藏的listingId 集合
	 * 
	 * @param email
	 * @return
	 */
	@Override
	public List<String> getCollectListingIdByEmail(String email,Integer website) {
		return productCollectMapper.getCollectListingIDByEmail(email,website);
	}
}