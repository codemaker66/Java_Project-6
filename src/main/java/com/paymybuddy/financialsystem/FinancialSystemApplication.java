package com.paymybuddy.financialsystem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;

@SpringBootApplication(exclude = { UserDetailsServiceAutoConfiguration.class })
public class FinancialSystemApplication {

	/**
	 * This method initialize the app.
	 */
	public static void main(String[] args) {
		SpringApplication.run(FinancialSystemApplication.class, args);
	}

}
