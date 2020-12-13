package com.paymybuddy.financialsystem.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.paymybuddy.financialsystem.entity.Transaction;
import com.paymybuddy.financialsystem.entity.User;

@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class Output {

	private String status;
	private String message;
	private List<String> details;
	private List<User> friends;
	private List<Transaction> transactionHistory;

	public String getStatus() {
		return status;
	}

	public void setStatus(String string) {
		this.status = string;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public List<String> getDetails() {
		return details;
	}

	public void setDetails(List<String> details) {
		this.details = details;
	}

	public List<User> getFriends() {
		return friends;
	}

	public void setFriends(List<User> friends) {
		this.friends = friends;
	}

	public List<Transaction> getTransactionHistory() {
		return transactionHistory;
	}

	public void setTransactionHistory(List<Transaction> transactionHistory) {
		this.transactionHistory = transactionHistory;
	}

}
