package com.tomtop.mappers.base;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Select;

import com.tomtop.dto.CurrencyRate;

public interface CurrencyRateMapper {

	@Select("select * from t_currency_rate "
			+ "where buse=true and ccode = #{0} and dcreatedate < #{1} "
			+ "order by dcreatedate limit 1")
	CurrencyRate findLatestRateByCode(String code, Date now);

	@Select("select * from t_currency_rate where iid in (select max(iid) from t_currency_rate group by ccode)")
	List<CurrencyRate> findLatestRate();

	@Select("select * from t_currency_rate where buse = true")
	List<CurrencyRate> findUsedRate();

	@Select("select * from t_currency_rate where ccode = #{0} order by iid desc limit 1")
	CurrencyRate getLatestRateByCode(String code);

	@Select("select * from t_currency_rate where iid = #{0} limit 1")
	CurrencyRate findById(Integer id);
}
