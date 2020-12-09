package com.paymybuddy.financialsystem.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.paymybuddy.financialsystem.model.Transaction;

public interface TransactionRepository extends CrudRepository<Transaction, Integer> {

	/**
	 * This method add the transaction information in the database.
	 * 
	 * @param userId represent the user id.
	 * @param friendId represent the friend id.
	 * @param description represent the description of the transaction given by the user.
	 * @param amount represent the amount given to the friend by the user.
	 * @param amountAfterCommission represent that amount plus the commission given by the app.
	 * @param commissionAmount represent the commission given by the app.
	 */
	@Modifying
	@Transactional
	@Query(value = "INSERT INTO transactions (user_id, friend_id, description, amount, amount_after_commission, commission_amount) VALUES "
			+ "(:userId, :friendId, :description, :amount, :amountAfterCommission, :commissionAmount)", nativeQuery = true)
	void makeATransaction(@Param("userId") int userId, @Param("friendId") int friendId,
			@Param("description") String description, @Param("amount") double amount,
			@Param("amountAfterCommission") double amountAfterCommission,
			@Param("commissionAmount") double commissionAmount);

	/**
	 * This method retrieve the transactions history of a user.
	 * 
	 * @param id represent the id of a user.
	 * @return a list containing the transactions history of a user.
	 */
	@Query(value = "SELECT * FROM transactions WHERE transactions.user_id = :id", nativeQuery = true)
	List<Transaction> retrieveTransactionsHistory(int id);

}
