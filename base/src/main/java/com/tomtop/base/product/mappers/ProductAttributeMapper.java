package com.tomtop.base.product.mappers;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.tomtop.base.models.dto.AttributeDto;

public interface ProductAttributeMapper {

	List<AttributeDto> getAttributeAttributeDto(@Param("lang") Integer lang);
}
