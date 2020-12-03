package com.paymybuddy.financialsystem.repository;

import java.util.List;

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
	@Query(value = "SELECT * FROM users u WHERE u.email = :email", nativeQuery = true)
	User retrieveUserByEmail(@Param("email") String email);

	/**
	 * This method retrieve the friends of a user by his id.
	 * 
	 * @param id represent the id of the user.
	 * @return a list containing all the friends of a user.
	 */
	@Query(value = "SELECT * FROM users u JOIN friends f ON u.id = f.friend_id WHERE f.user_id = :id", nativeQuery = true)
	List<User> retrieveFriendsByUserId(@Param("id") Integer id);

}
