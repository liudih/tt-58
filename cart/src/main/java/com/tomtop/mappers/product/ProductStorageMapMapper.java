package com.tomtop.mappers.product;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.tomtop.dto.product.ProductStorageMap;

public interface ProductStorageMapMapper {
	
	int deleteByPrimaryKey(Integer iid);

    int insert(ProductStorageMap record);

    int insertSelective(ProductStorageMap record);

    ProductStorageMap selectByPrimaryKey(Integer iid);

    int updateByPrimaryKeySelective(ProductStorageMap record);

    int updateByPrimaryKey(ProductStorageMap record);

	@Update("update t_product_storage_map set iqty=iqty+(${qty}) where clistingid=#{listingId} and istorageid=${storageId}")
	public void updateQty(@Param("listingId") String listingId,@Param("storageId") int storageId,@Param("qty") int qty);
	
	@Select("select iqty from t_product_storage_map where clistingid=#{0} and istorageid=#{1}")
	public Integer getQty(String listingId,int storageId);
	
	public List<ProductStorageMap> getProductStorageMaps(@Param("listingids")List<String> listingids, 
			@Param("sid")Integer storageId);
}
