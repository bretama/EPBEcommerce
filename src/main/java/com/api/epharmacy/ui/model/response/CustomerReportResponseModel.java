package com.api.epharmacy.ui.model.response;

public class CustomerReportResponseModel {
	
	private Long inventoryId;
	
	private String customerId;
	
	private String customerName;
	
	private String inventoryName;
	
	private double purchasedQuantity;
	
	private double soldQuantity;
	
	private double availableQuantity;
	
	public Long getInventoryId() {
		return inventoryId;
	}
	
	public void setInventoryId(Long inventoryId) {
		this.inventoryId = inventoryId;
	}
	
	public String getCustomerId() {
		return customerId;
	}
	
	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}
	
	public String getCustomerName() {
		return customerName;
	}
	
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	
	public String getInventoryName() {
		return inventoryName;
	}
	
	public void setInventoryName(String inventoryName) {
		this.inventoryName = inventoryName;
	}
	
	public double getPurchasedQuantity() {
		return purchasedQuantity;
	}
	
	public void setPurchasedQuantity(double purchasedQuantity) {
		this.purchasedQuantity = purchasedQuantity;
	}
	
	public double getSoldQuantity() {
		return soldQuantity;
	}
	
	public void setSoldQuantity(double soldQuantity) {
		this.soldQuantity = soldQuantity;
	}
	
	public double getAvailableQuantity() {
		return availableQuantity;
	}
	
	public void setAvailableQuantity(double availableQuantity) {
		this.availableQuantity = availableQuantity;
	}
	
}
