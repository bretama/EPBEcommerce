package com.api.epharmacy.service.Impl;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.api.epharmacy.io.entity.InventoryEntity;
import com.api.epharmacy.io.entity.InventorySellPriceListEntity;
import com.api.epharmacy.io.repositories.InventoryCostPriceListRepository;
import com.api.epharmacy.io.repositories.InventoryRepository;
import com.api.epharmacy.io.repositories.InventorySellPriceListRepository;
import com.api.epharmacy.service.InventoryPriceListService;
import com.api.epharmacy.ui.model.response.InventoryListViewResponse;
import com.api.epharmacy.ui.model.response.InventoryPriceListResponse;
import com.api.epharmacy.ui.model.response.InventorySellPriceDetail;

@Service
public class InventoryPriceListServiceImpl implements InventoryPriceListService {
	
	@Autowired
	InventoryCostPriceListRepository inventoryCostPriceListRepository;
	
	@Autowired
	InventorySellPriceListRepository inventorySellPriceListRepository;
	
	@Autowired
	InventoryRepository inventoryRepository;
	
	@Override
	public InventorySellPriceDetail getInventoryItemPriceList(long inventoryId, Integer page, Integer limit) {
		
		InventorySellPriceDetail returnValue = new InventorySellPriceDetail();
		List<InventoryPriceListResponse> inventoryPriceListResponses = new ArrayList<>();
		
		if (page > 0)
			page = page - 1;
		
		Pageable pageableRequest = PageRequest.of(page, limit, Sort.by("inventorySellPriceListId").descending());
		Page<InventorySellPriceListEntity> priceListPage = inventorySellPriceListRepository.findByInventoryId(inventoryId,
		    pageableRequest);
		List<InventorySellPriceListEntity> priceLists = priceListPage.getContent();
		int totalPage = priceListPage.getTotalPages();
		//		List<InventorySellPriceListEntity> priceLists = inventorySellPriceListRepository.findByInventoryId(inventoryId);
		
		InventoryEntity inventoryEntity = inventoryRepository.findByInventoryIdAndIsDeleted(inventoryId, false);
		InventoryListViewResponse inventoryListViewResponse = new InventoryListViewResponse();
		BeanUtils.copyProperties(inventoryEntity, inventoryListViewResponse);
		returnValue.setInvenoryDetail(inventoryListViewResponse);
		
		InventorySellPriceListEntity inventorySellPriceListEntity =inventorySellPriceListRepository
		        .findTopByInventoryIdAndEffectiveDateTimeLessThanAndIsDeletedOrderByEffectiveDateTimeDesc(inventoryId,
		            Instant.now(), false);
//		InventorySellPriceListEntity inventorySellPriceListEntity1 = inventorySellPriceListRepository
//		        .findTopByInventoryIdAndIsDeletedOrderByEffectiveDateTimeDesc(inventoryId, false);
		
		for (InventorySellPriceListEntity priceList : priceLists) {
			InventoryPriceListResponse inventoryPriceListResponse = new InventoryPriceListResponse(); 
			BeanUtils.copyProperties(priceList, inventoryPriceListResponse);
			if (priceList.getInventorySellPriceListId() == inventorySellPriceListEntity.getInventorySellPriceListId())
				inventoryPriceListResponse.setActiveSellPrice(true);
			
			if (inventoryPriceListResponses.size() == 0)
				inventoryPriceListResponse.setTotalPage(totalPage);
			inventoryPriceListResponses.add(inventoryPriceListResponse);
	    }
		

		returnValue.setInventoryPriceList(inventoryPriceListResponses);
		return returnValue;
	}

}
