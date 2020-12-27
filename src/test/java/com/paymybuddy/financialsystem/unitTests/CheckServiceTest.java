package com.paymybuddy.financialsystem.unitTests;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.TestPropertySource;

import com.paymybuddy.financialsystem.entity.User;
import com.paymybuddy.financialsystem.repository.UserRepository;
import com.paymybuddy.financialsystem.service.CheckService;

@WebMvcTest(CheckService.class)
@TestPropertySource(properties = {"spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration"})
class CheckServiceTest {

	@MockBean
	private UserRepository userRepository;

	@MockBean
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	@Autowired
	private CheckService checkService;

	@Test
	void checkUserCredentials() {

		// Given
		User user = new User();
		user.setPassword("somerandompassword");

		// When
		Mockito.when(userRepository.retrieveUserByEmail(Mockito.anyString())).thenReturn(user);
		Mockito.when(bCryptPasswordEncoder.matches(Mockito.anyString(), Mockito.anyString())).thenReturn(true);

		// Then
		User expected = checkService.checkUserCredentials("email@email.com", "somerandompassword");
		assertThat(user).isEqualTo(expected);
	}

	@Test
	void checkFriendExistence() {

		// Given
		User friend = new User();

		// When
		Mockito.when(userRepository.retrieveUserByEmail(Mockito.anyString())).thenReturn(friend);

		// Then
		User expected = checkService.checkFriendExistence("email@email.com");
		assertThat(friend).isEqualTo(expected);

	}

}
