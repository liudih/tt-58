package com.tomtop.services.impl;

import java.util.Date;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tomtop.dto.Currency;
import com.tomtop.dto.CurrencyRate;
import com.tomtop.mappers.base.CurrencyMapper;
import com.tomtop.mappers.base.CurrencyRateMapper;
import com.tomtop.services.ICurrencyService;

@Service
public class CurrencyService implements ICurrencyService {

	@Autowired
	CurrencyMapper ccyMapper;

	@Autowired
	CurrencyRateMapper ccyRateMapper;

	/*
	 * (non-Javadoc)
	 * 
	 * @see services.base.ICurrencyService#getRate(java.lang.String)
	 */
	@Override
	public double getRate(String ccy) {
		CurrencyRate rate = ccyRateMapper.findLatestRateByCode(ccy, new Date());
		if (rate == null) {
			Currency currency = ccyMapper.findByCode(ccy);
			if (currency == null) {
				throw new RuntimeException("Currency Unavailable");
			}
			return currency.getFexchangerate();
		}
		return rate.getFexchangerate();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see services.base.ICurrencyService#exchange(double, java.lang.String)
	 */
	@Override
	public double exchange(double moneyInUSD, String targetCCY) {
		return moneyInUSD * getRate(targetCCY);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see services.base.ICurrencyService#exchange(double, java.lang.String,
	 * java.lang.String)
	 */
	@Override
	public double exchange(double money, String originalCCY, String targetCCY) {
		if (originalCCY.equals(targetCCY)) {
			return money;
		}
		return money * exchange(originalCCY, targetCCY);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see services.base.ICurrencyService#exchange(java.lang.String,
	 * java.lang.String)
	 */
	@Override
	public double exchange(String originalCCY, String targetCCY) {
		if (originalCCY.equals(targetCCY)) {
			return 1;
		}
		return getRate(targetCCY) / getRate(originalCCY);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see services.base.ICurrencyService#getAllCurrencies()
	 */
	@Override
	public List<Currency> getAllCurrencies() {
		return ccyMapper.getAllShowCurrencies();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see services.base.ICurrencyService#getCurrencyById(java.lang.Integer)
	 */
	@Override
	public Currency getCurrencyById(Integer currencyId) {
		return ccyMapper.getCurrencyById(currencyId);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see services.base.ICurrencyService#getCurrencyByCode(java.lang.String)
	 */
	@Override
	public Currency getCurrencyByCode(String currency) {
		return ccyMapper.findByCode(currency);
	};

	/*
	 * (non-Javadoc)
	 * 
	 * @see services.base.ICurrencyService#getCurrencyByCode(java.lang.String)
	 */
	public Currency getShowCurrencyByCode(String currency, boolean bshow) {
		return ccyMapper.findShowByCode(currency, bshow);
	};

	/*
	 * (non-Javadoc)
	 * 
	 * @see services.base.ICurrencyService#getCurrencyByCodes(java.util.List)
	 */
	@Override
	public List<Currency> getCurrencyByCodes(Set<String> currency) {
		return ccyMapper.findByCodes(currency);
	};

	/*
	 * (non-Javadoc)
	 * 
	 * @see services.base.ICurrencyService#findLatestRate()
	 */
	@Override
	public List<CurrencyRate> findLatestRate() {
		return ccyRateMapper.findLatestRate();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see services.base.ICurrencyService#findUsedRate()
	 */
	@Override
	public List<CurrencyRate> findUsedRate() {
		return ccyRateMapper.findUsedRate();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see services.base.ICurrencyService#findRateById(java.lang.Integer)
	 */
	@Override
	public CurrencyRate findRateById(Integer id) {
		return ccyRateMapper.findById(id);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see services.base.ICurrencyService#getMaxCurrency(java.lang.Integer)
	 */
	@Override
	public List<Currency> getMaxCurrency(int mid) {
		return ccyMapper.getMaxCurrency(mid);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see services.base.ICurrencyService#getCurrencyMaxId(java.lang.Integer)
	 */
	@Override
	public int getCurrencyMaxId() {
		return ccyMapper.getCurrencyByMaxId();
	}

}
