package com.api.epharmacy.io.repositories;

import java.time.Instant;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.api.epharmacy.io.entity.CustomerInventorySellPriceEntity;

public interface CustomerInventorySellPriceRepository extends PagingAndSortingRepository<CustomerInventorySellPriceEntity, Long> {

	Page<CustomerInventorySellPriceEntity> findByIsDeleted(boolean b, Pageable pageableRequest);

	CustomerInventorySellPriceEntity findTopByInventoryIdAndEffectiveDateTimeLessThanAndIsDeletedOrderByEffectiveDateTimeDesc(
			long inventoryId, Instant now, boolean b);

	Page<CustomerInventorySellPriceEntity> findByCustomerIdAndIsDeleted(String name, boolean b,
			Pageable pageableRequest);

	Page<CustomerInventorySellPriceEntity> findByCustomerIdAndInventoryIdAndIsDeleted(String name, long inventoryId,
			boolean b, Pageable pageableRequest);

	CustomerInventorySellPriceEntity findTopByInventoryIdAndCustomerIdAndEffectiveDateTimeLessThanAndIsDeletedOrderByEffectiveDateTimeDesc(
			long inventoryId, String name, Instant now, boolean b);

	CustomerInventorySellPriceEntity findByCustomerInventorySellPriceIdAndIsDeleted(long customerInventorySellPriceId,
			boolean b);
//
//	Page<CustomerInventorySellPriceEntity> findByCompanyIdAndInventoryIdAndIsDeleted(long companyId, long inventoryId,
//			boolean b, Pageable pageableRequest);

	Page<CustomerInventorySellPriceEntity> findByCompanyIdAndInventoryIdAndIsDeleted(long companyId, long inventoryId,
			boolean b, Pageable pageableRequest);

	Page<CustomerInventorySellPriceEntity> findByInventoryIdAndIsDeleted(long inventoryId, boolean b,
			Pageable pageableRequest);

}
