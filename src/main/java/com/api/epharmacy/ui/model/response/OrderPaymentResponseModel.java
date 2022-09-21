package com.api.epharmacy.ui.model.response;

import java.time.Instant;
import java.util.List;

public class OrderPaymentResponseModel {
	
	private long orderPaymentId;
	
	private long orderId;
	
	private double paidAmount;
	
	private Instant paymentDateTime;
	
	private List<OrderItemStatusResponseModel> orderItemStatusInfo;

	public List<OrderItemStatusResponseModel> getOrderItemStatusInfo() {
		return orderItemStatusInfo;
	}

	public void setOrderItemStatusInfo(List<OrderItemStatusResponseModel> orderItemStatusInfo) {
		this.orderItemStatusInfo = orderItemStatusInfo;
	}

	public long getOrderPaymentId() {
		return orderPaymentId;
	}
	
	public void setOrderPaymentId(long orderPaymentId) {
		this.orderPaymentId = orderPaymentId;
	}
	
	public long getOrderId() {
		return orderId;
	}
	
	public void setOrderId(long orderId) {
		this.orderId = orderId;
	}
	
	public double getPaidAmount() {
		return paidAmount;
	}
	
	public void setPaidAmount(double paidAmount) {
		this.paidAmount = paidAmount;
	}
	
	public Instant getPaymentDateTime() {
		return paymentDateTime;
	}
	
	public void setPaymentDateTime(Instant paymentDateTime) {
		this.paymentDateTime = paymentDateTime;
	}
	
}
