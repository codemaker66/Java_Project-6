package com.paymybuddy.financialsystem.service;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.paymybuddy.financialsystem.dto.UserDto;
import com.paymybuddy.financialsystem.entity.User;
import com.paymybuddy.financialsystem.repository.BankAccountRepository;
import com.paymybuddy.financialsystem.repository.RegistrationRepository;
import com.paymybuddy.financialsystem.repository.UserRepository;

@Service
public class RegistrationService {

	@Autowired
	private RegistrationRepository registrationRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private BankAccountRepository bankAccountRepository;

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	/**
	 * This method call the registrationRepository to save the user in the database.
	 * 
	 * @param userDto is an object of type UserDto.
	 * @return true if the process was successful.
	 */
	public boolean registerTheUser(UserDto userDto) {

		if (userRepository.retrieveUserByEmail(userDto.getEmail()) == null) {

			User user = new User();

			BeanUtils.copyProperties(userDto, user);

			user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));

			registrationRepository.save(user);

			User userInfo = userRepository.retrieveUserByEmail(user.getEmail());

			bankAccountRepository.createABankAccount(userInfo.getId());

			return true;

		} else {
			return false;
		}

	}

}
