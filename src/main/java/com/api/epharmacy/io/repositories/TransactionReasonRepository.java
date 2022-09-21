package com.api.epharmacy.io.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.api.epharmacy.io.entity.TransactionReasonEntity;

public interface TransactionReasonRepository extends PagingAndSortingRepository<TransactionReasonEntity, Integer> {

	TransactionReasonEntity findByTransactionReasonIdAndIsDeleted(Integer transactionReasonId, boolean b);

	Page<TransactionReasonEntity> findByIsDeleted(boolean b, Pageable pageableRequest);

	Page<TransactionReasonEntity> findByTransactionReasonContainingAndIsDeleted(String searchKey, boolean b,
			Pageable pageableRequest);

}
