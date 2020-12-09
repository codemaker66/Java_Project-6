package com.paymybuddy.financialsystem.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.paymybuddy.financialsystem.dto.FriendDto;
import com.paymybuddy.financialsystem.model.Output;
import com.paymybuddy.financialsystem.model.User;
import com.paymybuddy.financialsystem.repository.FriendRepository;
import com.paymybuddy.financialsystem.repository.UserRepository;

@Service
public class FriendService {

	@Autowired
	private FriendRepository friendRepository;
	@Autowired
	private UserRepository userRepository;

	/**
	 * This method call the userRepository to check whether the user exist in the database.
	 * 
	 * @param email represent the email of the user.
	 * @return true if the user exist in the database.
	 */
	public User checkUserExistence(String email) {

		return userRepository.retrieveUserByEmail(email);

	}

	/**
	 * This method call the friendRepository to retrieve the friends of a user.
	 * 
	 * @param email represent the email of a user.
	 * @return a list of users.
	 */
	public Output findFriends(String email) {

		User user = userRepository.retrieveUserByEmail(email);

		Output output = new Output();

		List<User> friendsList = friendRepository.retrieveFriendsByUserId(user.getId());

		output.setFriends(friendsList);

		return output;
	}

	/**
	 * This method call the friendRepository to add a friend to the user.
	 * 
	 * @param friendDto represent an object of type FriendDto.
	 * @return true if the process was successful.
	 */
	public boolean addAFriend(FriendDto friendDto) {

		User user = userRepository.retrieveUserByEmail(friendDto.getUserEmail());

		User friend = userRepository.retrieveUserByEmail(friendDto.getFriendEmail());

		if (friendRepository.checkIfAlreadyFriends(user.getId(), friend.getId()) == null) {
			friendRepository.addAFriend(user.getId(), friend.getId());
			return true;
		} else {
			return false;
		}
	}

}
