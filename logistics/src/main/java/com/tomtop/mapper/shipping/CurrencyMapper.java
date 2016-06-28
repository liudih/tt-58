package com.tomtop.mapper.shipping;

import org.apache.ibatis.annotations.Select;

import com.tomtop.entry.po.base.Currency;


public interface CurrencyMapper {

	@Select("SELECT id iid,code ccode,symbol_code csymbol, current_rate fexchangerate from base_currency where code= #{0} and is_deleted=0")
	Currency findByCode(String code);
	
}
