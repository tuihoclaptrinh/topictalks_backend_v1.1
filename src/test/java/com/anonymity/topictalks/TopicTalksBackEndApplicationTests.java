package com.anonymity.topictalks;

import com.anonymity.topictalks.daos.user.IUserRepository;
import com.anonymity.topictalks.models.persists.user.UserPO;
import com.anonymity.topictalks.utils.logger.LoggerUtils;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest
class TopicTalksBackEndApplicationTests {

	private final LoggerUtils<TopicTalksBackEndApplicationTests> logger = new LoggerUtils<>(TopicTalksBackEndApplicationTests.class);

	@Autowired
	private IUserRepository userRepository;

	@Test
	void contextLoads() {
		logger.logException(new Exception("asdasdas"));
	}

	@Test
	void test() {
		UserPO username = userRepository.findByUsername("string").get();
		System.out.println(username);
	}

}
