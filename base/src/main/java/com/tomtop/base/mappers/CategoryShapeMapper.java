package com.tomtop.base.mappers;


import java.util.List;
import org.apache.ibatis.annotations.Select;
import com.tomtop.base.models.dto.CategoryShapeDto;

public interface CategoryShapeMapper {

	@Select({"select client_id client,category_id categoryId,path,name,type from category_shape",
			"where client_id=#{0} and is_enabled=1 and is_deleted=0" })
	List<CategoryShapeDto> getCategoryShape(Integer client);
}
