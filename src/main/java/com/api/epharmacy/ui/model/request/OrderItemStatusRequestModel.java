package com.api.epharmacy.ui.model.request;

import java.time.Instant;

public class OrderItemStatusRequestModel {
		
	private double quantity;
	
	private Integer orderStatusTypeId;
	
	private Instant statusDateTime;

	public Instant getStatusDateTime() {
		return statusDateTime;
	}

	public void setStatusDateTime(Instant statusDateTime) {
		this.statusDateTime = statusDateTime;
	}

	public double getQuantity() {
		return quantity;
	}

	public void setQuantity(double quantity) {
		this.quantity = quantity;
	}

	public Integer getOrderStatusTypeId() {
		return orderStatusTypeId;
	}

	public void setOrderStatusTypeId(Integer orderStatusTypeId) {
		this.orderStatusTypeId = orderStatusTypeId;
	}

}
