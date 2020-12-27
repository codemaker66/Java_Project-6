package com.paymybuddy.financialsystem.unitTests;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;

import com.paymybuddy.financialsystem.dto.BankAccountDto;
import com.paymybuddy.financialsystem.entity.BankAccount;
import com.paymybuddy.financialsystem.entity.User;
import com.paymybuddy.financialsystem.repository.BankAccountRepository;
import com.paymybuddy.financialsystem.service.BankAccountService;
import static org.assertj.core.api.Assertions.assertThat;

@WebMvcTest(BankAccountService.class)
@TestPropertySource(properties = {"spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration"})
class BankAccountServiceTest {

	@MockBean
	private BankAccountRepository bankAccountRepository;

	@Autowired
	private BankAccountService bankAccountService;

	@Test
	void findAvailableBalance() {

		// Given
		BankAccount bankAccount = new BankAccount();
		bankAccount.setAvailableBalance(10);

		User user = new User();
		user.setId(20);

		// When
		Mockito.when(bankAccountRepository.retrieveAvailableBalance(Mockito.anyInt())).thenReturn(bankAccount);

		// Then
		BankAccount expected = bankAccountService.findAvailableBalance(user);
		assertThat(bankAccount.getAvailableBalance()).isEqualTo(expected.getAvailableBalance());

	}

	@Test
	void addMoney() {

		// Given
		BankAccount bankAccount = new BankAccount();
		bankAccount.setAvailableBalance(10);

		BankAccountDto bankAccountDto = new BankAccountDto();
		bankAccountDto.setAmount(34.5);

		User user = new User();
		user.setId(1);

		// When
		Mockito.when(bankAccountRepository.retrieveAvailableBalance(Mockito.anyInt())).thenReturn(bankAccount);

		// Then
		bankAccountService.addMoney(user, bankAccountDto);
		Mockito.verify(bankAccountRepository, Mockito.times(1)).updateTheBankAccountBalance(Mockito.anyInt(), Mockito.anyDouble());

	}

	@Test
	void getMoney() {

		// Given
		BankAccount bankAccount = new BankAccount();
		bankAccount.setAvailableBalance(10.0);

		BankAccountDto bankAccountDto = new BankAccountDto();
		bankAccountDto.setAmount(5.0);

		User user = new User();
		user.setId(1);

		// When
		Mockito.when(bankAccountRepository.retrieveAvailableBalance(Mockito.anyInt())).thenReturn(bankAccount);

		// Then
		bankAccountService.getMoney(user, bankAccountDto);
		Mockito.verify(bankAccountRepository, Mockito.times(1)).updateTheBankAccountBalance(Mockito.anyInt(), Mockito.anyDouble());
	}

}
