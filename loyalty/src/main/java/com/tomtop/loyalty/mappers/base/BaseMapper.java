package com.tomtop.loyalty.mappers.base;

import org.apache.ibatis.annotations.Select;

public interface BaseMapper {
	@Select("select ccode from t_currency where iid = #{0}")
	String getCodeById(Integer id);
}
