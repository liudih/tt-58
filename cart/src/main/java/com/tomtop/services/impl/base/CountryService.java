package com.tomtop.services.impl.base;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.FluentIterable;
import com.tomtop.dto.Country;
import com.tomtop.mappers.base.CountryMapper;
import com.tomtop.services.base.ICountryService;
import com.tomtop.services.base.ILanguageService;

@Service
public class CountryService implements ICountryService {

	@Autowired
	CountryMapper countryMapper;

	@Autowired
	ILanguageService languageService;

	/*
	 * (non-Javadoc)
	 * 
	 * @see services.ICountryService#getCountryByCountryId(java.lang.Integer)
	 */
	@Override
	public Country getCountryByCountryId(Integer iid) {
		return countryMapper.getCountryByCountryId(iid);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * services.ICountryService#getCountryByShortCountryName(java.lang.String)
	 */
	@Override
	public Country getCountryByShortCountryName(String cshortname) {
		return countryMapper.getCountryByCountryName(cshortname);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * services.ICountryService#getLanguageByShortCountryName(java.lang.String)
	 */
	@Override
	public Integer getLanguageByShortCountryName(String cshortname) {
		Country country = countryMapper.getCountryByCountryName(cshortname);
		if (null != country && null != country.getIlanguageid()) {
			return country.getIlanguageid();
		}
		return languageService.getDefaultLanguage().getIid();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see services.ICountryService#getAllCountries()
	 */
	@Override
	public List<Country> getAllCountries() {
		List<Country> countries = getReallyAllCountries();
		return FluentIterable.from(countries).filter(c -> c.getBshow())
				.toList();
	}

	@Override
	public List<Country> getReallyAllCountries() {
		return countryMapper.getAllCountry();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see services.ICountryService#getCountryMaxId()
	 */
	@Override
	public Integer getCountryMaxId() {
		int maxid = countryMapper.getCountryMaxId();
		return maxid;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see services.ICountryService#getMaxCountry(java.lang.Integer)
	 */
	@Override
	public List<Country> getMaxCountry(Integer mid) {
		return countryMapper.getMaxCountry(mid);
	}

	public List<Country> getAllCountry() {
		List<Country> countries = countryMapper.getAllCountry();
		return countries;
	}

	@Override
	public boolean isShipable(String shortName) {
		Country country = this.getCountryByShortCountryName(shortName);
		if (country != null && country.getBshow()) {
			return true;
		}
		return false;
	}

	@Override
	public boolean isShipable(Country country) {
		if (country != null && country.getBshow()) {
			return true;
		}
		return false;
	}
}
