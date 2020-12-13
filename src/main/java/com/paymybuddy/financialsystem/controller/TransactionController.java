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

import com.paymybuddy.financialsystem.dto.TransactionDto;
import com.paymybuddy.financialsystem.dto.UserCredentialsDto;
import com.paymybuddy.financialsystem.entity.User;
import com.paymybuddy.financialsystem.exceptions.PropertiesException;
import com.paymybuddy.financialsystem.exceptions.ResourceException;
import com.paymybuddy.financialsystem.model.Output;
import com.paymybuddy.financialsystem.service.CheckService;
import com.paymybuddy.financialsystem.service.TransactionService;

@RestController
public class TransactionController {

	@Autowired
	private TransactionService transactionService;
	@Autowired
	private CheckService checkService;

	/**
	 * This method call the transactionService to make a transaction.
	 * 
	 * @param transactionDto is an object of type TransactionDto.
	 * @param bindingResult is a general interface that represents binding results.
	 * @return a ResponseEntity if the request was successful.
	 */
	@PostMapping(value = "/transaction")
	public ResponseEntity<Output> makeATransaction(@Valid @RequestBody TransactionDto transactionDto, BindingResult bindingResult) {

		if (bindingResult.hasErrors()) {
			List<String> details = new ArrayList<>();
			for (FieldError fieldError : bindingResult.getFieldErrors()) {
				details.add(fieldError.getDefaultMessage());
			}
			throw new PropertiesException(HttpStatus.BAD_REQUEST, "Validation failed", details);
		}

		User user = checkService.checkUserCredentials(transactionDto.getEmail(), transactionDto.getPassword());

		User friend = checkService.checkFriendExistence(transactionDto.getFriendEmail());

		if (user == null) {
			throw new ResourceException(HttpStatus.BAD_REQUEST, "Invalid email or password");
		}

		if (friend == null) {
			throw new ResourceException(HttpStatus.BAD_REQUEST, "The friend email you have entered does not exist in the database");
		}

		if (transactionService.makeATransaction(user, friend, transactionDto)) {
			Output output = new Output();
			output.setStatus(HttpStatus.CREATED + "");
			output.setMessage("Your transaction was successfully made");
			return ResponseEntity.status(HttpStatus.CREATED).body(output);
		} else {
			throw new ResourceException(HttpStatus.BAD_REQUEST, "You don't have the necessary amount of money for this transaction");
		}

	}

	/**
	 * This method call the transactionService to find the user transactions history.
	 * 
	 * @param userCredentialsDto is an object of type UserCredentialsDto.
	 * @param bindingResult is a general interface that represents binding results.
	 * @return an object of type Output.
	 */
	@PostMapping(value = "/transactionHistory")
	public Output getTransactionHistory(@Valid @RequestBody UserCredentialsDto userCredentialsDto, BindingResult bindingResult) {

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

		Output output = transactionService.findTransactionsHistory(user);

		if (output.getTransactionHistory() != null && !output.getTransactionHistory().isEmpty()) {
			return output;
		} else {
			throw new ResourceException(HttpStatus.BAD_REQUEST, "You didn't make any transactions with your account");
		}

	}

}
