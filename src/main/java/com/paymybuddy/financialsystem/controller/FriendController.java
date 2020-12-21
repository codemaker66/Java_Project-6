package com.paymybuddy.financialsystem.controller;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.paymybuddy.financialsystem.dto.FriendDto;
import com.paymybuddy.financialsystem.dto.UserCredentialsDto;
import com.paymybuddy.financialsystem.entity.User;
import com.paymybuddy.financialsystem.exceptions.PropertiesException;
import com.paymybuddy.financialsystem.exceptions.ResourceException;
import com.paymybuddy.financialsystem.model.Output;
import com.paymybuddy.financialsystem.service.CheckService;
import com.paymybuddy.financialsystem.service.FriendService;

@RestController
public class FriendController {

	@Autowired
	private FriendService friendService;
	@Autowired
	private CheckService checkService;

	/**
	 * This method call the friendService to find the user friends.
	 * 
	 * @param userCredentialsDto is an object of type UserCredentialsDto.
	 * @param bindingResult is a general interface that represents binding results.
	 * @return an object of type Output.
	 */
	@PostMapping(value = "/friends")
	public Output getFriends(@Valid @RequestBody UserCredentialsDto userCredentialsDto, BindingResult bindingResult) {

		if (bindingResult.hasErrors()) {
			List<String> details = new ArrayList<>();
			for (FieldError fieldError : bindingResult.getFieldErrors()) {
				details.add(fieldError.getDefaultMessage());
			}
			throw new PropertiesException(HttpStatus.BAD_REQUEST, "Validation failed", details);
		}

		User user = checkService.checkUserCredentials(userCredentialsDto.getEmail(), userCredentialsDto.getPassword());

		if (user == null) {
			throw new ResourceException(HttpStatus.BAD_REQUEST, "Invalid email or password");
		}

		Output output = friendService.findFriends(user);

		if (output.getFriends() != null && !output.getFriends().isEmpty()) {
			return output;
		} else {
			throw new ResourceException(HttpStatus.BAD_REQUEST, "You don't have any linked friends with your account");
		}

	}

	/**
	 * This method call the friendService to link a new friend to the user account.
	 * 
	 * @param friendDto is an object of type FriendDto.
	 * @param bindingResult is a general interface that represents binding results.
	 * @return a ResponseEntity if the request was successful.
	 */
	@PostMapping(value = "/friend")
	public ResponseEntity<Output> addAFriend(@Valid @RequestBody FriendDto friendDto, BindingResult bindingResult) {

		if (bindingResult.hasErrors()) {
			List<String> details = new ArrayList<>();
			for (FieldError fieldError : bindingResult.getFieldErrors()) {
				details.add(fieldError.getDefaultMessage());
			}
			throw new PropertiesException(HttpStatus.BAD_REQUEST, "Validation failed", details);
		}

		User user = checkService.checkUserCredentials(friendDto.getEmail(), friendDto.getPassword());

		User friend = checkService.checkFriendExistence(friendDto.getFriendEmail());

		if (user == null) {
			throw new ResourceException(HttpStatus.BAD_REQUEST, "Invalid email or password");
		}

		if (friend == null) {
			throw new ResourceException(HttpStatus.BAD_REQUEST, "The friend email you have entered does not exist in the database");
		}

		if (friendService.addAFriend(user, friend)) {
			Output output = new Output();
			output.setStatus(HttpStatus.OK);
			output.setMessage("Your new friend is now linked to your account");
			return ResponseEntity.status(HttpStatus.OK).body(output);
		} else {
			throw new ResourceException(HttpStatus.BAD_REQUEST, "You have already linked this friend to your account");
		}

	}

}
