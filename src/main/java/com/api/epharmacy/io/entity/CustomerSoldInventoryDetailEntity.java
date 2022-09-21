package com.api.epharmacy.io.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.api.epharmacy.model.audit.Audit;

@Entity(name="customer_sold_inventory_detail")
public class CustomerSoldInventoryDetailEntity extends Audit implements Serializable {

	private static final long serialVersionUID = -2914132881260721980L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long customerSoldInventoryDetailId;
	
	@Column
	private long customerSoldInventoryId;

	@Column(nullable = false, length = 50)
	private long inventoryId;
	
	@Column(nullable = false, length = 50)
	private double quantity;
	
	@Column(nullable = false, length = 50)
	private double sellPrice;

	public long getCustomerSoldInventoryDetailId() {
		return customerSoldInventoryDetailId;
	}

	public void setCustomerSoldInventoryDetailId(long customerSoldInventoryDetailId) {
		this.customerSoldInventoryDetailId = customerSoldInventoryDetailId;
	}

	public long getCustomerSoldInventoryId() {
		return customerSoldInventoryId;
	}

	public void setCustomerSoldInventoryId(long customerSoldInventoryId) {
		this.customerSoldInventoryId = customerSoldInventoryId;
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

	public double getSellPrice() {
		return sellPrice;
	}

	public void setSellPrice(double sellPrice) {
		this.sellPrice = sellPrice;
	}
}
