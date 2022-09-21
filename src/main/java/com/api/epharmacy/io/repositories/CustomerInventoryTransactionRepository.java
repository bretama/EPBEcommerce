package com.api.epharmacy.io.repositories;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.api.epharmacy.io.entity.CustomerInventoryTransactionEntity;

public interface CustomerInventoryTransactionRepository extends PagingAndSortingRepository<CustomerInventoryTransactionEntity, Long> {

	Page<CustomerInventoryTransactionEntity> findByIsDeleted(boolean b, Pageable pageableRequest);

	CustomerInventoryTransactionEntity findByCustomerInventoryTransactionIdAndIsDeleted(
			long customerInventoryTransactionId, boolean b);

	List<CustomerInventoryTransactionEntity> findByInventoryIdAndIsDeleted(long inventoryId, boolean b);

	Page<CustomerInventoryTransactionEntity> findByCustomerIdAndIsDeleted(String name, boolean b,
			Pageable pageableRequest);
	List<CustomerInventoryTransactionEntity> findByCustomerIdAndIsDeleted(String name, boolean b);

	Page<CustomerInventoryTransactionEntity> findByCompanyIdAndIsDeleted(long companyId, boolean b,
			Pageable pageableRequest);

	Page<CustomerInventoryTransactionEntity> findByCompanyIdAndCreatedByAndIsDeleted(long companyId, String name,
			boolean b, Pageable pageableRequest);

}
