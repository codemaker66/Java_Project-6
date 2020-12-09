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

import com.paymybuddy.financialsystem.dto.BankAccountDto;
import com.paymybuddy.financialsystem.exceptions.ResourceException;
import com.paymybuddy.financialsystem.model.BankAccount;
import com.paymybuddy.financialsystem.model.Output;
import com.paymybuddy.financialsystem.service.BankAccountService;

@RestController
public class BankAccountController {

	@Autowired
	private BankAccountService bankAccountService;

	/**
	 * This method call the bankAccountService to find the available balance.
	 * 
	 * @param email represent the email of the user.
	 * @return an object of type BankAccount.
	 */
	@GetMapping(value = "/availableBalance")
	public BankAccount getAvailableBalance(@RequestParam(name = "email") String email) {

		BankAccount bankAccount = bankAccountService.findAvailableBalance(email);

		if (bankAccount != null) {
			return bankAccount;
		} else {
			throw new ResourceException(HttpStatus.BAD_REQUEST, "Your email does not exist in the database");
		}

	}

	/**
	 * This method call the bankAccountService to add money to the user account.
	 * 
	 * @param bankAccountDto represent an object of type BankAccountDto.
	 * @return a ResponseEntity if the request was successful.
	 */
	@PostMapping(value = "/addMoney")
	public ResponseEntity<Output> addMoney(@RequestBody BankAccountDto bankAccountDto) {

		if (bankAccountService.addMoney(bankAccountDto)) {
			Output output = new Output();
			output.setMessage("The request was successfully made");
			List<String> details = new ArrayList<>();
			details.add("You have successfully added money to your account");
			output.setDetails(details);
			return ResponseEntity.status(HttpStatus.CREATED).body(output);
		} else {
			throw new ResourceException(HttpStatus.BAD_REQUEST, "Your email does not exist in the database");
		}

	}

}
