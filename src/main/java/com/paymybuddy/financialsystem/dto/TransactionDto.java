package com.paymybuddy.financialsystem.dto;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

public class TransactionDto {

	@Pattern(regexp = "\\A(?!\\s*\\Z).+", message = "email cannot be empty")
	@Email(message = "email must be a valid email")
	private String email;
	@NotNull(message = "password can not be empty")
	private String password;
	@Pattern(regexp = "\\A(?!\\s*\\Z).+", message = "friendEmail cannot be empty")
	@Email(message = "friendEmail must be a valid email")
	private String friendEmail;
	@NotNull(message = "description can not be empty")
	private String description;
	@NotNull(message = "amount can not be empty")
	@DecimalMin(value = "0.01", message = "amount minimum value is 0.01")
	private double amount;

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getFriendEmail() {
		return friendEmail;
	}

	public void setFriendEmail(String friendEmail) {
		this.friendEmail = friendEmail;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

}
