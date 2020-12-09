package com.paymybuddy.financialsystem.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.paymybuddy.financialsystem.model.BankAccount;

public interface BankAccountRepository extends CrudRepository<BankAccount, Integer> {

	/**
	 * This method create the default values for a user bank account.
	 * 
	 * @param id represent the id of the user.
	 */
	@Modifying
	@Transactional
	@Query(value = "INSERT INTO bank_account (user_id, available_balance) VALUES (:id, 0)", nativeQuery = true)
	void addBankAccount(@Param("id") int id);

	/**
	 * This method retrieve the available balance of a user from the database.
	 * 
	 * @param id represent the id of the user.
	 * @return an object of type BankAccount.
	 */
	@Query(value = "SELECT * FROM bank_account WHERE bank_account.user_id = :id", nativeQuery = true)
	BankAccount retrieveAvailableBalance(@Param("id") int id);

	/**
	 * This method add the money to the user bank account in the database.
	 * 
	 * @param id represent the id of the user.
	 * @param newBalance represent the new balance for the user bank account.
	 */
	@Modifying
	@Transactional
	@Query(value = "UPDATE bank_account SET available_balance = :newBalance WHERE bank_account.user_id = :id", nativeQuery = true)
	void addMoney(@Param("id") int id, @Param("newBalance") double newBalance);
}
