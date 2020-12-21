package com.paymybuddy.financialsystem.integrationTests.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.paymybuddy.financialsystem.repository.BankAccountRepository;
import com.paymybuddy.financialsystem.repository.FriendRepository;
import com.paymybuddy.financialsystem.repository.TransactionRepository;
import com.paymybuddy.financialsystem.repository.UserRepository;

@Service
public class TruncateService {
	
	@Autowired
	private UserRepository userRepository;

	@Autowired
	private BankAccountRepository bankAccountRepository;
	
	@Autowired
	private FriendRepository friendRepository;

	@Autowired
	private TransactionRepository transactionRepository;
	
	
	public void truncateAll() {
		bankAccountRepository.truncateBankAccount();
		friendRepository.truncateFriends();
		transactionRepository.truncateTransactions();
		userRepository.disableForeignKeyChecks();
		userRepository.truncateUsers();
		userRepository.enableForeignKeyChecks();
	}

}
