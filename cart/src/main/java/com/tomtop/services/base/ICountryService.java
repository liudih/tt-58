package com.tomtop.services.base;

import java.util.List;

import com.tomtop.dto.Country;

public interface ICountryService {

	public abstract Country getCountryByCountryId(Integer iid);

	public abstract Country getCountryByShortCountryName(String cshortname);

	public abstract Integer getLanguageByShortCountryName(String cshortname);

	public abstract List<Country> getAllCountries();

	public abstract List<Country> getReallyAllCountries();

	public abstract Integer getCountryMaxId();

	public abstract List<Country> getMaxCountry(Integer mid);

	public abstract List<Country> getAllCountry();

	/**
	 * 判断国家是否是可发货的国家
	 * 
	 * @author lijun
	 * @param shortName
	 * @return
	 */
	public boolean isShipable(String shortName);

	/**
	 * 判断国家是否是可发货的国家
	 * 
	 * @author lijun
	 * @param shortName
	 * @return
	 */
	public boolean isShipable(Country country);

}