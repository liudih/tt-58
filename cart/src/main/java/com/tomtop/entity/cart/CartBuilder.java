package com.tomtop.entity.cart;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import com.tomtop.valueobjects.CartItem;

@Service
public class CartBuilder {

	@Autowired
	ApplicationContext appCtx;

	public Cart build() {
		Cart cart = appCtx.getBean(Cart.class);
		return cart;
	}

	/**
	 * 会排除items中的null
	 * 
	 * @param items
	 * @return
	 */
	public Cart build(List<CartItem> items) {
		if (items == null) {
			throw new NullPointerException("items is null");
		}
		Cart cart = appCtx.getBean(Cart.class);

		items.forEach(i -> {
			if (i != null) {
				cart.add(i);
			}
		});
		return cart;
	}
}
