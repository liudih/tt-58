package com.tomtop.dao.impl.order;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.api.client.util.Maps;
import com.tomtop.dao.order.IOrderDetailDao;
import com.tomtop.mappers.order.DetailMapper;
import com.tomtop.valueobjects.CartItem;

@Service
public class OrderDetailDao implements IOrderDetailDao {

	@Autowired
	DetailMapper detailMapper;

	public List<CartItem> selectCartItemsByOrderNum(String orderNum) {
		if (orderNum == null || orderNum.length() == 0) {
			throw new NullPointerException("orderNum is null");
		}
		Map<String, Object> paras = Maps.newHashMap();
		paras.put("orderNum", orderNum);
		return detailMapper.selectCartItemsByOrderNum(paras);
	}
}
