package com.api.epharmacy.ui.model.request;

import java.time.Instant;
import java.util.List;

public class CustomerSoldInventoryRequestModel {
	
	private Instant soldDate;
	private String customerId;
	private List<CustomerSoldInventoryDetailRequestModel> customerSoldInventoryDetailRequestModels;

	public List<CustomerSoldInventoryDetailRequestModel> getCustomerSoldInventoryDetailRequestModels() {
		return customerSoldInventoryDetailRequestModels;
	}
	public void setCustomerSoldInventoryDetailRequestModels(
			List<CustomerSoldInventoryDetailRequestModel> customerSoldInventoryDetailRequestModels) {
		this.customerSoldInventoryDetailRequestModels = customerSoldInventoryDetailRequestModels;
	}
	public Instant getSoldDate() {
		return soldDate;
	}
	public void setSoldDate(Instant soldDate) {
		this.soldDate = soldDate;
	}
	public String getCustomerId() {
		return customerId;
	}
	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

}
