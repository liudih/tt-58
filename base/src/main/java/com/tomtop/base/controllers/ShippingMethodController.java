package com.tomtop.base.controllers;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.tomtop.base.models.bo.ShippingMethodBo;
import com.tomtop.base.models.bo.StorageBo;
import com.tomtop.base.models.vo.ShippingMethodVo;
import com.tomtop.base.service.IShippingMethodService;
import com.tomtop.base.service.IStoragService;
import com.tomtop.framework.core.utils.BeanUtils;
import com.tomtop.framework.core.utils.Result;

@RestController
public class ShippingMethodController {

	@Autowired
	private IStoragService storagService;

	@Autowired
	private IShippingMethodService shippingMethodService;

	@Cacheable(value = "ShippingMethod", keyGenerator = "customKeyGenerator")
	@RequestMapping(method = RequestMethod.GET, value = "/base/shippingMethod/v1")
	public Result getShippingMethod(
			@RequestParam(value = "storageName", required = false) String name,
			@RequestParam(value = "storageId", required = false) Integer id,
			@RequestParam(value = "lang", required = false, defaultValue = "1") Integer lang) {
		if (StringUtils.isBlank(name) && id == null) {
			Map<Integer, StorageBo> storages = Maps.uniqueIndex(
					storagService.getAll(), p -> p.getIid());
			List<ShippingMethodBo> dtos = shippingMethodService
					.getListByLang(lang);
			if (dtos != null && dtos.size() > 0) {
				Map<Integer, List<ShippingMethodBo>> maps = dtos
						.stream()
						.collect(
								Collectors
										.groupingBy(ShippingMethodBo::getStorageid));
				Map<String, List<ShippingMethodVo>> vomap = Maps.newHashMap();
				maps.forEach((k, v) -> {
					StorageBo bo = storages.get(k);
					if (bo != null
							&& StringUtils.isNoneBlank(bo.getCstoragename())) {
						vomap.put(bo.getCstoragename(), Lists
								.newArrayList(Lists.transform(v,
										p -> BeanUtils.mapFromClass(p,
												ShippingMethodVo.class))));
					}
				});
				return new Result(Result.SUCCESS, vomap);
			}
		} else if (id != null) {
			return new Result(Result.SUCCESS, shippingMethodService
					.getListByStorageId(id, lang).toArray());
		} else if (StringUtils.isNoneBlank(name)) {
			StorageBo sName = storagService.getByName(name);
			if (sName != null) {
				return new Result(Result.SUCCESS, shippingMethodService
						.getListByStorageId(sName.getIid(), lang).toArray());
			}
		}
		return new Result(Result.FAIL, null);
	}
}
