package com.paymybuddy.financialsystem.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class Output {

	private String message;
	private List<String> details;
	private List<User> friends;
	private List<Transaction> transactionsHistory;

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

	public List<Transaction> getTransactionsHistory() {
		return transactionsHistory;
	}

	public void setTransactionsHistory(List<Transaction> transactionsHistory) {
		this.transactionsHistory = transactionsHistory;
	}

}
