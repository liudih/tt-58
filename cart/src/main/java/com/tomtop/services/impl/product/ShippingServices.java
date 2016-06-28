package com.tomtop.services.impl.product;

import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.google.api.client.util.Lists;
import com.google.common.collect.Collections2;
import com.google.common.collect.Ordering;
import com.tomtop.dto.Country;
import com.tomtop.dto.base.Storage;
import com.tomtop.dto.base.Website;
import com.tomtop.dto.product.ShippingStorage;
import com.tomtop.mappers.product.ShippingStorageMapper;
import com.tomtop.services.base.ICountryService;
import com.tomtop.services.base.IStorageService;
import com.tomtop.services.base.IWebsiteService;
import com.tomtop.services.product.IShippingService;
import com.tomtop.valueobjects.CartItem;
import com.tomtop.valueobjects.order.ShippingMethod;
import com.tomtop.valueobjects.order.ShippingMethodMini;

public class ShippingServices implements IShippingService {

	@Autowired
	ShippingStorageMapper shippingStorageMapper;

	@Autowired
	ICountryService countryEnquiryService;

	@Autowired
	IStorageService storageEnquiryService;

	@Autowired
	IWebsiteService websiteService;

	/*
	 * (non-Javadoc)
	 * 
	 * @see services.shipping.IShippingServices#getWebsiteLimitStorage(int)
	 */
	public Storage getWebsiteLimitStorage(int siteId) {
		Website website = websiteService.getWebsite(siteId);
		if (website != null) {
			int defaultstoregeid = website.getIdefaultshippingcountry();
			return storageEnquiryService
					.getStorageForStorageId(defaultstoregeid);
		} else {
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see services.shipping.IShippingServices#getStorages(java.util.List)
	 */
	public List<ShippingStorage> getStorages(List<String> listingids) {
		// modify by lijun
		if (listingids == null || listingids.size() == 0) {
			return null;
		}
		return shippingStorageMapper.getShoppingStorageForListings(listingids);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * services.shipping.IShippingServices#getCountryDefaultStorage(dto.Country)
	 */
	public Storage getCountryDefaultStorage(Country country) {
		if (country != null) {
			int defaultStorage = country.getIdefaultstorage();
			return storageEnquiryService.getStorageForStorageId(defaultStorage);
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see services.shipping.IShippingServices#getShippingStorage(int,
	 * java.util.List)
	 */
	public Storage getShippingStorage(int siteId, List<String> listingids) {
		return getShippingStorage(siteId, null, listingids);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see services.shipping.IShippingServices#getShippingStorage(int,
	 * dto.Country, java.util.List)
	 */
	public Storage getShippingStorage(int siteId, Country country,
			List<String> listingids) {
		Storage Websitestorage = getWebsiteLimitStorage(siteId);
		if (Websitestorage != null) {
			return Websitestorage;
		}
		// modify by lijun
		if (listingids == null || listingids.size() == 0) {
			throw new NullPointerException("listingids is null");
		}
		Storage overseasStorage = storageEnquiryService.getNotOverseasStorage();
		List<ShippingStorage> list = getStorages(listingids);
		// Storage storage = getCountryDefaultStorage(country);
		// if (list == null) {
		// return storage;
		// }

		List<ShippingStorage> newList = Lists
				.newArrayList(Collections2.filter(list, e -> !e.getIstorageid()
						.equals(overseasStorage.getIid())));
		Comparator<ShippingStorage> bycount = new Comparator<ShippingStorage>() {
			public int compare(final ShippingStorage p1,
					final ShippingStorage p2) {
				return p1.getIcount().compareTo(p2.getIcount());
			}
		};

		List<ShippingStorage> sortedCopy = Ordering.from(bycount).reverse()
				.sortedCopy(newList);
		if (sortedCopy.size() != 0) {
			if (sortedCopy.get(0).getIcount() == listingids.size()) {
				return storageEnquiryService.getStorageForStorageId(sortedCopy
						.get(0).getIstorageid());
			} else {
				return overseasStorage;
			}
		} else {
			return overseasStorage;
		}
	}

	@Override
	public boolean isSameStorage(List<String> listingids, String storageId) {
		if (listingids == null || listingids.size() == 0 || storageId == null
				|| storageId.length() == 0) {
			throw new NullPointerException();
		}
		List<String> sames = shippingStorageMapper.getSameStorageListings(
				listingids, storageId);
		return listingids.size() == sames.size();
	}

	@Override
	public List<ShippingMethod> getShipMethod(
			String shipToCountryCode, int storageId, int language,
			List<CartItem> items, String currencyCode, double totalPrice) {
		return null;
	}
	
	@Override
	public ShippingMethodMini getShipMethodInfo(String shipCode, Integer lang){
		return null;
	}
}
