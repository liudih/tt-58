package com.tomtop.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * 常量工具实体类
 * @author renyy
 * @Date 2016-03-14
 */
public class ConstantUtil {

	 /**es索引映射文件*/
	 public static final String ES_CONFIG_MAPPING="mapping_path";
	 /**索引名称前缀*/
	 public static final String ES_INDEX_PREFIX="index_product_";
	 /**产品索引类型*/
	 public static final String ES_INDEX_PRODUCT_TYPE = "product";
	 /**配置文件索引域名站点的健对应的值*/
	 public static final String ES_CONFIG_PRODUCT_INDEX_ALL_KEY="product.all";
	 /**聚合属性键*/
	 public static final String ES_CONFIG_PRODUCT_INDEX_AGGREGATING_PROPERTIES="aggregating_properties";
	 /**索引Mapping是否已经存在Map<索引名称，索引Mapping存在状态>,true 为存在，false不存在*/
	 public static Map<String,Boolean> indexMappingFlagCache = new HashMap<String,Boolean>();
	 /**UTC查询时间格式**/
	 public static final String PATTERN = "yyyy-MM-dd HH:mm";
	 
}
