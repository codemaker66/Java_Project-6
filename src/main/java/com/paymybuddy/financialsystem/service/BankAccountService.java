package com.paymybuddy.financialsystem.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.paymybuddy.financialsystem.dto.BankAccountDto;
import com.paymybuddy.financialsystem.model.BankAccount;
import com.paymybuddy.financialsystem.model.User;
import com.paymybuddy.financialsystem.repository.BankAccountRepository;
import com.paymybuddy.financialsystem.repository.UserRepository;

@Service
public class BankAccountService {

	@Autowired
	private BankAccountRepository bankAccountRepository;
	@Autowired
	private UserRepository userRepository;

	/**
	 * This method call the bankAccountRepository to retrieve the available balance from the user bank account.
	 * 
	 * @param email represent the email of the user.
	 * @return an object of type BankAccount.
	 */
	public BankAccount findAvailableBalance(String email) {

		if (userRepository.retrieveUserByEmail(email) != null) {

			User user = userRepository.retrieveUserByEmail(email);

			BankAccount bankAccount = bankAccountRepository.retrieveAvailableBalance(user.getId());

			return bankAccount;

		} else {
			return null;
		}

	}

	/**
	 * This method call the bankAccountRepository to add money to the user bank account.
	 * 
	 * @param bankAccountDto represent an object of type BankAccountDto.
	 * @return true if the process was successful.
	 */
	public boolean addMoney(BankAccountDto bankAccountDto) {
		
		User user = userRepository.retrieveUserByEmail(bankAccountDto.getUserEmail());

		if (user != null) {
			BankAccount newBankAccount = bankAccountRepository.retrieveAvailableBalance(user.getId());

			bankAccountRepository.addMoney(user.getId(), newBankAccount.getAvailableBalance() + bankAccountDto.getAmount());
			return true;
		} else {
			return false;
		}

	}

}
