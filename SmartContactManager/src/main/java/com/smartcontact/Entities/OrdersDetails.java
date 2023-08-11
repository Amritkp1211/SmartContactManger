package com.smartcontact.Entities;

import java.sql.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "Orders_Details")
public class OrdersDetails {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long MyOrderId;
	
	private String orderId;

	private String amount;
	
	private String receipt;
	
	private String status;
	
   private java.util.Date date;


	public java.util.Date getDate() {
	return date;
}

public void setDate(java.util.Date date) {
	this.date = date;
}

	@ManyToOne
	private User user;

    private String paymentId;

	public long getMyOrderId() {
		return MyOrderId;
	}

	public void setMyOrderId(long myOrderId) {
		MyOrderId = myOrderId;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getReceipt() {
		return receipt;
	}

	public void setReceipt(String receipt) {
		this.receipt = receipt;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getPaymentId() {
		return paymentId;
	}

	public void setPaymentId(String paymentId) {
		this.paymentId = paymentId;
	}
}
