package com.api.epharmacy.ui.model.response;

import java.time.Instant;

public class InventoryPriceListResponse {
	private long inventoryId;
	private long inventoryPriceListId;
	private double quantity;
	private double costOfInventory;
	private double sellPrice;
	private double discountAmount;
	
	private Integer totalPage;
	
	private boolean activeSellPrice = false;
	
	private Instant effectiveDateTime;
	
	
	public long getInventoryId() {
		return inventoryId;
	}
	public void setInventoryId(long inventoryId) {
		this.inventoryId = inventoryId;
	}
	public long getInventoryPriceListId() {
		return inventoryPriceListId;
	}
	public void setInventoryPriceListId(long inventoryPriceListId) {
		this.inventoryPriceListId = inventoryPriceListId;
	}
	public double getQuantity() {
		return quantity;
	}
	public void setQuantity(double quantity) {
		this.quantity = quantity;
	}
	public double getCostOfInventory() {
		return costOfInventory;
	}
	public void setCostOfInventory(double costOfInventory) {
		this.costOfInventory = costOfInventory;
	}
	public double getSellPrice() {
		return sellPrice;
	}
	public void setSellPrice(double sellPrice) {
		this.sellPrice = sellPrice;
	}
	public double getDiscountAmount() {
		return discountAmount;
	}
	public void setDiscountAmount(double discountAmount) {
		this.discountAmount = discountAmount;
	}
	
	public Instant getEffectiveDateTime() {
		return effectiveDateTime;
	}
	
	public void setEffectiveDateTime(Instant effectiveDateTime) {
		this.effectiveDateTime = effectiveDateTime;
	}
	
	public boolean isActiveSellPrice() {
		return activeSellPrice;
	}
	
	public void setActiveSellPrice(boolean activeSellPrice) {
		this.activeSellPrice = activeSellPrice;
	}
	
	public Integer getTotalPage() {
		return totalPage;
	}
	
	public void setTotalPage(Integer totalPage) {
		this.totalPage = totalPage;
	}
	
}
