package com.paymybuddy.financialsystem.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.paymybuddy.financialsystem.model.User;
import com.paymybuddy.financialsystem.service.UserService;

@RestController
public class UserController {

	@Autowired
	private UserService userService;

	/**
	 * This method call the userService to find friends by email.
	 * 
	 * @param email represent the email of the user.
	 * @return a list containing the friends of the user.
	 */
	@GetMapping(value = "/friends")
	public List<User> getFriendsByEmail(@RequestParam(name = "email") String email) {

		return userService.findFriendsByEmail(email);

	}

}
