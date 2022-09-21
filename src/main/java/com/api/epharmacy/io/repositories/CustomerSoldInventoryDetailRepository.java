package com.api.epharmacy.io.repositories;

import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.api.epharmacy.io.entity.CustomerSoldInventoryDetailEntity;

public interface CustomerSoldInventoryDetailRepository extends PagingAndSortingRepository<CustomerSoldInventoryDetailEntity, Long> {

	List<CustomerSoldInventoryDetailEntity> findByCustomerSoldInventoryIdAndIsDeleted(long customerSoldInventoryId,
			boolean b);

	CustomerSoldInventoryDetailEntity findByCustomerSoldInventoryDetailIdAndIsDeleted(
			long customerSoldInventoryDetailId, boolean b);

}
