package com.api.epharmacy.io.entity;

import java.time.Instant;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import org.springframework.data.annotation.CreatedDate;

import com.api.epharmacy.model.audit.Audit;

@Entity(name="customer_purchased_inventory")
public class CustomerPurchasedInventoryEntity extends Audit {

	private static final long serialVersionUID = -5037377443545177244L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long customerPurchasedInventoryId;

	@Column(nullable = false)
	private String customerId;

	@Column(nullable = false)
	private long inventoryId;

	@Column(nullable = false)
	private double quantity;

	@Column(nullable = false)
	private double costPrice;

	@Column(nullable = false)
	private Instant purchasedDate;

	@Column(nullable = false)
	private long companyId;

	public long getCompanyId() {
		return companyId;
	}

	public void setCompanyId(long companyId) {
		this.companyId = companyId;
	}

	public long getCustomerPurchasedInventoryId() {
		return customerPurchasedInventoryId;
	}

	public void setCustomerPurchasedInventoryId(long customerPurchasedInventoryId) {
		this.customerPurchasedInventoryId = customerPurchasedInventoryId;
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

	public double getQuantity() {
		return quantity;
	}

	public void setQuantity(double quantity) {
		this.quantity = quantity;
	}

	public double getCostPrice() {
		return costPrice;
	}

	public void setCostPrice(double costPrice) {
		this.costPrice = costPrice;
	}

	public Instant getPurchasedDate() {
		return purchasedDate;
	}

	public void setPurchasedDate(Instant purchasedDate) {
		this.purchasedDate = purchasedDate;
	}
	
}
