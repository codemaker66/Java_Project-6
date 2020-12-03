package com.paymybuddy.financialsystem.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.paymybuddy.financialsystem.model.User;
import com.paymybuddy.financialsystem.repository.UserRepository;

@Service
public class UserService {

	@Autowired
	private UserRepository userRepository;

	/**
	 * This method call the userRepository to retrieve the friends of a user.
	 * 
	 * @param email represent the email of a user.
	 * @return a list of users.
	 */
	public List<User> findFriendsByEmail(String email) {

		User user = userRepository.retrieveUserByEmail(email);

		return userRepository.retrieveFriendsByUserId(user.getId());
	}

}
