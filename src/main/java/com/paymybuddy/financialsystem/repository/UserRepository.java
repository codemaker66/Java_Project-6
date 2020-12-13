package com.paymybuddy.financialsystem.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

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
	User retrieveUserByEmail(@Param("email") String email);

	/**
	 * This method retrieve a user by his id.
	 * 
	 * @param id represent the id of a user.
	 * @return an object of type User.
	 */
	@Query(value = "SELECT * FROM users WHERE users.id = :id", nativeQuery = true)
	User retrieveUserById(@Param("id") int id);

}
