package com.paymybuddy.financialsystem.integrationTests;

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
import com.paymybuddy.financialsystem.dto.BankAccountDto;
import com.paymybuddy.financialsystem.dto.UserCredentialsDto;
import com.paymybuddy.financialsystem.entity.BankAccount;
import com.paymybuddy.financialsystem.entity.User;
import com.paymybuddy.financialsystem.integrationTests.service.TruncateService;
import com.paymybuddy.financialsystem.model.Output;
import com.paymybuddy.financialsystem.repository.BankAccountRepository;
import com.paymybuddy.financialsystem.repository.RegistrationRepository;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations = "classpath:test.properties")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class BankAccountControllerIT {

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
	private BankAccountRepository bankAccountRepository;

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

		bankAccountRepository.createABankAccount(1);

	}

	@AfterAll
	void clearDataBase() {
		truncateService.truncateAll();
	}

	@Test
	@Order(1)
	void addMoney() throws JsonProcessingException {

		// Given
		BankAccountDto bankAccountDto = new BankAccountDto();

		bankAccountDto.setEmail("alpha@one.com");
		bankAccountDto.setPassword("cryptedpassword");
		bankAccountDto.setAmount(200);

		Output output = new Output();

		output.setStatus(HttpStatus.OK);
		output.setMessage("You have successfully added money to your account");

		// When
		String URL = "http://localhost:" + port + "/addMoney";
		HttpEntity<BankAccountDto> entity = new HttpEntity<BankAccountDto>(bankAccountDto, headers);
		ResponseEntity<String> response = restTemplate.exchange(URL, HttpMethod.POST, entity, String.class);

		// Then
		String expected = objectMapper.writeValueAsString(output);
		assertThat(response.getBody()).isEqualTo(expected);

	}

	@Test
	@Order(2)
	void retrieveAvailableBalance() throws JsonProcessingException {

		// Given
		UserCredentialsDto userCredentialsDto = new UserCredentialsDto();

		userCredentialsDto.setEmail("alpha@one.com");
		userCredentialsDto.setPassword("cryptedpassword");

		BankAccount bankAccount = new BankAccount();
		bankAccount.setAvailableBalance(200);

		// When
		String URL = "http://localhost:" + port + "/availableBalance";
		HttpEntity<UserCredentialsDto> entity = new HttpEntity<UserCredentialsDto>(userCredentialsDto, headers);
		ResponseEntity<String> response = restTemplate.exchange(URL, HttpMethod.POST, entity, String.class);

		// Then
		String expected = objectMapper.writeValueAsString(bankAccount);
		assertThat(response.getBody()).isEqualTo(expected);

	}

}
