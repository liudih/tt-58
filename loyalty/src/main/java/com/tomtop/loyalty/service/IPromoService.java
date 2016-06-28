package com.tomtop.loyalty.service;

import java.util.List;

import com.tomtop.loyalty.models.Prefer;
import com.tomtop.loyalty.models.Product;

public interface IPromoService {
	/**
	 * 推广码是否可用
	 * 
	 * @param code
	 * @param email
	 * @param products
	 * @param client
	 * @param terminal
	 * @param currency
	 * @return
	 */
	public Prefer isPromoAvailable(String code, String email,
			List<Product> products, Integer client, String terminal,
			String currency);
}
