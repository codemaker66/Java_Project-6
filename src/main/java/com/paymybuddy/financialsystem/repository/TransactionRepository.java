package com.paymybuddy.financialsystem.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.paymybuddy.financialsystem.entity.Transaction;

@Repository
public interface TransactionRepository extends CrudRepository<Transaction, Integer> {

	/**
	 * This method add the transaction informations in the database.
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
	+ "(:userId, :friendId, :description, ROUND(:amount, 2), ROUND(:amountAfterCommission, 2), ROUND(:commissionAmount, 2))", nativeQuery = true)
	void makeATransaction(@Param("userId") int userId, @Param("friendId") int friendId,@Param("description") String description, 
						  @Param("amount") double amount,
						  @Param("amountAfterCommission") double amountAfterCommission,
						  @Param("commissionAmount") double commissionAmount);

	/**
	 * This method retrieve the transaction history of a user.
	 * 
	 * @param id represent the id of a user.
	 * @return a list containing the transaction history of a user.
	 */
	@Query(value = "SELECT * FROM transactions WHERE transactions.user_id = :id", nativeQuery = true)
	List<Transaction> retrieveTransactionHistory(int id);
	
	/**
	 * This method truncate the transactions table from the database.
	 */
	@Modifying
	@Transactional
	@Query(value = "TRUNCATE TABLE transactions", nativeQuery = true)
	void truncateTransactions();

}
