package com.paymybuddy.financialsystem.unitTests;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;

import com.paymybuddy.financialsystem.dto.TransactionDto;
import com.paymybuddy.financialsystem.entity.BankAccount;
import com.paymybuddy.financialsystem.entity.User;
import com.paymybuddy.financialsystem.repository.BankAccountRepository;
import com.paymybuddy.financialsystem.repository.TransactionRepository;
import com.paymybuddy.financialsystem.repository.UserRepository;
import com.paymybuddy.financialsystem.service.TransactionService;

@WebMvcTest(TransactionService.class)
@TestPropertySource(properties = {"spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration"})
class TransactionServiceTest {

	@MockBean
	private TransactionRepository transactionRepository;

	@MockBean
	private BankAccountRepository bankAccountRepository;

	@MockBean
	private UserRepository userRepository;

	@Autowired
	private TransactionService transactionService;

	@Test
	void makeATransaction() {

		// Given
		User user = new User();
		user.setId(15);

		User friend = new User();
		friend.setId(16);

		TransactionDto transactionDto = new TransactionDto();

		transactionDto.setAmount(100);
		transactionDto.setDescription("description");

		BankAccount bankAccount = new BankAccount();
		bankAccount.setAvailableBalance(200);

		// When
		Mockito.when(bankAccountRepository.retrieveAvailableBalance(Mockito.anyInt())).thenReturn(bankAccount);

		// Then
		transactionService.makeATransaction(user, friend, transactionDto);
		Mockito.verify(transactionRepository, Mockito.times(1)).makeATransaction(Mockito.anyInt(), Mockito.anyInt(), Mockito.anyString(), Mockito.anyDouble(), Mockito.anyDouble(), Mockito.anyDouble());
		Mockito.verify(bankAccountRepository, Mockito.times(2)).updateTheBankAccountBalance(Mockito.anyInt(), Mockito.anyDouble());
	}

}
