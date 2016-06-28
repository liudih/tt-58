package com.tomtop.services.payment;

import java.util.List;
import java.util.Map;

public interface IPaymentService {

	/**
	 * 获取Payment by id
	 *
	 * @param id
	 * @return 如果不存在，则返回null
	 * @author luojiaheng
	 */
	public abstract IPaymentProvider getPaymentById(String id);

	public abstract Map<String, IPaymentProvider> getMap();

	public abstract List<String> filterByOrderTag(List<String> tags);

}