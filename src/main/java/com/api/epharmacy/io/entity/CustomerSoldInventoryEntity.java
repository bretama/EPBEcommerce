package com.api.epharmacy.io.entity;

import java.io.Serializable;
import java.time.Instant;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.api.epharmacy.model.audit.Audit;

@Entity(name="customer_sold_inventory")
public class CustomerSoldInventoryEntity extends Audit implements Serializable{

	private static final long serialVersionUID = -2636989465460228775L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long customerSoldInventoryId;

	@Column(nullable = false, length = 50)
	private String customerId;
	
	@Column(nullable = false, length = 50)
	private String sellNumber;	
	
	@Column(nullable = false, length = 50)
	private Instant soldDate;
	
	@Column(nullable = false)
	private long companyId;

	public long getCompanyId() {
		return companyId;
	}

	public void setCompanyId(long companyId) {
		this.companyId = companyId;
	}

	public long getCustomerSoldInventoryId() {
		return customerSoldInventoryId;
	}

	public void setCustomerSoldInventoryId(long customerSoldInventoryId) {
		this.customerSoldInventoryId = customerSoldInventoryId;
	}

	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
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
	
}
