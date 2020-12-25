package com.paymybuddy.financialsystem.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.paymybuddy.financialsystem.entity.User;

@Repository
public interface UserRepository extends CrudRepository<User, Integer> {

	/**
	 * This method retrieve a user by his email.
	 * 
	 * @param email represent the email of a user.
	 * @return an object of type User.
	 */
	@Query(value = "SELECT * FROM users WHERE users.email = :email", nativeQuery = true)
	User retrieveUserByEmail(String email);

	/**
	 * This method retrieve a user by his id.
	 * 
	 * @param id represent the id of a user.
	 * @return an object of type User.
	 */
	@Query(value = "SELECT * FROM users WHERE users.id = :id", nativeQuery = true)
	User retrieveUserById(int id);
	
	/**
	 * This method truncate the users table from the database.
	 */
	@Modifying
	@Transactional
	@Query(value = "TRUNCATE TABLE users", nativeQuery = true)
	void truncateUsers();
	
	/**
	 * This method disable foreign key checks for the users table.
	 */
	@Modifying
	@Transactional
	@Query(value = "SET foreign_key_checks = 0", nativeQuery = true)
	void disableForeignKeyChecks();
	
	/**
	 * This method enable foreign key checks for the users table.
	 */
	@Modifying
	@Transactional
	@Query(value = "SET foreign_key_checks = 1", nativeQuery = true)
	void enableForeignKeyChecks();

}
