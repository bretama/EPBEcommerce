package com.api.epharmacy.ui.model.response;

import java.time.Instant;

public class CustomerInventorySellPriceResponseModel {
	
	private long customerPurchasedInventoryId;
	private double sellPrice;	
	private Instant effectiveDateTime;
	private long inventoryId;
	private double quantity;
	private String inventoryName;
	private String customer;
	private boolean activeSellPrice = false;
	private long companyId;
	private int totalPages;

	public long getCompanyId() {
		return companyId;
	}
	public void setCompanyId(long companyId) {
		this.companyId = companyId;
	}
	public boolean isActiveSellPrice() {
		return activeSellPrice;
	}
	public void setActiveSellPrice(boolean activeSellPrice) {
		this.activeSellPrice = activeSellPrice;
	}
	public long getCustomerPurchasedInventoryId() {
		return customerPurchasedInventoryId;
	}
	public void setCustomerPurchasedInventoryId(long customerPurchasedInventoryId) {
		this.customerPurchasedInventoryId = customerPurchasedInventoryId;
	}

	public Instant getEffectiveDateTime() {
		return effectiveDateTime;
	}
	public void setEffectiveDateTime(Instant effectiveDateTime) {
		this.effectiveDateTime = effectiveDateTime;
	}
	public long getInventoryId() {
		return inventoryId;
	}
	public void setInventoryId(long inventoryId) {
		this.inventoryId = inventoryId;
	}

	public double getSellPrice() {
		return sellPrice;
	}
	public void setSellPrice(double sellPrice) {
		this.sellPrice = sellPrice;
	}
	public double getQuantity() {
		return quantity;
	}
	public void setQuantity(double quantity) {
		this.quantity = quantity;
	}
	public String getInventoryName() {
		return inventoryName;
	}
	public void setInventoryName(String inventoryName) {
		this.inventoryName = inventoryName;
	}
	public String getCustomer() {
		return customer;
	}
	public void setCustomer(String customer) {
		this.customer = customer;
	}
	public int getTotalPages() {
		return totalPages;
	}
	public void setTotalPages(int totalPages) {
		this.totalPages = totalPages;
	}
}
