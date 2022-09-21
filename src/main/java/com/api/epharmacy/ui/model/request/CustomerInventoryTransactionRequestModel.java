package com.api.epharmacy.ui.model.request;
import java.time.Instant;

public class CustomerInventoryTransactionRequestModel {
	
	private long inventoryId;
	private String customerId;
	private double quantity;
	private String transactionType;
    private Integer transactionReasonId;
	private Instant transactionDate;
	
	public Integer getTransactionReasonId() {
		return transactionReasonId;
	}
	public void setTransactionReasonId(Integer transactionReasonId) {
		this.transactionReasonId = transactionReasonId;
	}
	public long getInventoryId() {
		return inventoryId;
	}
	public void setInventoryId(long inventoryId) {
		this.inventoryId = inventoryId;
	}
	public String getCustomerId() {
		return customerId;
	}
	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	public double getQuantity() {
		return quantity;
	}
	public void setQuantity(double quantity) {
		this.quantity = quantity;
	}
	public String getTransactionType() {
		return transactionType;
	}
	public void setTransactionType(String transactionType) {
		this.transactionType = transactionType;
	}


	public Instant getTransactionDate() {
		return transactionDate;
	}
	public void setTransactionDate(Instant transactionDate) {
		this.transactionDate = transactionDate;
	}
	
}
