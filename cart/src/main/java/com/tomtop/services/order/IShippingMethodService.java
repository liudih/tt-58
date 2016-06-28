package com.tomtop.services.order;

import java.util.List;

import com.tomtop.dto.order.ShippingMethod;
import com.tomtop.dto.order.ShippingMethodDetail;
import com.tomtop.entity.order.ShippingMethodRequst;
import com.tomtop.valueobjects.order.IOrderFragment;
import com.tomtop.valueobjects.order.ShippingMethodInformation;

public interface IShippingMethodService {

	public boolean isSpecial(List<String> listingIds);

	public List<ShippingMethodDetail> getShippingMethods(Integer storageId,
			String country, Double weight, Integer lang, Double subTotal,
			Boolean isSpecial);

	public ShippingMethod getShippingMethodById(Integer id);

	public ShippingMethodDetail getShippingMethodDetail(Integer id, Integer lang);

	public List<ShippingMethodDetail> getShippingMethodDetailByLanguageId(
			Integer languageId);

	public ShippingMethodDetail getShippingMethodDetailByCode(String code,
			Integer lang);

	public boolean checkIsAllfree(List<String> listingIds);

	public List<String> getPcodeList();

	public List<ShippingMethodInformation> processingInPlugin(
			List<ShippingMethodInformation> smiList, ShippingMethodRequst requst);

	public IOrderFragment getShippingMethodInformations(
			ShippingMethodRequst requst);

}