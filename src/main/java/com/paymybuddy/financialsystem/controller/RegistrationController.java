package com.paymybuddy.financialsystem.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.paymybuddy.financialsystem.model.User;
import com.paymybuddy.financialsystem.service.RegistrationService;

@RestController
public class RegistrationController {

	@Autowired
	private RegistrationService registrationService;

	/**
	 * This method call the RegistrationService to register the user.
	 * 
	 * @return a ResponseEntity if the request was successful.
	 */
	@PostMapping(value = "/signup")
	public ResponseEntity<String> signUp(@RequestBody User user) {

		registrationService.registerTheUser(user);

		return ResponseEntity.status(HttpStatus.CREATED).body("You were successfully registered to the app");

	}

}
