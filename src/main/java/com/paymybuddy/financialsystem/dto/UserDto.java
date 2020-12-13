package com.paymybuddy.financialsystem.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;

public class UserDto {

	@Length(min = 1, message = "firstName must have at least one character")
	private String firstName;
	@Length(min = 1, message = "lastName must have at least one character")
	private String lastName;
	@Pattern(regexp = "\\A(?!\\s*\\Z).+", message = "email cannot be empty")
	@Email(message = "email must be a valid email")
	private String email;
	@Length(min = 8, message = "password must have at least eight characters")
	private String password;

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

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
