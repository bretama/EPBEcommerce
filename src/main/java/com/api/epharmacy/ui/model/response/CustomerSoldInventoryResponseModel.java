package com.api.epharmacy.ui.model.response;

import java.time.Instant;
import java.util.List;

public class CustomerSoldInventoryResponseModel {
	
	private String sellNumber;	
	private Instant soldDate;
	private String customer;
	private int totalPages;
    private List<CustomerSoldInventoryDetailResponseModel> customerSoldInventoryDetailResponseModels;
	
	public List<CustomerSoldInventoryDetailResponseModel> getCustomerSoldInventoryDetailResponseModels() {
		return customerSoldInventoryDetailResponseModels;
	}
	public void setCustomerSoldInventoryDetailResponseModels(
			List<CustomerSoldInventoryDetailResponseModel> customerSoldInventoryDetailResponseModels) {
		this.customerSoldInventoryDetailResponseModels = customerSoldInventoryDetailResponseModels;
	}
	public String getSellNumber() {
		return sellNumber;
	}
	public void setSellNumber(String sellNumber) {
		this.sellNumber = sellNumber;
	}
	public Instant getSoldDate() {
		return soldDate;
	}
	public void setSoldDate(Instant soldDate) {
		this.soldDate = soldDate;
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
