package com.tomtop.mappers.base;

import java.util.List;
import java.util.Set;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.tomtop.dto.Currency;

public interface CurrencyMapper {

	@Select("select * from t_currency where ccode = #{0} ")
	Currency findByCode(String code);

	@Select("select * from t_currency where ccode = #{0} and bshow=#{1} ")
	Currency findShowByCode(String code, boolean show);

	@Select({
			"<script>",
			"select * from t_currency where ",
			"ccode in ",
			"<foreach item='item' index='index' collection='list' open='(' separator=',' close=')'>#{item}</foreach> ",
			"</script>" })
	List<Currency> findByCodes(@Param("list") Set<String> codes);

	@Select("select iid,ccode,csymbol from t_currency where bshow=true")
	List<Currency> getAllShowCurrencies();

	@Select("select * from t_currency where iid=#{0}")
	Currency getCurrencyById(Integer currencyId);

	@Select("select iid,ccode,csymbol from t_currency where iid>#{0} and bshow=true order by iid asc")
	List<Currency> getMaxCurrency(int mid);

	@Select("select max(iid) from t_currency")
	int getCurrencyByMaxId();

}
