package com.tomtop.loyalty.models.third;

import java.util.Map;

import com.tomtop.framework.core.utils.Result;
import com.tomtop.loyalty.models.Product;

public class ProductThird extends Result{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Map<String, Product> data;
	public Map<String, Product> getData() {
		return data;
	}
	public void setData(Map<String, Product> data) {
		this.data = data;
	}

	
}
