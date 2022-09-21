package com.api.epharmacy.io.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.api.epharmacy.io.entity.CustomerSoldInventoryEntity;

public interface CustomerSoldInventoryRepository extends PagingAndSortingRepository<CustomerSoldInventoryEntity, Long> {

	Page<CustomerSoldInventoryEntity> findByIsDeleted(boolean b, Pageable pageableRequest);

	CustomerSoldInventoryEntity findByCustomerSoldInventoryIdAndIsDeleted(long customerSoldInventoryId, boolean b);

	Object findBySellNumber(String sellNumber);

	Page<CustomerSoldInventoryEntity> findByCustomerIdAndIsDeleted(String name, boolean b, Pageable pageableRequest);

	Page<CustomerSoldInventoryEntity> findByCompanyIdAndIsDeleted(long companyId, boolean b, Pageable pageableRequest);

	Page<CustomerSoldInventoryEntity> findByCompanyIdAndCreatedByAndIsDeleted(long companyId, String name, boolean b,
			Pageable pageableRequest);

}
