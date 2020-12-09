package com.paymybuddy.financialsystem.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.paymybuddy.financialsystem.model.User;

@Repository
public interface UserRepository extends CrudRepository<User, Integer> {

	/**
	 * This method retrieve a user by his email.
	 * 
	 * @param email represent the email of a user.
	 * @return an object of type User that represent all the info of a user.
	 */
	@Query(value = "SELECT * FROM users WHERE users.email = :email", nativeQuery = true)
	User retrieveUserByEmail(@Param("email") String email);

}
