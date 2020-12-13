package com.paymybuddy.financialsystem.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.paymybuddy.financialsystem.entity.User;
import com.paymybuddy.financialsystem.model.Output;
import com.paymybuddy.financialsystem.repository.FriendRepository;

@Service
public class FriendService {

	@Autowired
	private FriendRepository friendRepository;

	/**
	 * This method call the friendRepository to retrieve the friends of a user.
	 * 
	 * @param user is an object of type User.
	 * @return an object of type Output.
	 */
	public Output findFriends(User user) {

		Output output = new Output();

		List<User> friendsList = friendRepository.retrieveFriendsByUserId(user.getId());

		output.setFriends(friendsList);

		return output;
	}

	/**
	 * This method call the friendRepository to link a friend to the user account.
	 * 
	 * @param user is an object of type User.
	 * @param friend is an object of type User.
	 * @return true if the process was successful.
	 */
	public boolean addAFriend(User user, User friend) {

		if (friendRepository.checkIfAlreadyFriends(user.getId(), friend.getId()) == null) {
			friendRepository.addAFriend(user.getId(), friend.getId());
			return true;
		} else {
			return false;
		}
	}

}
