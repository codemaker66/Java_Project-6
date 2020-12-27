package com.paymybuddy.financialsystem.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

public class UserCredentialsDto {

	@Pattern(regexp = "\\A(?!\\s*\\Z).+", message = "email cannot be empty")
	@Email(message = "email must be a valid email")
	private String email;
	@NotEmpty(message = "password can not be empty")
	private String password;

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

}
