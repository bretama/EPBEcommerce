package com.api.epharmacy.ui.model.response;

import java.time.Instant;


public class CustomerInventoryTransactionResponseModel {
	private long customerPurchasedInventoryId;
	private long inventoryId;
	private String inventoryName;
	private String customerId;
	private String customer;
	private double quantity;
	private String transactionType;
    private Integer transactionReasonId;
    private String transactionReason;
	private Instant transactionDate;
	private long companyId;
	private long totalPages;

	public long getCompanyId() {
		return companyId;
	}
	public void setCompanyId(long companyId) {
		this.companyId = companyId;
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
	public long getCustomerPurchasedInventoryId() {
		return customerPurchasedInventoryId;
	}
	public void setCustomerPurchasedInventoryId(long customerPurchasedInventoryId) {
		this.customerPurchasedInventoryId = customerPurchasedInventoryId;
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

	public Integer getTransactionReasonId() {
		return transactionReasonId;
	}
	public void setTransactionReasonId(Integer transactionReasonId) {
		this.transactionReasonId = transactionReasonId;
	}
	public String getTransactionReason() {
		return transactionReason;
	}
	public void setTransactionReason(String transactionReason) {
		this.transactionReason = transactionReason;
	}
	public Instant getTransactionDate() {
		return transactionDate;
	}
	public void setTransactionDate(Instant transactionDate) {
		this.transactionDate = transactionDate;
	}
	public long getTotalPages() {
		return totalPages;
	}
	public void setTotalPages(long totalPages) {
		this.totalPages = totalPages;
	}
}
