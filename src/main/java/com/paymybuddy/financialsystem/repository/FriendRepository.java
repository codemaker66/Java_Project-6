package com.paymybuddy.financialsystem.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.paymybuddy.financialsystem.entity.User;

@Repository
public interface FriendRepository extends CrudRepository<User, Integer> {

	/**
	 * This method retrieve the friends of a user by his id.
	 * 
	 * @param id represent the id of the user.
	 * @return a list containing all the friends of a user.
	 */
	@Query(value = "SELECT * FROM users JOIN friends ON users.id = friends.friend_id WHERE friends.user_id = :id", nativeQuery = true)
	List<User> retrieveFriendsByUserId(@Param("id") int id);

	/**
	 * This method add a friend to the user in the database.
	 * 
	 * @param userId represent the id of the user.
	 * @param friendId represent the id of the friend.
	 */
	@Modifying
	@Transactional
	@Query(value = "INSERT INTO friends (user_id, friend_id) VALUES (:userId, :friendId)", nativeQuery = true)
	void addAFriend(@Param("userId") int userId, @Param("friendId") int friendId);

	/**
	 * This method check if the user already linked this friend to his account.
	 * 
	 * @param userId represent the id of the user.
	 * @param friendId represent the id of the friend.
	 * @return an object of type User that represent the friend of the user.
	 */
	@Query(value = "SELECT * FROM users JOIN friends ON users.id = friends.friend_id WHERE friends.user_id = :userId and friends.friend_id = :friendId", nativeQuery = true)
	User checkIfAlreadyFriends(@Param("userId") int userId, @Param("friendId") int friendId);
	
	/**
	 * This method truncate the friends table from the database.
	 */
	@Modifying
	@Transactional
	@Query(value = "TRUNCATE TABLE friends", nativeQuery = true)
	void truncateFriends();

}
