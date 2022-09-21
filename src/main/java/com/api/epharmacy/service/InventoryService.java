
package com.api.epharmacy.service;

import java.io.IOException;
import java.util.List;

import com.api.epharmacy.io.entity.InventoryEntity;
import com.api.epharmacy.ui.model.request.ImportInventoryDataRequestModel;
import com.api.epharmacy.ui.model.request.ImportInventoryRequestModel;
import com.api.epharmacy.ui.model.request.InventoryCategoryRequestModel;
import com.api.epharmacy.ui.model.request.InventoryRequestModel;
import com.api.epharmacy.ui.model.request.SearchRequestModel;
import com.api.epharmacy.ui.model.response.InventoryCategoryResponse;

public interface InventoryService {

	InventoryRequestModel insertNewInventory(InventoryRequestModel inventoryDetails) throws IOException;

	InventoryRequestModel getInventoryByInventoryId(long inventoryId);

	List<InventoryRequestModel> getInventeryItems(String customerType, int page, int limit);

	InventoryRequestModel updateInventoryItem(long inventoryId, InventoryRequestModel inventoryItem) throws IOException;

	List<InventoryRequestModel> searchInventeryItems(SearchRequestModel searchkeyDetail, int page, int limit);

	List<InventoryCategoryResponse> getInventoryCategories();

	String importInventoryItems(ImportInventoryRequestModel importInventoryDetails) throws IOException;

	List<InventoryRequestModel> getSelectedInventeryItems(Long[] inventoryIds);

	String importPatientData(ImportInventoryDataRequestModel requestDetail) throws IOException;

	String importInventoryCategory(InventoryCategoryRequestModel importInventoryCategoryDetails) throws IOException;	

}
