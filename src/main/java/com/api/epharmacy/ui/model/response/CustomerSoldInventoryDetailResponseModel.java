package com.api.epharmacy.ui.model.response;

public class CustomerSoldInventoryDetailResponseModel {
	
	private long customerSoldInventoryDetailId;
	private long inventoryId;
	private String inventoryName;
	private double quantity;
	private double sellPrice;
	
	public long getCustomerSoldInventoryDetailId() {
		return customerSoldInventoryDetailId;
	}
	public void setCustomerSoldInventoryDetailId(long customerSoldInventoryDetailId) {
		this.customerSoldInventoryDetailId = customerSoldInventoryDetailId;
	}
	public long getInventoryId() {
		return inventoryId;
	}
	public void setInventoryId(long inventoryId) {
		this.inventoryId = inventoryId;
	}
	public String getInventoryName() {
		return inventoryName;
	}
	public void setInventoryName(String inventoryName) {
		this.inventoryName = inventoryName;
	}
	public double getQuantity() {
		return quantity;
	}
	public void setQuantity(double quantity) {
		this.quantity = quantity;
	}
	public double getSellPrice() {
		return sellPrice;
	}
	public void setSellPrice(double sellPrice) {
		this.sellPrice = sellPrice;
	}

}
