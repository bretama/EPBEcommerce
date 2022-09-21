package com.api.epharmacy.ui.model.response;

public class ManufacturerReportResponseModel {
	
	private Long inventoryId;
	private String inventoryGenericName;
	
	private double manufacturedQuantity;
	
	private double soldQuantity;
	
	private double availableQuantity;
	
	public Long getInventoryId() {
		return inventoryId;
	}
	
	public void setInventoryId(Long inventoryId) {
		this.inventoryId = inventoryId;
	}
	
	public String getInventoryGenericName() {
		return inventoryGenericName;
	}
	
	public void setInventoryGenericName(String inventoryGenericName) {
		this.inventoryGenericName = inventoryGenericName;
	}

	
	public double getManufacturedQuantity() {
		return manufacturedQuantity;
	}

	
	public void setManufacturedQuantity(double manufacturedQuantity) {
		this.manufacturedQuantity = manufacturedQuantity;
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
