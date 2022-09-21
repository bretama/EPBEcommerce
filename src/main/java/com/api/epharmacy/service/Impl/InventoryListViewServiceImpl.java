package com.api.epharmacy.service.Impl;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.api.epharmacy.io.entity.InventoryEntity;
import com.api.epharmacy.io.entity.InventorySellPriceListEntity;
import com.api.epharmacy.io.repositories.InventoryCostPriceListRepository;
import com.api.epharmacy.io.repositories.InventoryRepository;
import com.api.epharmacy.io.repositories.InventorySellPriceListRepository;
import com.api.epharmacy.service.InventoryListViewService;
import com.api.epharmacy.ui.model.request.InventoryListViewSearchRequestModel;
import com.api.epharmacy.ui.model.response.InventoryListViewResponse;

@Service
public class InventoryListViewServiceImpl implements InventoryListViewService {

	@Autowired
	InventoryRepository inventoryRepository;

	@Autowired
	InventoryCostPriceListRepository inventoryCostPriceListRepository;

	@Autowired
	InventorySellPriceListRepository inventorySellPriceListRepository;
	
	@Autowired
	EntityManager entityManager;
	
	@Override
	public List<InventoryListViewResponse> getInventeryListView(int page, int limit) {
		
		List<InventoryListViewResponse> returnValue = new ArrayList<>();
	    
	    if(page > 0) page = page - 1;
	   
	    Pageable pageableRequest = PageRequest.of(page, limit);
	    //Page<InventoryEntity> inventoryPage = inventoryRepository.findByAvailableQuantityNot(notZero,pageableRequest);
	    Page<InventoryEntity> inventoryPage = inventoryRepository.findByIsDeleted(false,pageableRequest);
	    int totalPages = inventoryPage.getTotalPages();
	    List<InventoryEntity> inventoryItems = inventoryPage.getContent();
	    for(InventoryEntity inventoryEntity : inventoryItems) {
			InventoryListViewResponse inventoryListViewResponse = new InventoryListViewResponse();
	    	BeanUtils.copyProperties(inventoryEntity, inventoryListViewResponse); 
	    	if(returnValue.size() == 0) {
	    		inventoryListViewResponse.setTotalPages(totalPages);
	    	}
	    	InventorySellPriceListEntity price = inventorySellPriceListRepository.findTopByInventoryIdAndEffectiveDateTimeLessThanAndIsDeletedOrderByEffectiveDateTimeDesc(inventoryEntity.getInventoryId(), Instant.now(), false);						
	    	if(price != null) {
	    		inventoryListViewResponse.setSellPrice(price.getSellPrice());
		    	inventoryListViewResponse.setDiscountAmount(price.getDiscountAmount());
		    	returnValue.add(inventoryListViewResponse);
	    	}
	    }
	    
		return returnValue;
	}

	@Override
	public InventoryListViewResponse getInventoryByInventoryId(long inventoryId) {

		InventoryListViewResponse returnvalue = new InventoryListViewResponse();
		InventoryEntity inventoryEntity = inventoryRepository.findByInventoryId(inventoryId);
		if(inventoryEntity == null) throw new RuntimeException("Inventory Item not found.");
		
		InventorySellPriceListEntity price = inventorySellPriceListRepository
		        .findTopByInventoryIdAndEffectiveDateTimeLessThanAndIsDeletedOrderByEffectiveDateTimeDesc(
		            inventoryEntity.getInventoryId(), Instant.now(), false);
		if (price != null) {
			returnvalue.setSellPrice(price.getSellPrice());
			returnvalue.setDiscountAmount(price.getDiscountAmount());
		}
		
		BeanUtils.copyProperties(inventoryEntity, returnvalue);
		
		return returnvalue;
	}

	@Override
	public List<InventoryListViewResponse> searchInventoryListView(InventoryListViewSearchRequestModel searchDetails,
			int page, int limit) {
		
		List<InventoryListViewResponse> returnValue = new ArrayList<>();
	    
	    if(page > 0) page = page - 1;
	    
	    PageRequest.of(page, limit);
		String searchKey = searchDetails.getInventoryName();
	    String inventoryType = searchDetails.getInventoryType();
		List<Object[]> results;
		long totalRows = 0;
		if (inventoryType.equals("") && !searchKey.equals("")) {
			results = entityManager.createQuery(
			    "select i.inventoryId, i.inventoryGenericName, i.inventoryType, i.subCategory, i.inventoryBrandName, i.dosageForm, "
			            + "i.strength, i.volume, i.measuringUnit, i.availableQuantity, i.inventoryImage from inventory i WHERE i.inventoryGenericName "
			            + "like '%' || :searchKey || '%' OR i.inventoryBrandName like '%' || :searchKey || '%'",
			    Object[].class).setParameter("searchKey", searchKey).setFirstResult((page) * limit).setMaxResults(limit)
			        .getResultList();
			
			totalRows = (long) entityManager.createQuery(
					"SELECT count(*) from inventory i WHERE i.inventoryGenericName "
					+ "like '%' || :searchKey || '%' OR i.inventoryBrandName like '%' || :searchKey || '%'")
					.setParameter("searchKey", searchKey).getSingleResult();
		}
		
		else if (!inventoryType.equals("") && searchKey.equals("")) {
			results = entityManager.createQuery(
			    "select i.inventoryId, i.inventoryGenericName, i.inventoryType, i.subCategory, i.inventoryBrandName, i.dosageForm, "
			            + "i.strength, i.volume, i.measuringUnit, i.availableQuantity, i.inventoryImage from inventory i WHERE i.inventoryType=:inventoryType ",
			    Object[].class).setParameter("inventoryType", inventoryType).setFirstResult((page) * limit)
			        .setMaxResults(limit).getResultList();
			
			totalRows = (long) entityManager.createQuery(
				"SELECT count(*) from inventory i  WHERE i.inventoryType=:inventoryType")
				.setParameter("inventoryType", inventoryType).getSingleResult();
			
		}
		
		else if (!inventoryType.equals("") && !searchKey.equals("")) {
			results = entityManager.createQuery(
			    "select i.inventoryId, i.inventoryGenericName, i.inventoryType, i.subCategory, i.inventoryBrandName, i.dosageForm, "
			            + "i.strength, i.volume, i.measuringUnit, i.availableQuantity, i.inventoryImage from inventory i WHERE i.inventoryGenericName "
			            + "like '%' || :searchKey || '%' OR i.inventoryBrandName like '%' || :searchKey || '%' AND "
			            + "i.inventoryType=:inventoryType",
			    Object[].class).setParameter("searchKey", searchKey).setParameter("inventoryType", inventoryType)
			        .setFirstResult((page) * limit).setMaxResults(limit).getResultList();
			
			totalRows = (long) entityManager.createQuery(
				"SELECT count(*) from inventory i WHERE i.inventoryGenericName "
				+ "like '%' || :searchKey || '%' OR i.inventoryBrandName like '%' || :searchKey || '%' AND "
				+ "i.inventoryType=:inventoryType")
				.setParameter("searchKey", searchKey).setParameter("inventoryType", inventoryType).getSingleResult();
		}
		
		else {
			results = entityManager.createQuery(
			    "select i.inventoryId, i.inventoryGenericName, i.inventoryType, i.subCategory, i.inventoryBrandName, i.dosageForm, "
			            + "i.strength, i.volume, i.measuringUnit, i.availableQuantity, i.inventoryImage from inventory i",
			    Object[].class).setFirstResult((page) * limit).setMaxResults(limit).getResultList();
			
			totalRows = (long) entityManager.createQuery(
				"SELECT count(*) from inventory i").getSingleResult();
		}
		
		// int totalPages = searchPage.getTotalPages();
		// List<InventoryEntity> searchItems = searchPage.getContent();
		for (Object[] result : results) {
	    	InventoryListViewResponse inventoryListViewResponse = new InventoryListViewResponse(); 
			inventoryListViewResponse.setInventoryId((long) result[0]);
			inventoryListViewResponse.setInventoryGenericName((String) result[1]);
			inventoryListViewResponse.setInventoryType((String) result[2]);
			inventoryListViewResponse.setSubCategory((String) result[3]);
			inventoryListViewResponse.setInventoryBrandName((String) result[4]);
			inventoryListViewResponse.setDosageForm((String) result[5]);
			inventoryListViewResponse.setStrength((String) result[6]);
			inventoryListViewResponse.setVolume((String) result[7]);
			inventoryListViewResponse.setMeasuringUnit((String) result[8]);
			inventoryListViewResponse.setAvailableQuantity((double) result[9]);
			inventoryListViewResponse.setInventoryImage((String) result[10]);
			
			if (returnValue.size() == 0) {
				if (totalRows % limit == 0)
					inventoryListViewResponse.setTotalPages((int) totalRows / limit);
				else {
					inventoryListViewResponse.setTotalPages(((int) totalRows / limit) + 1);
				}
			}
			
			InventorySellPriceListEntity priceList = inventorySellPriceListRepository
			        .findFirstByInventoryIdOrderBySellPriceDesc(inventoryListViewResponse.getInventoryId());
			if (priceList != null) {
				inventoryListViewResponse.setSellPrice(priceList.getSellPrice());
				inventoryListViewResponse.setDiscountAmount(priceList.getDiscountAmount());
				if (inventoryType == null || inventoryType == "") {
					returnValue.add(inventoryListViewResponse);
				} else {
					if (inventoryType.equals(inventoryListViewResponse.getInventoryType())) {
						returnValue.add(inventoryListViewResponse);
					}
				}
	    	}
			
	    }
		//	    for(InventoryEntity searchItem : searchItems) {
		//	    	InventoryListViewResponse inventoryListViewResponse = new InventoryListViewResponse(); 
		//	    	BeanUtils.copyProperties(searchItem, inventoryListViewResponse);
		//	    	if(returnValue.size() == 0) {
		//	    		inventoryListViewResponse.setTotalPages(totalPages);
		//	    	}
		//				    	InventorySellPriceListEntity priceList = inventorySellPriceListRepository.findFirstByInventoryIdOrderBySellPriceDesc(searchItem.getInventoryId());
		//				    	if(priceList != null) {
		//					    	inventoryListViewResponse.setSellPrice(priceList.getSellPrice());
		//					    	inventoryListViewResponse.setDiscountAmount(priceList.getDiscountAmount());
		//					    	if(inventoryType == null || inventoryType == "") {
		//					    		returnValue.add(inventoryListViewResponse);
		//					    	}else{
		//					//					    		if(inventoryType.equals(searchItem.getInventoryType())) {
		//					    			returnValue.add(inventoryListViewResponse);
		//					//					    		}
		//					    	}
		//				    	}
		//	    }
	    
		return returnValue;
	}

}
