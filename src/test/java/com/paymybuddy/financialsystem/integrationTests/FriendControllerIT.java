package com.paymybuddy.financialsystem.integrationTests;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.TestPropertySource;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.paymybuddy.financialsystem.dto.FriendDto;
import com.paymybuddy.financialsystem.dto.UserCredentialsDto;
import com.paymybuddy.financialsystem.entity.User;
import com.paymybuddy.financialsystem.integrationTests.service.TruncateService;
import com.paymybuddy.financialsystem.model.Output;
import com.paymybuddy.financialsystem.repository.RegistrationRepository;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations = "classpath:test.properties")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class FriendControllerIT {

	@LocalServerPort
	private int port;

	@Autowired
	private TestRestTemplate restTemplate = new TestRestTemplate();

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private TruncateService truncateService;

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	@Autowired
	private RegistrationRepository registrationRepository;
	
	private HttpHeaders headers = new HttpHeaders();

	@BeforeAll
	void init() {

		User user = new User();

		user.setFirstName("alpha");
		user.setLastName("one");
		user.setEmail("alpha@one.com");
		user.setPassword(bCryptPasswordEncoder.encode("cryptedpassword"));

		registrationRepository.save(user);

		User friend = new User();

		friend.setFirstName("beta");
		friend.setLastName("two");
		friend.setEmail("beta@two.com");
		friend.setPassword(bCryptPasswordEncoder.encode("hashedpassword"));

		registrationRepository.save(friend);
	}

	@AfterAll
	void clearDataBase() {
		truncateService.truncateAll();
	}

	@Test
	@Order(1)
	void addAFriend() throws JsonProcessingException {

		// Given
		FriendDto friendDto = new FriendDto();

		friendDto.setEmail("alpha@one.com");
		friendDto.setPassword("cryptedpassword");
		friendDto.setFriendEmail("beta@two.com");

		Output output = new Output();

		output.setStatus(HttpStatus.OK);
		output.setMessage("Your new friend is now linked to your account");

		// When
		String URL = "http://localhost:" + port + "/friend";
		HttpEntity<FriendDto> entity = new HttpEntity<FriendDto>(friendDto, headers);
		ResponseEntity<String> response = restTemplate.exchange(URL, HttpMethod.POST, entity, String.class);

		// Then
		String expected = objectMapper.writeValueAsString(output);
		assertThat(response.getBody()).isEqualTo(expected);
	}

	@Test
	@Order(2)
	void getFriends() throws JsonProcessingException {

		// Given
		UserCredentialsDto userCredentialsDto = new UserCredentialsDto();

		userCredentialsDto.setEmail("alpha@one.com");
		userCredentialsDto.setPassword("cryptedpassword");

		User user = new User();

		user.setFirstName("beta");
		user.setLastName("two");
		user.setEmail("beta@two.com");

		List<User> friends = new ArrayList<>();
		friends.add(user);

		Output output = new Output();
		output.setFriends(friends);

		// When
		String URL = "http://localhost:" + port + "/friends";
		HttpEntity<UserCredentialsDto> entity = new HttpEntity<UserCredentialsDto>(userCredentialsDto, headers);
		ResponseEntity<String> response = restTemplate.exchange(URL, HttpMethod.POST, entity, String.class);

		// Then
		String expected = objectMapper.writeValueAsString(output);
		assertThat(response.getBody()).isEqualTo(expected);
	}

}
