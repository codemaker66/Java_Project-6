package com.paymybuddy.financialsystem.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.paymybuddy.financialsystem.entity.User;
import com.paymybuddy.financialsystem.repository.UserRepository;

@Service
public class CheckService {

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	/**
	 * This method call the userRepository to check if the user exist in the database.
	 * 
	 * @param email represent the email of the user.
	 * @param password represent the password of the user.
	 * @return all the user info if he exist and if his password is the same as the one in the database.
	 */
	public User checkUserCredentials(String email, String password) {

		User user = userRepository.retrieveUserByEmail(email);

		if (user != null && bCryptPasswordEncoder.matches(password, user.getPassword())) {

			return user;

		} else {
			return null;
		}

	}

	/**
	 * This method call the userRepository to check if the friend exist in the database.
	 * 
	 * @param friendEmail represent the email of the friend.
	 * @return an object of type User.
	 */
	public User checkFriendExistence(String friendEmail) {
		return userRepository.retrieveUserByEmail(friendEmail);
	}

}
