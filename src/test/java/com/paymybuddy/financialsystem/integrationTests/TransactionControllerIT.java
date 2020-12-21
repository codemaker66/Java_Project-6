package com.paymybuddy.financialsystem.integrationTests;

import static org.assertj.core.api.Assertions.assertThat;

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
import com.paymybuddy.financialsystem.dto.TransactionDto;
import com.paymybuddy.financialsystem.dto.UserCredentialsDto;
import com.paymybuddy.financialsystem.entity.Transaction;
import com.paymybuddy.financialsystem.entity.User;
import com.paymybuddy.financialsystem.integrationTests.service.TruncateService;
import com.paymybuddy.financialsystem.model.Output;
import com.paymybuddy.financialsystem.repository.BankAccountRepository;
import com.paymybuddy.financialsystem.repository.RegistrationRepository;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations = "classpath:test.properties")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class TransactionControllerIT {

	@LocalServerPort
	private int port;

	@Autowired
	private TestRestTemplate restTemplate = new TestRestTemplate();

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private TruncateService truncateService;

	@Autowired
	private BankAccountRepository bankAccountRepository;

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

		bankAccountRepository.createABankAccount(1);

		bankAccountRepository.updateTheBankAccountBalance(1, 200);

		User friend = new User();

		friend.setFirstName("beta");
		friend.setLastName("two");
		friend.setEmail("beta@two.com");
		friend.setPassword(bCryptPasswordEncoder.encode("hashedpassword"));

		registrationRepository.save(friend);

		bankAccountRepository.createABankAccount(2);
	}

	@AfterAll
	void clearDataBase() {
		truncateService.truncateAll();
	}

	@Test
	@Order(1)
	void makeATransaction() throws JsonProcessingException {

		// Given
		TransactionDto transactionDto = new TransactionDto();

		transactionDto.setEmail("alpha@one.com");
		transactionDto.setPassword("cryptedpassword");
		transactionDto.setFriendEmail("beta@two.com");
		transactionDto.setDescription("desc");
		transactionDto.setAmount(50);

		Output output = new Output();

		output.setMessage("Your transaction was successfully made");
		output.setStatus(HttpStatus.CREATED);

		// When
		String URL = "http://localhost:" + port + "/transaction";
		HttpEntity<TransactionDto> entity = new HttpEntity<TransactionDto>(transactionDto, headers);
		ResponseEntity<String> response = restTemplate.exchange(URL, HttpMethod.POST, entity, String.class);

		// Then
		String expected = objectMapper.writeValueAsString(output);
		assertThat(response.getBody()).isEqualTo(expected);
	}

	@Test
	@Order(2)
	void retrieveTransactionsHistory() throws JsonProcessingException {

		// Given
		UserCredentialsDto userCredentialsDto = new UserCredentialsDto();

		userCredentialsDto.setEmail("alpha@one.com");
		userCredentialsDto.setPassword("cryptedpassword");

		Transaction transaction = new Transaction();

		transaction.setFriendFirstName("beta");
		transaction.setFriendLastName("two");
		transaction.setFriendEmail("beta@two.com");
		transaction.setDescription("desc");
		transaction.setAmount(50);
		transaction.setAmountAfterCommission(50.25);
		transaction.setCommissionAmount(0.25);

		List<Transaction> transactionHistory = new ArrayList<>();
		transactionHistory.add(transaction);

		Output output = new Output();
		output.setTransactionHistory(transactionHistory);

		// When
		String URL = "http://localhost:" + port + "/transactionHistory";
		HttpEntity<UserCredentialsDto> entity = new HttpEntity<UserCredentialsDto>(userCredentialsDto, headers);
		ResponseEntity<String> response = restTemplate.exchange(URL, HttpMethod.POST, entity, String.class);

		// Then
		String expected = objectMapper.writeValueAsString(output);
		assertThat(response.getBody()).isEqualTo(expected);
	}

}
