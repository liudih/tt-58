package com.tomtop.member.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tomtop.member.mappers.product.ProductImageMapper;
import com.tomtop.member.service.IProductImageService;
@Service
public class ProductImageServiceImpl implements IProductImageService{

	@Autowired
	ProductImageMapper productImageMapper;
	
	@Override
	public String getProductSmallImageForMemberViewsByListingId(String listingId) {
		return this.productImageMapper.getProductSmallImageForMemberViewsByListingId(listingId);
	}

}
