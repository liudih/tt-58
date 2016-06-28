package com.tomtop.entity.cart;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.google.common.collect.FluentIterable;
import com.google.common.collect.Lists;
import com.tomtop.services.order.ICheckoutService;
import com.tomtop.services.order.IFreightService;
import com.tomtop.valueobjects.CartItem;

/**
 * 
 * @author lijun
 *
 */
public class Cart {

	private List<CartItem> items = Lists.newLinkedList();
	private boolean frozen = false;

	@Autowired
	IFreightService freightService;

	@Autowired
	ICheckoutService checkoutService;

	public void add(CartItem item) {
		if (frozen) {
			throw new UnsupportedOperationException();
		}
		if (item == null) {
			throw new NullPointerException("item is null");
		}
		this.items.add(item);
	}

	/**
	 * 当购物车构造完成后把购物车锁定,锁定的购物车不能再添加item
	 */
	public void frozen() {
		this.frozen = true;
	}

	/**
	 * 计算总重量
	 * 
	 * @return
	 */
	public Double getTotalWeight() {
		return this.freightService.getTotalWeightV2(items);
	}

	/**
	 * 计算总重量(但是排除freeshiping的item的重量)
	 * 
	 * @return
	 */
	public Double getTotalWeightExcludeFreeshiping() {
		return this.freightService.getTotalShipWeight(items);
	}

	public List<String> getAllListingId() {
		return FluentIterable.from(items).transform(i -> i.getClistingid())
				.toList();
	}

	/**
	 * 获取商品的应付基本金额(不包括优惠券 积分 推广码)
	 * 
	 * @return
	 */
	public Double getBaseTotal() {
		return checkoutService.subToatl(items);
	}
}
