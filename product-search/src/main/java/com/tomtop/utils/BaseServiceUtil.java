package com.tomtop.utils;


import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.tomtop.configuration.EsConfigSettings;
import com.tomtop.entity.Category;
import com.tomtop.entity.Langage;
import com.tomtop.entity.index.RemoteAttributeEntity;
import com.tomtop.entity.index.RemoteProductType;
import com.tomtop.framework.core.utils.BeanUtils;
import com.tomtop.services.IBaseInfoService;
import com.tomtop.services.ICategoryService;

/**
 * base 服务util
 * 
 * @author lijun
 *
 */
@Component
public class BaseServiceUtil {
	//private static final Logger logger = LoggerFactory
	//		.getLogger(BaseServiceUtil.class);

	@Autowired
	EsConfigSettings esSetting;
	@Autowired
	ICategoryService categoryService;
	@Autowired
	IBaseInfoService baseInfoService;
	
	public static Map<String, Map<String,RemoteProductType>> map = new HashMap<String, Map<String,RemoteProductType>>();

	@PostConstruct
	public void init() {
		try {
			//取所有语言
			Map<Integer, Langage> languages = baseInfoService.getLangageBeanMap();
			if (languages != null && languages.size() > 0) {
				if(map==null || map.size()<1){
					for (Map.Entry<Integer, Langage> entry : languages.entrySet()){
						int id = entry.getKey();
						String languageCode = entry.getValue().getCode();
						List<Category> obj = categoryService.getCategoryList(null, null, id, null);
						if (obj != null) {
							Map<String, RemoteProductType> productTypeMap =  new HashMap<String, RemoteProductType>();
							
							if (obj != null && obj.size() > 0) {
								RemoteProductType model = null;
								for (Category cat : obj) {
									model = new RemoteProductType();
									BeanUtils.copyPropertys(cat, model);
									//类目对应Id
									productTypeMap.put(String.valueOf(model.getIcategoryid()), model);
								}
							}
							map.put(languageCode, productTypeMap);
						}
					}
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * 获取类目信息
	 * 
	 * @param v
	 *            类目id
	 * @return
	 */
	public RemoteProductType getProductType(int v) {
		Assert.notNull(v, "类目id不能为空");
		RemoteProductType model = null;
		try {
			Category civo = categoryService.getCategoryByCategoryId(v, 1, 1);
			if (civo != null) {
				BeanUtils.copyPropertys(civo, model);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return model;
	}

	/**
	 * @return
	 */

	public static Map<String, Map<String,RemoteProductType>> getAllProductType() {
		return map;
	}

	/**
	 * 根据类目ID找到所有的key
	 * 
	 * @param productTypeId
	 * 
	 * 
	 */
	@SuppressWarnings("unchecked")
	public Map<String, List<RemoteAttributeEntity>> getAllShowKeyByProductTypeId(
			int productTypeId) {
		String url = this.esSetting.getBaseGetkeysbytypeid() + productTypeId;
		Map<String, List<RemoteAttributeEntity>> map = null;
		try {
			String result = HttpClientUtil.doGet(url);
			JSONObject obj = JSON.parseObject(result);
			if (obj != null) {
				if (obj.get("data") != null) {
					String mapStr = obj.get("data").toString();
					map = JSON.parseObject(mapStr, Map.class);
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return map;
	}

	/**
	 * 根据key找到所有的类目
	 * 
	 * @param key
	 * @return Map<String,RemoteAttributeEntity>
	 */
	@SuppressWarnings("unchecked")
	public Map<String, List<RemoteAttributeEntity>> getAllShowKeyByKey(
			String key) {
		Map<String, List<RemoteAttributeEntity>> map = new HashMap<String, List<RemoteAttributeEntity>>();
		try {
			String url = this.esSetting.getBaseGetkeysbytypeid() + key;
			String result = HttpClientUtil.doGet(url);
			if (StringUtils.isNotBlank(result)) {
				JSONObject obj = JSON.parseObject(result);
				if (obj != null) {
					if (obj.get("data") != null) {
						String mapStr = obj.get("data").toString();
						map = JSON.parseObject(mapStr, Map.class);
					}
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return map;
	}

	/**
	 * 查询所有key对应的value
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Map<String, List<String>> getAllShowValues() {
		String url = this.esSetting.getBaseAllshowvalues();
		Map<String, List<String>> map = new TreeMap<String, List<String>>();
		try {
			String result = HttpClientUtil.doGet(url);
			if (StringUtils.isNotBlank(result)) {
				JSONObject obj = JSON.parseObject(result);
				if (obj != null) {
					if (obj.get("data") != null) {
						String mapStr = obj.get("data").toString();
						map = JSON.parseObject(mapStr, Map.class);
					}
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return map;
	}

	/**
	 * 获取所有的需要展示的key
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Map<String, String> getAllShowKey() {
		String url = this.esSetting.getBaseAllshowkey();
		Map<String, String> map = new HashMap<String, String>();
		try {
			String result = HttpClientUtil.doGet(url);
			if (StringUtils.isNotBlank(result)) {
				JSONObject obj = JSON.parseObject(result);
				if (obj != null) {
					if (obj.get("data") != null) {
						String mapStr = obj.get("data").toString();
						map = JSON.parseObject(mapStr, Map.class);
					}
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return map;
	}

}
