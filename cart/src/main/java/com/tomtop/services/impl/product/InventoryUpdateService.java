package com.tomtop.services.impl.product;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tomtop.dto.product.InventoryHistory;
import com.tomtop.mappers.product.InventoryHistoryMapper;
import com.tomtop.mappers.product.ProductStorageMapMapper;

@Service
public class InventoryUpdateService {
	private static final Logger LOGGER = LoggerFactory
			.getLogger(InventoryUpdateService.class);
	@Autowired
	InventoryHistoryMapper inventoryHistoryMapper;

	@Autowired
	ProductStorageMapMapper mapper;

	public boolean insert(InventoryHistory ih) {
		try {
			int sum = mapper.getQty(ih.getClistingid(), ih.getIstorageid());
			ih.setBenabled(true);
			ih.setIbeforechangeqty(sum);
			ih.setIafterchangeqty(sum + ih.getIqty());
			inventoryHistoryMapper.insert(ih);

			mapper.updateQty(ih.getClistingid(), ih.getIstorageid(),
					ih.getIqty());
			return true;

		} catch (Exception e) {
			LOGGER.error("database operate error,orderNum:{} store:{}",
					ih.getClistingid(), ih.getIstorageid(), e);
			return false;
		}

	}

}
