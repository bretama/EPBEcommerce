package com.api.epharmacy.ui.model.response;

import java.util.List;

public class InventorySellPriceDetail {
	
	InventoryListViewResponse invenoryDetail;
	
	List<InventoryPriceListResponse> inventoryPriceList;
	
	public InventoryListViewResponse getInvenoryDetail() {
		return invenoryDetail;
	}
	
	public void setInvenoryDetail(InventoryListViewResponse invenoryDetail) {
		this.invenoryDetail = invenoryDetail;
	}
	
	public List<InventoryPriceListResponse> getInventoryPriceList() {
		return inventoryPriceList;
	}
	
	public void setInventoryPriceList(List<InventoryPriceListResponse> inventoryPriceList) {
		this.inventoryPriceList = inventoryPriceList;
	}
	
}
