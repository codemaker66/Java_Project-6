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

import com.paymybuddy.financialsystem.dto.BankAccountDto;
import com.paymybuddy.financialsystem.dto.UserCredentialsDto;
import com.paymybuddy.financialsystem.entity.BankAccount;
import com.paymybuddy.financialsystem.entity.User;
import com.paymybuddy.financialsystem.exceptions.PropertiesException;
import com.paymybuddy.financialsystem.exceptions.ResourceException;
import com.paymybuddy.financialsystem.model.Output;
import com.paymybuddy.financialsystem.service.BankAccountService;
import com.paymybuddy.financialsystem.service.CheckService;

@RestController
public class BankAccountController {

	@Autowired
	private BankAccountService bankAccountService;
	@Autowired
	private CheckService checkService;

	/**
	 * This method call the bankAccountService to find the available balance.
	 * 
	 * @param userCredentialsDto is an object of type UserCredentialsDto.
	 * @param bindingResult is a general interface that represents binding results.
	 * @return an object of type BankAccount.
	 */
	@PostMapping(value = "/availableBalance")
	public BankAccount getAvailableBalance(@Valid @RequestBody UserCredentialsDto userCredentialsDto, BindingResult bindingResult) {

		if (bindingResult.hasErrors()) {
			List<String> details = new ArrayList<>();
			for (FieldError fieldError : bindingResult.getFieldErrors()) {
				details.add(fieldError.getDefaultMessage());
			}
			throw new PropertiesException(HttpStatus.BAD_REQUEST, "Validation failed", details);
		}

		User user = checkService.checkUserCredentials(userCredentialsDto.getEmail(), userCredentialsDto.getPassword());

		if (user == null) {
			throw new ResourceException(HttpStatus.BAD_REQUEST, "Invalid email or password");
		}

		return bankAccountService.findAvailableBalance(user);

	}

	/**
	 * This method call the bankAccountService to add the money to the user bank account.
	 * 
	 * @param bankAccountDto is an object of type BankAccountDto.
	 * @param bindingResult is a general interface that represents binding results.
	 * @return a ResponseEntity if the request was successful.
	 */
	@PostMapping(value = "/addMoney")
	public ResponseEntity<Output> addMoney(@Valid @RequestBody BankAccountDto bankAccountDto, BindingResult bindingResult) {

		if (bindingResult.hasErrors()) {
			List<String> details = new ArrayList<>();
			for (FieldError fieldError : bindingResult.getFieldErrors()) {
				details.add(fieldError.getDefaultMessage());
			}
			throw new PropertiesException(HttpStatus.BAD_REQUEST, "Validation failed", details);
		}

		User user = checkService.checkUserCredentials(bankAccountDto.getEmail(), bankAccountDto.getPassword());

		if (user == null) {
			throw new ResourceException(HttpStatus.BAD_REQUEST, "Invalid email or password");
		}

		bankAccountService.addMoney(user, bankAccountDto);
		Output output = new Output();
		output.setStatus(HttpStatus.OK);
		output.setMessage("You have successfully added money to your account");
		return ResponseEntity.status(HttpStatus.OK).body(output);
	}

	/**
	 * This method call the bankAccountService to get the money from the user bank account.
	 * 
	 * @param bankAccountDto is an object of type BankAccountDto.
	 * @param bindingResult is a general interface that represents binding results.
	 * @return a ResponseEntity if the request was successful.
	 */
	@PostMapping(value = "/getMoney")
	public ResponseEntity<Output> getMoney(@Valid @RequestBody BankAccountDto bankAccountDto, BindingResult bindingResult) {

		if (bindingResult.hasErrors()) {
			List<String> details = new ArrayList<>();
			for (FieldError fieldError : bindingResult.getFieldErrors()) {
				details.add(fieldError.getDefaultMessage());
			}
			throw new PropertiesException(HttpStatus.BAD_REQUEST, "Validation failed", details);
		}

		User user = checkService.checkUserCredentials(bankAccountDto.getEmail(), bankAccountDto.getPassword());

		if (user == null) {
			throw new ResourceException(HttpStatus.BAD_REQUEST, "Invalid email or password");
		}

		if (bankAccountService.getMoney(user, bankAccountDto)) {
			Output output = new Output();
			output.setStatus(HttpStatus.OK);
			output.setMessage("You have successfully retrieved " + bankAccountDto.getAmount() + " euro from your account");
			return ResponseEntity.status(HttpStatus.OK).body(output);
		} else {
			throw new ResourceException(HttpStatus.BAD_REQUEST, "You don't have this amount of money in your bank account");
		}
	}

}
