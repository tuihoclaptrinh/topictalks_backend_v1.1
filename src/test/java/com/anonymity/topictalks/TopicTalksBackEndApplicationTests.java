package com.anonymity.topictalks;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class TopicTalksBackEndApplicationTests {

	@Autowired
	private IUserRepository userRepository;

	@Test
	void contextLoads() {
	}

	@Test
	void test() {

		User user = User.builder().id(1L).name("a").build();
		User user2 = User.builder().id(2L).name("b").build();
		User user3 = User.builder().id(3L).name("c").build();
		User user4 = User.builder().id(4L).name("d").build();

		userRepository.save(user);
		userRepository.save(user2);
		userRepository.save(user3);
		userRepository.save(user4);
	}

	@Test
	void testUpdate() {

		User user = User.builder().id(1L).name("aqweqweqwe").build();

		userRepository.save(user);
	}

}
