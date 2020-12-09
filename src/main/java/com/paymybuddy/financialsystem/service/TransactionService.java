package com.paymybuddy.financialsystem.service;

import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.paymybuddy.financialsystem.dto.TransactionDto;
import com.paymybuddy.financialsystem.model.BankAccount;
import com.paymybuddy.financialsystem.model.Output;
import com.paymybuddy.financialsystem.model.Transaction;
import com.paymybuddy.financialsystem.model.User;
import com.paymybuddy.financialsystem.repository.BankAccountRepository;
import com.paymybuddy.financialsystem.repository.TransactionRepository;
import com.paymybuddy.financialsystem.repository.UserRepository;

@Service
public class TransactionService {

	@Autowired
	private UserRepository userRepository;
	@Autowired
	private TransactionRepository transactionRepository;
	@Autowired
	private BankAccountRepository bankAccountRepository;

	/**
	 * This method call the userRepository to check whether the user exist in the database.
	 * 
	 * @param email represent the email of the user.
	 * @return true if the user exist in the database.
	 */
	public User checkUserExistence(String email) {

		return userRepository.retrieveUserByEmail(email);

	}

	/**
	 * This method call the transactionRepository to make the transaction then the
	 * bankAccountRepository to update the bank account of the user and it's friend
	 * with there new balance.
	 * 
	 * @param transactionDto represent an object of type TransactionDto.
	 * @return true if the process was successful.
	 */
	public boolean makeATransaction(TransactionDto transactionDto) {

		User user = userRepository.retrieveUserByEmail(transactionDto.getUserEmail());
		User friend = userRepository.retrieveUserByEmail(transactionDto.getFriendEmail());

		BankAccount userBankAccount = bankAccountRepository.retrieveAvailableBalance(user.getId());
		BankAccount friendBankAccount = bankAccountRepository.retrieveAvailableBalance(friend.getId());

		if (userBankAccount.getAvailableBalance() >= transactionDto.getAmount() + (0.005 * transactionDto.getAmount())) {

			Transaction transaction = new Transaction();
			BeanUtils.copyProperties(transactionDto, transaction);

			transactionRepository.makeATransaction(user.getId(), friend.getId(), transaction.getDescription(),
					transaction.getAmount(), transaction.getAmount() + (0.005 * transaction.getAmount()),
					(0.005 * transaction.getAmount()));
			bankAccountRepository.addMoney(user.getId(), userBankAccount.getAvailableBalance() - transaction.getAmount()
					+ (0.005 * transaction.getAmount()));
			bankAccountRepository.addMoney(friend.getId(), friendBankAccount.getAvailableBalance()
					+ transaction.getAmount() + (0.005 * transaction.getAmount()));
			return true;
		} else {
			return false;
		}

	}

	/**
	 * This method call the transactionRepository to find the transactions history.
	 * 
	 * @param email represent the email of the user.
	 * @return an object of type output.
	 */
	public Output findTransactionsHistory(String email) {
		
		User user = userRepository.retrieveUserByEmail(email);
		Output output = new Output();

		if (user != null) {
			List<Transaction> transactionsHistory = transactionRepository.retrieveTransactionsHistory(user.getId());
			output.setTransactionsHistory(transactionsHistory);
			return output;
		} else {
			return null;
		}
	}
}
