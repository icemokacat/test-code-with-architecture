package com.example.demo.repository;

import static org.assertj.core.api.AssertionsForClassTypes.*;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;

import com.example.demo.model.UserStatus;

// @ExtendWith 는 DataJpaTest 에 포함되어 있음
@DataJpaTest(showSql = true)
//@Sql("/sql/user-repository-test-data.sql")
@SqlGroup({
	@Sql("/sql/user-repository-test-data.sql"),
	@Sql(value = "/sql/delete-all-data.sql",executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
})
class UserRepositoryTest {

	@Autowired
	private UserRepository userRepository;

	@Test
	void findByIdAndStatus_로_유저_데이터를_찾아올_수_있다(){
		// given

		// when
		Optional<UserEntity> savedUser = userRepository.findByIdAndStatus(1, UserStatus.ACTIVE);

		// then
		assertThat(savedUser.isPresent()).isTrue();
	}

	@Test
	void findByIdAndStatus_는_데이터가_없으면_Optional_empty_를_내려준다(){
		// given

		// when
		Optional<UserEntity> savedUser = userRepository.findByIdAndStatus(1, UserStatus.PENDING);

		// then
		assertThat(savedUser.isPresent()).isFalse();
		assertThat(savedUser.isEmpty()).isTrue();
	}

	@Test
	void findByEmailAndStatus_로_유저_데이터를_찾아올_수_있다(){
		// given

		// when
		Optional<UserEntity> savedUser = userRepository.findByEmailAndStatus("naver@naver.com", UserStatus.ACTIVE);

		// then
		assertThat(savedUser.isPresent()).isTrue();
	}

	@Test
	void findByEmailAndStatus_는_데이터가_없으면_Optional_empty_를_내려준다(){
		// given

		// when
		Optional<UserEntity> savedUser = userRepository.findByEmailAndStatus("naver@naver.com", UserStatus.PENDING);

		// then
		assertThat(savedUser.isPresent()).isFalse();
		assertThat(savedUser.isEmpty()).isTrue();
	}
}