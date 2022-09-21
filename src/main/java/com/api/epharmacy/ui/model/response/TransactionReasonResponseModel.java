package com.api.epharmacy.ui.model.response;

public class TransactionReasonResponseModel {
	private Integer transactionReasonId;
	private String transactionReason;
    private String reasonType;
	private String customerId;
	private int totalPages;
	
	public Integer getTransactionReasonId() {
		return transactionReasonId;
	}
	public void setTransactionReasonId(Integer transactionReasonId) {
		this.transactionReasonId = transactionReasonId;
	}
	public String getTransactionReason() {
		return transactionReason;
	}
	public void setTransactionReason(String transactionReason) {
		this.transactionReason = transactionReason;
	}
	public String getReasonType() {
		return reasonType;
	}
	public void setReasonType(String reasonType) {
		this.reasonType = reasonType;
	}
	public String getCustomerId() {
		return customerId;
	}
	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}
	public int getTotalPages() {
		return totalPages;
	}
	public void setTotalPages(int totalPages) {
		this.totalPages = totalPages;
	}
}
