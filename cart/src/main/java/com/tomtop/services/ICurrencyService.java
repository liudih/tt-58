package com.tomtop.services;

import java.util.List;
import java.util.Set;

import com.tomtop.dto.Currency;
import com.tomtop.dto.CurrencyRate;

public interface ICurrencyService {

	public double getRate(String ccy);

	public double exchange(double moneyInUSD, String targetCCY);

	/**
	 * 币种之间的转换
	 *
	 * @param money
	 *            面值
	 * @param originalCCY
	 *            原始币种
	 * @param targetCCY
	 *            目标币种
	 * @return 返回计算后原始数值
	 * @author luojiaheng
	 */
	public double exchange(double money, String originalCCY, String targetCCY);

	/**
	 * 获取汇率
	 *
	 * @param originalCCY
	 *            原始币种
	 * @param targetCCY
	 *            目标币种
	 * @return 返回计算后原始数值
	 * @author luojiaheng
	 */
	public double exchange(String originalCCY, String targetCCY);

	public List<Currency> getAllCurrencies();

	public Currency getCurrencyById(Integer currencyId);

	public Currency getCurrencyByCode(String currency);

	public List<Currency> getCurrencyByCodes(Set<String> currency);

	public List<CurrencyRate> findLatestRate();

	public List<CurrencyRate> findUsedRate();

	public CurrencyRate findRateById(Integer id);

	/**
	 * 获取币种 iid>mid
	 *
	 */
	public List<Currency> getMaxCurrency(int mid);

	/**
	 * 获取币种iid最大值
	 *
	 */
	public int getCurrencyMaxId();

}