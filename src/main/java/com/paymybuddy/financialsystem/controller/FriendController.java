package com.paymybuddy.financialsystem.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.paymybuddy.financialsystem.dto.FriendDto;
import com.paymybuddy.financialsystem.exceptions.ResourceException;
import com.paymybuddy.financialsystem.model.Output;
import com.paymybuddy.financialsystem.service.FriendService;

@RestController
public class FriendController {

	@Autowired
	private FriendService friendService;

	/**
	 * This method call the friendService to find friends by email.
	 * 
	 * @param email represent the email of the user.
	 * @return a list containing the friends of the user.
	 */
	@GetMapping(value = "/friends")
	public Output getFriendsByEmail(@RequestParam(name = "email") String email) {

		if (friendService.checkUserExistence(email) == null) {
			throw new ResourceException(HttpStatus.BAD_REQUEST, "This email does not exist in the databse");
		}

		Output output = friendService.findFriends(email);

		if (output.getFriends() != null && !output.getFriends().isEmpty()) {
			return output;
		} else {
			throw new ResourceException(HttpStatus.BAD_REQUEST, "You don't have any linked friends with your account");
		}

	}

	/**
	 * This method call the friendService to add a friend.
	 * 
	 * @param friendDto represent an object of type FriendDto.
	 * @return a ResponseEntity if the request was successful.
	 */
	@PostMapping(value = "/friend")
	public ResponseEntity<Output> addAFriend(@RequestBody FriendDto friendDto) {

		if (friendService.checkUserExistence(friendDto.getUserEmail()) == null) {
			throw new ResourceException(HttpStatus.BAD_REQUEST, "Your email does not exist in the database");
		}

		if (friendService.checkUserExistence(friendDto.getFriendEmail()) == null) {
			throw new ResourceException(HttpStatus.BAD_REQUEST, "The friend email you have entered does not exist in the database");
		}

		if (friendService.addAFriend(friendDto)) {
			Output output = new Output();
			output.setMessage("The request was successfully made");
			List<String> details = new ArrayList<>();
			details.add("Your new friend is now linked to your account");
			output.setDetails(details);
			return ResponseEntity.status(HttpStatus.CREATED).body(output);
		} else {
			throw new ResourceException(HttpStatus.BAD_REQUEST, "You have already linked this friend to your account");
		}

	}

}
