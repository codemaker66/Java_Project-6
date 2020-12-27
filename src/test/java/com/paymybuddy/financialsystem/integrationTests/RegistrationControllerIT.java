package com.paymybuddy.financialsystem.integrationTests;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.paymybuddy.financialsystem.dto.UserDto;
import com.paymybuddy.financialsystem.integrationTests.service.TruncateService;
import com.paymybuddy.financialsystem.model.Output;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations = "classpath:test.properties")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class RegistrationControllerIT {

	@LocalServerPort
	private int port;

	@Autowired
	private TestRestTemplate restTemplate = new TestRestTemplate();

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private TruncateService truncateService;
	
	private HttpHeaders headers = new HttpHeaders();

	@AfterAll
	void clearDataBase() {
		truncateService.truncateAll();
	}

	@Test
	void signUp() throws JsonProcessingException {

		// Given
		UserDto userDto = new UserDto();

		userDto.setFirstName("gamma");
		userDto.setLastName("one");
		userDto.setEmail("gamma@one.com");
		userDto.setPassword("ultimatepassword");

		Output output = new Output();

		output.setStatus(HttpStatus.CREATED);
		output.setMessage("You were successfully registered to the app");

		// When
		String URL = "http://localhost:" + port + "/signup";
		HttpEntity<UserDto> entity = new HttpEntity<UserDto>(userDto, headers);
		ResponseEntity<String> response = restTemplate.exchange(URL, HttpMethod.POST, entity, String.class);

		// Then
		String expected = objectMapper.writeValueAsString(output);
		assertThat(response.getBody()).isEqualTo(expected);

	}

}
