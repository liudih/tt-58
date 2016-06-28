package com.tomtop.services.impl;


import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tomtop.entity.BaseBean;
import com.tomtop.entity.DropshipBase;
import com.tomtop.entity.DropshipProduct;
import com.tomtop.entity.Product;
import com.tomtop.mappers.interaction.DropshipProductMapper;
import com.tomtop.mappers.member.DropshipMapper;
import com.tomtop.mappers.product.ProductDtlMapper;
import com.tomtop.services.IDropshipProductService;
import com.tomtop.utils.CommonDefn;

@Service
public class DropshipProductServiceImpl implements IDropshipProductService {

	private static final Logger logger = LoggerFactory.getLogger(DropshipProductServiceImpl.class);
	
	@Autowired
	DropshipProductMapper dropshipProductMapper;
	
	@Autowired
	DropshipMapper dropshipMapper;
	
	@Autowired
	ProductDtlMapper productDtlMapper;
	
	@Override
	public BaseBean addProductDropshipByListingId(String email, String listingId, Integer siteId) {
		BaseBean bb = new BaseBean();
		//先判断用户是否为dropship
		DropshipBase dbdto = dropshipMapper.getDropshipBaseDto(email, siteId);
		if(dbdto == null){
		 	bb.setRes(-82001);
	    	bb.setMsg("user is not dropship");
	    	return bb;
		}
		Integer productCount = dbdto.getIproductcount();//用户可以收藏的dropship数量
		Integer pcount = dropshipProductMapper.getDropshipPrdouctCountByEmail(email, siteId);
		if(pcount == null){
			pcount = 0;
		}
		//根据listingId获取所有的sku
		List<String> skuList = new ArrayList<String>();
		Product pb = productDtlMapper.getProductBaseByListingId(listingId,CommonDefn.ONE, siteId);
		skuList.add(pb.getCsku());
		String parentSku = pb.getCparentsku();
		if(parentSku != null){
			List<Product> pbList = productDtlMapper.getProductBaseByParentSku(listingId,1, parentSku, siteId);
			if(pbList != null && pbList.size() > 0 ){
				for (int i = 0; i < pbList.size(); i++) {
					skuList.add(pbList.get(i).getCsku());
				}
			}
		}
		Integer leftCount = productCount - pcount;
		if(leftCount == 0 ){
			bb.setRes(-82002);
			bb.setMsg("Collection number reaches the upper limit");
			return bb;
		}
		Integer skuLen = skuList.size();
		leftCount = leftCount - skuLen;
		if(leftCount <= 0 ){
			bb.setRes(-82003);
			bb.setMsg("the number of collection sku is beyond the capacity ");
			return bb;
		}	
		DropshipProduct dpdto = null;
		int res = 0;
		String sku = "";
		for (int i = 0; i < skuLen; i++) {
			sku = skuList.get(i);
			dpdto = new DropshipProduct();
			dpdto.setCemail(email);
			dpdto.setCsku(sku);
			dpdto.setBstate(true);
			dpdto.setIwebsiteid(siteId);
			res = dropshipProductMapper.insertSelective(dpdto);
			if(res == 0){
				logger.error("dropship product add error [" + sku + "]-[" + email + "]" );
			}
		}
		bb.setRes(1);
		return bb;
	}

	@Override
	public BaseBean addProductDropshipBySku(String email, String sku,
			Integer siteId) {
		BaseBean bb = new BaseBean();
		if(email == null || "".equals(email)){
			    bb.setRes(-82004);
			    bb.setMsg("email is null");
			    return bb;
		}
		if(sku == null || "".equals(sku)){
			    bb.setRes(-82005);
			    bb.setMsg("sku is null");
			    return bb;
		}
		if(siteId == null){
			 siteId = 1;
		}
		 
		//先判断用户是否为dropship
		DropshipBase dbdto = dropshipMapper.getDropshipBaseDto(email, siteId);
		if(dbdto == null){
		 	bb.setRes(-82006);
	    	bb.setMsg("user is not dropship");
	    	return bb;
		}
		Integer productCount = dbdto.getIproductcount();//用户可以收藏的dropship数量
		Integer pcount = dropshipProductMapper.getDropshipPrdouctCountByEmail(email, siteId);
		if(pcount == null){
			pcount = 0;
		}
		Integer leftCount = productCount - pcount;
		if(leftCount == 0 ){
			bb.setRes(-82007);
			bb.setMsg("Collection number reaches the upper limit");
			return bb;
		}
		
		DropshipProduct dpdto = new DropshipProduct();
		dpdto.setCemail(email);
		dpdto.setCsku(sku);
		dpdto.setBstate(true);
		dpdto.setIwebsiteid(siteId);
		DropshipProduct dp = dropshipProductMapper.getDropshipProduct(dpdto);
		if(dp != null){
			bb.setRes(-82008);
			bb.setMsg("drop ship product has favorites");
			return bb;
		}
		int res = 0;
		res = dropshipProductMapper.insertSelective(dpdto);
		if(res == 0){
			logger.error("dropship product add error [" + sku + "]-[" + email + "]" );
		}
		bb.setRes(1);
		return bb;
	}

}
