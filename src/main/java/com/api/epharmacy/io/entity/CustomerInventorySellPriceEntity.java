package com.api.epharmacy.io.entity;

import java.io.Serializable;
import java.time.Instant;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.api.epharmacy.model.audit.Audit;

@Entity(name="customer_inventory_sell_price")
public class CustomerInventorySellPriceEntity extends Audit implements Serializable{

	private static final long serialVersionUID = 7018940012827577325L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long customerInventorySellPriceId;

	@Column(nullable = false, length = 50)
	private String customerId;
	
	@Column(nullable = false, length = 50)
	private long inventoryId;
	
	@Column(nullable = false, length = 50)
	private double sellPrice;	
	
	@Column(nullable = false, length = 50)
	private Instant effectiveDateTime;
	
	@Column(nullable = false)
	private long companyId;

	public long getCompanyId() {
		return companyId;
	}

	public void setCompanyId(long companyId) {
		this.companyId = companyId;
	}

	public long getCustomerInventorySellPriceId() {
		return customerInventorySellPriceId;
	}

	public void setCustomerInventorySellPriceId(long customerInventorySellPriceId) {
		this.customerInventorySellPriceId = customerInventorySellPriceId;
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

	public double getSellPrice() {
		return sellPrice;
	}

	public void setSellPrice(double sellPrice) {
		this.sellPrice = sellPrice;
	}

	public Instant getEffectiveDateTime() {
		return effectiveDateTime;
	}

	public void setEffectiveDateTime(Instant effectiveDateTime) {
		this.effectiveDateTime = effectiveDateTime;
	}

}
