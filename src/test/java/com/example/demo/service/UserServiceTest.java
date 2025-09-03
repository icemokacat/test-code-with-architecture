package com.example.demo.service;

import static org.assertj.core.api.AssertionsForClassTypes.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;

import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.UserEntity;
import com.example.demo.repository.UserRepository;

@SpringBootTest
@SqlGroup({
	@Sql("/sql/user-service-test-data.sql"),
	@Sql(value = "/sql/delete-all-data.sql",executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
})
class UserServiceTest {

	@Autowired
	private UserService userService;

	@Test
	void getByEmail은_Active_상태의_유저를_찾아올_수_있다() {
		// given
		String email = "naver@naver.com";

		// when
		UserEntity result = userService.getByEmail(email);

		// then
		assertThat(result.getNickname()).isEqualTo("홍길동");
	}

	@Test
	void getByEmail은_PENDING_상태인_유저는_찾아올_수_없다() {
		// given
		String email = "naver2@naver.com";

		// when
		// then
		assertThatThrownBy(() -> {
			UserEntity result = userService.getByEmail(email);
		}).isInstanceOf(ResourceNotFoundException.class);
	}

	@Test
	void getById는_ACTIVE_상태인_유저는_찾아올_수_있다() {
		// given
		long id = 1;

		// when
		UserEntity result = userService.getById(id);

		// then
		assertThat(result.getNickname()).isEqualTo("홍길동");
	}

	@Test
	void getById은_PENDING_상태인_유저는_찾아올_수_없다() {
		// given
		long id = 2;

		// when
		// then
		assertThatThrownBy(() -> {
			UserEntity result = userService.getById(id);
		}).isInstanceOf(ResourceNotFoundException.class);
	}

}