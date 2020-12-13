package com.paymybuddy.financialsystem.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.paymybuddy.financialsystem.dto.BankAccountDto;
import com.paymybuddy.financialsystem.entity.BankAccount;
import com.paymybuddy.financialsystem.entity.User;
import com.paymybuddy.financialsystem.repository.BankAccountRepository;

@Service
public class BankAccountService {

	@Autowired
	private BankAccountRepository bankAccountRepository;

	/**
	 * This method call the bankAccountRepository to retrieve the available balance from the user bank account.
	 * 
	 * @param user is an object of type User.
	 * @return an object of type BankAccount.
	 */
	public BankAccount findAvailableBalance(User user) {

		return bankAccountRepository.retrieveAvailableBalance(user.getId());

	}

	/**
	 * This method call the bankAccountRepository to add money to the user bank account.
	 * 
	 * @param user is an object of type User.
	 * @param bankAccountDto is an object of type BankAccountDto.
	 */
	public void addMoney(User user, BankAccountDto bankAccountDto) {

		BankAccount bankAccount = bankAccountRepository.retrieveAvailableBalance(user.getId());

		bankAccountRepository.updateTheBankAccountBalance(user.getId(), bankAccount.getAvailableBalance() + bankAccountDto.getAmount());

	}

	/**
	 * This method call the bankAccountRepository to retrieve an amount of money from the user bank account.
	 * 
	 * @param user is an object of type User.
	 * @param bankAccountDto is an object of type BankAccountDto.
	 * @return true if the process was successful.
	 */
	public boolean getMoney(User user, BankAccountDto bankAccountDto) {

		BankAccount bankAccount = bankAccountRepository.retrieveAvailableBalance(user.getId());

		if (bankAccount.getAvailableBalance() >= bankAccountDto.getAmount()) {
			bankAccountRepository.updateTheBankAccountBalance(user.getId(), bankAccount.getAvailableBalance() - bankAccountDto.getAmount());
			return true;
		} else {
			return false;
		}

	}

}
