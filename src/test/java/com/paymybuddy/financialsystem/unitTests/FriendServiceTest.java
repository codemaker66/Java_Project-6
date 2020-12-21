package com.paymybuddy.financialsystem.unitTests;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;

import com.paymybuddy.financialsystem.entity.User;
import com.paymybuddy.financialsystem.model.Output;
import com.paymybuddy.financialsystem.repository.FriendRepository;
import com.paymybuddy.financialsystem.service.FriendService;

@WebMvcTest(FriendService.class)
@TestPropertySource(properties = {"spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration" })
class FriendServiceTest {

	@MockBean
	private FriendRepository friendRepository;

	@Autowired
	private FriendService friendService;

	@Test
	void findFriends() {

		// Given
		User user = new User();
		user.setId(12);

		List<User> list = new ArrayList<>();

		Output output = new Output();
		output.setFriends(list);

		// When
		Mockito.when(friendRepository.retrieveFriendsByUserId(Mockito.anyInt())).thenReturn(list);

		// Then
		Output expected = friendService.findFriends(user);
		assertThat(output.getFriends()).isEqualTo(expected.getFriends());
	}

	@Test
	void addAFriend() {

		// Given
		User user = new User();
		user.setId(12);

		User friend = new User();
		friend.setId(13);

		// When
		friendService.addAFriend(user, friend);

		// Then
		Mockito.verify(friendRepository, Mockito.times(1)).checkIfAlreadyFriends(Mockito.anyInt(), Mockito.anyInt());
		Mockito.verify(friendRepository, Mockito.times(1)).addAFriend(Mockito.anyInt(), Mockito.anyInt());

	}

}
