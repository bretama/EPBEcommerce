package com.api.epharmacy.io.repositories;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.api.epharmacy.io.entity.InventoryEntity;

public interface InventoryRepository extends PagingAndSortingRepository<InventoryEntity, Long>{

	InventoryEntity findByInventoryGenericNameIgnoreCaseContainingAndInventoryBrandNameIgnoreCaseContainingAndInventoryTypeIgnoreCaseContaining(
			String inventoryGenericName, String inventoryBrandName, String inventoryType);

	InventoryEntity findByInventoryId(long inventoryId);

	Page<InventoryEntity> findByInventoryGenericNameContainingOrInventoryBrandNameContainingOrInventoryTypeContainingOrMeasuringUnitContaining(
			String searchKey, String searchKey2, String searchKey3, String searchKey4, Pageable pageableRequest);

	Object findByInventoryGenericNameIgnoreCaseAndInventoryBrandNameIgnoreCaseAndInventoryTypeIgnoreCaseAndDosageFormIgnoreCaseAndStrengthIgnoreCase(
			String inventoryGenericName, String inventoryBrandName, String inventoryType, String dosageForm,
			String strength);
	
	@Query("SELECT DISTINCT inventoryType FROM inventory")
	List<String> findDistinctInventoryType();

	Page<InventoryEntity> findByInventoryGenericNameIgnoreCaseAndInventoryBrandNameIgnoreCaseAndInventoryTypeIgnoreCaseAndDosageFormIgnoreCaseAndStrengthIgnoreCase(
			String genericName, String brandName, String inventoryType, String dosageForm, String strength,
			Pageable pageableRequest);

	List<InventoryEntity> findByInventoryIdIn(Long[] updatedInventotyIds);

	Page<InventoryEntity> findByIsDeleted(boolean b, Pageable pageableRequest);

	InventoryEntity findByInventoryIdAndIsDeleted(long inventoryId, boolean b);

	Page<InventoryEntity> findByInventoryGenericNameContainingOrInventoryBrandNameContaining(String searchKey,
	        String searchKey2, Pageable pageableRequest);

	List<InventoryEntity> findByIsDeleted(boolean b);
	
	Page<InventoryEntity> findByInventoryGenericNameContainingOrInventoryBrandNameContainingAndInventoryType(
	        String searchKey, String searchKey2, String inventoryType, Pageable pageableRequest);
	
	Page<InventoryEntity> findByInventoryType(String inventoryType, Pageable pageableRequest);
	
	Page<InventoryEntity> findByInventoryTypeAndInventoryGenericNameContainingOrInventoryBrandNameContaining(
	        String searchKey, String searchKey2, String inventoryType, Pageable pageableRequest);

	Page<InventoryEntity> findByInventoryIdIn(List<Long> inventoryIds, Pageable pageableRequest);

	Page<InventoryEntity> findByInventoryIdInAndInventoryGenericNameContainingOrInventoryIdInAndInventoryBrandNameContainingOrInventoryIdInAndInventoryTypeContainingOrInventoryIdInAndMeasuringUnitContaining(
			List<Long> inventoryIds, String searchKey, List<Long> inventoryIds2, String searchKey2,
			List<Long> inventoryIds3, String searchKey3, List<Long> inventoryIds4, String searchKey4,
			Pageable pageableRequest);
	
}
