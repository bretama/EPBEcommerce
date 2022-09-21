
package com.api.epharmacy.ui.model.response;

import java.time.Instant;
import java.util.List;

import com.api.epharmacy.ui.model.request.OrderItemRequestModel;

public class CustomerPurchasedInventoryResponseModel {
		
	private String customerId;
	private String customerName;
	private long inventoryId;
	private String inventoryName;
	private long quantity;
	private long costPrice;
	private Instant purchasedDate;
	private long companyId;
	private int totalPages;

	public long getCompanyId() {
		return companyId;
	}

	public void setCompanyId(long companyId) {
		this.companyId = companyId;
	}

	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	public long getInventoryId() {
		return inventoryId;
	}

	public void setInventoryId(long inventoryId) {
		this.inventoryId = inventoryId;
	}

	public long getQuantity() {
		return quantity;
	}

	public void setQuantity(long quantity) {
		this.quantity = quantity;
	}

	public long getCostPrice() {
		return costPrice;
	}

	public void setCostPrice(long costPrice) {
		this.costPrice = costPrice;
	}

	public Instant getPurchasedDate() {
		return purchasedDate;
	}

	public void setPurchasedDate(Instant purchasedDate) {
		this.purchasedDate = purchasedDate;
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

	public int getTotalPages() {
		return totalPages;
	}

	public void setTotalPages(int totalPages) {
		this.totalPages = totalPages;
	}
}
