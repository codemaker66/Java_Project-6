package com.paymybuddy.financialsystem.controller;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.paymybuddy.financialsystem.dto.UserDto;
import com.paymybuddy.financialsystem.exceptions.PropertiesException;
import com.paymybuddy.financialsystem.exceptions.ResourceException;
import com.paymybuddy.financialsystem.model.Output;
import com.paymybuddy.financialsystem.service.RegistrationService;

@RestController
public class RegistrationController {

	@Autowired
	private RegistrationService registrationService;

	/**
	 * This method call the registrationService to register the user.
	 * 
	 * @return a ResponseEntity if the request was successful.
	 */
	@PostMapping(value = "/signup")
	public ResponseEntity<Output> signUp(@Valid @RequestBody UserDto userDto, BindingResult bindingResult) {

		if (bindingResult.hasErrors()) {
			List<String> details = new ArrayList<>();
			for (FieldError fieldError : bindingResult.getFieldErrors()) {
				details.add(fieldError.getDefaultMessage());
			}
			throw new PropertiesException(HttpStatus.BAD_REQUEST, "validation failed", details);
		}

		if (registrationService.registerTheUser(userDto)) {
			Output output = new Output();
			output.setMessage("The request was successfully made");
			List<String> details = new ArrayList<>();
			details.add("You were successfully registered to the app");
			output.setDetails(details);
			return ResponseEntity.status(HttpStatus.CREATED).body(output);
		} else {
			throw new ResourceException(HttpStatus.BAD_REQUEST, "A person with the same email already exist in the database");
		}

	}

}
