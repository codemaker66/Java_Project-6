package com.paymybuddy.financialsystem.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.paymybuddy.financialsystem.model.User;
import com.paymybuddy.financialsystem.repository.RegistrationRepository;

@Service
public class RegistrationService {

	@Autowired
	private RegistrationRepository registrationRepository;

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	/**
	 * This method call the RegistrationRepository to save the user in the database.
	 * 
	 * @param user represent an object of type User.
	 */
	public void registerTheUser(User user) {

		user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));

		registrationRepository.save(user);

	}

}
