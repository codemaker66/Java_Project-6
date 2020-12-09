package com.paymybuddy.financialsystem.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.paymybuddy.financialsystem.dto.TransactionDto;
import com.paymybuddy.financialsystem.exceptions.ResourceException;
import com.paymybuddy.financialsystem.model.Output;
import com.paymybuddy.financialsystem.service.TransactionService;

@RestController
public class TransactionController {

	@Autowired
	private TransactionService transactionService;

	/**
	 * This method call the transactionService to make a transaction.
	 * 
	 * @param transactionDto represent an object of type TransactionDto.
	 * @return a ResponseEntity if the request was successful.
	 */
	@PostMapping(value = "/transaction")
	public ResponseEntity<Output> makeATransaction(@RequestBody TransactionDto transactionDto) {

		if (transactionService.checkUserExistence(transactionDto.getUserEmail()) == null) {
			throw new ResourceException(HttpStatus.BAD_REQUEST, "Your email does not exist in the database");
		}

		if (transactionService.checkUserExistence(transactionDto.getFriendEmail()) == null) {
			throw new ResourceException(HttpStatus.BAD_REQUEST, "The friend email you have entered does not exist in the database");
		}

		if (transactionService.makeATransaction(transactionDto)) {
			Output output = new Output();
			output.setMessage("The request was successfully made");
			List<String> details = new ArrayList<>();
			details.add("Your transaction was successfully made");
			output.setDetails(details);
			return ResponseEntity.status(HttpStatus.CREATED).body(output);
		} else {
			throw new ResourceException(HttpStatus.BAD_REQUEST, "You don't have the necessary amount of money for this transaction");
		}

	}

	/**
	 * This method call the transactionService to get the transactions history of a user.
	 * 
	 * @param email represent the email of a user.
	 * @return an object of type Output.
	 */
	@GetMapping(value = "/transactionHistory")
	public Output getTransactionHistory(@RequestParam(name = "email") String email) {

		if (transactionService.checkUserExistence(email) == null) {
			throw new ResourceException(HttpStatus.BAD_REQUEST, "This email does not exist in the database");
		}

		Output output = transactionService.findTransactionsHistory(email);

		if (output.getTransactionsHistory() != null && !output.getTransactionsHistory().isEmpty()) {
			return output;
		} else {
			throw new ResourceException(HttpStatus.BAD_REQUEST, "You didn't make any transactions in your account");
		}

	}

}
