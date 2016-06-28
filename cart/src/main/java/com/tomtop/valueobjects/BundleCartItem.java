package com.tomtop.valueobjects;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import com.google.common.collect.FluentIterable;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.tomtop.valueobjects.product.price.Price;

public class BundleCartItem extends CartItem implements Serializable {
	private static final long serialVersionUID = 1L;
	private ImmutableList<SingleCartItem> childList; // 商品属性
	private Price allPrice;
	// add by lijun
	private ImmutableList<String> allListingId = null;
	private ImmutableList<String> childListingId = null;
	private boolean frozen = false;

	public BundleCartItem(List<SingleCartItem> child) {
		if (child == null || child.size() == 0) {
			throw new IllegalArgumentException("child can not null");
		}
		this.childList = ImmutableList.<SingleCartItem> builder().addAll(child)
				.build();
	}

	public List<SingleCartItem> getChildList() {
		return childList;
	}

	public Price getAllPrice() {
		return allPrice;
	}

	public void setAllPrice(Price allPrice) {
		this.allPrice = allPrice;
	}

	public String getMainListingId() {
		return super.getClistingid();
	}

	private synchronized void initChildListing() {
		if (!frozen) {
			frozen = true;
			childListingId = FluentIterable.from(this.childList)
					.transform(c -> c.getClistingid()).toList();
			allListingId = ImmutableList.<String> builder()
					.add(this.getMainListingId()).addAll(childListingId)
					.build();
		}
	}

	public List<String> getAllListingId() {
		initChildListing();
		return allListingId;
	}

	public List<String> getChildListingId() {
		initChildListing();
		return childListingId;
	}

	public List<CartItem> getAllItems() {
		LinkedList<CartItem> result = Lists.newLinkedList();
		result.addAll(this.getChildList());
		result.add(this);
		return result;
	}
}
