package com.paymybuddy.financialsystem.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.paymybuddy.financialsystem.dto.TransactionDto;
import com.paymybuddy.financialsystem.entity.BankAccount;
import com.paymybuddy.financialsystem.entity.Transaction;
import com.paymybuddy.financialsystem.entity.User;
import com.paymybuddy.financialsystem.model.Output;
import com.paymybuddy.financialsystem.repository.BankAccountRepository;
import com.paymybuddy.financialsystem.repository.TransactionRepository;
import com.paymybuddy.financialsystem.repository.UserRepository;

@Service
public class TransactionService {

	@Autowired
	private TransactionRepository transactionRepository;
	
	@Autowired
	private BankAccountRepository bankAccountRepository;
	
	@Autowired
	private UserRepository userRepository;

	/**
	 * This method call the transactionRepository to make the transaction.
	 * 
	 * @param user is an object of type User.
	 * @param friend is an object of type User.
	 * @param transactionDto is an object of type TransactionDto.
	 * @return true if the process was successful.
	 */
	public boolean makeATransaction(User user, User friend, TransactionDto transactionDto) {

		BankAccount userBankAccount = bankAccountRepository.retrieveAvailableBalance(user.getId());
		BankAccount friendBankAccount = bankAccountRepository.retrieveAvailableBalance(friend.getId());

		if (userBankAccount.getAvailableBalance() >= transactionDto.getAmount() + (0.005 * transactionDto.getAmount())) {

			transactionRepository.makeATransaction(user.getId(), friend.getId(), transactionDto.getDescription(), transactionDto.getAmount(), 
												  transactionDto.getAmount() + (0.005 * transactionDto.getAmount()),
												  (0.005 * transactionDto.getAmount()));
			bankAccountRepository.updateTheBankAccountBalance(user.getId(), userBankAccount.getAvailableBalance()
															 - (transactionDto.getAmount() + (0.005 * transactionDto.getAmount())));
			bankAccountRepository.updateTheBankAccountBalance(friend.getId(), friendBankAccount.getAvailableBalance() + transactionDto.getAmount());
			return true;
		} else {
			return false;
		}

	}

	/**
	 * This method call the transactionRepository to find the transaction history.
	 * 
	 * @param user is an object of type User.
	 * @return an object of type Output.
	 */
	public Output findTransactionHistory(User user) {

		List<Transaction> list = transactionRepository.retrieveTransactionHistory(user.getId());

		List<Transaction> transactionHistory = new ArrayList<>();

		for (int i = 0; i < list.size(); i++) {
			
			User friend = userRepository.retrieveUserById(list.get(i).getFriendId());

			list.get(i).setFriendFirstName(friend.getFirstName());
			list.get(i).setFriendLastName(friend.getLastName());
			list.get(i).setFriendEmail(friend.getEmail());

			transactionHistory.add(list.get(i));
		}

		Output output = new Output();

		output.setTransactionHistory(transactionHistory);

		return output;

	}
}
