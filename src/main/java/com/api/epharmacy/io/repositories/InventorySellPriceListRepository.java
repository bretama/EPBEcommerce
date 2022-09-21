package com.api.epharmacy.io.repositories;

import java.time.Instant;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.api.epharmacy.io.entity.InventorySellPriceListEntity;

public interface InventorySellPriceListRepository extends PagingAndSortingRepository<InventorySellPriceListEntity, Long>{

	List<InventorySellPriceListEntity> findByInventoryIdAndIsDeleted(long inventoryId, boolean b);

	InventorySellPriceListEntity findTopByInventoryIdAndIsDeletedOrderByInventorySellPriceListIdDesc(long inventoryId, boolean b);

	InventorySellPriceListEntity findFirstByInventoryIdOrderBySellPriceDesc(long inventoryId);

	InventorySellPriceListEntity findTopByInventoryIdAndEffectiveDateTimeLessThanAndIsDeletedOrderByEffectiveDateTimeDesc(long orderItemId,
	        Instant now, boolean b);

	List<InventorySellPriceListEntity> findByInventoryId(long inventoryId);
	
	Page<InventorySellPriceListEntity> findByInventoryId(long inventoryId, Pageable pageableRequest);
	
	InventorySellPriceListEntity findTopByInventoryIdAndIsDeletedOrderByEffectiveDateTimeDesc(long inventoryId,
	        boolean b);
	
}


