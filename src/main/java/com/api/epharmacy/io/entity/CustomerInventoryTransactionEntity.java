package com.api.epharmacy.io.entity;

import java.io.Serializable;
import java.time.Instant;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.api.epharmacy.model.audit.Audit;

@Entity(name = "customer_inventory_transaction")

public class CustomerInventoryTransactionEntity extends Audit implements Serializable {
	
	private static final long serialVersionUID = 1733786199147459353L;
	
	@Id
	
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	
	private long customerInventoryTransactionId;
	
	@Column(nullable = false)
	
	private long inventoryId;
	
	@Column(nullable = false)
	private double quantity;
	
	@Column(nullable = false)
	
	private String transactionType;
	
	@Column(nullable = false)
	
	private String customerId;
	
	@Column(nullable = false)
	private long companyId;
	
	@Column
	
	private Integer transactionReasonId;
	
	@Column
	
	private Instant transactionDate;
	
	public long getCompanyId() {
		return companyId;
	}

	public void setCompanyId(long companyId) {
		this.companyId = companyId;
	}

	public Integer getTransactionReasonId() {
		
		return transactionReasonId;
		
	}
	
	public void setTransactionReasonId(Integer transactionReasonId) {
		
		this.transactionReasonId = transactionReasonId;
		
	}
	
	public String getCustomerId() {
		
		return customerId;
		
	}
	
	public void setCustomerId(String customerId) {
		
		this.customerId = customerId;
		
	}
	
	public long getCustomerInventoryTransactionId() {
		
		return customerInventoryTransactionId;
		
	}
	
	public void setCustomerInventoryTransactionId(long customerInventoryTransactionId) {
		
		this.customerInventoryTransactionId = customerInventoryTransactionId;
		
	}
	
	public long getInventoryId() {
		
		return inventoryId;
		
	}
	
	public void setInventoryId(long inventoryId) {
		
		this.inventoryId = inventoryId;
		
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
