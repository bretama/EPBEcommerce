package com.api.epharmacy.io.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.api.epharmacy.model.audit.Audit;

@Entity(name="transaction_reason")
public class TransactionReasonEntity extends Audit implements Serializable {

	private static final long serialVersionUID = 1016757595910320635L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer transactionReasonId;
	
	@Column
	private String transactionReason;
	
	@Column
	private String reasonType;
	
	@Column
	private String customerId;
	
	@Column
	private boolean isDeleted=false;

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

	public boolean isDeleted() {
		return isDeleted;
	}

	public void setDeleted(boolean isDeleted) {
		this.isDeleted = isDeleted;
	}
}
