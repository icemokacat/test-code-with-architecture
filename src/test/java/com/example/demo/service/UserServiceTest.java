package com.example.demo.service;

import static org.assertj.core.api.AssertionsForClassTypes.*;
import static org.mockito.ArgumentMatchers.*;

import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;

import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.UserStatus;
import com.example.demo.model.dto.UserCreateDto;
import com.example.demo.repository.UserEntity;

@SpringBootTest
@SqlGroup({
	@Sql("/sql/user-service-test-data.sql"),
	@Sql(value = "/sql/delete-all-data.sql",executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
})
class UserServiceTest {

	@Autowired
	private UserService userService;
	@MockBean
	private JavaMailSender mailSender;

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

	@Test
	void userCreateDto_를_이용하여_유저를_생성할_수_있다(){
		// given
		UserCreateDto userCreateDto = UserCreateDto.builder()
			.email("naver3@naver.com")
			.address("Gangnam")
			.nickname("abcd")
			.build();
		BDDMockito.doNothing().when(mailSender).send(any(SimpleMailMessage.class));

		// when
		UserEntity userEntity = userService.create(userCreateDto);

		// then
		assertThat(userEntity.getId()).isNotNull();
		assertThat(userEntity.getStatus()).isEqualTo(UserStatus.PENDING);
	}

}