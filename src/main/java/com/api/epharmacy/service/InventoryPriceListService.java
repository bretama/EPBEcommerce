package com.api.epharmacy.service;

import com.api.epharmacy.ui.model.response.InventorySellPriceDetail;

public interface InventoryPriceListService {

	InventorySellPriceDetail getInventoryItemPriceList(long inventoryId, Integer page, Integer limit);

}
