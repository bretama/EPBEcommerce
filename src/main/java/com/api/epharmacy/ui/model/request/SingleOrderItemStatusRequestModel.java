package com.api.epharmacy.ui.model.request;

import java.time.Instant;

public class SingleOrderItemStatusRequestModel extends OrderItemStatusRequestModel {
	private long orderItemId;

	public long getOrderItemId() {
		return orderItemId;
	}

	public void setOrderItemId(long orderItemId) {
		this.orderItemId = orderItemId;
	}

}
