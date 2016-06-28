package com.tomtop.services.impl;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tomtop.entity.BaseBean;
import com.tomtop.entity.WholeSaleBase;
import com.tomtop.entity.WholeSaleProduct;
import com.tomtop.mappers.wholesale.WholeSaleBaseMapper;
import com.tomtop.mappers.wholesale.WholeSaleProductMapper;
import com.tomtop.services.IWholeSaleProductService;


@Service
public class WholeSaleProductServiceImpl implements IWholeSaleProductService {

	@Autowired
	WholeSaleBaseMapper wholeSaleBaseMapper;
	@Autowired
	WholeSaleProductMapper wholeSaleProductMapper;
	
	private final static Integer STATUS = 1;
	
	@Override
	public BaseBean addWholeSaleProduct(String email, String sku, Integer qty,
			Integer siteId) {
		BaseBean bb = new BaseBean();
		if(email == null || "".equals(email)){
		    bb.setRes(-83001);
		    bb.setMsg("email is null");
		    return bb;
		}
		if(sku == null || "".equals(sku)){
		    bb.setRes(-83002);
			bb.setMsg("sku is null");
		    return bb;
		}
		if(qty == null || qty < 5){
			qty = 5;
		}
		if(siteId == null){
			 siteId = 1;
		}
		
		WholeSaleBase wsbdto = wholeSaleBaseMapper.getWholeSaleBaseDto(email,siteId,STATUS);
		if(wsbdto == null){
			 bb.setRes(-83003);
			 bb.setMsg("Not WholeSale user");
			 return bb;
		}
		int res = 0;
		WholeSaleProduct wspdto = wholeSaleProductMapper.getWholeSaleProductsByEmailAndSkuAndWebsite(email, siteId, sku);
		if(wspdto == null){
			wspdto = new WholeSaleProduct();
			wspdto.setCemail(email);
			wspdto.setCsku(sku);
			wspdto.setIwebsiteid(siteId);
			wspdto.setIqty(qty);
			res = wholeSaleProductMapper.addWholeSaleProduct(wspdto);
			if(res <= 0){
				 bb.setRes(-83004);
				 bb.setMsg("WholeSale product add failure");
				 return bb;
			}
		}else{
			WholeSaleProduct upd = new WholeSaleProduct();
			qty += wspdto.getIqty();
			upd.setIid(wspdto.getIid());
			upd.setIqty(qty);
			res = wholeSaleProductMapper.updateQtyByIid(upd);
			if(res <= 0){
				 bb.setRes(-83005);
				 bb.setMsg("WholeSale product update failure");
				 return bb;
			}
		}
		bb.setRes(1);
		
		return bb;
	}

}
