package com.paymybuddy.financialsystem.unitTests;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.TestPropertySource;

import com.paymybuddy.financialsystem.dto.UserDto;
import com.paymybuddy.financialsystem.entity.User;
import com.paymybuddy.financialsystem.repository.BankAccountRepository;
import com.paymybuddy.financialsystem.repository.RegistrationRepository;
import com.paymybuddy.financialsystem.repository.UserRepository;
import com.paymybuddy.financialsystem.service.RegistrationService;

@WebMvcTest(RegistrationService.class)
@TestPropertySource(properties = {"spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration" })
class RegistrationServiceTest {

	@MockBean
	private RegistrationRepository registrationRepository;

	@MockBean
	private UserRepository userRepository;

	@MockBean
	private BankAccountRepository bankAccountRepository;

	@MockBean
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	@Autowired
	private RegistrationService registrationService;

	@Test
	void registerTheUser() {

		// Given
		User user = new User();
		user.setId(15);

		UserDto userDto = new UserDto();

		userDto.setEmail("email@email.com");
		userDto.setPassword("superpassword");

		// When
		Mockito.when(userRepository.retrieveUserByEmail(Mockito.anyString())).thenReturn(null, user);

		// Then
		registrationService.registerTheUser(userDto);
		Mockito.verify(userRepository, Mockito.times(2)).retrieveUserByEmail(Mockito.anyString());
		Mockito.verify(bCryptPasswordEncoder, Mockito.times(1)).encode(Mockito.anyString());
		Mockito.verify(registrationRepository, Mockito.times(1)).save(Mockito.any());
		Mockito.verify(bankAccountRepository, Mockito.times(1)).createABankAccount(Mockito.anyInt());

	}

}
